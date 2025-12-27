package academy.model;

import java.awt.Color;

public record AffineCoefficients(
    double a, double b, double c,
    double d, double e, double f,
    Color color
) {}
