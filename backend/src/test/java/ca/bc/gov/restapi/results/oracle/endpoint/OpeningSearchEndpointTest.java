package ca.bc.gov.restapi.results.oracle.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WithMockJwt
@DisplayName("Integrated Test | Opening Search Endpoint")
class OpeningSearchEndpointTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Opening search happy path should succeed")
  void openingSearch_happyPath_shouldSucceed() throws Exception {
    PaginatedResult<OpeningSearchResponseDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(0);
    paginatedResult.setPerPage(5);
    paginatedResult.setTotalPages(1);
    paginatedResult.setHasNextPage(false);

    OpeningSearchResponseDto response = new OpeningSearchResponseDto();
    response.setOpeningId(101);
    response.setOpeningNumber(null);
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
    response.setEarlyFreeGrowingDate(null);
    response.setLateFreeGrowingDate(null);
    response.setUpdateTimestamp(LocalDateTime.now());
    response.setEntryUserId("TEST");
    response.setSubmittedToFrpa(true);
    response.setSilvaReliefAppId(333L);
    response.setForestFileId("TFL47");

    mockMvc
        .perform(
            get("/api/opening-search?mainSearchTerm=101")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.pageIndex").value("0"))
        .andExpect(jsonPath("$.perPage").value("1"))
        .andExpect(jsonPath("$.totalPages").value("1"))
        .andExpect(jsonPath("$.hasNextPage").value("false"))
        .andExpect(jsonPath("$.data[0].openingId").value(response.getOpeningId()))
        .andExpect(jsonPath("$.data[0].openingNumber").value(response.getOpeningNumber()))
        .andExpect(jsonPath("$.data[0].category.code").value(response.getCategory().getCode()))
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

    mockMvc
        .perform(
            get("/api/opening-search?mainSearchTerm=ABC1234J")
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

  @Test
  @DisplayName("Get Opening Categories happy Path should Succeed")
  void getOpeningCategories_happyPath_shouldSucceed() throws Exception {
    CodeDescriptionDto category = new CodeDescriptionDto("CONT",
        "SP as a part of contractual agreement");

    List<CodeDescriptionDto> openCategoryCodeEntityList = List.of(category);

    mockMvc
        .perform(
            get("/api/opening-search/categories")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].code").value(category.code()))
        .andExpect(jsonPath("$[0].description").value(category.description()))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Org Units happy Path should Succeed")
  void getOpeningOrgUnits_happyPath_shouldSucceed() throws Exception {
    OrgUnitEntity orgUnit = new OrgUnitEntity();
    orgUnit.setOrgUnitNo(1L);
    orgUnit.setOrgUnitCode("DAS");
    orgUnit.setOrgUnitName("Org one");
    orgUnit.setLocationCode("123");
    orgUnit.setAreaCode("1");
    orgUnit.setTelephoneNo("25436521");
    orgUnit.setOrgLevelCode('R');
    orgUnit.setOfficeNameCode("RR");
    orgUnit.setRollupRegionNo(12L);
    orgUnit.setRollupRegionCode("19");
    orgUnit.setRollupDistNo(13L);
    orgUnit.setRollupDistCode("25");
    orgUnit.setEffectiveDate(LocalDate.now().minusYears(3L));
    orgUnit.setExpiryDate(LocalDate.now().plusYears(3L));
    orgUnit.setUpdateTimestamp(LocalDate.now());

    mockMvc
        .perform(
            get("/api/opening-search/org-units")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].orgUnitNo").value(orgUnit.getOrgUnitNo()))
        .andExpect(jsonPath("$[0].orgUnitCode").value(orgUnit.getOrgUnitCode()))
        .andExpect(jsonPath("$[0].orgUnitName").value(orgUnit.getOrgUnitName()))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Org Units By Code happy Path should Succeed")
  void getOpeningOrgUnitsByCode_happyPath_shouldSucceed() throws Exception {
    OrgUnitEntity orgUnit = new OrgUnitEntity();
    orgUnit.setOrgUnitNo(1L);
    orgUnit.setOrgUnitCode("DAS");
    orgUnit.setOrgUnitName("Org one");
    orgUnit.setLocationCode("123");
    orgUnit.setAreaCode("1");
    orgUnit.setTelephoneNo("25436521");
    orgUnit.setOrgLevelCode('R');
    orgUnit.setOfficeNameCode("RR");
    orgUnit.setRollupRegionNo(12L);
    orgUnit.setRollupRegionCode("19");
    orgUnit.setRollupDistNo(13L);
    orgUnit.setRollupDistCode("25");
    orgUnit.setEffectiveDate(LocalDate.now().minusYears(3L));
    orgUnit.setExpiryDate(LocalDate.now().plusYears(3L));
    orgUnit.setUpdateTimestamp(LocalDate.now());

    mockMvc
        .perform(
            get("/api/opening-search/org-units-by-code?orgUnitCodes=DAS")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].orgUnitNo").value(orgUnit.getOrgUnitNo()))
        .andExpect(jsonPath("$[0].orgUnitCode").value(orgUnit.getOrgUnitCode()))
        .andExpect(jsonPath("$[0].orgUnitName").value(orgUnit.getOrgUnitName()))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Org Units By Code not Found should Succeed")
  void getOpeningOrgUnitsByCode_notFound_shouldSucceed() throws Exception {
    //when(orgUnitService.findAllOrgUnitsByCode(List.of("DAS"))).thenReturn(List.of());

    mockMvc
        .perform(
            get("/api/opening-search/org-units-by-code?orgUnitCodes=XYZ")
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
