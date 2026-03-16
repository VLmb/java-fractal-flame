package academy.render.transformations;

import academy.model.Point;

public class PolarTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double radius = point.radius();
        double angle = point.angle();

        return new Point(angle / Math.PI, radius - 1.0);
    }
}
