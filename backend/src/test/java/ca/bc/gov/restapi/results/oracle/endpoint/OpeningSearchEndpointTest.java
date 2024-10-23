package ca.bc.gov.restapi.results.oracle.endpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WithMockJwt(value = "ttester")
@AutoConfigureMockMvc
@DisplayName("Integrated Test | Opening Search Endpoint")
class OpeningSearchEndpointTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Opening search happy path should succeed")
  void openingSearch_happyPath_shouldSucceed() throws Exception {

    mockMvc
        .perform(
            get("/api/opening-search?mainSearchTerm=TFL49")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.pageIndex").value("0"))
        .andExpect(jsonPath("$.perPage").value("1"))
        .andExpect(jsonPath("$.totalPages").value("1"))
        .andExpect(jsonPath("$.hasNextPage").value("false"))
        .andExpect(jsonPath("$.data[0].openingId").value(103))
        .andExpect(jsonPath("$.data[0].openingNumber").isEmpty())
        .andExpect(jsonPath("$.data[0].category.code").value("FTML"))
        .andExpect(jsonPath("$.data[0].status.code").value("APP"))
        .andExpect(jsonPath("$.data[0].cuttingPermitId").value("14T"))
        .andExpect(jsonPath("$.data[0].timberMark").value("49/14S"))
        .andExpect(jsonPath("$.data[0].cutBlockId").value("14-71"))
        .andExpect(jsonPath("$.data[0].openingGrossAreaHa").value("14.9"))
        .andExpect(
            jsonPath("$.data[0].disturbanceStartDate").value("2024-01-24T00:00:00"))
        .andExpect(jsonPath("$.data[0].forestFileId").value("TFL49"))
        .andExpect(jsonPath("$.data[0].orgUnitCode").value("DAS"))
        .andExpect(jsonPath("$.data[0].orgUnitName").value("Org one"))
        .andExpect(jsonPath("$.data[0].clientNumber").isEmpty())
        .andExpect(jsonPath("$.data[0].regenDelayDate").value("2024-01-24T00:00:00"))
        .andExpect(
            jsonPath("$.data[0].earlyFreeGrowingDate").value("2024-01-24T00:00:00"))
        .andExpect(
            jsonPath("$.data[0].lateFreeGrowingDate").value("2024-01-24T00:00:00"))
        .andExpect(jsonPath("$.data[0].entryUserId").value("IDIR@TTESTER"))
        .andExpect(jsonPath("$.data[0].submittedToFrpa").value(false))
        .andExpect(jsonPath("$.data[0].silvaReliefAppId").value(0))
        .andReturn();
  }

  @Test
  @DisplayName("Opening search no records found should succeed")
  void openingSearch_noRecordsFound_shouldSucceed() throws Exception {

    mockMvc
        .perform(
            get("/api/opening-search?mainSearchTerm=AAA")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.pageIndex").value("0"))
        .andExpect(jsonPath("$.perPage").value("5"))
        .andExpect(jsonPath("$.totalPages").value("0"))
        .andExpect(jsonPath("$.hasNextPage").value("false"))
        .andExpect(jsonPath("$.data", Matchers.empty()))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Categories happy Path should Succeed")
  void getOpeningCategories_happyPath_shouldSucceed() throws Exception {

    mockMvc
        .perform(
            get("/api/opening-search/categories")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].code").value("CONT"))
        .andExpect(jsonPath("$[0].description").value("SP as a part of contractual agreement"))
        .andExpect(jsonPath("$[0].effectiveDate").value("2000-04-17"))
        .andExpect(jsonPath("$[0].expiryDate").value("9999-12-31"))
        .andExpect(jsonPath("$[0].updateTimestamp").value("2013-07-31"))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Org Units happy Path should Succeed")
  void getOpeningOrgUnits_happyPath_shouldSucceed() throws Exception {

    mockMvc
        .perform(
            get("/api/opening-search/org-units")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].orgUnitNo").value(1))
        .andExpect(jsonPath("$[0].orgUnitCode").value("DAS"))
        .andExpect(jsonPath("$[0].orgUnitName").value("Org one"))
        .andExpect(jsonPath("$[0].locationCode").value("001"))
        .andExpect(jsonPath("$[0].areaCode").value("AAA"))
        .andExpect(jsonPath("$[0].telephoneNo").value("1122334"))
        .andExpect(jsonPath("$[0].orgLevelCode").value("H"))
        .andExpect(jsonPath("$[0].officeNameCode").value("VI"))
        .andExpect(jsonPath("$[0].rollupRegionNo").value(111))
        .andExpect(jsonPath("$[0].rollupRegionCode").value("1Code"))
        .andExpect(jsonPath("$[0].rollupDistNo").value(222))
        .andExpect(jsonPath("$[0].rollupDistCode").value("22Code"))
        .andExpect(jsonPath("$[0].effectiveDate").value("2020-01-01"))
        .andExpect(jsonPath("$[0].expiryDate").value("9999-12-31"))
        .andExpect(jsonPath("$[0].updateTimestamp").value("2024-09-03"))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Org Units By Code happy Path should Succeed")
  void getOpeningOrgUnitsByCode_happyPath_shouldSucceed() throws Exception {

    mockMvc
        .perform(
            get("/api/opening-search/org-units-by-code?orgUnitCodes=DAS")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].orgUnitNo").value(1))
        .andExpect(jsonPath("$[0].orgUnitCode").value("DAS"))
        .andExpect(jsonPath("$[0].orgUnitName").value("Org one"))
        .andExpect(jsonPath("$[0].locationCode").value("001"))
        .andExpect(jsonPath("$[0].areaCode").value("AAA"))
        .andExpect(jsonPath("$[0].telephoneNo").value("1122334"))
        .andExpect(jsonPath("$[0].orgLevelCode").value("H"))
        .andExpect(jsonPath("$[0].officeNameCode").value("VI"))
        .andExpect(jsonPath("$[0].rollupRegionNo").value(111))
        .andExpect(jsonPath("$[0].rollupRegionCode").value("1Code"))
        .andExpect(jsonPath("$[0].rollupDistNo").value(222))
        .andExpect(jsonPath("$[0].rollupDistCode").value("22Code"))
        .andExpect(jsonPath("$[0].effectiveDate").value("2020-01-01"))
        .andExpect(jsonPath("$[0].expiryDate").value("9999-12-31"))
        .andExpect(jsonPath("$[0].updateTimestamp").value("2024-09-03"))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Org Units By Code not Found should Succeed")
  void getOpeningOrgUnitsByCode_notFound_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/opening-search/org-units-by-code?orgUnitCodes=ABC")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$", Matchers.empty()))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Org Units By Code bad Request should Succeed")
  void getOpeningOrgUnitsByCode_badRequest_shouldFail() throws Exception {
    mockMvc
        .perform(
            get("/api/opening-search/org-units-by-code?")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();
  }
}
