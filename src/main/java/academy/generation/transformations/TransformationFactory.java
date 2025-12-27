package academy.generation.transformations;

import academy.model.TransformationsEnum;
import java.util.HashMap;
import java.util.Map;

public class TransformationFactory {

    private static final Map<TransformationsEnum, Transformation> transformations = Map.of(
            TransformationsEnum.LINEAR, new LinearTransformation(),
            TransformationsEnum.HEART, new HeartTransformation(),
            TransformationsEnum.POLAR, new PolarTransformation(),
            TransformationsEnum.DISC, new DiscTransformation()
    );



    public static Transformation getTransformation(TransformationsEnum type) {
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
