package ca.bc.gov.restapi.results.oracle.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import ca.bc.gov.restapi.results.common.enums.OpeningCategoryEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | Opening Category Converter")
class OpeningCategoryConverterTest {

  private final OpeningCategoryConverter converter = new OpeningCategoryConverter();

  @Test
  @DisplayName("To database")
  void convertToDatabaseColumnWithValidEnum() {
    assertEquals("FTML", converter.convertToDatabaseColumn(OpeningCategoryEnum.FTML));
  }

  @Test
  @DisplayName("Null to database")
  void convertToDatabaseColumnWithNull() {
    assertNull(converter.convertToDatabaseColumn(null));
  }

  @Test
  @DisplayName("From database")
  void convertToEntityAttributeWithValidCode() {
    assertEquals(OpeningCategoryEnum.FTML, converter.convertToEntityAttribute("FTML"));
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
