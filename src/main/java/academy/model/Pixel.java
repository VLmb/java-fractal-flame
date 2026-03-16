package academy.model;

import java.awt.Color;
import lombok.Data;

@Data
public class Pixel {

    private int red;
    private int green;
    private int blue;
    private int hitCount;

    public int red() {
        if (hitCount == 0) {
            return 0;
        }
        return red / hitCount;
    }

    public int green() {
        if (hitCount == 0) {
            return 0;
        }
        return green / hitCount;
    }

    public int blue() {
        if (hitCount == 0) {
            return 0;
        }
        return blue / hitCount;
    }

    public void addHit(int red, int green, int blue) {
        this.hitCount++;
        this.red += red;
        this.green += green;
        this.blue += blue;
    }

    public void addHit(Color color) {
        addHit(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void merge(Pixel other) {
        this.red += other.red();
        this.green += other.green();
        this.blue += other.blue();
        this.hitCount += other.hitCount;
    }
}
