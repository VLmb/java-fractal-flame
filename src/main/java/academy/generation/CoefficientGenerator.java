package academy.generation;

import academy.model.AffineCoefficients;
import java.util.Random;

public class CoefficientGenerator {

    private static final double MIN_COEFFICIENT = -1.0;
    private static final double MAX_COEFFICIENT = 1.0;

    private static final int COEFFICIENT_NORM_BOUND = 1;

    public static AffineCoefficients generate(Random random) {
        while (true) {

            double a = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double d = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double b = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double e = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double c = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double f = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);

            if (isValid(a, b, d, e)) {
                return new AffineCoefficients(a, b, c, d, e, f);
            }
        }
    }

    private static boolean isValid(double a, double b, double d, double e) {
        return (a * a + d * d < COEFFICIENT_NORM_BOUND)
            && (b * b + e * e < COEFFICIENT_NORM_BOUND)
            && (a * a + b * b + d * d + e * e < 1 + (a * e - b * d) * (a * e - b * d));
    }
}
