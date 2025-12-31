package academy.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.model.AffineCoefficients;
import academy.model.TransformationSpec;
import academy.model.TransformationType;
import java.util.List;
import org.junit.jupiter.api.Test;

class ConfigBuilderTest {

    @Test
    void shouldOverlayConfigAndOverrideOnlyProvidedFields() {
        AppConfig base = AppConfig.builder()
                .setDefaultValues()
                .withWidth(800)
                .withHeight(600)
                .withIterationCount(1000)
                .withSampleCount(800)
                .withOutputPath("test.png")
                .withThreads(2)
                .withSeed(10)
                .withTransformations(List.of(new TransformationSpec(TransformationType.SWIRL, 1.0)))
                .withAffineCoefficients(List.of(new AffineCoefficients(0.5, 0.0, 0.5, 0.0, 0.5, 0.0)))
                .build();

        AppConfig overlay = new AppConfig(
                new AppConfig.Size(1200, 0),
                0,
                222,
                null,
                8,
                10,
                List.of(new TransformationSpec(TransformationType.SINUSOIDAL, 0.4)),
                List.of());

        AppConfig result = AppConfig.builder()
                .setDefaultValues()
                .overlayConfig(base)
                .overlayConfig(overlay)
                .build();

        assertEquals(1200, result.size().width());
        assertEquals(600, result.size().height());

        assertEquals(1000, result.iterationCount());
        assertEquals(222, result.sampleCount());
        assertEquals("test.png", result.outputPath());
        assertEquals(8, result.threads());
        assertEquals(10L, result.seed());

        assertEquals(List.of(new TransformationSpec(TransformationType.SINUSOIDAL, 0.4)), result.transformations());
        assertEquals(List.of(new AffineCoefficients(0.5, 0.0, 0.5, 0.0, 0.5, 0.0)), result.affineCoefficients());
    }
}
