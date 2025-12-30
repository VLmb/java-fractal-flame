package academy.render.transformations;

import academy.model.Point;

public class HeartTransformation implements Transformation {

    @Override
    public Point apply(Point point) {
        double radius = point.radius();
        double angle = point.angle();

        double newX = radius * Math.sin(angle * radius);
        double newY = -radius * Math.cos(angle * radius);

        return new Point(newX, newY);
    }

}
