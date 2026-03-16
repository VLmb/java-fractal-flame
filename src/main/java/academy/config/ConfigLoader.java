package academy.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    public static AppConfig loadConfig(Path configPath) {
        try {
            return MAPPER.readValue(Files.newBufferedReader(configPath), AppConfig.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config: " + configPath.toAbsolutePath(), e);
        }
    }

    public static AppConfig createDefaultConfig() {
        return DefaultConfigFactory.create();
    }
}
