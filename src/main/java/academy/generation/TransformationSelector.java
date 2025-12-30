package academy.generation;

import academy.generation.transformations.Transformation;
import academy.generation.transformations.TransformationFactory;
import academy.model.TransformationSpec;
import academy.model.TransformationType;
import java.util.List;
import java.util.SplittableRandom;

public class TransformationSelector {
    private final List<TransformationSpec> transformations;
    private final double[] prefixSums;
    private final double totalSums;

    TransformationSelector(List<TransformationSpec> transformations) {
        this.transformations = transformations;
        this.prefixSums = new double[transformations.size()];
        double sum = transformations.getFirst().weight();
        prefixSums[0] = sum;
        for (int i = 1; i < transformations.size(); i++) {
            sum += transformations.get(i).weight();
            prefixSums[i] = sum;
        }
        this.totalSums = sum;
    }

    public Transformation selectTransformation(SplittableRandom random) {
        double randomValue = random.nextDouble();
        double scaledValue = randomValue * totalSums;
        int index = upperBound(prefixSums, scaledValue);
        TransformationType transformationType = transformations.get(index).name();
        return TransformationFactory.getTransformation(transformationType);
    }

    private int upperBound(double[] array, double val) {
        int left = 0;
        int right = array.length;
        while (left < right) {
            int med = left + (right - left) / 2;
            if (array[med] <= val){
                left = med + 1;
            }
            else {
                right = med;
            }
        }
        return left;
    }
}
