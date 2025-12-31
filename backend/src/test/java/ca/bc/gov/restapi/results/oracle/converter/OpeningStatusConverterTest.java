package ca.bc.gov.restapi.results.oracle.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import ca.bc.gov.restapi.results.common.enums.OpeningStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | Opening Status Converter")
class OpeningStatusConverterTest {

  private final OpeningStatusConverter converter = new OpeningStatusConverter();

  @Test
  @DisplayName("To database")
  void convertToDatabaseColumnWithValidEnum() {
    assertEquals("APP", converter.convertToDatabaseColumn(OpeningStatusEnum.APP));
  }

  @Test
  @DisplayName("Null to database")
  void convertToDatabaseColumnWithNull() {
    assertNull(converter.convertToDatabaseColumn(null));
  }

  @Test
  @DisplayName("From database")
  void convertToEntityAttributeWithValidCode() {
    assertEquals(OpeningStatusEnum.APP, converter.convertToEntityAttribute("APP"));
  }

  @Test
  @DisplayName("Invalid code")
  void convertToEntityAttributeWithInvalidCode() {
    assertNull(converter.convertToEntityAttribute("INVALID"));
  }

  @Test
  @DisplayName("Null")
  void convertToEntityAttributeWithNull() {
    assertNull(converter.convertToEntityAttribute(null));
  }

}
