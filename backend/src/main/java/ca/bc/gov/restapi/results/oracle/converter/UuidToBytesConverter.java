package ca.bc.gov.restapi.results.oracle.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * JPA attribute converter that converts between {@link UUID} and {@code byte[]} to support storage
 * of UUID values in Oracle databases using the {@code RAW(16)} column type.
 *
 * <p>This converter enables seamless conversion when persisting and retrieving UUID values in
 * entities annotated with {@link jakarta.persistence.Convert}.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * @Column(name = "OPENING_ATTACHMENT_GUID", columnDefinition = "RAW(16)")
 * @Convert(converter = UuidToBytesConverter.class)
 * private UUID attachmentGuid;
 * }</pre>
 */
@Converter(autoApply = false)
public class UuidToBytesConverter implements AttributeConverter<UUID, byte[]> {

  /**
   * Converts a {@link UUID} to a 16-byte array for storage in the database.
   *
   * @param uuid the UUID to convert
   * @return the 16-byte array representation of the UUID, or {@code null} if the UUID is null
   */
  @Override
  public byte[] convertToDatabaseColumn(UUID uuid) {
    if (uuid == null) return null;
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return bb.array();
  }

  /**
   * Converts a 16-byte array from the database into a {@link UUID}.
   *
   * @param bytes the byte array to convert
   * @return the UUID representation of the byte array, or {@code null} if the input is null or not
   *     16 bytes
   */
  @Override
  public UUID convertToEntityAttribute(byte[] bytes) {
    if (bytes == null || bytes.length != 16) return null;
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    return new UUID(bb.getLong(), bb.getLong());
  }
}
