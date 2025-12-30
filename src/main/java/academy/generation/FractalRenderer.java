package academy.generation;

import academy.generation.transformations.Transformation;
import academy.generation.transformations.TransformationFactory;
import academy.model.AffineCoefficients;
import academy.model.ImageBuffer;
import academy.model.Pixel;
import academy.model.Point;
import academy.model.TransformationSpec;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class FractalRenderer {

    private static final double IMAGE_HEIGHT = 2.0;
    private static final int STEPS_TO_SKIP = 20;

    public ImageBuffer renderFractal(
        int width, int height,
        List<TransformationSpec> transformations,
        List<AffineCoefficients> coefficients,
        int samplesPerIteration,
        int iterationsPerSample,
        long seed
    ) {
        log.info("Однопоточный пошел");
        SplittableRandom random = new SplittableRandom(seed);
        TransformationSelector transformationSelector = new TransformationSelector(transformations);
        return renderTask(width, height, transformations, coefficients, samplesPerIteration, iterationsPerSample, random, transformationSelector);
    }

    public ImageBuffer renderFractalParallel(
        int width, int height,
        List<TransformationSpec> transformations,
        List<AffineCoefficients> coefficients,
        int totalSamples,
        int iterationsPerSample,
        int threadsCount,
        long seed
    ) {
        log.info("Многопоточный пошел");
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        int samplesPerThread = Math.max(1, totalSamples / threadsCount);
        List<Callable<ImageBuffer>> tasks = new ArrayList<>();

        TransformationSelector transformationSelector = new TransformationSelector(transformations);

        SplittableRandom random = new SplittableRandom(seed);

        for (int i = 0; i < threadsCount; i++) {
            tasks.add(() -> renderTask(width, height, transformations, coefficients, samplesPerThread, iterationsPerSample, random.split(), transformationSelector));
        }

        try {
            List<Future<ImageBuffer>> futures = executor.invokeAll(tasks);
            ImageBuffer finalImage = ImageBuffer.create(width, height);

            for (Future<ImageBuffer> future : futures) {
                mergeImage(finalImage, future.get());
            }

            return finalImage;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    private ImageBuffer renderTask(
        int width, int height,
        List<TransformationSpec> transformations,
        List<AffineCoefficients> coefficients,
        int samples,
        int iterationsPerSample,
        SplittableRandom random,
        TransformationSelector transformationSelector
    ) {
        ImageBuffer image = ImageBuffer.create(width, height);
        ImageBounds bounds = calculateImageBounds(width, height);

        for (int sample = 0; sample < samples; sample++) {
            Point currentPoint = new Point(
                random.nextDouble(bounds.xMin(), bounds.xMax()),
                random.nextDouble(bounds.yMin(), bounds.yMax())
            );

            for (int step = 0; step < STEPS_TO_SKIP + iterationsPerSample; step++) {
                Transformation transformation = transformationSelector.selectTransformation(random);
                AffineCoefficients coeff = randomElement(coefficients, random);

                currentPoint = applyTransformation(currentPoint, transformation, coeff);

                if (step >= STEPS_TO_SKIP) {
                    mapAndColor(image, currentPoint, bounds, coeff);
                }
            }
        }
        return image;
    }

    private Point applyTransformation(Point p, Transformation transformation, AffineCoefficients coeff) {
        double x = coeff.a() * p.x() + coeff.b() * p.y() + coeff.c();
        double y = coeff.d() * p.x() + coeff.e() * p.y() + coeff.f();
        return transformation.apply(new Point(x, y));
    }

    private Point applyTransformation(Point point, List<TransformationSpec> transformations, AffineCoefficients coefficients) {
        double affineX = coefficients.a() * point.x() + coefficients.b() * point.y() + coefficients.c();
        double affineY = coefficients.d() * point.x() + coefficients.e() * point.y() + coefficients.f();

        for (TransformationSpec spec : transformations) {
            Transformation transformation = TransformationFactory.getTransformation(spec.name());
            double weight = spec.weight();

            Point transformedPoint = transformation.apply(new Point(newX, newY));

            x = transformedPoint.x() * weight;
            y = transformedPoint.y() * weight;
        }

        return new Point(x, y);
    }

    private void mapAndColor(ImageBuffer image, Point p, ImageBounds bounds, AffineCoefficients coeff) {
        if (p.x() < bounds.xMin() || p.x() > bounds.xMax() ||
            p.y() < bounds.yMin() || p.y() > bounds.yMax()) {
            return;
        }

        int pixelX = (int) ((p.x() - bounds.xMin()) / (bounds.xMax() - bounds.xMin()) * image.getWidth());
        int pixelY = (int) ((p.y() - bounds.yMin()) / (bounds.yMax() - bounds.yMin()) * image.getHeight());

        if (image.inBounds(pixelX, pixelY)) {
            image.getPixel(pixelX, pixelY).addHit(coeff.color());
            image.updateMaxHitCount(image.getPixel(pixelX, pixelY).getHitCount());
        }
    }

    private ImageBounds calculateImageBounds(int width, int height) {
        double ratio = (double) width / height;
        double worldWidth = ratio * IMAGE_HEIGHT;
        return new ImageBounds(
            -worldWidth / 2,
            worldWidth / 2,
            -IMAGE_HEIGHT / 2,
            IMAGE_HEIGHT / 2
        );
    }

    private void mergeImage(ImageBuffer target, ImageBuffer source) {
        for (int y = 0; y < target.getHeight(); y++) {
            for (int x = 0; x < target.getWidth(); x++) {
                Pixel targetPixel = target.getPixel(x, y);
                Pixel sourcePixel = source.getPixel(x, y);
                targetPixel.merge(sourcePixel);
                target.updateMaxHitCount(targetPixel.getHitCount());
            }
        }
    }

    private <T> T randomElement(List<T> list, SplittableRandom random) {
        return list.get(random.nextInt(list.size()));
    }

    private record ImageBounds(double xMin, double xMax, double yMin, double yMax) {}
}
