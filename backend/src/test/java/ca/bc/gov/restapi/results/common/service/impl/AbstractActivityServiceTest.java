package ca.bc.gov.restapi.results.common.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchFiltersDto;
import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchResponseDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.common.projection.ActivitySearchProjection;
import ca.bc.gov.restapi.results.common.repository.ActivityTreatmentUnitRepository;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | AbstractActivityService")
class AbstractActivityServiceTest {

  @Mock private ActivityTreatmentUnitRepository activityRepository;

  @Mock private ForestClientService forestClientService;

  private AbstractActivityService service;

  private ActivitySearchFiltersDto testFilters;

  private Pageable testPageable;

  @BeforeEach
  void setup() {
    service = new AbstractActivityService(activityRepository, forestClientService);
    testFilters =
        new ActivitySearchFiltersDto(
            List.of("SU"),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "NW10",
            null,
            null,
            "2020-01-01",
            "2025-12-31");
    testPageable = PageRequest.of(0, 10);
  }

  @Test
  @DisplayName("activitySearch should return empty page when no projections")
  void activitySearch_emptyProjections_shouldReturnEmptyPage() {
    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(new ArrayList<>());

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result).isNotNull();
    assertThat(result.getContent()).isEmpty();
    assertThat(result.getTotalElements()).isZero();
  }

  @Test
  @DisplayName("activitySearch should map projections to response DTOs")
  void activitySearch_withProjections_shouldMapToResponseDtos() {
    ActivitySearchProjection projection1 = createMockProjection(
        1L,
        "SU",
        "Survey",
        "VISUAL",
        "Visual",
        "M1",
        "Method 1",
        1L,
        "PROV",
        "Provincial",
        "NW10",
        "CB1",
        101017L,
        "CP001",
        BigDecimal.valueOf(50.5),
        "IA001",
        "CUTBLOCK",
        "Cut Block",
        "VIC",
        "Victoria",
        "00132184",
        100L,
        LocalDateTime.now());

    List<ActivitySearchProjection> projections = List.of(projection1);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), any()))
        .thenReturn(
            List.of(
                ForestClientDto.builder()
                    .clientNumber("00132184")
                    .clientName("Test Client")
                    .legalFirstName("John")
                    .legalMiddleName("Doe")
                    .clientStatusCode(ForestClientStatusEnum.ACTIVE)
                    .clientTypeCode(ForestClientTypeEnum.INDIVIDUAL)
                    .acronym("TC")
                    .name("John Doe Test Client")
                    .build()));

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getTotalElements()).isEqualTo(100);
    ActivitySearchResponseDto responseDto = result.getContent().get(0);
    assertThat(responseDto.activityId()).isEqualTo(1L);
    assertThat(responseDto.base()).isNotNull();
    assertThat(responseDto.base().code()).isEqualTo("SU");
    assertThat(responseDto.base().description()).isEqualTo("Survey");
  }

  @Test
  @DisplayName("activitySearch should map all null codes to null CodeDescriptionDto")
  void activitySearch_withNullCodes_shouldMapToNullDto() {
    ActivitySearchProjection projection = createMockProjection(
        1L,
        null,
        null,
        null,
        null,
        null,
        null,
        1L,
        null,
        null,
        "NW10",
        "CB1",
        101017L,
        "CP001",
        BigDecimal.valueOf(50.5),
        "IA001",
        null,
        null,
        "VIC",
        "Victoria",
        "00132184",
        1L,
        LocalDateTime.now());

    List<ActivitySearchProjection> projections = List.of(projection);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), any()))
        .thenReturn(new ArrayList<>());

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result.getContent()).hasSize(1);
    ActivitySearchResponseDto responseDto = result.getContent().get(0);
    assertThat(responseDto.base()).isNull();
    assertThat(responseDto.technique()).isNull();
    assertThat(responseDto.method()).isNull();
    assertThat(responseDto.fundingSource()).isNull();
    assertThat(responseDto.openingCategory()).isNull();
  }

  @Test
  @DisplayName("activitySearch should map blank codes to null CodeDescriptionDto")
  void activitySearch_withBlankCodes_shouldMapToNullDto() {
    ActivitySearchProjection projection = createMockProjection(
        1L,
        "   ",
        "Description",
        "",
        "Technique",
        "   ",
        null,
        1L,
        "",
        "FundingSource",
        "NW10",
        "CB1",
        101017L,
        "CP001",
        BigDecimal.valueOf(50.5),
        "IA001",
        "  ",
        "Category",
        "VIC",
        "Victoria",
        "00132184",
        1L,
        LocalDateTime.now());

    List<ActivitySearchProjection> projections = List.of(projection);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), any()))
        .thenReturn(new ArrayList<>());

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result.getContent()).hasSize(1);
    ActivitySearchResponseDto responseDto = result.getContent().get(0);
    assertThat(responseDto.base()).isNull();
    assertThat(responseDto.technique()).isNull();
    assertThat(responseDto.method()).isNull();
    assertThat(responseDto.fundingSource()).isNull();
    assertThat(responseDto.openingCategory()).isNull();
  }

  @Test
  @DisplayName("activitySearch should gather unique client numbers")
  void activitySearch_shouldGatherUniqueClientNumbers() {
    ActivitySearchProjection projection1 = createMockProjection(
        1L,
        "SU",
        "Survey",
        "VISUAL",
        "Visual",
        "M1",
        "Method 1",
        1L,
        "PROV",
        "Provincial",
        "NW10",
        "CB1",
        101017L,
        "CP001",
        BigDecimal.valueOf(50.5),
        "IA001",
        "CUTBLOCK",
        "Cut Block",
        "VIC",
        "Victoria",
        "00132184",
        2L,
        LocalDateTime.now());

    ActivitySearchProjection projection2 = createMockProjection(
        2L,
        "SU",
        "Survey",
        "VISUAL",
        "Visual",
        "M1",
        "Method 1",
        1L,
        "PROV",
        "Provincial",
        "NW10",
        "CB1",
        101017L,
        "CP001",
        BigDecimal.valueOf(50.5),
        "IA001",
        "CUTBLOCK",
        "Cut Block",
        "VIC",
        "Victoria",
        "00132184",
        2L,
        LocalDateTime.now());

    List<ActivitySearchProjection> projections = List.of(projection1, projection2);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(eq(0), eq(1), argThat(list -> list != null && list.size() <= 1)))
        .thenReturn(
            List.of(
                ForestClientDto.builder()
                    .clientNumber("00132184")
                    .clientName("Test Client")
                    .legalFirstName("John")
                    .legalMiddleName("Doe")
                    .clientStatusCode(ForestClientStatusEnum.ACTIVE)
                    .clientTypeCode(ForestClientTypeEnum.INDIVIDUAL)
                    .acronym("TC")
                    .name("John Doe Test Client")
                    .build()));

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result.getContent()).hasSize(2);
    // Both DTOs should reference the same client since client numbers are unique
    result.getContent().forEach(dto -> assertThat(dto.openingClient()).isNotNull());
  }

  @Test
  @DisplayName("activitySearch should handle blank client numbers")
  void activitySearch_withBlankClientNumbers_shouldIgnore() {
    ActivitySearchProjection projection = createMockProjection(
        1L,
        "SU",
        "Survey",
        "VISUAL",
        "Visual",
        "M1",
        "Method 1",
        1L,
        "PROV",
        "Provincial",
        "NW10",
        "CB1",
        101017L,
        "CP001",
        BigDecimal.valueOf(50.5),
        "IA001",
        "CUTBLOCK",
        "Cut Block",
        "VIC",
        "Victoria",
        "   ",
        1L,
        LocalDateTime.now());

    List<ActivitySearchProjection> projections = List.of(projection);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), any()))
        .thenReturn(new ArrayList<>());

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result.getContent()).hasSize(1);
    ActivitySearchResponseDto responseDto = result.getContent().get(0);
    assertThat(responseDto.openingClient()).isNull();
  }

  @Test
  @DisplayName("activitySearch should handle null client numbers")
  void activitySearch_withNullClientNumbers_shouldIgnore() {
    ActivitySearchProjection projection = createMockProjection(
        1L,
        "SU",
        "Survey",
        "VISUAL",
        "Visual",
        "M1",
        "Method 1",
        1L,
        "PROV",
        "Provincial",
        "NW10",
        "CB1",
        101017L,
        "CP001",
        BigDecimal.valueOf(50.5),
        "IA001",
        "CUTBLOCK",
        "Cut Block",
        "VIC",
        "Victoria",
        null,
        1L,
        LocalDateTime.now());

    List<ActivitySearchProjection> projections = List.of(projection);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), any()))
        .thenReturn(new ArrayList<>());

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result.getContent()).hasSize(1);
    ActivitySearchResponseDto responseDto = result.getContent().get(0);
    assertThat(responseDto.openingClient()).isNull();
  }

  @Test
  @DisplayName("activitySearch should map isComplete correctly when 1")
  void activitySearch_isComplete1_shouldMapToTrue() {
    ActivitySearchProjection projection = createMockProjection(
        1L,
        "SU",
        "Survey",
        "VISUAL",
        "Visual",
        "M1",
        "Method 1",
        1L,
        "PROV",
        "Provincial",
        "NW10",
        "CB1",
        101017L,
        "CP001",
        BigDecimal.valueOf(50.5),
        "IA001",
        "CUTBLOCK",
        "Cut Block",
        "VIC",
        "Victoria",
        "00132184",
        1L,
        LocalDateTime.now());

    List<ActivitySearchProjection> projections = List.of(projection);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), any()))
        .thenReturn(new ArrayList<>());

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result.getContent()).hasSize(1);
    ActivitySearchResponseDto responseDto = result.getContent().get(0);
    assertThat(responseDto.isComplete()).isTrue();
  }

  @Test
  @DisplayName("activitySearch should map isComplete correctly when 0")
  void activitySearch_isComplete0_shouldMapToFalse() {
    ActivitySearchProjection projection = createMockProjection(
        1L,
        "SU",
        "Survey",
        "VISUAL",
        "Visual",
        "M1",
        "Method 1",
        0L,
        "PROV",
        "Provincial",
        "NW10",
        "CB1",
        101017L,
        "CP001",
        BigDecimal.valueOf(50.5),
        "IA001",
        "CUTBLOCK",
        "Cut Block",
        "VIC",
        "Victoria",
        "00132184",
        1L,
        LocalDateTime.now());

    List<ActivitySearchProjection> projections = List.of(projection);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), any()))
        .thenReturn(new ArrayList<>());

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result.getContent()).hasSize(1);
    ActivitySearchResponseDto responseDto = result.getContent().get(0);
    assertThat(responseDto.isComplete()).isFalse();
  }

  @Test
  @DisplayName("activitySearch should map isComplete correctly when null")
  void activitySearch_isCompleteNull_shouldMapToFalse() {
    ActivitySearchProjection projection = createMockProjection(
        1L,
        "SU",
        "Survey",
        "VISUAL",
        "Visual",
        "M1",
        "Method 1",
        null,
        "PROV",
        "Provincial",
        "NW10",
        "CB1",
        101017L,
        "CP001",
        BigDecimal.valueOf(50.5),
        "IA001",
        "CUTBLOCK",
        "Cut Block",
        "VIC",
        "Victoria",
        "00132184",
        1L,
        LocalDateTime.now());

    List<ActivitySearchProjection> projections = List.of(projection);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), any()))
        .thenReturn(new ArrayList<>());

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result.getContent()).hasSize(1);
    ActivitySearchResponseDto responseDto = result.getContent().get(0);
    assertThat(responseDto.isComplete()).isFalse();
  }

  @Test
  @DisplayName("activitySearch should preserve all response fields")
  void activitySearch_shouldPreserveAllFields() {
    LocalDateTime now = LocalDateTime.now();
    ActivitySearchProjection projection = createMockProjection(
        123L,
        "SU",
        "Survey",
        "VISUAL",
        "Visual",
        "M1",
        "Method 1",
        1L,
        "PROV",
        "Provincial",
        "NW10",
        "CB1",
        101017L,
        "CP001",
        BigDecimal.valueOf(50.5),
        "IA001",
        "CUTBLOCK",
        "Cut Block",
        "VIC",
        "Victoria",
        "00132184",
        1L,
        now);

    List<ActivitySearchProjection> projections = List.of(projection);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), any()))
        .thenReturn(new ArrayList<>());

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result.getContent()).hasSize(1);
    ActivitySearchResponseDto responseDto = result.getContent().get(0);

    assertThat(responseDto.activityId()).isEqualTo(123L);
    assertThat(responseDto.base().code()).isEqualTo("SU");
    assertThat(responseDto.base().description()).isEqualTo("Survey");
    assertThat(responseDto.technique().code()).isEqualTo("VISUAL");
    assertThat(responseDto.technique().description()).isEqualTo("Visual");
    assertThat(responseDto.method().code()).isEqualTo("M1");
    assertThat(responseDto.method().description()).isEqualTo("Method 1");
    assertThat(responseDto.isComplete()).isTrue();
    assertThat(responseDto.fundingSource().code()).isEqualTo("PROV");
    assertThat(responseDto.fundingSource().description()).isEqualTo("Provincial");
    assertThat(responseDto.fileId()).isEqualTo("NW10");
    assertThat(responseDto.cutBlock()).isEqualTo("CB1");
    assertThat(responseDto.openingId()).isEqualTo(101017L);
    assertThat(responseDto.cuttingPermit()).isEqualTo("CP001");
    assertThat(responseDto.treatmentAmountArea()).isEqualByComparingTo(BigDecimal.valueOf(50.5));
    assertThat(responseDto.intraAgencyNumber()).isEqualTo("IA001");
    assertThat(responseDto.openingCategory().code()).isEqualTo("CUTBLOCK");
    assertThat(responseDto.openingCategory().description()).isEqualTo("Cut Block");
    assertThat(responseDto.orgUnit().code()).isEqualTo("VIC");
    assertThat(responseDto.orgUnit().description()).isEqualTo("Victoria");
    assertThat(responseDto.updateTimestamp()).isEqualTo(now);
  }

  @Test
  @DisplayName("activitySearch should handle multiple projections with mixed client data")
  void activitySearch_multipleMixedProjections_shouldMapCorrectly() {
    ActivitySearchProjection projection1 = createMockProjection(
        1L, "SU", "Survey", "VISUAL", "Visual", "M1", "Method 1", 1L, "PROV", "Provincial",
        "NW10", "CB1", 101017L, "CP001", BigDecimal.valueOf(50.5), "IA001", "CUTBLOCK",
        "Cut Block", "VIC", "Victoria", "00132184", 3L, LocalDateTime.now());

    ActivitySearchProjection projection2 = createMockProjection(
        2L, "PR", "Pruning", "VISUAL", "Visual", "M2", "Method 2", 0L, "FED", "Federal",
        "NW11", "CB2", 101018L, "CP002", BigDecimal.valueOf(30.0), "IA002", "CUTBLOCK",
        "Cut Block", "VIC", "Victoria", "00132185", 3L, LocalDateTime.now());

    ActivitySearchProjection projection3 = createMockProjection(
        3L, "JS", "Juvenile", "VISUAL", "Visual", "M3", "Method 3", 1L, "PROV", "Provincial",
        "NW12", "CB3", 101019L, "CP003", BigDecimal.valueOf(25.0), "IA003", "CUTBLOCK",
        "Cut Block", "VIC", "Victoria", "00132184", 3L, LocalDateTime.now());

    List<ActivitySearchProjection> projections = List.of(projection1, projection2, projection3);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(eq(0), eq(2), argThat(
        list -> list != null && list.size() <= 2)))
        .thenReturn(
            List.of(
                ForestClientDto.builder()
                    .clientNumber("00132184")
                    .clientName("Test Client 1")
                    .legalFirstName("John")
                    .legalMiddleName("Doe")
                    .clientStatusCode(ForestClientStatusEnum.ACTIVE)
                    .clientTypeCode(ForestClientTypeEnum.INDIVIDUAL)
                    .acronym("TC1")
                    .name("John Doe Test Client 1")
                    .build(),
                ForestClientDto.builder()
                    .clientNumber("00132185")
                    .clientName("Test Client 2")
                    .legalFirstName("Jane")
                    .legalMiddleName("Smith")
                    .clientStatusCode(ForestClientStatusEnum.ACTIVE)
                    .clientTypeCode(ForestClientTypeEnum.GOVERNMENT)
                    .acronym("TC2")
                    .name("Test Client 2")
                    .build()));

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result.getContent()).hasSize(3);
    assertThat(result.getTotalElements()).isEqualTo(3);

    // First and third should have same client
    ActivitySearchResponseDto dto1 = result.getContent().get(0);
    ActivitySearchResponseDto dto3 = result.getContent().get(2);
    assertThat(dto1.openingClient().clientNumber()).isEqualTo(dto3.openingClient().clientNumber());

    // Second should have different client
    ActivitySearchResponseDto dto2 = result.getContent().get(1);
    assertThat(dto1.openingClient().clientNumber()).isNotEqualTo(dto2.openingClient().clientNumber());
  }

  @Test
  @DisplayName("activitySearch should use correct pagination parameters")
  void activitySearch_shouldUseCorrectPaginationParameters() {
    ActivitySearchProjection projection = createMockProjection(
        1L, "SU", "Survey", "VISUAL", "Visual", "M1", "Method 1", 1L, "PROV", "Provincial",
        "NW10", "CB1", 101017L, "CP001", BigDecimal.valueOf(50.5), "IA001", "CUTBLOCK",
        "Cut Block", "VIC", "Victoria", "00132184", 100L, LocalDateTime.now());

    List<ActivitySearchProjection> projections = List.of(projection);

    when(activityRepository.activitySearch(testFilters, 0, 10)).thenReturn(projections);
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), any()))
        .thenReturn(new ArrayList<>());

    Page<ActivitySearchResponseDto> result =
        service.activitySearch(testFilters, testPageable);

    assertThat(result.getTotalElements()).isEqualTo(100);
    assertThat(result.getNumber()).isEqualTo(0);
    assertThat(result.getSize()).isEqualTo(10);
  }

  // Helper method to create mock projections
  private ActivitySearchProjection createMockProjection(
      Long activityId,
      String baseCode,
      String baseDescription,
      String techniqueCode,
      String techniqueDescription,
      String methodCode,
      String methodDescription,
      Long isComplete,
      String fundingSourceCode,
      String fundingSourceDescription,
      String fileId,
      String cutBlock,
      Long openingId,
      String cuttingPermit,
      BigDecimal treatmentAmountArea,
      String intraAgencyNumber,
      String openingCategoryCode,
      String openingCategoryDescription,
      String orgUnitCode,
      String orgUnitDescription,
      String openingClientCode,
      Long totalCount,
      LocalDateTime updateTimestamp) {

    ActivitySearchProjection projection = mock(ActivitySearchProjection.class);
    when(projection.getActivityId()).thenReturn(activityId);
    when(projection.getBaseCode()).thenReturn(baseCode);
    when(projection.getBaseDescription()).thenReturn(baseDescription);
    when(projection.getTechniqueCode()).thenReturn(techniqueCode);
    when(projection.getTechniqueDescription()).thenReturn(techniqueDescription);
    when(projection.getMethodCode()).thenReturn(methodCode);
    when(projection.getMethodDescription()).thenReturn(methodDescription);
    when(projection.getIsComplete()).thenReturn(isComplete);
    when(projection.getFundingSourceCode()).thenReturn(fundingSourceCode);
    when(projection.getFundingSourceDescription()).thenReturn(fundingSourceDescription);
    when(projection.getFileId()).thenReturn(fileId);
    when(projection.getCutBlock()).thenReturn(cutBlock);
    when(projection.getOpeningId()).thenReturn(openingId);
    when(projection.getCuttingPermit()).thenReturn(cuttingPermit);
    when(projection.getTreatmentAmountArea()).thenReturn(treatmentAmountArea);
    when(projection.getIntraAgencyNumber()).thenReturn(intraAgencyNumber);
    when(projection.getOpeningCategoryCode()).thenReturn(openingCategoryCode);
    when(projection.getOpeningCategoryDescription()).thenReturn(openingCategoryDescription);
    when(projection.getOrgUnitCode()).thenReturn(orgUnitCode);
    when(projection.getOrgUnitDescription()).thenReturn(orgUnitDescription);
    when(projection.getOpeningClientCode()).thenReturn(openingClientCode);
    when(projection.getTotalCount()).thenReturn(totalCount);
    when(projection.getUpdateTimestamp()).thenReturn(updateTimestamp);
    return projection;
  }
}
