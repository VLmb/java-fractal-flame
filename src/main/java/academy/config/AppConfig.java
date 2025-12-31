package academy.config;

import academy.model.AffineCoefficients;
import academy.model.TransformationSpec;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AppConfig(
    Size size,
    int iterationCount,
    @JsonProperty("count_of_samples")
    int sampleCount,
    String outputPath,
    int threads,
    long seed,
    @JsonProperty("functions")
    List<TransformationSpec> transformations,
    @JsonProperty("affine_params")
    List<AffineCoefficients> affineCoefficients
) {

    public record Size(
        int width,
        int height
    ) {}

    public static ConfigBuilder builder() {
        return new ConfigBuilder();
    }
}
