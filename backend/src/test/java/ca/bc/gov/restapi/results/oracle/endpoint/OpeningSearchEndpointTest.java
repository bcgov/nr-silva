package ca.bc.gov.restapi.results.oracle.endpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.service.OpenCategoryCodeService;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import ca.bc.gov.restapi.results.oracle.service.OrgUnitService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

  @MockBean private OpenCategoryCodeService openCategoryCodeService;

  @MockBean private OrgUnitService orgUnitService;

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
    response.setEarlyFreeGrowingDate(null);
    response.setLateFreeGrowingDate(null);
    response.setUpdateTimestamp(LocalDateTime.now());
    response.setEntryUserId("TEST");
    response.setSubmittedToFrpa(true);
    response.setSilvaReliefAppId(333L);
    response.setForestFileId("TFL47");

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
        .andExpect(jsonPath("$.data[0].forestFileId").value(response.getForestFileId()))
        .andExpect(jsonPath("$.data[0].orgUnitCode").value(response.getOrgUnitCode()))
        .andExpect(jsonPath("$.data[0].orgUnitName").value(response.getOrgUnitName()))
        .andExpect(jsonPath("$.data[0].clientNumber").value(response.getClientNumber()))
        .andExpect(jsonPath("$.data[0].regenDelayDate").value(response.getRegenDelayDate()))
        .andExpect(
            jsonPath("$.data[0].earlyFreeGrowingDate").value(response.getEarlyFreeGrowingDate()))
        .andExpect(
            jsonPath("$.data[0].lateFreeGrowingDate").value(response.getLateFreeGrowingDate()))
        .andExpect(jsonPath("$.data[0].entryUserId").value(response.getEntryUserId()))
        .andExpect(jsonPath("$.data[0].submittedToFrpa").value(response.getSubmittedToFrpa()))
        .andExpect(jsonPath("$.data[0].silvaReliefAppId").value(response.getSilvaReliefAppId()))
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

  @Test
  @DisplayName("Get Opening Categories happy Path should Succeed")
  void getOpeningCategories_happyPath_shouldSucceed() throws Exception {
    CodeDescriptionDto category = new CodeDescriptionDto("FTML", "Free Growing");

    List<CodeDescriptionDto> openCategoryCodeEntityList = List.of(category);

    when(openCategoryCodeService.findAllCategories(false)).thenReturn(openCategoryCodeEntityList);

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
    orgUnit.setOrgUnitNo(22L);
    orgUnit.setOrgUnitCode("DAS");
    orgUnit.setOrgUnitName("DAS Name");
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String effectiveDateStr = orgUnit.getEffectiveDate().format(formatter);
    String expiryDateStr = orgUnit.getExpiryDate().format(formatter);
    String updateTimestampStr = orgUnit.getUpdateTimestamp().format(formatter);

    List<OrgUnitEntity> orgUnitEntityList = List.of(orgUnit);

    when(orgUnitService.findAllOrgUnits()).thenReturn(orgUnitEntityList);

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
        .andExpect(jsonPath("$[0].locationCode").value(orgUnit.getLocationCode()))
        .andExpect(jsonPath("$[0].areaCode").value(orgUnit.getAreaCode()))
        .andExpect(jsonPath("$[0].telephoneNo").value(orgUnit.getTelephoneNo()))
        .andExpect(jsonPath("$[0].orgLevelCode").value(orgUnit.getOrgLevelCode().toString()))
        .andExpect(jsonPath("$[0].officeNameCode").value(orgUnit.getOfficeNameCode()))
        .andExpect(jsonPath("$[0].rollupRegionNo").value(orgUnit.getRollupRegionNo()))
        .andExpect(jsonPath("$[0].rollupRegionCode").value(orgUnit.getRollupRegionCode()))
        .andExpect(jsonPath("$[0].rollupDistNo").value(orgUnit.getRollupDistNo()))
        .andExpect(jsonPath("$[0].rollupDistCode").value(orgUnit.getRollupDistCode()))
        .andExpect(jsonPath("$[0].effectiveDate").value(effectiveDateStr))
        .andExpect(jsonPath("$[0].expiryDate").value(expiryDateStr))
        .andExpect(jsonPath("$[0].updateTimestamp").value(updateTimestampStr))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Org Units By Code happy Path should Succeed")
  void getOpeningOrgUnitsByCode_happyPath_shouldSucceed() throws Exception {
    OrgUnitEntity orgUnit = new OrgUnitEntity();
    orgUnit.setOrgUnitNo(22L);
    orgUnit.setOrgUnitCode("DAS");
    orgUnit.setOrgUnitName("DAS Name");
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String effectiveDateStr = orgUnit.getEffectiveDate().format(formatter);
    String expiryDateStr = orgUnit.getExpiryDate().format(formatter);
    String updateTimestampStr = orgUnit.getUpdateTimestamp().format(formatter);

    List<OrgUnitEntity> orgUnitEntityList = List.of(orgUnit);

    when(orgUnitService.findAllOrgUnitsByCode(List.of("DAS"))).thenReturn(orgUnitEntityList);

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
        .andExpect(jsonPath("$[0].locationCode").value(orgUnit.getLocationCode()))
        .andExpect(jsonPath("$[0].areaCode").value(orgUnit.getAreaCode()))
        .andExpect(jsonPath("$[0].telephoneNo").value(orgUnit.getTelephoneNo()))
        .andExpect(jsonPath("$[0].orgLevelCode").value(orgUnit.getOrgLevelCode().toString()))
        .andExpect(jsonPath("$[0].officeNameCode").value(orgUnit.getOfficeNameCode()))
        .andExpect(jsonPath("$[0].rollupRegionNo").value(orgUnit.getRollupRegionNo()))
        .andExpect(jsonPath("$[0].rollupRegionCode").value(orgUnit.getRollupRegionCode()))
        .andExpect(jsonPath("$[0].rollupDistNo").value(orgUnit.getRollupDistNo()))
        .andExpect(jsonPath("$[0].rollupDistCode").value(orgUnit.getRollupDistCode()))
        .andExpect(jsonPath("$[0].effectiveDate").value(effectiveDateStr))
        .andExpect(jsonPath("$[0].expiryDate").value(expiryDateStr))
        .andExpect(jsonPath("$[0].updateTimestamp").value(updateTimestampStr))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Org Units By Code not Found should Succeed")
  void getOpeningOrgUnitsByCode_notFound_shouldSucceed() throws Exception {
    when(orgUnitService.findAllOrgUnitsByCode(List.of("DAS"))).thenReturn(List.of());

    mockMvc
        .perform(
            get("/api/opening-search/org-units-by-code?orgUnitCodes=DAS")
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
