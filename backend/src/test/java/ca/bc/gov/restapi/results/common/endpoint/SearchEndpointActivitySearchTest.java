package ca.bc.gov.restapi.results.common.endpoint;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchResponseDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.common.exception.MissingSearchParameterException;
import ca.bc.gov.restapi.results.common.service.ActivityService;
import ca.bc.gov.restapi.results.common.service.OpeningSearchService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SearchEndpoint.class)
@AutoConfigureMockMvc
@DisplayName("Unit Test | SearchEndpoint | Activity Search")
class SearchEndpointActivitySearchTest {

  @Autowired private MockMvc mockMvc;

  @Mock private ActivityService activityService;

  @Mock private OpeningSearchService openingSearchService;

  private ActivitySearchResponseDto testActivity1;
  private ActivitySearchResponseDto testActivity2;

  @BeforeEach
  void setup() {
    LocalDateTime now = LocalDateTime.now();

    ForestClientDto client =
        ForestClientDto.builder()
            .clientNumber("00132184")
            .clientName("Test Client")
            .legalFirstName("John")
            .legalMiddleName("Doe")
            .clientStatusCode(ForestClientStatusEnum.ACTIVE)
            .clientTypeCode(ForestClientTypeEnum.INDIVIDUAL)
            .acronym("TC")
            .name("John Doe Test Client")
            .build();

    testActivity1 =
        new ActivitySearchResponseDto(
            1L,
            new CodeDescriptionDto("SU", "Survey"),
            new CodeDescriptionDto("VISUAL", "Visual"),
            new CodeDescriptionDto("M1", "Method 1"),
            true,
            new CodeDescriptionDto("PROV", "Provincial"),
            "NW10",
            "CB1",
            101017L,
            "CP001",
            BigDecimal.valueOf(50.5),
            "IA001",
            new CodeDescriptionDto("CUTBLOCK", "Cut Block"),
            new CodeDescriptionDto("VIC", "Victoria"),
            client,
            now);

    testActivity2 =
        new ActivitySearchResponseDto(
            2L,
            new CodeDescriptionDto("PR", "Pruning"),
            new CodeDescriptionDto("VISUAL", "Visual"),
            new CodeDescriptionDto("M2", "Method 2"),
            false,
            new CodeDescriptionDto("FED", "Federal"),
            "NW11",
            "CB2",
            101018L,
            "CP002",
            BigDecimal.valueOf(30.0),
            "IA002",
            new CodeDescriptionDto("CUTBLOCK", "Cut Block"),
            new CodeDescriptionDto("VIC", "Victoria"),
            client,
            now);
  }

