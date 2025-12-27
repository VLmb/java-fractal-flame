package academy.model;

public record Point(
    double x,
    double y)
{

    public double radius() {
        return Math.sqrt(x * x + y * y);
    }

    public double radiusSquared() {
        return x * x + y * y;
    }

    public double angle() {
        return Math.atan2(x, y);
    }

}
