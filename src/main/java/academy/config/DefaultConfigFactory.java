package academy.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultConfigFactory {

    public static AppConfig create() {
        return AppConfig.builder()
            .loadDefaultValues()
            .overlayAffineCoefficients(DefaultConfigSpec.AFFINE_COEFFICIENTS)
            .overlayTransformations(DefaultConfigSpec.TRANSFORMATIONS)
            .build();
    }
}
