package ca.bc.gov.restapi.results.converter;

import ca.bc.gov.restapi.results.enums.OpeningCategoryEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Objects;

/** This class represents a column converter for the opening category column. */
@Converter(autoApply = true)
public class OpeningCategoryConverter implements AttributeConverter<OpeningCategoryEnum, String> {

  @Override
  public String convertToDatabaseColumn(OpeningCategoryEnum attribute) {
    if (Objects.isNull(attribute)) {
      return null;
    }

    return attribute.getCode();
  }

  @Override
  public OpeningCategoryEnum convertToEntityAttribute(String dbData) {
    return OpeningCategoryEnum.of(dbData);
  }
}
