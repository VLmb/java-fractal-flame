package academy.config;

import academy.app.DefaultValues;
import academy.model.AffineCoefficients;
import academy.model.TransformationSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ConfigBuilder {

    private int width;
    private int height;
    private int iterationCount;
    private int sampleCount;
    private String outputPath;
    private int threads;
    private long seed;
    private List<TransformationSpec> transformations = new ArrayList<>();
    private List<AffineCoefficients> affineCoefficients = new ArrayList<>();

    public ConfigBuilder setDefaultValues() {
        this.width = DefaultValues.WIDTH;
        this.height = DefaultValues.HEIGHT;
        this.iterationCount = DefaultValues.ITERATION_COUNT;

        long pixels = (long) this.width * this.height;
        this.sampleCount = Math.max(1, (int) (DefaultValues.SAMPLES_PER_PIXEL * pixels));

        this.outputPath = DefaultValues.OUTPUT_PATH;
        this.threads = DefaultValues.THREADS;
        this.seed = DefaultValues.SEED;
        return this;
    }

    public ConfigBuilder overlayConfig(AppConfig config) {
        if (config == null) {
            return this;
        }
        if (config.size() != null) {
            this.width = (config.size().width() > 0) ? config.size().width() : this.width;
            this.height = (config.size().height() > 0) ? config.size().height() : this.height;
        }
        if (config.sampleCount() > 0) {
            this.sampleCount = config.sampleCount();
        }
        if (config.iterationCount() > 0) {
            this.iterationCount = config.iterationCount();
        }
        if (config.outputPath() != null && !config.outputPath().isBlank()) {
            this.outputPath = config.outputPath();
        }
        if (config.threads() > 0) {
            this.threads = config.threads();
        }
        this.seed = config.seed();
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

    public ConfigBuilder withTransformations(List<TransformationSpec> transformations) {
        if (transformations == null || transformations.isEmpty()) {
            return this;
        }
        this.transformations = new ArrayList<>(transformations);
        return this;
    }

    public ConfigBuilder withAffineCoefficients(List<AffineCoefficients> coefficients) {
        if (coefficients == null || coefficients.isEmpty()) {
            return this;
        }
        this.affineCoefficients = new ArrayList<>(coefficients);
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

    public ConfigBuilder withWidth(int width) {
        if (width <= 0) {
            throw new IllegalArgumentException("width must be > 0");
        }
        this.width = width;
        return this;
    }

    public ConfigBuilder withHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("height must be > 0");
        }
        this.height = height;
        return this;
    }

    public ConfigBuilder withSize(int width, int height) {
        return withWidth(width).withHeight(height);
    }

    public ConfigBuilder withIterationCount(int iterationCount) {
        if (iterationCount <= 0) {
            throw new IllegalArgumentException("iterationCount must be > 0");
        }
        this.iterationCount = iterationCount;
        return this;
    }

    public ConfigBuilder withSampleCount(int sampleCount) {
        if (sampleCount <= 0) {
            throw new IllegalArgumentException("sampleCount must be > 0");
        }
        this.sampleCount = sampleCount;
        return this;
    }

    public ConfigBuilder withSamplesPerPixel(double samplesPerPixel) {
        if (samplesPerPixel > 0.0) {
            throw new IllegalArgumentException("samplesPerPixel must be a finite number > 0");
        }
        long pixels = (long) this.width * this.height;
        this.sampleCount = Math.max(1, (int) Math.min(Integer.MAX_VALUE, (long) (samplesPerPixel * pixels)));
        return this;
    }

    public ConfigBuilder withOutputPath(String outputPath) {
        if (outputPath == null || outputPath.isBlank()) {
            throw new IllegalArgumentException("outputPath must not be blank");
        }
        this.outputPath = outputPath;
        return this;
    }

    public ConfigBuilder withThreads(int threads) {
        if (threads <= 0) {
            throw new IllegalArgumentException("threads must be >= 1");
        }
        this.threads = threads;
        return this;
    }

    public ConfigBuilder withSeed(long seed) {
        this.seed = seed;
        return this;
    }

    public AppConfig build() {
        return new AppConfig(
                new AppConfig.Size(width, height),
                iterationCount,
                sampleCount,
                outputPath,
                threads,
                seed,
                List.copyOf(transformations),
                List.copyOf(affineCoefficients));
    }
}
