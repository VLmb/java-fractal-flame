package academy.config;

import academy.app.DefaultValues;
import academy.generation.CoefficientGenerator;
import academy.model.TransformationSpec;
import academy.model.TransformationType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class ConfigLoader {

    private static final double DEFAULT_TRANSFORMATION_WEIGHT = 1.0;
    private static final int DEFAULT_AFFINE_COUNT = 3;

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    public static AppConfig loadConfig(Path configPath) {
        try {
            var config = MAPPER.readValue(Files.newBufferedReader(configPath), AppConfig.class);

            Random random = new Random(DefaultValues.SEED);
            for (int i = 0; i < config.affineCoefficients().size(); i++) {
                var params = CoefficientGenerator.generateRandomColor(config.affineCoefficients().get(i), random);
                config.affineCoefficients().set(i, params);
            }
            return config;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config: " + configPath.toAbsolutePath(), e);
        }
    }

    public static AppConfig createDefaultConfig() {

        AppConfig.ConfigBuilder configBuilder = AppConfig.builder()
            .loadDefaultValues()
            .addTransformations(
                new TransformationSpec(  //ToDo: убрать хардкодинг
                    TransformationType.LINEAR,
                    1.5
                ),
                new TransformationSpec(
                    TransformationType.SWIRL,
                    0.5
                ),
                new TransformationSpec(
                    TransformationType.SINUSOIDAL,
                    2.0
                )
            );

        Random random = new Random(DefaultValues.SEED);

        for (int i = 0; i < DEFAULT_AFFINE_COUNT; i++) {
            configBuilder.addAffineCoefficients(CoefficientGenerator.generate(random));
        }

        return configBuilder.build();
    }
}
