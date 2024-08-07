package ca.bc.gov.restapi.results.oracle.endpoint;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OpeningEndpoint.class)
@WithMockUser(roles = "user_read")
class OpeningEndpointTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private OpeningService openingService;

  @Test
  @DisplayName("Request a list of recent openings for the home screen")
  void getRecentOpenings_fetchPaginated_shouldSucceed() throws Exception {
    PaginatedResult<RecentOpeningDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(0);
    paginatedResult.setPerPage(5);
    paginatedResult.setTotalPages(1);
    paginatedResult.setHasNextPage(false);

    LocalDate now = LocalDate.now();
    LocalDateTime entryLastYear = LocalDateTime.now().minusYears(1L);
    LocalDateTime updateLastMonth = entryLastYear.plusMonths(11L);

    RecentOpeningDto recentOpeningDto =
        new RecentOpeningDto(
            114207L,
            "TFL47",
            "12T",
            "47/12S",
            "12-69",
            new BigDecimal("12.9"),
            OpeningStatusEnum.APP,
            OpeningCategoryEnum.FTML,
            now,
            entryLastYear,
            updateLastMonth);
    paginatedResult.setData(List.of(recentOpeningDto));

    PaginationParameters params = new PaginationParameters(0, 5);

    when(openingService.getRecentOpeningsCurrentUser(params)).thenReturn(paginatedResult);

    mockMvc
        .perform(
            get("/api/openings/recent-openings")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(header().exists("x-opening-source"))
        .andExpect(jsonPath("$.pageIndex").value("0"))
        .andExpect(jsonPath("$.perPage").value("5"))
        .andExpect(jsonPath("$.totalPages").value("1"))
        .andExpect(jsonPath("$.hasNextPage").value("false"))
        .andExpect(jsonPath("$.data[0].openingId").value("114207"))
        .andExpect(jsonPath("$.data[0].fileId").value("TFL47"))
        .andExpect(jsonPath("$.data[0].cuttingPermit").value("12T"))
        .andExpect(jsonPath("$.data[0].timberMark").value("47/12S"))
        .andExpect(jsonPath("$.data[0].cutBlock").value("12-69"))
        .andExpect(jsonPath("$.data[0].grossAreaHa").value("12.9"))
        .andExpect(jsonPath("$.data[0].status.code").value(OpeningStatusEnum.APP.getCode()))
        .andExpect(
            jsonPath("$.data[0].status.description").value(OpeningStatusEnum.APP.getDescription()))
        .andExpect(jsonPath("$.data[0].category.code").value(OpeningCategoryEnum.FTML.getCode()))
        .andExpect(
            jsonPath("$.data[0].category.description")
                .value(OpeningCategoryEnum.FTML.getDescription()))
        .andExpect(jsonPath("$.data[0].disturbanceStart").value(now.toString()))
        .andReturn();
  }
}
