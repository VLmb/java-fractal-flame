package academy.generation;

import academy.model.ImageBuffer;
import academy.model.Pixel;

public class LogCorrector {

    public static void correct(ImageBuffer imageBuffer) {
        int maxHitCount = imageBuffer.getMaxHitCount();

        if (maxHitCount == 0) {
            return;
        }

        double maxLog = Math.log10(maxHitCount);

        for (int y = 0; y < imageBuffer.getHeight(); y++) {
            for (int x = 0; x < imageBuffer.getWidth(); x++) {
                Pixel pixel = imageBuffer.getPixel(x, y);

                if (pixel.getHitCount() == 0) {
                    continue;
                }

                correctPixel(pixel, maxLog);
            }
        }
    }

    private static void correctPixel(Pixel pixel, double maxLog) {
        double logIntensity = Math.log10(pixel.getHitCount()) / maxLog;

        double colorScale = logIntensity / pixel.getHitCount();

        int red = (int) (pixel.getRed() * colorScale);
        int green = (int) (pixel.getGreen() * colorScale);
        int blue = (int) (pixel.getBlue() * colorScale);

        pixel.setRed(red);
        pixel.setGreen(green);
        pixel.setBlue(blue);
    }
}
