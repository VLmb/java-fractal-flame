package academy.generation;

import academy.model.AffineCoefficients;
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

public class CoefficientGenerator {

    private static final int MIN_COEFFICIENT = -1;
    private static final int MAX_COEFFICIENT = 1;

    private static final int COEFFICIENT_NORM_BOUND = 1;

    private static final int MIN_COLOR_COMPONENT = 0;
    private static final int MAX_COLOR_COMPONENT= 256;

    private int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public AffineCoefficients generate() {
        while (true) {

            double a = randomInt(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double d = randomInt(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double b = randomInt(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double e = randomInt(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double c = randomInt(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double f = randomInt(MIN_COEFFICIENT, MAX_COEFFICIENT);

            if (isValid(a, b, d, e)) {
                Color color = randomColor();
                return new AffineCoefficients(a, b, c, d, e, f, color);
            }
        }
    }

    private boolean isValid(double a, double b, double d, double e) {
        return (a * a + d * d < COEFFICIENT_NORM_BOUND)
            && (b * b + e * e < COEFFICIENT_NORM_BOUND)
            && (a * a + b * b + d * d + e * e < 1 + (a * e - b * d) * (a * e - b * d));
    }

    private Color randomColor() {
        int r = ThreadLocalRandom.current().nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        int g = ThreadLocalRandom.current().nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        int b = ThreadLocalRandom.current().nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        return new Color(r, g, b);
    }
}
