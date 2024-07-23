package ca.bc.gov.restapi.results.oracle.endpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OpeningSearchEndpoint.class)
@WithMockUser(roles = "user_read")
class OpeningSearchEndpointTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private OpeningService openingService;

  @Test
  @DisplayName("Opening search happy path should succeed")
  void openingSearch_happyPath_shouldSucceed() throws Exception {
    PaginatedResult<OpeningSearchResponseDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(0);
    paginatedResult.setPerPage(5);
    paginatedResult.setTotalPages(1);
    paginatedResult.setHasNextPage(false);

    OpeningSearchResponseDto response = new OpeningSearchResponseDto();
    response.setOpeningId(123456789);
    response.setOpeningNumber("589");
    response.setCategory(OpeningCategoryEnum.FTML);
    response.setStatus(OpeningStatusEnum.APP);
    response.setCuttingPermitId(null);
    response.setTimberMark(null);
    response.setCutBlockId(null);
    response.setOpeningGrossAreaHa(new BigDecimal("11"));
    response.setDisturbanceStartDate(null);
    response.setOrgUnitCode(null);
    response.setOrgUnitName(null);
    response.setClientNumber(null);
    response.setClientAcronym(null);
    response.setRegenDelayDate(null);
    response.setFreeGrowingDate(null);
    response.setUpdateTimestamp(LocalDateTime.now());
    response.setEntryUserId("TEST");
    response.setSubmittedToFrpa(false);
    response.setFileId(407);

    paginatedResult.setData(List.of(response));

    when(openingService.openingSearch(any(), any())).thenReturn(paginatedResult);

    mockMvc
        .perform(
            get("/api/opening-search?mainSearchTerm=407")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.pageIndex").value("0"))
        .andExpect(jsonPath("$.perPage").value("5"))
        .andExpect(jsonPath("$.totalPages").value("1"))
        .andExpect(jsonPath("$.hasNextPage").value("false"))
        .andExpect(jsonPath("$.data[0].openingId").value(response.getOpeningId()))
        .andExpect(jsonPath("$.data[0].openingNumber").value(response.getOpeningNumber()))
        .andExpect(jsonPath("$.data[0].category.code").value(response.getCategory().getCode()))
        .andExpect(jsonPath("$.data[0].status.code").value(response.getStatus().getCode()))
        .andExpect(jsonPath("$.data[0].cuttingPermitId").value(response.getCuttingPermitId()))
        .andExpect(jsonPath("$.data[0].timberMark").value(response.getTimberMark()))
        .andExpect(jsonPath("$.data[0].cutBlockId").value(response.getCutBlockId()))
        .andExpect(jsonPath("$.data[0].openingGrossAreaHa").value(response.getOpeningGrossAreaHa()))
        .andExpect(
            jsonPath("$.data[0].disturbanceStartDate").value(response.getDisturbanceStartDate()))
        .andExpect(jsonPath("$.data[0].fileId").value(response.getFileId()))
        .andExpect(jsonPath("$.data[0].orgUnitCode").value(response.getOrgUnitCode()))
        .andExpect(jsonPath("$.data[0].orgUnitName").value(response.getOrgUnitName()))
        .andExpect(jsonPath("$.data[0].clientNumber").value(response.getClientNumber()))
        .andExpect(jsonPath("$.data[0].regenDelayDate").value(response.getRegenDelayDate()))
        .andExpect(jsonPath("$.data[0].freeGrowingDate").value(response.getFreeGrowingDate()))
        .andExpect(jsonPath("$.data[0].entryUserId").value(response.getEntryUserId()))
        .andExpect(jsonPath("$.data[0].submittedToFrpa").value(response.getSubmittedToFrpa()))
        .andReturn();
  }

  @Test
  @DisplayName("Opening search no records found should succeed")
  void openingSearch_noRecordsFound_shouldSucceed() throws Exception {
    PaginatedResult<OpeningSearchResponseDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(0);
    paginatedResult.setPerPage(5);
    paginatedResult.setTotalPages(1);
    paginatedResult.setHasNextPage(false);
    paginatedResult.setData(List.of());

    when(openingService.openingSearch(any(), any())).thenReturn(paginatedResult);

    mockMvc
        .perform(
            get("/api/opening-search?mainSearchTerm=AAA")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.pageIndex").value("0"))
        .andExpect(jsonPath("$.perPage").value("5"))
        .andExpect(jsonPath("$.totalPages").value("1"))
        .andExpect(jsonPath("$.hasNextPage").value("false"))
        .andExpect(jsonPath("$.data", Matchers.empty()))
        .andReturn();
  }
}
