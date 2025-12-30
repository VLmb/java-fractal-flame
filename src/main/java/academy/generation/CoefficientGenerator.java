package academy.generation;

import academy.model.AffineCoefficients;
import java.awt.Color;
import java.util.Random;

public class CoefficientGenerator {

    private static final double MIN_COEFFICIENT = -1.0;
    private static final double MAX_COEFFICIENT = 1.0;

    private static final int COEFFICIENT_NORM_BOUND = 1;

    private static final int MIN_COLOR_COMPONENT = 0;
    private static final int MAX_COLOR_COMPONENT= 256;

    public static AffineCoefficients generate(Random random) {
        while (true) {

            double a = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double d = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double b = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double e = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double c = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);
            double f = random.nextDouble(MIN_COEFFICIENT, MAX_COEFFICIENT);

            if (isValid(a, b, d, e)) {
                Color color = randomColor(random);
                return new AffineCoefficients(a, b, c, d, e, f, color);
            }
        }
    }

    private static boolean isValid(double a, double b, double d, double e) {
        return (a * a + d * d < COEFFICIENT_NORM_BOUND)
            && (b * b + e * e < COEFFICIENT_NORM_BOUND)
            && (a * a + b * b + d * d + e * e < 1 + (a * e - b * d) * (a * e - b * d));
    }

    private static Color randomColor(Random random) {
        int r = random.nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        int g = random.nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        int b = random.nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        return new Color(r, g, b);
    }

    public static AffineCoefficients generateRandomColor(AffineCoefficients affineCoefficients, Random random) {
        Color color = randomColor(random);
        return new AffineCoefficients(
            affineCoefficients.a(),
            affineCoefficients.b(),
            affineCoefficients.c(),
            affineCoefficients.d(),
            affineCoefficients.e(),
            affineCoefficients.f(),
            color
        );
    }
}
