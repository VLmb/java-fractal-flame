package academy.render.transformations;

import academy.model.Point;

public class SphericalTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double r2 = point.x() * point.x() + point.y() * point.y();

        if (r2 == 0) {
            return point;
        }

        double newX = point.x() / r2;
        double newY = point.y() / r2;

        return new Point(newX, newY);
    }
}
