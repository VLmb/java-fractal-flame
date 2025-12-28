package academy.model;

import lombok.Data;

@Data
public class Pixel {

    private int red;
    private int green;
    private int blue;
    private int hitCount;

    public int red() {
        return hitCount == 0 ? 0 : red / hitCount;
    }
    public int green() {
        return hitCount == 0 ? 0 : green / hitCount;
    }
    public int blue() {
        return hitCount == 0 ? 0 : blue / hitCount;
    }

    public void addHit(int red, int green, int blue) {
        this.hitCount++;
        this.red += red;
        this.green += green;
        this.blue += blue;
    }

    public void merge(Pixel other) {
        this.red += other.red();
        this.green += other.green();
        this.blue += other.blue();
        this.hitCount += other.hitCount;
    }
}
