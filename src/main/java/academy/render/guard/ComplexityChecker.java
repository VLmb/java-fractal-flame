package academy.render.guard;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ComplexityChecker {

    private static final long MAX_ITERATIONS_PER_THREAD = 2_000_000_000L;

    public static void checkComplexity(int iterationCount, int sampleCount, int threadCount) {

        long iterationsPerThread = (long) iterationCount * sampleCount / threadCount;
        if (iterationsPerThread > MAX_ITERATIONS_PER_THREAD) {
            log.warn(
                    "A large number of iterations per thread: {}, the program will take a long time to complete. To speed up the program, try lowering iterationCount, sampleCount or increase threads count.",
                    iterationsPerThread);
        }
    }
}
