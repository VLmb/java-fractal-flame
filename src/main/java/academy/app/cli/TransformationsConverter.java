package academy.app.cli;

import academy.model.TransformationSpec;
import academy.model.TransformationType;
import java.util.ArrayList;
import java.util.List;
import picocli.CommandLine;

public class TransformationsConverter implements CommandLine.ITypeConverter<List<TransformationSpec>> {

    @Override
    public List<TransformationSpec> convert(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        String[] items = value.split(",");
        List<TransformationSpec> result = new ArrayList<>(items.length);

        for (String rawItem : items) {
            String item = rawItem.trim();
            if (item.isEmpty()) {
                continue;
            }

            String[] parts = item.split(":");
            if (parts.length != 2) {
                throw new CommandLine.TypeConversionException(
                        "Invalid functions format: '" + item + "'. Expected <name>:<weight>.");
            }

            TransformationType type = parseType(parts[0].trim());
            double weight = parseDouble(parts[1].trim());

            result.add(new TransformationSpec(type, weight));
        }

        return result;
    }

    private static TransformationType parseType(String raw) {
        if (raw.isEmpty()) {
            throw new CommandLine.TypeConversionException("Function name must not be empty.");
        }
        try {
            return TransformationType.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommandLine.TypeConversionException("Unknown transformation type: '" + raw + "'.");
        }
    }

    private static double parseDouble(String raw) {
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            throw new CommandLine.TypeConversionException("Invalid number");
        }
    }
}
