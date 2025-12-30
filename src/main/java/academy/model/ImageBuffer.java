package academy.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageBuffer {

    private final int width;
    private final int height;
    private final Pixel[] pixels;
    private int maxHitCount = 0;

    public static ImageBuffer create(int width, int height) {
        Pixel[] pixels = new Pixel[width * height];

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = new Pixel();
        }

        return new ImageBuffer(width, height, pixels);
    }

    public boolean inBounds(int x, int y) {

        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Pixel getPixel(int x, int y) {

        return pixels[y * width + x];
    }

    public void updateMaxHitCount(int hitCount) {
        if (hitCount > maxHitCount) {
            maxHitCount = hitCount;
        }
    }
}
