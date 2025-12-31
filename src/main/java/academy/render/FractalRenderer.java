package academy.render;

import academy.model.FractalImage;
import academy.model.Pixel;
import academy.model.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FractalRenderer {

    private static final double IMAGE_HEIGHT = 2.0;
    private static final int STEPS_TO_SKIP = 20;

    public FractalImage renderFractal(
            int width, int height, List<FlameFunction> functions, int iterationsPerSample, int sampleCount, long seed) {
        log.info("Starting single-threaded rendering");
        SplittableRandom random = new SplittableRandom(seed);
        return renderTask(width, height, functions, iterationsPerSample, sampleCount, random);
    }

    public FractalImage renderFractalParallel(
            int width,
            int height,
            List<FlameFunction> functions,
            int iterationsPerSample,
            int totalSamples,
            int threadsCount,
            long seed) {
        log.info("Starting multi-threaded rendering with {} threads", threadsCount);

        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        int samplesPerThread = Math.max(1, totalSamples / threadsCount);
        List<Callable<FractalImage>> tasks = new ArrayList<>();

        SplittableRandom random = new SplittableRandom(seed);

        for (int i = 0; i < threadsCount; i++) {
            tasks.add(
                    () -> renderTask(width, height, functions, iterationsPerSample, samplesPerThread, random.split()));
        }

        try {
            List<Future<FractalImage>> futures = executor.invokeAll(tasks);
            FractalImage finalImage = FractalImage.create(width, height);

            for (Future<FractalImage> future : futures) {
                mergeImage(finalImage, future.get());
            }

            log.info("Parallel rendering completed successfully");
            return finalImage;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Rendering interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Rendering task failed", e.getCause());
        } finally {
            executor.shutdown();
        }
    }

    private FractalImage renderTask(
            int width,
            int height,
            List<FlameFunction> functions,
            int iterationsPerSample,
            int samples,
            SplittableRandom random) {
        FractalImage image = FractalImage.create(width, height);
        ImageBounds bounds = calculateImageBounds(width, height);

        for (int sample = 0; sample < samples; sample++) {
            Point currentPoint = new Point(
                    random.nextDouble(bounds.xMin(), bounds.xMax()), random.nextDouble(bounds.yMin(), bounds.yMax()));

            for (int step = 0; step < STEPS_TO_SKIP + iterationsPerSample; step++) {
                FlameFunction func = randomElement(functions, random);
                currentPoint = func.applyTransformation(currentPoint);

                if (step >= STEPS_TO_SKIP) {
                    mapAndColor(image, currentPoint, bounds, func.getColor());
                }
            }
        }
        return image;
    }

    private void mapAndColor(FractalImage image, Point p, ImageBounds bounds, Color color) {
        if (p.x() < bounds.xMin() || p.x() > bounds.xMax() || p.y() < bounds.yMin() || p.y() > bounds.yMax()) {
            return;
        }

        int pixelX = (int) ((p.x() - bounds.xMin()) / (bounds.xMax() - bounds.xMin()) * image.getWidth());
        int pixelY = (int) ((p.y() - bounds.yMin()) / (bounds.yMax() - bounds.yMin()) * image.getHeight());

        if (image.inBounds(pixelX, pixelY)) {
            image.getPixel(pixelX, pixelY).addHit(color);
            image.updateMaxHitCount(image.getPixel(pixelX, pixelY).getHitCount());
        }
    }

    private ImageBounds calculateImageBounds(int width, int height) {
        double ratio = (double) width / height;
        double worldWidth = ratio * IMAGE_HEIGHT;
        return new ImageBounds(-worldWidth / 2, worldWidth / 2, -IMAGE_HEIGHT / 2, IMAGE_HEIGHT / 2);
    }

    private void mergeImage(FractalImage target, FractalImage source) {
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
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("list is null or empty");
        }
        return list.get(random.nextInt(list.size()));
    }

    private record ImageBounds(double xMin, double xMax, double yMin, double yMax) {}
}
