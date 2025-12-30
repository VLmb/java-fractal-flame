package academy.generation;

import academy.generation.transformations.Transformation;
import academy.model.AffineCoefficients;
import academy.model.TransformationSpec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FlameFunctionFactory {

    public static List<FlameFunction> createFunctions(
        List<AffineCoefficients> affineCoefficients,
        List<TransformationSpec> transformations,
        long seed
    ) {
        if (transformations == null || transformations.isEmpty()) {
            throw new IllegalArgumentException("Transformations list must not be null or empty");
        }
        if (affineCoefficients == null || affineCoefficients.isEmpty()) {
            throw new IllegalArgumentException("Affine coefficients list must not be null or empty");
        }

        Random random = new Random(seed);
        var functions = new ArrayList<FlameFunction>();
        for (var coefficients : affineCoefficients) {
            functions.add(new FlameFunction(coefficients, transformations, random));
        }

        return functions;
    }
}
