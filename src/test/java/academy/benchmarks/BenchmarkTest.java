package academy.benchmarks;

import academy.config.AppConfig;
import academy.render.FlameFunction;
import academy.render.FlameFunctionFactory;
import academy.render.FractalRenderer;
import academy.model.TransformationSpec;
import academy.model.TransformationType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BenchmarkTest {

    @Test
    void shouldCompareTimeOfDifferentThreadCount() {
        AppConfig config = AppConfig.builder()
            .loadDefaultValues()
            .setIterationCount(2500)
            .setCountOfSamples(40000)
            .overlayTransformations(
                List.of(
                    new TransformationSpec(TransformationType.HANDKERCHIEF, 1.0),
                    new TransformationSpec(TransformationType.SWIRL, 0.8),
                    new TransformationSpec(TransformationType.SINUSOIDAL, 0.5)
                )
            )
            .overlayAffineCoefficients(
                List.of(
                    new academy.model.AffineCoefficients(0.5,  0.0,  0.5, 0.0, 0.5,  0.0),
                    new academy.model.AffineCoefficients(0.5,  0.0, -0.5, 0.0, 0.5,  0.0),
                    new academy.model.AffineCoefficients(0.5,  0.0,  0.0, 0.0, 0.5,  0.5),
                    new academy.model.AffineCoefficients(0.5,  0.0,  0.0, 0.0, 0.5, -0.5),
                    new academy.model.AffineCoefficients(0.0, -0.5,  0.0, 0.5, 0.0,  0.0)
                )
            )
            .build();

        long singleThreadTime = trackDuration(1, config);
        long twoThreadsTime = trackDuration(2, config);
        long fourThreadsTime = trackDuration(4, config);
        long eightThreadsTime = trackDuration(8, config);

        System.out.println("Single Thread Time: " + singleThreadTime + " ms");
        System.out.println("Two Threads Time: " + twoThreadsTime + " ms");
        System.out.println("Four Threads Time: " + fourThreadsTime + " ms");
        System.out.println("Eight Threads Time: " + eightThreadsTime + " ms");

        assertTrue(singleThreadTime > twoThreadsTime &&
            twoThreadsTime > fourThreadsTime &&
            fourThreadsTime > eightThreadsTime
        );

    }

    long trackDuration(int countOfThreads, AppConfig config) {
        List<FlameFunction> functions = FlameFunctionFactory.createFunctions(
            config.affineCoefficients(),
            config.transformations(),
            config.seed()
        );

        FractalRenderer renderer = new FractalRenderer();

        if (countOfThreads == 1) {
            long start = System.currentTimeMillis();
            renderer.renderFractal(
                config.size().width(),
                config.size().height(),
                functions,
                config.iterationCount(),
                config.countOfSamples(),
                (long) config.seed()
            );
            return System.currentTimeMillis() - start;
        } else {
            long start = System.currentTimeMillis();
            renderer.renderFractalParallel(
                config.size().width(),
                config.size().height(),
                functions,
                config.iterationCount(),
                config.countOfSamples(),
                countOfThreads,
                (long) config.seed()
            );
            return System.currentTimeMillis() - start;
        }
    }
}
