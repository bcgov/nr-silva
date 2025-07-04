package ca.bc.gov.restapi.results.oracle.converter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | UUID to Bytes Converter")
class UuidToBytesConverterTest {

    private final UuidToBytesConverter converter = new UuidToBytesConverter();

    @Test
    @DisplayName("Convert UUID to byte array for database")
    void convertToDatabaseColumn_withValidUUID() {
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        byte[] expected = ByteBuffer.allocate(16)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .array();

        assertArrayEquals(expected, converter.convertToDatabaseColumn(uuid));
    }

    @Test
    @DisplayName("Convert null UUID to database column")
    void convertToDatabaseColumn_withNullUUID() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    @DisplayName("Convert 16-byte array to UUID from database")
    void convertToEntityAttribute_withValidBytes() {
        UUID original = UUID.randomUUID();
        byte[] bytes = ByteBuffer.allocate(16)
                .putLong(original.getMostSignificantBits())
                .putLong(original.getLeastSignificantBits())
                .array();

        UUID result = converter.convertToEntityAttribute(bytes);
        assertEquals(original, result);
    }

    @Test
    @DisplayName("Convert null bytes to UUID")
    void convertToEntityAttribute_withNullBytes() {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    @DisplayName("Convert invalid byte length to UUID")
    void convertToEntityAttribute_withInvalidLength() {
        byte[] invalid = new byte[10];
        assertNull(converter.convertToEntityAttribute(invalid));
    }
}