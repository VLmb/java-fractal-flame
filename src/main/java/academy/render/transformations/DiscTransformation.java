package academy.render.transformations;

import academy.model.Point;

public class DiscTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double radius = point.radius();
        double angle = point.angle();

        double factor = angle / Math.PI;

        return new Point(
            factor * Math.sin(Math.PI * radius),
            factor * Math.cos(Math.PI * radius)
        );
    }

}
