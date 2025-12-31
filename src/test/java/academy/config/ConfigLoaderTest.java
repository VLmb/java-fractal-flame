package academy.config;

import academy.app.DefaultValues;
import academy.model.AffineCoefficients;
import academy.model.TransformationSpec;
import academy.model.TransformationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigLoaderTest {
    @TempDir
    Path tempDir;

    @Test
    void shouldLoadConfig() throws Exception {
        Path jsonConfigPath = tempDir.resolve("config.json");

        String content = """
            {
              "size": { "width": 1000, "height": 800 },
              "iteration_count": 2500,
              "count_of_samples": 1200000,
              "output_path": "test.png",
              "threads": 4,
              "seed": 12345,
              "functions": [
                { "name": "swirl", "weight": 1.0 },
                { "name": "sinusoidal", "weight": 0.4 }
              ],
              "affine_params": [
                { "a": 0.5, "b": 0.0, "c": 0.5, "d": 0.0, "e": 0.5, "f": 0.0 },
                { "a": 0.0, "b": -0.5, "c": 0.0, "d": 0.5, "e": 0.0, "f": 0.0 }
              ]
            }
            """;

        Files.writeString(jsonConfigPath, content);

        AppConfig loadedConfig = ConfigLoader.loadConfig(jsonConfigPath);

        assertNotNull(loadedConfig.size());
        assertEquals(1000, loadedConfig.size().width());
        assertEquals(800, loadedConfig.size().height());
        assertEquals(2500, loadedConfig.iterationCount());
        assertEquals(1200000, loadedConfig.sampleCount());
        assertEquals("test.png", loadedConfig.outputPath());
        assertEquals(4, loadedConfig.threads());
        assertEquals(12345L, loadedConfig.seed());

        assertNotNull(loadedConfig.transformations());
        assertEquals(2, loadedConfig.transformations().size());
        assertEquals(new TransformationSpec(TransformationType.SWIRL, 1.0), loadedConfig.transformations().get(0));
        assertEquals(new TransformationSpec(TransformationType.SINUSOIDAL, 0.4), loadedConfig.transformations().get(1));

        assertNotNull(loadedConfig.affineCoefficients());
        assertEquals(2, loadedConfig.affineCoefficients().size());
        assertEquals(new AffineCoefficients(0.5, 0.0, 0.5, 0.0, 0.5, 0.0), loadedConfig.affineCoefficients().get(0));
        assertEquals(new AffineCoefficients(0.0, -0.5, 0.0, 0.5, 0.0, 0.0), loadedConfig.affineCoefficients().get(1));
    }

    @Test
    void shouldCreateDefaultConfig() {
        AppConfig defaultConfig = ConfigLoader.createDefaultConfig();

        assertNotNull(defaultConfig.size());
        assertEquals(DefaultValues.WIDTH, defaultConfig.size().width());
        assertEquals(DefaultValues.HEIGHT, defaultConfig.size().height());
        assertEquals(DefaultValues.ITERATION_COUNT, defaultConfig.iterationCount());
        assertEquals(DefaultValues.OUTPUT_PATH, defaultConfig.outputPath());
        assertEquals(DefaultValues.THREADS, defaultConfig.threads());
        assertEquals(DefaultConfigSpec.SEED, defaultConfig.seed());
        assertEquals(DefaultConfigSpec.TRANSFORMATIONS, defaultConfig.transformations());
        assertEquals(DefaultConfigSpec.AFFINE_COEFFICIENTS, defaultConfig.affineCoefficients());
    }
}
