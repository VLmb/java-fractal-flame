#!/bin/sh

echo "Testing image properties..."

JAR_PATH="$1"
JAVA_OPTS="-Dlog4j2.level=OFF"
ARGS="-w 800 -H 600 -o test_output.png -t 8"

# Генерация тестового изображения, если оно не существует
if [ ! -f "test_output.png" ]; then
    echo "Generating test image..."
    java $JAVA_OPTS -jar "$JAR_PATH" $ARGS
    if [ $? -ne 0 ]; then
        echo "✗ Failed to generate test image"
        exit 1
    fi
fi

# Проверка, существует ли файл
if [ ! -f "test_output.png" ]; then
    echo "✗ Image file 'test_output.png' does not exist"
    exit 1
fi

# Проверка расширения файла
case "test_output.png" in
    *.png)
        echo "✓ Image file has .png extension"
        ;;
    *)
        echo "✗ Image file does not have .png extension"
        exit 1
        ;;
esac

# Проверка, имеет ли файл содержимое (ненулевой размер)
FILE_SIZE=$(stat -c%s "test_output.png" 2>/dev/null || stat -f%z "test_output.png" 2>/dev/null)
if [ -n "$FILE_SIZE" ] && [ "$FILE_SIZE" -gt 0 ]; then
    echo "✓ Image file has content (size: $FILE_SIZE bytes)"
else
    echo "✗ Image file is empty or could not determine size"
    exit 1
fi

# Проверка сигнатуры PNG без xxd: через od
# PNG signature bytes: 89 50 4E 47 0D 0A 1A 0A => "89504e470d0a1a0a"
PNG_SIGNATURE=$(dd if="test_output.png" bs=8 count=1 2>/dev/null | od -An -tx1 2>/dev/null | tr -d ' \n')
if [ "$PNG_SIGNATURE" = "89504e470d0a1a0a" ]; then
    echo "✓ Image file has valid PNG signature"
else
    echo "✗ Image file does not have valid PNG signature (got: ${PNG_SIGNATURE:-<empty>})"
    exit 1
fi

echo "Image properties test passed!"
