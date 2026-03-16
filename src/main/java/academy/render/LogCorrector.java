package academy.render;

import academy.model.FractalImage;
import academy.model.Pixel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogCorrector {

    public static void correct(FractalImage fractalImage) {
        int maxHitCount = fractalImage.getMaxHitCount();

        if (maxHitCount == 0) {
            log.debug("Log correction skipped: max hit count is zero");
            return;
        }
        if (maxHitCount <= 1) {
            log.debug("Log correction skipped: max hit count is {}", maxHitCount);
            return;
        }

        double maxLog = Math.log10(maxHitCount);
        log.debug("Applying logarithmic correction with maxHitCount={}", maxHitCount);

        for (int y = 0; y < fractalImage.getHeight(); y++) {
            for (int x = 0; x < fractalImage.getWidth(); x++) {
                Pixel pixel = fractalImage.getPixel(x, y);

                if (pixel.getHitCount() == 0) {
                    continue;
                }

                correctPixel(pixel, maxLog);
            }
        }
    }

    private static void correctPixel(Pixel pixel, double maxLog) {
        int hit = pixel.getHitCount();

        double intensity = Math.log10(hit) / maxLog;
        double invHit = 1.0 / hit;
        double scale = intensity * invHit;

        int red = (int) (pixel.getRed() * scale);
        int green = (int) (pixel.getGreen() * scale);
        int blue = (int) (pixel.getBlue() * scale);

        pixel.setRed(red);
        pixel.setGreen(green);
        pixel.setBlue(blue);
    }
}
