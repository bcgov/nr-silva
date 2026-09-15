package ca.bc.gov.restapi.results.postgres.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | StockingLayerDto validation")
class StockingLayerDtoTest {

  @Test
  @DisplayName("Minimum horizontal distance greater than numeric(3,1) is rejected")
  void minHorizontalDistance_exceedingColumnPrecision_isRejected() {
    StockingLayerDto layer =
        new StockingLayerDto(
            "I", null, null, new BigDecimal("100"), null, null, null, null, null, null, null);

    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      assertThat(factory.getValidator().validate(layer)).isNotEmpty();
    }
  }
}
