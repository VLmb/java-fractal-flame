package academy.app;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultValues {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final long SEED = 777L;
    public static final int ITERATION_COUNT = 2500;
    public static final String OUTPUT_PATH = "result.png";
    public static final int THREADS = 16;

}
