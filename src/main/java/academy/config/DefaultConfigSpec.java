package academy.config;

import academy.app.DefaultValues;
import academy.model.AffineCoefficients;
import academy.model.TransformationSpec;
import academy.model.TransformationType;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultConfigSpec {

    public static final long SEED = DefaultValues.SEED;
    public static final List<TransformationSpec> TRANSFORMATIONS = List.of(
            new TransformationSpec(TransformationType.SWIRL, 1.0),
            new TransformationSpec(TransformationType.SINUSOIDAL, 0.40));
    public static final List<AffineCoefficients> AFFINE_COEFFICIENTS = List.of(
            new AffineCoefficients(0.5, 0.0, 0.5, 0.0, 0.5, 0.0),
            new AffineCoefficients(0.5, 0.0, -0.5, 0.0, 0.5, 0.0),
            new AffineCoefficients(0.5, 0.0, 0.0, 0.0, 0.5, 0.5),
            new AffineCoefficients(0.5, 0.0, 0.0, 0.0, 0.5, -0.5),
            new AffineCoefficients(0.0, -0.5, 0.0, 0.5, 0.0, 0.0));
}
