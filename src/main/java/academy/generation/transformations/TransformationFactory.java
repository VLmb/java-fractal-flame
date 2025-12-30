package academy.generation.transformations;

import academy.model.TransformationType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransformationFactory {

    private static final Map<TransformationType, Transformation> transformations = Map.of(
            TransformationType.LINEAR, new LinearTransformation(),
            TransformationType.HEART, new HeartTransformation(),
            TransformationType.POLAR, new PolarTransformation(),
            TransformationType.DISC, new DiscTransformation(),
            TransformationType.SWIRL, new SwirlTransformation(),
            TransformationType.SINUSOIDAL, new SinusoidalTransformation()
    );



    public static Transformation getTransformation(TransformationType type) {
        if (type == null) {
            throw new IllegalArgumentException("Transformation type cannot be null");
        }

        if (transformations.containsKey(type)) {
            return transformations.get(type);
        } else {
            throw new IllegalArgumentException("Unknown transformation type: " + type);
        }
    }
}
