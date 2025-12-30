package academy.config;

import academy.app.DefaultValues;
import academy.model.AffineCoefficients;
import academy.model.TransformationSpec;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record AppConfig(
    Size size,
    int iterationCount,
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

    public static class ConfigBuilder {
        private int width;
        private int height;
        private int iterationCount;
        private String outputPath;
        private int threads;
        private long seed;
        private List<TransformationSpec> transformations = new ArrayList<>();
        private List<AffineCoefficients> affineCoefficients = new ArrayList<>();

        public ConfigBuilder loadDefaultValues() {
            this.width = DefaultValues.WIDTH;
            this.height = DefaultValues.HEIGHT;
            this.iterationCount = DefaultValues.ITERATION_COUNT;
            this.outputPath = DefaultValues.OUTPUT_PATH;
            this.threads = DefaultValues.THREADS;
            this.seed = DefaultValues.SEED;
            return this;
        }

        public ConfigBuilder overlayConfig(AppConfig config) {
            if (config == null) {
                return this;
            }
            //Тернарный оператор показался мне здесь очень уместным :)
            this.width = (config.size() != null && config.size().width() > 0) ? config.size().width() : this.width;
            this.height = (config.size() != null && config.size().height() > 0) ? config.size().height() : this.height;
            this.iterationCount = config.iterationCount() > 0 ? config.iterationCount() : this.iterationCount;
            this.outputPath = config.outputPath() != null ? config.outputPath() : this.outputPath;
            this.threads = config.threads() > 0 ? config.threads() : this.threads;
            this.seed = config.seed() != 0.0 ? config.seed() : this.seed;
            if (config.transformations() != null && !config.transformations().isEmpty()) {
                this.transformations.clear();
                this.transformations.addAll(config.transformations());
            }
            if (config.affineCoefficients() != null && !config.affineCoefficients().isEmpty()) {
                this.affineCoefficients.clear();
                this.affineCoefficients.addAll(config.affineCoefficients());
            }
            return this;
        }

        public ConfigBuilder setWidth(int width) {
            this.width = width;
            return this;
        }

        public ConfigBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public ConfigBuilder setIterationCount(int iterationCount) {
            this.iterationCount = iterationCount;
            return this;
        }

        public ConfigBuilder setOutputPath(String outputPath) {
            this.outputPath = outputPath;
            return this;
        }

        public ConfigBuilder setThreads(int threads) {
            this.threads = threads;
            return this;
        }

        public ConfigBuilder setSeed(long seed) {
            this.seed = seed;
            return this;
        }

        public ConfigBuilder overlayTransformations(List<TransformationSpec> transformations) {
            if (transformations == null || transformations.isEmpty()) {
                return this;
            }
            this.transformations = transformations;
            return this;
        }

        public ConfigBuilder overlayAffineCoefficients(List<AffineCoefficients> coefficients) {
            if (coefficients == null || coefficients.isEmpty()) {
                return this;
            }
            this.affineCoefficients = coefficients;
            return this;
        }

        public ConfigBuilder addTransformations(TransformationSpec... transformations) {
            Collections.addAll(this.transformations, transformations);
            return this;
        }

        public ConfigBuilder addAffineCoefficients(AffineCoefficients... coefficients) {
            Collections.addAll(this.affineCoefficients, coefficients);
            return this;
        }

        public AppConfig build() {
            return new AppConfig(
                new Size(width, height),
                iterationCount,
                outputPath,
                threads,
                seed,
                transformations,
                affineCoefficients
            );
        }
    }
}
