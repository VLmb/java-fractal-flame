package academy.view;

import academy.model.FractalImage;
import academy.model.Pixel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageRenderer {

    public static void save(FractalImage fractalImage, Path path, ImageFormat format) {
        if (fractalImage == null) {
            throw new IllegalArgumentException("Image fractalImage cannot be null");
        }
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        int width = fractalImage.getWidth();
        int height = fractalImage.getHeight();

        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Invalid image size");

        log.info("Saving image ({}x{}) to {}", width, height, path);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] rgb = new int[width * height];
        int idx = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel p = fractalImage.getPixel(x, y);
                rgb[idx++] = packRgb(p.getRed(), p.getGreen(), p.getBlue());
            }
        }

        image.setRGB(0, 0, width, height, rgb, 0, width);

        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            ImageIO.write(image, format.getExtension(), path.toFile());
            log.info("Image successfully written to disk");
        } catch (IOException e) {
            log.warn("Failed to write image to {}", path, e);
            throw new RuntimeException("Failed to save image to " + path, e);
        }
    }

    private static int packRgb(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    public enum ImageFormat {
        PNG("png"),
        JPEG("jpg"),
        BMP("bmp");

        private final String extension;

        ImageFormat(String extension) {
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }
    }
}
