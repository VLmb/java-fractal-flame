package academy.render.transformations;

import academy.model.Point;

public class SwirlTransformation implements Transformation {
    @Override
    public Point apply(Point p) {

        double r2 = p.radiusSquared();
        double sinR2 = Math.sin(r2);
        double cosR2 = Math.cos(r2);

        double newX = p.x() * sinR2 - p.y() * cosR2;
        double newY = p.x() * cosR2 + p.y() * sinR2;

        return new Point(newX, newY);
    }
}
