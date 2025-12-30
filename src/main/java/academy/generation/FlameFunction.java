package academy.generation;

import academy.generation.transformations.Transformation;
import academy.generation.transformations.TransformationFactory;
import academy.model.AffineCoefficients;
import academy.model.Point;
import academy.model.TransformationSpec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
public class FlameFunction {

    private static final int MIN_COLOR_COMPONENT = 0;
    private static final int MAX_COLOR_COMPONENT = 256;

    private final List<TransformationSpec> transformations;
    private final AffineCoefficients coefficients;
    @Getter
    private final Color color;

    public FlameFunction(AffineCoefficients coefficients, List<TransformationSpec> transformations, Random random) {
        this.transformations = transformations;
        this.coefficients = coefficients;
        this.color = getRandomColor(random);
        log.debug("FlameFunction created with {} transformations", transformations != null ? transformations.size() : 0);
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

    private Color getRandomColor(Random random) {
        int r = random.nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        int g = random.nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        int b = random.nextInt(MIN_COLOR_COMPONENT, MAX_COLOR_COMPONENT);
        return new Color(r, g, b);
    }
}
