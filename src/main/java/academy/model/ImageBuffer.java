package academy.model;

public record ImageBuffer(
    int width,
    int height,
    Pixel[] pixels
) {

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
}
