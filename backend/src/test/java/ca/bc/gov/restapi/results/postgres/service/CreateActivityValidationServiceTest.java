package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.entity.GenericCodeEntity;
import ca.bc.gov.restapi.results.postgres.dto.CreateActivityRequestDto;
import ca.bc.gov.restapi.results.postgres.entity.code.SilvBaseCodePostgresEntity;
import ca.bc.gov.restapi.results.postgres.entity.code.SilvFundSrceCodePostgresEntity;
import ca.bc.gov.restapi.results.postgres.entity.code.SilvMethodCodePostgresEntity;
import ca.bc.gov.restapi.results.postgres.entity.code.SilvObjectiveCodePostgresEntity;
import ca.bc.gov.restapi.results.postgres.entity.code.SilvTechniqueCodePostgresEntity;
import ca.bc.gov.restapi.results.postgres.enums.ActivityStatus;
import ca.bc.gov.restapi.results.postgres.repository.SilvBaseCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvFundSrceCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvMethodCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvObjectiveCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvTechniqueCodePostgresRepository;
import jakarta.validation.Validation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Unit Test | CreateActivityValidationService")
class CreateActivityValidationServiceTest {

  @Mock private SilvBaseCodePostgresRepository baseCodeRepository;
  @Mock private SilvTechniqueCodePostgresRepository techniqueCodeRepository;
  @Mock private SilvMethodCodePostgresRepository methodCodeRepository;
  @Mock private SilvObjectiveCodePostgresRepository objectiveCodeRepository;
  @Mock private SilvFundSrceCodePostgresRepository fundSrceCodeRepository;

  private CreateActivityValidationService service;

  /** Mutable copy of the request so each test only changes what it cares about. */
  private static class Request {
    ActivityStatus status = ActivityStatus.PLANNED;
    String baseCode = "HC";
    String techniqueCode = null;
    String methodCode = null;
    List<String> objectiveCodes = List.of("A");
    List<String> fundingSourceCodes = List.of("BCP");
    LocalDate plannedDate = LocalDate.now().plusDays(1);
    BigDecimal plannedArea = new BigDecimal("10.5");
    BigDecimal plannedCost = null;
    LocalDate completionDate = null;
    BigDecimal treatedArea = null;
    BigDecimal actualCost = null;

    CreateActivityRequestDto build() {
      return new CreateActivityRequestDto(
          status,
          baseCode,
          techniqueCode,
          methodCode,
          objectiveCodes,
          fundingSourceCodes,
          null,
          null,
          null,
          plannedDate,
          plannedArea,
          plannedCost,
          completionDate,
          treatedArea,
          actualCost);
    }
  }

  private static <T extends GenericCodeEntity> T inEffect(T entity) {
    entity.setDescription("description");
    entity.setEffectiveDate(LocalDate.of(2000, 1, 1));
    entity.setExpiryDate(LocalDate.of(9999, 12, 31));
    return entity;
  }

  private static <T extends GenericCodeEntity> List<T> codes(
      Iterable<String> codes, Function<String, T> factory) {
    List<T> entities = new java.util.ArrayList<>();
    codes.forEach(code -> entities.add(inEffect(factory.apply(code))));
    return entities;
  }

  @BeforeEach
  @SuppressWarnings("unchecked")
  void setUp() {
    service =
        new CreateActivityValidationService(
            Validation.buildDefaultValidatorFactory().getValidator(),
            baseCodeRepository,
            techniqueCodeRepository,
            methodCodeRepository,
            objectiveCodeRepository,
            fundSrceCodeRepository);

    when(baseCodeRepository.findAllById(any()))
        .thenAnswer(
            call ->
                codes(
                    (Iterable<String>) call.getArgument(0),
                    code -> SilvBaseCodePostgresEntity.builder().code(code).build()));
    when(techniqueCodeRepository.findAllById(any()))
        .thenAnswer(
            call ->
                codes(
                    (Iterable<String>) call.getArgument(0),
                    code -> SilvTechniqueCodePostgresEntity.builder().code(code).build()));
    when(methodCodeRepository.findAllById(any()))
        .thenAnswer(
            call ->
                codes(
                    (Iterable<String>) call.getArgument(0),
                    code -> SilvMethodCodePostgresEntity.builder().code(code).build()));
    when(objectiveCodeRepository.findAllById(any()))
        .thenAnswer(
            call ->
                codes(
                    (Iterable<String>) call.getArgument(0),
                    code -> SilvObjectiveCodePostgresEntity.builder().code(code).build()));
    when(fundSrceCodeRepository.findAllById(any()))
        .thenAnswer(
            call ->
                codes(
                    (Iterable<String>) call.getArgument(0),
                    code -> SilvFundSrceCodePostgresEntity.builder().code(code).build()));
  }

