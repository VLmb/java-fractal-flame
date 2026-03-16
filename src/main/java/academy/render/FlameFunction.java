package academy.render;

import academy.model.AffineCoefficients;
import academy.model.Point;
import academy.model.TransformationSpec;
import academy.render.transformations.Transformation;
import academy.render.transformations.TransformationFactory;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FlameFunction {

    private static final int MIN_COLOR_COMPONENT = 0;
    private static final int MAX_COLOR_COMPONENT = 256;

    private final List<TransformationSpec> transformations;
    private final AffineCoefficients coefficients;

    @Getter
    private final Color color;

    public static FlameFunction createFlameFunction(
            AffineCoefficients coefficients, List<TransformationSpec> transformations, Random random) {
        return new FlameFunction(transformations, coefficients, getRandomColor(random));
    }

    public Point applyTransformation(Point point) {
        if (transformations == null || transformations.isEmpty()) {
            log.warn("Attempted to apply transformation with empty transformation list");
            throw new IllegalArgumentException("Transformations list cannot be null or empty");
        }
        if (coefficients == null) {
            log.warn("Affine coefficients are null");
            throw new IllegalArgumentException("Affine coefficients cannot be null");
        }
        if (point == null) {
            log.warn("Input point is null");
            throw new IllegalArgumentException("Point cannot be null");
        }

        double affineX = coefficients.a() * point.x() + coefficients.b() * point.y() + coefficients.c();
        double affineY = coefficients.d() * point.x() + coefficients.e() * point.y() + coefficients.f();

        double sumX = 0.0;
        double sumY = 0.0;

        for (TransformationSpec spec : transformations) {
            double weight = spec.weight();
            if (weight == 0.0) {
                continue;
            }

            Transformation transformation = TransformationFactory.getTransformation(spec.name());
            Point out = transformation.apply(new Point(affineX, affineY));

            sumX += weight * out.x();
            sumY += weight * out.y();
        }

        return new Point(sumX, sumY);
    }

    private static Color getRandomColor(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("Random generator cannot be null");
        }
        int r = random.nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        int g = random.nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        int b = random.nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        return new Color(r, g, b);
    }
}
