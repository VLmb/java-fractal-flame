package academy.benchmarks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.config.AppConfig;
import academy.model.TransformationSpec;
import academy.model.TransformationType;
import academy.render.FlameFunction;
import academy.render.FlameFunctionFactory;
import academy.render.FractalRenderer;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class BenchmarkTest {

    @Disabled
    @Test
    void shouldCompareTimeOfDifferentThreadCount() {
        AppConfig config = AppConfig.builder()
                .setDefaultValues()
                .withTransformations(List.of(
                        new TransformationSpec(TransformationType.HANDKERCHIEF, 1.0),
                        new TransformationSpec(TransformationType.SWIRL, 0.8),
                        new TransformationSpec(TransformationType.SINUSOIDAL, 0.5)))
                .withAffineCoefficients(List.of(
                        new academy.model.AffineCoefficients(0.5, 0.0, 0.5, 0.0, 0.5, 0.0),
                        new academy.model.AffineCoefficients(0.5, 0.0, -0.5, 0.0, 0.5, 0.0),
                        new academy.model.AffineCoefficients(0.5, 0.0, 0.0, 0.0, 0.5, 0.5),
                        new academy.model.AffineCoefficients(0.5, 0.0, 0.0, 0.0, 0.5, -0.5),
                        new academy.model.AffineCoefficients(0.0, -0.5, 0.0, 0.5, 0.0, 0.0)))
                .build();

        long singleThreadTime = trackDuration(1, config);
        long twoThreadsTime = trackDuration(2, config);
        long fourThreadsTime = trackDuration(4, config);
        long eightThreadsTime = trackDuration(8, config);

        log.info("Single Thread Time: " + singleThreadTime + " ms");
        log.info("Two Threads Time: " + twoThreadsTime + " ms");
        log.info("Four Threads Time: " + fourThreadsTime + " ms");
        log.info("Eight Threads Time: " + eightThreadsTime + " ms");

        assertTrue(singleThreadTime > twoThreadsTime
                && twoThreadsTime > fourThreadsTime
                && fourThreadsTime > eightThreadsTime);
    }

    long trackDuration(int countOfThreads, AppConfig config) {
        List<FlameFunction> functions = FlameFunctionFactory.createFunctions(
                config.affineCoefficients(), config.transformations(), config.seed());

        FractalRenderer renderer = new FractalRenderer();

        if (countOfThreads == 1) {
            long start = System.currentTimeMillis();
            renderer.renderFractal(
                    config.size().width(),
                    config.size().height(),
                    functions,
                    config.iterationCount(),
                    config.sampleCount(),
                    (long) config.seed());
            return System.currentTimeMillis() - start;
        } else {
            long start = System.currentTimeMillis();
            renderer.renderFractalParallel(
                    config.size().width(),
                    config.size().height(),
                    functions,
                    config.iterationCount(),
                    config.sampleCount(),
                    countOfThreads,
                    (long) config.seed());
            return System.currentTimeMillis() - start;
        }
    }
}