  private String errorOf(Request request) {
    CreateActivityRequestDto dto = request.build();
    ResponseStatusException exception =
        org.junit.jupiter.api.Assertions.assertThrows(
            ResponseStatusException.class, () -> service.validate(dto));
    assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    return exception.getReason();
  }

  private void assertValid(Request request) {
    CreateActivityRequestDto dto = request.build();
    assertThatCode(() -> service.validate(dto)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Accepts a valid planned activity")
  void validate_validPlanned_passes() {
    assertValid(new Request());
  }

  @Test
  @DisplayName("Accepts a valid completed activity with technique, method and costs")
  void validate_validCompleted_passes() {
    Request request = new Request();
    request.status = ActivityStatus.COMPLETED;
    request.completionDate = LocalDate.now().minusYears(1);
    request.treatedArea = new BigDecimal("20.0");
    request.techniqueCode = "BU";
    request.methodCode = "MACH";
    request.plannedCost = new BigDecimal("1500");
    request.actualCost = new BigDecimal("1200");
    request.objectiveCodes = List.of("A", "B", "C", "D", "E");
    request.fundingSourceCodes = List.of("BCP", "BCT", "CBI", "CF", "FIP");

    assertValid(request);
  }

  @Test
  @DisplayName("Completed activity does not need planned date, area or cost, and accepts any date")
  void validate_completedWithoutPlannedFields_passes() {
    Request request = new Request();
    request.status = ActivityStatus.COMPLETED;
    request.completionDate = LocalDate.now().plusDays(10);
    request.treatedArea = new BigDecimal("20.0");
    request.plannedDate = null;
    request.plannedArea = null;
    request.plannedCost = null;
    assertValid(request);

    request.plannedDate = LocalDate.now().minusYears(2);
    assertValid(request);
    request.plannedDate = LocalDate.now().plusYears(2);
    assertValid(request);
  }

  @Test
  @DisplayName("Planned activity requires planned date and planned area")
  void validate_plannedWithoutDateAndArea_rejected() {
    Request request = new Request();
    request.plannedDate = null;
    request.plannedArea = null;

    assertThat(errorOf(request))
        .contains("plannedDate: must not be null")
        .contains("plannedArea: must not be null");
  }

  @Test
  @DisplayName("Reports list element violations with the plain field path")
  void validate_blankListElement_reportedWithoutListElementSuffix() {
    Request request = new Request();
    request.objectiveCodes = List.of(" ");

    assertThat(errorOf(request))
        .contains("objectiveCodes[0]: must not be blank")
        .doesNotContain("<list element>");
  }

  @Test
  @DisplayName("Accepts every basic base code")
  void validate_everyBasicBase_passes() {
    for (String base : CreateActivityValidationService.BASIC_BASE_CODES) {
      Request request = new Request();
      request.baseCode = base;
      assertValid(request);
    }
    assertThat(CreateActivityValidationService.BASIC_BASE_CODES)
        .containsExactlyInAnyOrder(
            "AU", "BR", "CC", "CM", "EP", "FE", "HC", "HI", "JS", "LB", "PC", "PR", "RC", "RD",
            "ST", "SX");
  }

  @Test
  @DisplayName("Rejects non-basic and unknown base codes")
  void validate_nonBasicBase_rejected() {
    for (String base : List.of("PL", "DS", "SP", "SU", "DN", "ZZ")) {
      Request request = new Request();
      request.baseCode = base;
      assertThat(errorOf(request)).contains("baseCode: must be one of");
    }
  }

  @Test
  @DisplayName("Reports every field violation together")
  void validate_manyViolations_reportedTogether() {
    Request request = new Request();
    request.status = null;
    request.fundingSourceCodes = List.of();
    request.objectiveCodes = List.of("A", "B", "C", "D", "E", "F");
    request.plannedArea = new BigDecimal("0");
    request.plannedCost = new BigDecimal("10.5");

    String error = errorOf(request);

    assertThat(error)
        .contains("status: must not be null")
        .contains("fundingSourceCodes: must not be empty")
        .contains("objectiveCodes: size must be between 0 and 5")
        .contains("plannedArea: must be greater than 0")
        .contains("plannedCost: must be a whole number of dollars");
  }

  @Test
  @DisplayName("Reports a missing base code without failing the other checks")
  void validate_missingBaseCode_reportedWithoutFailing() {
    Request request = new Request();
    request.baseCode = null;
    request.plannedArea = new BigDecimal("900.0");

    assertThat(errorOf(request)).contains("baseCode: must not be blank");
  }

  @Test
  @DisplayName("Rejects negative costs, and areas with more than one decimal")
  void validate_numericBounds_rejected() {
    Request request = new Request();
    request.plannedArea = new BigDecimal("10.55");
    request.plannedCost = new BigDecimal("-1");

    assertThat(errorOf(request))
        .contains("plannedArea:")
        .contains("plannedCost: must be greater than or equal to 0");
  }

  @Test
  @DisplayName("Requires completion date and treated area for completed activities")
  void validate_completedWithoutFields_rejected() {
    Request request = new Request();
    request.status = ActivityStatus.COMPLETED;

    assertThat(errorOf(request))
        .contains("completionDate: must not be null")
        .contains("treatedArea: must not be null");
  }

  @Test
  @DisplayName("Rejects completed-only fields on a planned activity")
  void validate_plannedWithCompletedFields_rejected() {
    Request request = new Request();
    request.completionDate = LocalDate.now();
    request.treatedArea = new BigDecimal("5.0");
    request.actualCost = new BigDecimal("100");

    assertThat(errorOf(request))
        .contains("completionDate: only allowed when the status is COMPLETED")
        .contains("treatedArea: only allowed when the status is COMPLETED")
        .contains("actualCost: only allowed when the status is COMPLETED");
  }

  @Test
  @DisplayName("Planned date must be later than the current date")
  void validate_plannedDate_mustBeInTheFuture() {
    Request today = new Request();
    today.plannedDate = LocalDate.now();
    Request past = new Request();
    past.plannedDate = LocalDate.now().minusDays(1);

    assertThat(errorOf(today)).contains("plannedDate: must be later than the current date");
    assertThat(errorOf(past)).contains("plannedDate: must be later than the current date");
  }

  @Test
  @DisplayName("Rejects codes that do not exist or are not currently in effect")
  void validate_codesNotInEffect_rejected() {
    SilvTechniqueCodePostgresEntity expired =
        inEffect(SilvTechniqueCodePostgresEntity.builder().code("BU").build());
    expired.setExpiryDate(LocalDate.now().minusDays(1));
    SilvObjectiveCodePostgresEntity notStarted =
        inEffect(SilvObjectiveCodePostgresEntity.builder().code("A").build());
    notStarted.setEffectiveDate(LocalDate.now().plusDays(1));
    doReturn(List.of(expired)).when(techniqueCodeRepository).findAllById(any());
    doReturn(List.of(notStarted)).when(objectiveCodeRepository).findAllById(any());
    doReturn(List.of()).when(fundSrceCodeRepository).findAllById(any());

    Request request = new Request();
    request.techniqueCode = "BU";
    request.objectiveCodes = List.of("A");

    assertThat(errorOf(request))
        .contains("techniqueCode: 'BU' is not a valid code currently in effect")
        .contains("objectiveCodes: 'A' is not a valid code currently in effect")
        .contains("fundingSourceCodes: 'BCP' is not a valid code currently in effect");
  }

  @Test
  @DisplayName("Rejects duplicate objectives and funding sources")
  void validate_duplicateCodes_rejected() {
    Request request = new Request();
    request.objectiveCodes = List.of("A", "A");
    request.fundingSourceCodes = List.of("BCP", "BCP");

    assertThat(errorOf(request))
        .contains("objectiveCodes: duplicate values are not allowed")
        .contains("fundingSourceCodes: duplicate values are not allowed");
  }
}
