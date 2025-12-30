package academy.view;

import academy.model.ImageBuffer;
import academy.model.Pixel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class ImageRenderer {

    public static void save(ImageBuffer buffer, Path path, ImageFormat format) {
        int width = buffer.getWidth();
        int height = buffer.getHeight();

        log.info("Saving image ({}x{}) to {}", width, height, path);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] rgb = new int[width * height];
        int idx = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel p = buffer.getPixel(x, y);
                rgb[idx++] = packRgb(p.getRed(), p.getGreen(), p.getBlue());
            }
        }

        image.setRGB(0, 0, width, height, rgb, 0, width);

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
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