  @Test
  @DisplayName("activitySearch with bases filter should succeed")
  void activitySearch_withBasesFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("bases", "SU")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].activityId").value(1L))
        .andExpect(jsonPath("$.content[0].base.code").value("SU"))
        .andExpect(jsonPath("$.content[0].isComplete").value(true))
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  @DisplayName("activitySearch with techniques filter should succeed")
  void activitySearch_withTechniquesFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("techniques", "VISUAL")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].technique.code").value("VISUAL"));
  }

  @Test
  @DisplayName("activitySearch with methods filter should succeed")
  void activitySearch_withMethodsFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("methods", "M1")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].method.code").value("M1"));
  }

  @Test
  @DisplayName("activitySearch with isComplete filter should succeed")
  void activitySearch_withIsCompleteFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("isComplete", "true")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].isComplete").value(true));
  }

  @Test
  @DisplayName("activitySearch with objectives filter should succeed")
  void activitySearch_withObjectivesFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("objectives", "OBJ1")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @DisplayName("activitySearch with fundingSources filter should succeed")
  void activitySearch_withFundingSourcesFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("fundingSources", "PROV")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].fundingSource.code").value("PROV"));
  }

  @Test
  @DisplayName("activitySearch with orgUnits filter should succeed")
  void activitySearch_withOrgUnitsFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("orgUnits", "VIC")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].orgUnit.code").value("VIC"));
  }

  @Test
  @DisplayName("activitySearch with openingCategories filter should succeed")
  void activitySearch_withOpeningCategoriesFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("openingCategories", "CUTBLOCK")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].openingCategory.code").value("CUTBLOCK"));
  }

  @Test
  @DisplayName("activitySearch with fileId filter should succeed")
  void activitySearch_withFileIdFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("fileId", "NW10")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].fileId").value("NW10"));
  }

  @Test
  @DisplayName("activitySearch with clientNumbers filter should succeed")
  void activitySearch_withClientNumbersFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("clientNumbers", "00132184")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].openingClient.clientNumber").value("00132184"));
  }

  @Test
  @DisplayName("activitySearch with openingStatuses filter should succeed")
  void activitySearch_withOpeningStatusesFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("openingStatuses", "APP")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @DisplayName("activitySearch with updateDateStart filter should succeed")
  void activitySearch_withUpdateDateStartFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("updateDateStart", "2020-01-01")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @DisplayName("activitySearch with updateDateEnd filter should succeed")
  void activitySearch_withUpdateDateEndFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("updateDateEnd", "2025-12-31")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @DisplayName("activitySearch with multiple filters should succeed")
  void activitySearch_withMultipleFilters_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("bases", "SU")
                .queryParam("techniques", "VISUAL")
                .queryParam("methods", "M1")
                .queryParam("isComplete", "true")
                .queryParam("fundingSources", "PROV")
                .queryParam("orgUnits", "VIC")
                .queryParam("fileId", "NW10")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].activityId").value(1L));
  }

  @Test
  @DisplayName("activitySearch with no filters should throw MissingSearchParameterException")
  void activitySearch_noFilters_shouldThrowException() throws Exception {
    when(activityService.activitySearch(any(), any()))
        .thenThrow(new MissingSearchParameterException());

    mockMvc
        .perform(get("/api/search/activities").queryParam("page", "0").queryParam("size", "10"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("activitySearch with pagination parameters should succeed")
  void activitySearch_withPagination_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1, testActivity2), PageRequest.of(0, 2), 100);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("bases", "SU")
                .queryParam("page", "0")
                .queryParam("size", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.totalElements").value(100))
        .andExpect(jsonPath("$.totalPages").value(50));
  }

  @Test
  @DisplayName("activitySearch should return empty page when no results")
  void activitySearch_noResults_shouldReturnEmptyPage() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("bases", "SU")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)))
        .andExpect(jsonPath("$.totalElements").value(0));
  }

  @Test
  @DisplayName("activitySearch with multiple values for same filter should succeed")
  void activitySearch_withMultipleValuesPerFilter_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1, testActivity2), PageRequest.of(0, 10), 2);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("bases", "SU", "PR")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content[0].base.code").value("SU"))
        .andExpect(jsonPath("$.content[1].base.code").value("PR"));
  }

  @Test
  @DisplayName("activitySearch should map all response fields correctly")
  void activitySearch_shouldMapAllFields() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("bases", "SU")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].activityId").value(1L))
        .andExpect(jsonPath("$.content[0].base.code").value("SU"))
        .andExpect(jsonPath("$.content[0].base.description").value("Survey"))
        .andExpect(jsonPath("$.content[0].technique.code").value("VISUAL"))
        .andExpect(jsonPath("$.content[0].technique.description").value("Visual"))
        .andExpect(jsonPath("$.content[0].method.code").value("M1"))
        .andExpect(jsonPath("$.content[0].method.description").value("Method 1"))
        .andExpect(jsonPath("$.content[0].isComplete").value(true))
        .andExpect(jsonPath("$.content[0].fundingSource.code").value("PROV"))
        .andExpect(jsonPath("$.content[0].fundingSource.description").value("Provincial"))
        .andExpect(jsonPath("$.content[0].fileId").value("NW10"))
        .andExpect(jsonPath("$.content[0].cutBlock").value("CB1"))
        .andExpect(jsonPath("$.content[0].openingId").value(101017L))
        .andExpect(jsonPath("$.content[0].cuttingPermit").value("CP001"))
        .andExpect(jsonPath("$.content[0].treatmentAmountArea").value(50.5))
        .andExpect(jsonPath("$.content[0].intraAgencyNumber").value("IA001"))
        .andExpect(jsonPath("$.content[0].openingCategory.code").value("CUTBLOCK"))
        .andExpect(jsonPath("$.content[0].openingCategory.description").value("Cut Block"))
        .andExpect(jsonPath("$.content[0].orgUnit.code").value("VIC"))
        .andExpect(jsonPath("$.content[0].orgUnit.description").value("Victoria"))
        .andExpect(jsonPath("$.content[0].openingClient.clientNumber").value("00132184"));
  }

  @Test
  @DisplayName("activitySearch should handle false isComplete value")
  void activitySearch_isCompleteFalse_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity2), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("isComplete", "false")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].isComplete").value(false));
  }

  @Test
  @DisplayName("activitySearch should respect sorting parameters")
  void activitySearch_withSortParameters_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1, testActivity2), PageRequest.of(0, 10), 2);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("bases", "SU")
                .queryParam("page", "0")
                .queryParam("size", "10")
                .queryParam("sort", "activityId,desc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)));
  }

  @Test
  @DisplayName("activitySearch with date range should succeed")
  void activitySearch_withDateRange_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("updateDateStart", "2020-01-01")
                .queryParam("updateDateEnd", "2025-12-31")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @DisplayName("activitySearch should handle special characters in filters")
  void activitySearch_withSpecialCharacters_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("fileId", "NW10-TEST")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @DisplayName("activitySearch should call activityService with correct parameters")
  void activitySearch_shouldCallServiceWithCorrectParameters() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("bases", "SU")
                .queryParam("techniques", "VISUAL")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk());

    verify(activityService, times(1)).activitySearch(any(), any());
  }

  @Test
  @DisplayName("activitySearch with large page size should succeed")
  void activitySearch_withLargePageSize_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(testActivity1), PageRequest.of(0, 1000), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("bases", "SU")
                .queryParam("page", "0")
                .queryParam("size", "1000"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @DisplayName("activitySearch with large page number should succeed")
  void activitySearch_withLargePageNumber_shouldSucceed() throws Exception {
    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(), PageRequest.of(99, 10), 1000);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("bases", "SU")
                .queryParam("page", "99")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)));
  }

  @Test
  @DisplayName("activitySearch should handle null openingClient gracefully")
  void activitySearch_nullClient_shouldSucceed() throws Exception {
    ActivitySearchResponseDto activityWithoutClient =
        new ActivitySearchResponseDto(
            1L,
            new CodeDescriptionDto("SU", "Survey"),
            new CodeDescriptionDto("VISUAL", "Visual"),
            new CodeDescriptionDto("M1", "Method 1"),
            true,
            new CodeDescriptionDto("PROV", "Provincial"),
            "NW10",
            "CB1",
            101017L,
            "CP001",
            BigDecimal.valueOf(50.5),
            "IA001",
            new CodeDescriptionDto("CUTBLOCK", "Cut Block"),
            new CodeDescriptionDto("VIC", "Victoria"),
            null,
            LocalDateTime.now());

    Page<ActivitySearchResponseDto> mockPage =
        new PageImpl<>(List.of(activityWithoutClient), PageRequest.of(0, 10), 1);
    when(activityService.activitySearch(any(), any())).thenReturn(mockPage);

    mockMvc
        .perform(
            get("/api/search/activities")
                .queryParam("bases", "SU")
                .queryParam("page", "0")
                .queryParam("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].openingClient").doesNotExist());
  }
}
