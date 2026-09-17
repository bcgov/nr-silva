package ca.bc.gov.restapi.results.postgres.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.postgres.enums.StockingLayerType;
import ca.bc.gov.restapi.results.postgres.enums.StockingSpeciesMilestone;
import ca.bc.gov.restapi.results.postgres.enums.StockingSpeciesType;
import ca.bc.gov.restapi.results.postgres.enums.StockingStandardAuthorityType;
import ca.bc.gov.restapi.results.postgres.enums.StockingType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | CreateStockingStandardRequestDto validation")
class CreateStockingStandardRequestDtoTest {

  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @AfterAll
  static void tearDownValidator() {
    factory.close();
  }

  private CreateStockingStandardRequestDto validRequest() {
    return new CreateStockingStandardRequestDto(
        "Reforestation objective",
        "My Standard",
        "Somewhere",
        StockingStandardAuthorityType.OPERATIONAL_PLAN,
        List.of("DAS"),
        List.of("00012797"),
        false,
        true,
        null,
        StockingType.REGEN_OBLIGATION,
        1,
        20,
        null,
        null,
        StockingLayerType.SINGLE,
        new StockingLayerDto(
            "I",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            List.of(
                new StockingSpeciesDto(
                    "CW",
                    StockingSpeciesType.PREFERRED,
                    BigDecimal.ONE,
                    StockingSpeciesMilestone.BOTH))),
        null,
        null);
  }

  @Test
  @DisplayName("A fully populated request has no violations")
  void validRequest_hasNoViolations() {
    Set<ConstraintViolation<CreateStockingStandardRequestDto>> violations =
        validator.validate(validRequest());
    assertThat(violations).isEmpty();
  }

  @Test
  @DisplayName("Blank objective is rejected")
  void blankObjective_isRejected() {
    CreateStockingStandardRequestDto request = validRequest();
    CreateStockingStandardRequestDto invalid =
        new CreateStockingStandardRequestDto(
            " ",
            request.name(),
            request.location(),
            request.authorityType(),
            request.orgUnitCodes(),
            request.clientNumbers(),
            request.becInfoSelected(),
            request.alternativeMethodSelected(),
            request.becData(),
            request.stockingType(),
            request.regenDelayYears(),
            request.freeGrowingYears(),
            request.earlyYears(),
            request.lateYears(),
            request.layerType(),
            request.singleLayer(),
            request.multiLayers(),
            request.additionalStandards());

    assertThat(validator.validate(invalid)).isNotEmpty();
  }

  @Test
  @DisplayName("Empty layer species list is allowed")
  void emptyLayerSpeciesList_isAllowed() {
    CreateStockingStandardRequestDto request = validRequest();
    CreateStockingStandardRequestDto invalid =
        new CreateStockingStandardRequestDto(
            request.objective(),
            request.name(),
            request.location(),
            request.authorityType(),
            request.orgUnitCodes(),
            request.clientNumbers(),
            request.becInfoSelected(),
            request.alternativeMethodSelected(),
            request.becData(),
            request.stockingType(),
            request.regenDelayYears(),
            request.freeGrowingYears(),
            request.earlyYears(),
            request.lateYears(),
            request.layerType(),
            new StockingLayerDto(
                "I", null, null, null, null, null, null, null, null, null, null, List.of()),
            request.multiLayers(),
            request.additionalStandards());

    assertThat(validator.validate(invalid)).isEmpty();
  }

  @Test
  @DisplayName("A blank field on a nested BecDataDto is reported via cascading @Valid")
  void invalidNestedBecData_isRejected() {
    CreateStockingStandardRequestDto request = validRequest();
    CreateStockingStandardRequestDto invalid =
        new CreateStockingStandardRequestDto(
            request.objective(),
            request.name(),
            request.location(),
            request.authorityType(),
            request.orgUnitCodes(),
            request.clientNumbers(),
            true,
            request.alternativeMethodSelected(),
            List.of(new BecDataDto("", "wh1", null, null, "01", null)),
            request.stockingType(),
            request.regenDelayYears(),
            request.freeGrowingYears(),
            request.earlyYears(),
            request.lateYears(),
            request.layerType(),
            request.singleLayer(),
            request.multiLayers(),
            request.additionalStandards());

    assertThat(validator.validate(invalid)).isNotEmpty();
  }

  @Test
  @DisplayName("Null BEC and layer-species elements are rejected")
  void nullCollectionElements_areRejected() {
    CreateStockingStandardRequestDto request = validRequest();
    CreateStockingStandardRequestDto nullBecData =
        new CreateStockingStandardRequestDto(
            request.objective(), request.name(), request.location(), request.authorityType(),
            request.orgUnitCodes(), request.clientNumbers(), request.becInfoSelected(),
            request.alternativeMethodSelected(), Collections.singletonList(null),
            request.stockingType(), request.regenDelayYears(), request.freeGrowingYears(),
            request.earlyYears(), request.lateYears(), request.layerType(), request.singleLayer(),
            request.multiLayers(), request.additionalStandards());
    CreateStockingStandardRequestDto nullLayerSpecies =
        new CreateStockingStandardRequestDto(
            request.objective(), request.name(), request.location(), request.authorityType(),
            request.orgUnitCodes(), request.clientNumbers(), request.becInfoSelected(),
            request.alternativeMethodSelected(), request.becData(), request.stockingType(),
            request.regenDelayYears(), request.freeGrowingYears(), request.earlyYears(),
            request.lateYears(), request.layerType(),
            new StockingLayerDto(
                "I", null, null, null, null, null, null, null, null, null, null,
                Collections.singletonList(null)),
            request.multiLayers(), request.additionalStandards());

    assertThat(validator.validate(nullBecData)).isNotEmpty();
    assertThat(validator.validate(nullLayerSpecies)).isNotEmpty();
  }

  @Test
  @DisplayName("Species type, milestone, and height bounds are validated")
  void speciesFields_areValidated() {
    assertThat(
            validator.validate(
                new StockingSpeciesDto(
                    "CW", null, BigDecimal.ONE, StockingSpeciesMilestone.BOTH)))
        .isNotEmpty();
    assertThat(
            validator.validate(
                new StockingSpeciesDto(
                    "CW", StockingSpeciesType.PREFERRED, BigDecimal.ONE, null)))
        .isNotEmpty();
    assertThat(
            validator.validate(
                new StockingSpeciesDto(
                    "CW",
                    StockingSpeciesType.PREFERRED,
                    new BigDecimal("100.0"),
                    StockingSpeciesMilestone.BOTH)))
        .isNotEmpty();
  }
}
