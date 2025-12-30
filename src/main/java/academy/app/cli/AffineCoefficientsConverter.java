package academy.app.cli;

import academy.model.AffineCoefficients;
import picocli.CommandLine;
import java.util.ArrayList;
import java.util.List;

public class AffineCoefficientsConverter implements CommandLine.ITypeConverter<List<AffineCoefficients>> {

    @Override
    public List<AffineCoefficients> convert(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        String[] blocks = value.split("/");
        List<AffineCoefficients> result = new ArrayList<>(blocks.length);

        for (String rawBlock : blocks) {
            String block = rawBlock.trim();
            if (block.isEmpty()) {
                continue;
            }

            String[] nums = block.split(",");
            if (nums.length != 6) {
                throw new CommandLine.TypeConversionException(
                    "Invalid affine params block: '" + block + "'. Expected 6 numbers: a,b,c,d,e,f."
                );
            }

            double a = parseDouble(nums[0].trim(), "a", block);
            double b = parseDouble(nums[1].trim(), "b", block);
            double c = parseDouble(nums[2].trim(), "c", block);
            double d = parseDouble(nums[3].trim(), "d", block);
            double e = parseDouble(nums[4].trim(), "e", block);
            double f = parseDouble(nums[5].trim(), "f", block);

            result.add(new AffineCoefficients(a, b, c, d, e, f));
        }

        return result;
    }

    private static double parseDouble(String raw, String name, String block) {
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            throw new CommandLine.TypeConversionException(
                "Invalid number for '" + name + "' in block '" + block + "': '" + raw + "'."
            );
        }
    }
}
