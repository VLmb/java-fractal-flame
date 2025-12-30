package academy.render;

import academy.model.FractalImage;
import academy.model.Pixel;
import academy.model.Point;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FractalRendererTest {

    private final FractalRenderer renderer = new FractalRenderer();
    private final FlameFunction fakeFunction = new FlameFunction(
        null,
        null,
        new Color(28, 54, 93)
    ) {
        @Override
        public Point applyTransformation(Point p) {
            return new Point(0.0, 0.0);
        }
    };

    @Test
    void shouldReturnSameResultForSameSeed() {
        long seed = 12345L;
        int width = 100;
        int height = 100;
        List<FlameFunction> functions = List.of(fakeFunction);
        int iterationsPerSample = 100;
        int samplesPerIteration = 100;
        int threadsCount = 4;


        FractalImage image1 = renderer.renderFractalParallel(
            width,
            height,
            functions,
            iterationsPerSample,
            samplesPerIteration,
            threadsCount,
            seed
        );

        FractalImage image2 = renderer.renderFractalParallel(
            width,
            height,
            functions,
            iterationsPerSample,
            samplesPerIteration,
            threadsCount,
            seed
        );

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel p1 = image1.getPixel(x, y);
                Pixel p2 = image2.getPixel(x, y);

                assertEquals(p1.getHitCount(), p2.getHitCount());
                assertEquals(p1.getRed(), p2.getRed());
                assertEquals(p1.getGreen(), p2.getGreen());
                assertEquals(p1.getBlue(), p2.getBlue());
            }
        }
    }

    @Test
    void shouldReturnValidResult() {
        long seed = 12345L;
        int width = 100;
        int height = 100;
        List<FlameFunction> functions = List.of(fakeFunction);
        int iterationsPerSample = 100;
        int samplesPerIteration = 100;
        int threadsCount = 4;

        FractalImage image = renderer.renderFractalParallel(
            width,
            height,
            functions,
            iterationsPerSample,
            samplesPerIteration,
            threadsCount,
            seed
        );

        assertTrue(image.getMaxHitCount() == 10000);
        assertTrue(image.getPixel(50, 50).getHitCount() == image.getMaxHitCount());
    }
}
