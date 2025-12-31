package academy.render;

import academy.model.AffineCoefficients;
import academy.model.TransformationSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FlameFunctionFactory {

    public static List<FlameFunction> createFunctions(
            List<AffineCoefficients> affineCoefficients, List<TransformationSpec> transformations, long seed) {
        if (transformations == null || transformations.isEmpty()) {
            throw new IllegalArgumentException("Transformations list must not be null or empty");
        }
        if (affineCoefficients == null || affineCoefficients.isEmpty()) {
            throw new IllegalArgumentException("Affine coefficients list must not be null or empty");
        }

        Random random = new Random(seed);
        var functions = new ArrayList<FlameFunction>();
        for (var coefficients : affineCoefficients) {
            functions.add(FlameFunction.createFlameFunction(coefficients, transformations, random));
        }

        return functions;
    }
}
