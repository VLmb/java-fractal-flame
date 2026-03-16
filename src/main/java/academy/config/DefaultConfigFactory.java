package academy.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultConfigFactory {

    public static AppConfig create() {
        return AppConfig.builder()
                .setDefaultValues()
                .withAffineCoefficients(DefaultConfigSpec.AFFINE_COEFFICIENTS)
                .withTransformations(DefaultConfigSpec.TRANSFORMATIONS)
                .build();
    }
}
