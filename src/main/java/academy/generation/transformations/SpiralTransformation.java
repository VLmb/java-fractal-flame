package academy.generation.transformations;

import academy.model.Point;

public class SpiralTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double radius = point.radius();
        double angle = point.angle();

        if (radius == 0) {
            return point;
        }

        double invRadius = 1.0 / radius;

        double newX = invRadius * (Math.cos(angle) + Math.sin(radius));
        double newY = invRadius * (Math.sin(angle) - Math.cos(radius));

        return new Point(newX, newY);
    }
}
