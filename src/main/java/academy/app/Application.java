package academy.app;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import academy.config.AppConfig;
import academy.config.ConfigLoader;
import academy.generation.FlameFunction;
import academy.generation.FlameFunctionFactory;
import academy.generation.FractalRenderer;
import academy.model.AffineCoefficients;
import academy.model.ImageBuffer;
import academy.model.Pixel;
import academy.model.TransformationSpec;
import academy.view.ImageRenderer;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Slf4j
@Command(name = "Fractal Flame Application", version = "App 1.0", mixinStandardHelpOptions = true)
public class Application implements Runnable {

    @Option(
        names = {"-w", "--width"},
        description = "Width of the output image.")
    private int width;

    @Option(
        names = {"-h", "--height"},
        description = "Height of the output image.")
    private int height;

    @Option(
        names = {"--seed"},
        description = "Initial seed value for the random generator.")
    private long seed;

    @Option(
        names = {"-i", "--iteration-count"},
        description = "Number of generation iterations.")
    private int iterationCount;

    @Option(
        names = {"-o", "--output-path"},
        description = "Relative path to the output PNG file.")
    private String outputPath;

    @Option(
        names = {"-t", "--threads"},
        description = "Number of worker threads.")
    private int threads;

    @Option(
        names = {"-ap", "--affine-params"},
        description = "Affine transforms configuration: <a_1>,<b_1>,<c_1>,<d_1>,<e_1>,<f_1>/<a_N>,<b_N>,<c_N>,<d_N>,<e_N>,<f_N>.",
        converter = academy.app.cli.AffineCoefficientsConverter.class)
    private List<AffineCoefficients> affineCoefficients = null;

    @Option(
        names = {"-f", "--functions"},
        description = "Function configuration: <name1>:<weight1>,<name2>:<weight2> (e.g. swirl:1.0,horseshoe:0.8).",
        converter = academy.app.cli.TransformationsConverter.class)
    private List<TransformationSpec> transformations = null;

    @Option(
        names = {"-c", "--config-path"},
        description = "Path to JSON config file.")
    private Path configPath;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {

        int cores = Runtime.getRuntime().availableProcessors();

        log.info("Доступно ядер процессора: {}", cores);

        AppConfig defaultConfig = ConfigLoader.createDefaultConfig();

        AppConfig jsonConfig = null;
         if (configPath != null) {
             jsonConfig = ConfigLoader.loadConfig(configPath);
         }

         AppConfig appConfig = AppConfig.builder()
             .overlayConfig(defaultConfig)
             .overlayConfig(jsonConfig)
             .overlayConfig(new AppConfig(
                    new AppConfig.Size(width, height),
                    iterationCount,
                    outputPath,
                    threads,
                    seed,
                    transformations,
                    affineCoefficients
             ))
             .build();

         log.info("Конфигурация приложения: {}", appConfig);

         //ToDo: убрать это куда-то
        int sample = 500000;
        //ToDo: нэминг
        ImageBuffer image;

        List<FlameFunction> functions = FlameFunctionFactory.createFunctions(
            appConfig.affineCoefficients(),
            appConfig.transformations(),
            appConfig.seed()
        );

        FractalRenderer renderer = new FractalRenderer();
        if (appConfig.threads() == 1) {
            image = renderer.renderFractal(
                appConfig.size().width(),
                appConfig.size().height(),
                functions,
                sample,
                appConfig.iterationCount(),
                (long) appConfig.seed()
            );
        } else {
            image = renderer.renderFractalParallel(
                appConfig.size().width(),
                appConfig.size().height(),
                functions,
                sample,
                appConfig.iterationCount(),
                appConfig.threads(),
                (long) appConfig.seed()
            );
        }

        ImageRenderer.save(
            image,
            Path.of(appConfig.outputPath()),
            ImageRenderer.ImageFormat.PNG
        );

        log.info("Конец!!!");
    }
}
