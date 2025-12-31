package academy.app;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultValues {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static final long SEED = 12345L;
    public static final int ITERATION_COUNT = 2500;
    public static final String OUTPUT_PATH = "result.png";
    public static final int THREADS = 1;
    public static final double SAMPLES_PER_PIXEL = 0.25;
}
