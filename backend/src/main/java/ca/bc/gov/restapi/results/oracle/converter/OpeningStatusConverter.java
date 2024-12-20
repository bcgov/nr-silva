package ca.bc.gov.restapi.results.oracle.converter;

import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Objects;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

/** This class represents a column converter for the opening status column. */
@Converter(autoApply = true)
@RegisterReflectionForBinding
public class OpeningStatusConverter implements AttributeConverter<OpeningStatusEnum, String> {

  @Override
  public String convertToDatabaseColumn(OpeningStatusEnum attribute) {
    if (Objects.isNull(attribute)) {
      return null;
    }

    return attribute.getCode();
  }

  @Override
  public OpeningStatusEnum convertToEntityAttribute(String dbData) {
    return OpeningStatusEnum.of(dbData);
  }
}
