package academy.config;

import academy.model.AffineCoefficients;
import academy.model.TransformationSpec;
import academy.model.TransformationType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigBuilderTest {

    @Test
    void shouldOverlayConfigAndOverrideOnlyProvidedFields() {
        AppConfig base = AppConfig.builder()
            .loadDefaultValues()
            .setWidth(800)
            .setHeight(600)
            .setIterationCount(1000)
            .setCountOfSamples(800)
            .setOutputPath("test.png")
            .setThreads(2)
            .setSeed(10)
            .overlayTransformations(List.of(new TransformationSpec(TransformationType.SWIRL, 1.0)))
            .overlayAffineCoefficients(List.of(new AffineCoefficients(0.5, 0.0, 0.5, 0.0, 0.5, 0.0)))
            .build();

        AppConfig overlay = new AppConfig(
            new AppConfig.Size(1200, 0),
            0,
            222,
            null,
            8,
            0,
            List.of(new TransformationSpec(TransformationType.SINUSOIDAL, 0.4)),
            List.of()
        );

        AppConfig result = AppConfig.builder()
            .loadDefaultValues()
            .overlayConfig(base)
            .overlayConfig(overlay)
            .build();

        assertEquals(1200, result.size().width());
        assertEquals(600, result.size().height());

        assertEquals(1000, result.iterationCount());
        assertEquals(222, result.countOfSamples());
        assertEquals("test.png", result.outputPath());
        assertEquals(8, result.threads());
        assertEquals(10L, result.seed());

        assertEquals(List.of(new TransformationSpec(TransformationType.SINUSOIDAL, 0.4)), result.transformations());
        assertEquals(List.of(new AffineCoefficients(0.5, 0.0, 0.5, 0.0, 0.5, 0.0)), result.affineCoefficients());
    }

}
