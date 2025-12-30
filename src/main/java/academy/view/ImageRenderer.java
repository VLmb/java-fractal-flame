package academy.view;

import academy.model.ImageBuffer;
import academy.model.Pixel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageRenderer {
    public static void save(ImageBuffer buffer, Path path, ImageFormat format) {
        BufferedImage image = new BufferedImage(buffer.getWidth(), buffer.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < buffer.getHeight(); y++) {
            for (int x = 0; x < buffer.getWidth(); x++) {
                Pixel pixel = buffer.getPixel(x, y);

                // cтруктура int (32 бита): [00000000][RRRRRRRR][GGGGGGGG][BBBBBBBB]
                int rgb = (pixel.getRed() << 16) | (pixel.getGreen() << 8) | pixel.getBlue();

                image.setRGB(x, y, rgb);
            }
        }

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            ImageIO.write(image, format.getExtension(), path.toFile());

        } catch (IOException e) {
            throw new RuntimeException("Failed to save image to " + path, e);
        }
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
