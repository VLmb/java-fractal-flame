package academy.generation.transformations;

import academy.model.Point;

public class LinearTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        return point;
    }
}
