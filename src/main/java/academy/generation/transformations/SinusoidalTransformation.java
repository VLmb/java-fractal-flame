package academy.generation.transformations;

import academy.model.Point;

public class SinusoidalTransformation implements Transformation {
    @Override
    public Point apply(Point p) {
        return new Point(Math.sin(p.x()), Math.sin(p.y()));
    }
}
