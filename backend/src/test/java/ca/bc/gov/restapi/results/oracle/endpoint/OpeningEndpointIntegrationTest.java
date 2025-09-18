package ca.bc.gov.restapi.results.oracle.endpoint;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.jayway.jsonpath.JsonPath;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@WithMockJwt
@DisplayName("Integrated Test | Opening Endpoint")
class OpeningEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @RegisterExtension
  static WireMockExtension clientApiStub =
      WireMockExtension.newInstance()
          .options(
              wireMockConfig()
                  .port(10000)
                  .notifier(new WiremockLogNotifier())
                  .asynchronousResponseEnabled(true)
                  .stubRequestLoggingDisabled(false))
          .configureStaticDsl(true)
          .build();

  @Test
  @DisplayName("Opening search happy path should succeed")
  void openingSearch_happyPath_shouldSucceed() throws Exception {
    OpeningSearchResponseDto response = new OpeningSearchResponseDto();
    response.setOpeningId(101017L);
    response.setOpeningNumber(" 514");
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

    String clientNumber = "00000003";
    clientApiStub.stubFor(
        WireMock.get(urlPathEqualTo("/clients/findByClientNumber/" + clientNumber))
            .willReturn(
                okJson(
                    """
                {
                  "clientNumber": "00000003",
                  "clientName": "MINISTRY OF FORESTS",
                  "legalFirstName": null,
                  "legalMiddleName": null,
                  "clientStatusCode": "ACT",
                  "clientTypeCode": "F",
                  "acronym": "MOF"
                }
                """)));

    mockMvc
        .perform(
            get("/api/openings/search?mainSearchTerm=101017")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").value("1"))
        .andExpect(jsonPath("$.content[0].openingId").value(response.getOpeningId()))
        .andExpect(jsonPath("$.content[0].openingNumber").value(response.getOpeningNumber()))
        .andExpect(jsonPath("$.content[0].category.code").value(response.getCategory().getCode()))
        .andReturn();
  }

  @Test
  @DisplayName("Opening search no records found should succeed")
  void openingSearch_noRecordsFound_shouldSucceed() throws Exception {

    mockMvc
        .perform(
            get("/api/openings/search?mainSearchTerm=ABC1234J")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").value("0"))
        .andExpect(jsonPath("$.content", Matchers.empty()))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Tombstone by existing openingId should succeed")
  void getOpeningTombstone_existingOpeningId_shouldSucceed() throws Exception {
    Long openingId = 101017L;
    String clientNumber = "00010003";

    clientApiStub.stubFor(
        WireMock.get(urlPathEqualTo("/clients/findByClientNumber/" + clientNumber))
            .willReturn(
                okJson(
                    """
                {
                  "clientNumber": "00010003",
                  "clientName": "MINISTRY OF FORESTS",
                  "legalFirstName": null,
                  "legalMiddleName": null,
                  "clientStatusCode": "ACT",
                  "clientTypeCode": "F",
                  "acronym": "MOF"
                }
                """)));
    mockMvc
        .perform(
            get("/api/openings/101017/tombstone")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        // Verify openingId
        .andExpect(jsonPath("$.openingId").value(openingId))

        // Verify tombstone section
        .andExpect(jsonPath("$.tombstone.openingNumber").value(" 92K 014 0.0  514"))
        .andExpect(jsonPath("$.tombstone.openingStatus.code").value("FG"))
        .andExpect(jsonPath("$.tombstone.openingStatus.description").value("Free Growing"))
        .andExpect(jsonPath("$.tombstone.orgUnitCode").value("DAS"))
        .andExpect(jsonPath("$.tombstone.orgUnitName").value("Development Unit"))
        .andExpect(jsonPath("$.tombstone.openCategory.code").value("FTML"))
        .andExpect(
            jsonPath("$.tombstone.openCategory.description")
                .value("Forest Tenure - Major Licensee"))
        .andExpect(jsonPath("$.tombstone.client.clientNumber").value("00010003"))
        .andExpect(jsonPath("$.tombstone.fileId").value("TFL47"))
        .andExpect(jsonPath("$.tombstone.cuttingPermitId").value("12K"))
        .andExpect(jsonPath("$.tombstone.timberMark").value("47/12K"))
        .andExpect(jsonPath("$.tombstone.maxAllowedAccess").value("7.8"))
        .andExpect(jsonPath("$.tombstone.openingGrossArea").value(16.6))
        .andExpect(jsonPath("$.tombstone.createdBy").value("BABAYAGA"))
        .andExpect(jsonPath("$.tombstone.createdOn").value("2001-06-07"))
        .andExpect(jsonPath("$.tombstone.lastUpdatedOn").value("2014-04-02"))
        .andExpect(jsonPath("$.tombstone.disturbanceStartDate").value("2001-09-18"))

        // Verify overview.opening section
        .andExpect(jsonPath("$.overview.opening.tenureType.code").value("A02"))
        .andExpect(jsonPath("$.overview.opening.tenureType.description").value("Tree Farm Licence"))
        .andExpect(jsonPath("$.overview.opening.managementUnitType.code").value("T"))
        .andExpect(
            jsonPath("$.overview.opening.managementUnitType.description")
                .value("TREE FARM LICENCE"))
        .andExpect(jsonPath("$.overview.opening.managementUnitId").value("47"))
        .andExpect(jsonPath("$.overview.opening.timberSaleOffice.code").value("LSB"))
        .andExpect(
            jsonPath("$.overview.opening.timberSaleOffice.description").value("Lumber Sale Branch"))

        // Verify comments
        .andExpect(jsonPath("$.overview.opening.comments[0].commentSource.code").value("OPEN"))
        .andExpect(
            jsonPath("$.overview.opening.comments[0].commentSource.description").value("Opening"))
        .andExpect(jsonPath("$.overview.opening.comments[0].commentType.code").value("GENERAL"))
        .andExpect(
            jsonPath("$.overview.opening.comments[0].commentType.description")
                .value("General Comments"))
        .andExpect(jsonPath("$.overview.opening.comments[0].commentText").value("All good so far"))

        // Verify milestones section
        .andExpect(jsonPath("$.overview.milestones.standardsUnitId").value("A"))
        .andExpect(jsonPath("$.overview.milestones.regenOffsetYears").value(3))
        .andExpect(jsonPath("$.overview.milestones.regenDueDate").value("2004-09-18"))
        .andExpect(jsonPath("$.overview.milestones.freeGrowingDeclaredDate").value("2012-04-30"))
        .andExpect(jsonPath("$.overview.milestones.freeGrowingOffsetYears").value(11))
        .andExpect(jsonPath("$.overview.milestones.freeGrowingDueDate").value("2012-09-18"))

        // Verify notifications section
        .andExpect(
            jsonPath("$.notifications[0].title")
                .value("Regeneration milestone reminder for standard unit \"A, B\""))
        .andExpect(
            jsonPath("$.notifications[0].description").value("Please update your forest cover."))
        .andExpect(jsonPath("$.notifications[0].status").value("INFO"))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Tombstone with non-existing openingId should return 404")
  void getOpeningTombstone_nonExistingOpeningId_shouldReturn404() throws Exception {
    // Use a non-existent opening ID
    Long nonExistentOpeningId = 999999999L;

    mockMvc
        .perform(
            get("/api/openings/" + nonExistentOpeningId + "/tombstone")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Stocking Details by existing openingId should succeed")
  void getOpeningStockingDetails_existingOpeningId_shouldSucceed() throws Exception {
    Long openingId = 1009974L;

    mockMvc
        .perform(
            get("/api/openings/" + openingId + "/ssu")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        // Verify stocking details
        .andExpect(jsonPath("$[0].stocking.stockingStandardUnit").value("A"))
        .andExpect(jsonPath("$[0].stocking.ssuId").value(1013720L))
        .andExpect(jsonPath("$[0].stocking.defaultMof").value(false))
        .andExpect(jsonPath("$[0].stocking.manualEntry").value(false))
        .andExpect(jsonPath("$[0].stocking.netArea").value(25.5))
        .andExpect(jsonPath("$[0].stocking.soilDisturbancePercent").value(5.0))
        .andExpect(jsonPath("$[0].stocking.bec.becZoneCode").value("CWH"))
        .andExpect(jsonPath("$[0].stocking.bec.becSubzoneCode").value("vm"))
        .andExpect(jsonPath("$[0].stocking.bec.becVariant").value("1"))
        .andExpect(jsonPath("$[0].stocking.bec.becSiteSeries").value("01"))
        .andExpect(jsonPath("$[0].stocking.regenDelay").value(6L))
        .andExpect(jsonPath("$[0].stocking.freeGrowingLate").value(14L))
        .andExpect(jsonPath("$[0].stocking.freeGrowingEarly").value(11L))
        .andExpect(
            jsonPath("$[0].stocking.additionalStandards")
                .value(Matchers.containsString("(ALL625)")))
        .andExpect(jsonPath("$[0].stocking.milestones.postHarvestDeclaredDate").value("2005-01-24"))
        .andExpect(jsonPath("$[0].stocking.milestones.regenDeclaredDate").value("2007-06-15"))
        .andExpect(jsonPath("$[0].stocking.milestones.regenOffsetYears").value(6))
        .andExpect(jsonPath("$[0].stocking.milestones.regenDueDate").value("2010-01-19"))
        .andExpect(jsonPath("$[0].stocking.milestones.noRegenDeclaredDate").isEmpty())
        .andExpect(jsonPath("$[0].stocking.milestones.noRegenOffsetYears").isEmpty())
        .andExpect(jsonPath("$[0].stocking.milestones.noRegenDueDate").isEmpty())
        .andExpect(jsonPath("$[0].stocking.milestones.freeGrowingDeclaredDate").value("2017-05-30"))
        .andExpect(jsonPath("$[0].stocking.milestones.freeGrowingOffsetYears").value(14))
        .andExpect(jsonPath("$[0].stocking.milestones.freeGrowingDueDate").value("2018-01-19"))
        .andExpect(jsonPath("$[0].stocking.milestones.noRegenIndicated").value(false))
        .andExpect(jsonPath("$[0].stocking.milestones.comments").isArray())
        .andExpect(jsonPath("$[0].stocking.milestones.comments").isEmpty())

        // Verify preferred species
        .andExpect(jsonPath("$[0].preferredSpecies[0].species.code").value("CW"))
        .andExpect(
            jsonPath("$[0].preferredSpecies[0].species.description").value("western redcedar"))
        .andExpect(jsonPath("$[0].preferredSpecies[0].minHeight").value(1L))
        .andExpect(jsonPath("$[0].preferredSpecies[1].species.code").value("HW"))
        .andExpect(
            jsonPath("$[0].preferredSpecies[1].species.description").value("western hemlock"))
        .andExpect(jsonPath("$[0].preferredSpecies[1].minHeight").value(3L))
        .andExpect(jsonPath("$[0].preferredSpecies[2].species.code").value("FDC"))
        .andExpect(
            jsonPath("$[0].preferredSpecies[2].species.description").value("coastal Douglas-fir"))
        .andExpect(jsonPath("$[0].preferredSpecies[2].minHeight").value(3L))

        // Verify acceptable species
        .andExpect(jsonPath("$[0].acceptableSpecies[0].species.code").value("BA"))
        .andExpect(jsonPath("$[0].acceptableSpecies[0].species.description").value("amabilis fir"))
        .andExpect(jsonPath("$[0].acceptableSpecies[0].minHeight").value(1L))

        // Verify stocking layers
        .andExpect(jsonPath("$[0].layers[0].minWellspacedTrees").value(500))
        .andExpect(jsonPath("$[0].layers[0].minPreferredWellspacedTrees").value(400L))
        .andExpect(jsonPath("$[0].layers[0].minHorizontalDistanceWellspacedTrees").value(2L))
        .andExpect(jsonPath("$[0].layers[0].targetWellspacedTrees").value(900L))
        .andExpect(jsonPath("$[0].layers[0].minPostspacingDensity").value(800L))
        .andExpect(jsonPath("$[0].layers[0].maxPostspacingDensity").value(2000L))
        .andExpect(jsonPath("$[0].layers[0].maxConiferous").value(10000L))
        .andExpect(jsonPath("$[0].layers[0].heightRelativeToComp").value(150L));
  }

  @Test
  @DisplayName("Get Opening SSU History - should return expected list for opening 101017")
  void getOpeningSsuHistory_shouldReturnExpectedList() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1480130/ssu/history")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        // Entry 0
        .andExpect(jsonPath("$[0].stockingEventHistoryId").value(1741496))
        .andExpect(jsonPath("$[0].amendmentNumber").value(3))
        .andExpect(jsonPath("$[0].eventTimestamp").value("2025-03-14T10:46:37"))
        .andExpect(jsonPath("$[0].suCount").value(3))
        .andExpect(jsonPath("$[0].totalNar").value(285.0))
        .andExpect(jsonPath("$[0].auditAction.code").value("SPA"))
        .andExpect(jsonPath("$[0].auditAction.description").value("Site Plan Amendment"))
        .andExpect(jsonPath("$[0].esfSubmissionId").doesNotExist())
        .andExpect(jsonPath("$[0].submittedByUserId").value("IDIR\\CAROWOOD"))
        .andExpect(jsonPath("$[0].approvedByUserId").doesNotExist())
        .andExpect(jsonPath("$[0].isLatest").value(true))
        .andExpect(jsonPath("$[0].isOldest").value(false))
        // Entry 1
        .andExpect(jsonPath("$[1].stockingEventHistoryId").value(1739159))
        .andExpect(jsonPath("$[1].amendmentNumber").value(2))
        .andExpect(jsonPath("$[1].eventTimestamp").value("2025-02-03T17:22:17"))
        .andExpect(jsonPath("$[1].suCount").value(2))
        .andExpect(jsonPath("$[1].totalNar").value(280.0))
        .andExpect(jsonPath("$[1].auditAction.code").value("AMD"))
        .andExpect(jsonPath("$[1].auditAction.description").value("Amended"))
        .andExpect(jsonPath("$[1].esfSubmissionId").doesNotExist())
        .andExpect(jsonPath("$[1].submittedByUserId").value("IDIR\\SKILLAM"))
        .andExpect(jsonPath("$[1].approvedByUserId").value("IDIR\\CAROWOOD"))
        .andExpect(jsonPath("$[1].isLatest").value(false))
        .andExpect(jsonPath("$[1].isOldest").value(false))
        // Entry 2
        .andExpect(jsonPath("$[2].stockingEventHistoryId").value(1739158))
        .andExpect(jsonPath("$[2].amendmentNumber").value(1))
        .andExpect(jsonPath("$[2].eventTimestamp").value("2025-02-03T16:56:50"))
        .andExpect(jsonPath("$[2].suCount").value(2))
        .andExpect(jsonPath("$[2].totalNar").value(280.0))
        .andExpect(jsonPath("$[2].auditAction.code").value("AMD"))
        .andExpect(jsonPath("$[2].auditAction.description").value("Amended"))
        .andExpect(jsonPath("$[2].esfSubmissionId").doesNotExist())
        .andExpect(jsonPath("$[2].submittedByUserId").value("IDIR\\SKILLAM"))
        .andExpect(jsonPath("$[2].approvedByUserId").value("IDIR\\CAROWOOD"))
        .andExpect(jsonPath("$[2].isLatest").value(false))
        .andExpect(jsonPath("$[2].isOldest").value(false))
        // Entry 3
        .andExpect(jsonPath("$[3].stockingEventHistoryId").value(1524245))
        .andExpect(jsonPath("$[3].amendmentNumber").doesNotExist())
        .andExpect(jsonPath("$[3].eventTimestamp").value("2012-09-26T12:08:48"))
        .andExpect(jsonPath("$[3].suCount").value(2))
        .andExpect(jsonPath("$[3].totalNar").value(280.0))
        .andExpect(jsonPath("$[3].auditAction.code").value("COR"))
        .andExpect(jsonPath("$[3].auditAction.description").value("Correction"))
        .andExpect(jsonPath("$[3].esfSubmissionId").doesNotExist())
        .andExpect(jsonPath("$[3].submittedByUserId").value("IDIR\\SKILLAM"))
        .andExpect(jsonPath("$[3].approvedByUserId").doesNotExist())
        .andExpect(jsonPath("$[3].isLatest").value(false))
        .andExpect(jsonPath("$[3].isOldest").value(true));
  }

  @Test
  @DisplayName("Get Opening SSU History Details - should return expected details for opening 1480130 and eventHistoryId 1741496")
  void getOpeningSsuHistoryDetails_shouldReturnExpectedDetails() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1480130/ssu/history/1741496")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        // Entry 0
        .andExpect(jsonPath("$[0].stocking.stockingStandardUnit").value("A"))
        .andExpect(jsonPath("$[0].stocking.ssuId").value(1795272))
        .andExpect(jsonPath("$[0].stocking.srid").value(36008))
        .andExpect(jsonPath("$[0].stocking.defaultMof").value(false))
        .andExpect(jsonPath("$[0].stocking.manualEntry").value(false))
        .andExpect(jsonPath("$[0].stocking.fspId").doesNotExist())
        .andExpect(jsonPath("$[0].stocking.netArea").value(140.0))
        .andExpect(jsonPath("$[0].stocking.soilDisturbancePercent").value(2.0))
        .andExpect(jsonPath("$[0].stocking.bec.becZoneCode").value("CWH"))
        .andExpect(jsonPath("$[0].stocking.bec.becSubzoneCode").value("vm"))
        .andExpect(jsonPath("$[0].stocking.bec.becVariant").value("1"))
        .andExpect(jsonPath("$[0].stocking.bec.becSiteSeries").value("01"))
        .andExpect(jsonPath("$[0].stocking.regenDelay").value(3))
        .andExpect(jsonPath("$[0].stocking.freeGrowingLate").value(11))
        .andExpect(jsonPath("$[0].stocking.freeGrowingEarly").value(8))
        .andExpect(jsonPath("$[0].stocking.additionalStandards").isNotEmpty())
        .andExpect(jsonPath("$[0].preferredSpecies[0].layer").value("I"))
        .andExpect(jsonPath("$[0].preferredSpecies[0].species.code").value("CW"))
        .andExpect(jsonPath("$[0].preferredSpecies[0].species.description").value("western redcedar"))
        .andExpect(jsonPath("$[0].preferredSpecies[0].minHeight").value(1.5))
        .andExpect(jsonPath("$[0].preferredSpecies[1].species.code").value("YC"))
        .andExpect(jsonPath("$[0].preferredSpecies[1].species.description").value("yellow-cedar"))
        .andExpect(jsonPath("$[0].preferredSpecies[1].minHeight").value(1.5))
        .andExpect(jsonPath("$[0].acceptableSpecies[0].species.code").value("HW"))
        .andExpect(jsonPath("$[0].acceptableSpecies[0].species.description").value("western hemlock"))
        .andExpect(jsonPath("$[0].acceptableSpecies[0].minHeight").value(2.0))
        .andExpect(jsonPath("$[0].layers[0].layer.code").value("I"))
        .andExpect(jsonPath("$[0].layers[0].layer.description").value("Inventory Layer"))
        .andExpect(jsonPath("$[0].layers[0].minWellspacedTrees").value(500))
        .andExpect(jsonPath("$[0].layers[0].minPreferredWellspacedTrees").value(400))
        .andExpect(jsonPath("$[0].layers[0].minHorizontalDistanceWellspacedTrees").value(2))
        .andExpect(jsonPath("$[0].layers[0].targetWellspacedTrees").value(900))
        .andExpect(jsonPath("$[0].layers[0].minPostspacingDensity").value(800))
        .andExpect(jsonPath("$[0].layers[0].maxPostspacingDensity").value(1200))
        .andExpect(jsonPath("$[0].layers[0].maxConiferous").value(10000))
        .andExpect(jsonPath("$[0].layers[0].heightRelativeToComp").value(150))
        .andExpect(jsonPath("$[0].comments").isArray())
        .andExpect(jsonPath("$[0].comments").isEmpty())
        // Entry 1
        .andExpect(jsonPath("$[1].stocking.stockingStandardUnit").value("B"))
        .andExpect(jsonPath("$[1].stocking.ssuId").value(1795273))
        .andExpect(jsonPath("$[1].stocking.srid").doesNotExist())
        .andExpect(jsonPath("$[1].stocking.defaultMof").value(false))
        .andExpect(jsonPath("$[1].stocking.manualEntry").value(false))
        .andExpect(jsonPath("$[1].stocking.fspId").doesNotExist())
        .andExpect(jsonPath("$[1].stocking.netArea").value(140.0))
        .andExpect(jsonPath("$[1].stocking.soilDisturbancePercent").value(2.0))
        .andExpect(jsonPath("$[1].stocking.bec.becZoneCode").value("CWH"))
        .andExpect(jsonPath("$[1].stocking.bec.becSubzoneCode").value("mm"))
        .andExpect(jsonPath("$[1].stocking.bec.becVariant").value("1"))
        .andExpect(jsonPath("$[1].stocking.bec.becSiteSeries").value("05"))
        .andExpect(jsonPath("$[1].stocking.regenDelay").value(5))
        .andExpect(jsonPath("$[1].stocking.freeGrowingLate").value(20))
        .andExpect(jsonPath("$[1].stocking.freeGrowingEarly").doesNotExist())
        .andExpect(jsonPath("$[1].stocking.additionalStandards").doesNotExist())
        .andExpect(jsonPath("$[1].preferredSpecies").isArray())
        .andExpect(jsonPath("$[1].preferredSpecies").isEmpty())
        .andExpect(jsonPath("$[1].acceptableSpecies").isArray())
        .andExpect(jsonPath("$[1].acceptableSpecies").isEmpty())
        .andExpect(jsonPath("$[1].layers[0].layer.code").value("I"))
        .andExpect(jsonPath("$[1].layers[0].layer.description").value("Inventory Layer"))
        .andExpect(jsonPath("$[1].layers[0].minWellspacedTrees").doesNotExist())
        .andExpect(jsonPath("$[1].layers[0].minPreferredWellspacedTrees").doesNotExist())
        .andExpect(jsonPath("$[1].layers[0].minHorizontalDistanceWellspacedTrees").doesNotExist())
        .andExpect(jsonPath("$[1].layers[0].targetWellspacedTrees").doesNotExist())
        .andExpect(jsonPath("$[1].layers[0].minPostspacingDensity").doesNotExist())
        .andExpect(jsonPath("$[1].layers[0].maxPostspacingDensity").doesNotExist())
        .andExpect(jsonPath("$[1].layers[0].maxConiferous").doesNotExist())
        .andExpect(jsonPath("$[1].layers[0].heightRelativeToComp").doesNotExist())
        .andExpect(jsonPath("$[1].comments").isArray())
        .andExpect(jsonPath("$[1].comments").isEmpty())
        // Entry 2
        .andExpect(jsonPath("$[2].stocking.stockingStandardUnit").value("C"))
        .andExpect(jsonPath("$[2].stocking.ssuId").value(2411888))
        .andExpect(jsonPath("$[2].stocking.srid").value(1079794))
        .andExpect(jsonPath("$[2].stocking.defaultMof").value(false))
        .andExpect(jsonPath("$[2].stocking.manualEntry").value(false))
        .andExpect(jsonPath("$[2].stocking.fspId").doesNotExist())
        .andExpect(jsonPath("$[2].stocking.netArea").value(5.0))
        .andExpect(jsonPath("$[2].stocking.soilDisturbancePercent").value(5.0))
        .andExpect(jsonPath("$[2].stocking.bec.becZoneCode").value("CWH"))
        .andExpect(jsonPath("$[2].stocking.bec.becSubzoneCode").value("vm"))
        .andExpect(jsonPath("$[2].stocking.bec.becVariant").value("1"))
        .andExpect(jsonPath("$[2].stocking.bec.becSiteSeries").value("01"))
        .andExpect(jsonPath("$[2].stocking.regenDelay").value(3))
        .andExpect(jsonPath("$[2].stocking.freeGrowingLate").value(20))
        .andExpect(jsonPath("$[2].stocking.freeGrowingEarly").doesNotExist())
        .andExpect(jsonPath("$[2].stocking.additionalStandards").value("Test"))
        .andExpect(jsonPath("$[2].preferredSpecies[0].species.code").value("CW"))
        .andExpect(jsonPath("$[2].preferredSpecies[0].species.description").value("western redcedar"))
        .andExpect(jsonPath("$[2].preferredSpecies[0].minHeight").value(1.5))
        .andExpect(jsonPath("$[2].preferredSpecies[1].species.code").value("FDC"))
        .andExpect(jsonPath("$[2].preferredSpecies[1].species.description").value("coastal Douglas-fir"))
        .andExpect(jsonPath("$[2].preferredSpecies[1].minHeight").value(1.8))
        .andExpect(jsonPath("$[2].acceptableSpecies").isArray())
        .andExpect(jsonPath("$[2].acceptableSpecies").isEmpty())
        .andExpect(jsonPath("$[2].layers[0].layer.code").value("I"))
        .andExpect(jsonPath("$[2].layers[0].layer.description").value("Inventory Layer"))
        .andExpect(jsonPath("$[2].layers[0].minWellspacedTrees").doesNotExist())
        .andExpect(jsonPath("$[2].layers[0].minPreferredWellspacedTrees").value(600))
        .andExpect(jsonPath("$[2].layers[0].minHorizontalDistanceWellspacedTrees").value(1))
        .andExpect(jsonPath("$[2].layers[0].targetWellspacedTrees").value(900))
        .andExpect(jsonPath("$[2].layers[0].heightRelativeToComp").value(180))
        .andExpect(jsonPath("$[2].comments").isArray())
        .andExpect(jsonPath("$[2].comments").isEmpty());
  }


  @Test
  @DisplayName("Get Opening Activities Disturbances Details by existing openingId should succeed")
  void getOpeningActivitiesDisturbancesDetails_noResults_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1796497/disturbances")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").value("1"))
        .andExpect(jsonPath("$.content[0].atuId").value(4184301L))
        .andExpect(jsonPath("$.content[0].disturbance.code").value("B"))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Activities Disturbances Details sorted")
  void getOpeningActivitiesDisturbancesDetails_sorted() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1796497/disturbances?sort=atuId,asc")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").value("1"))
        .andExpect(jsonPath("$.content[0].atuId").value(4184301L))
        .andExpect(jsonPath("$.content[0].disturbance.code").value("B"))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Activities Activities Details by existing openingId should succeed")
  void getOpeningActivitiesActivitiesDetails_noResults_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1796497/activities")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").value("2"))
        .andExpect(jsonPath("$.content[0].atuId").value(4184319))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Activities PL should succeed")
  void getOpeningActivitiesActivitiesDetails_typePL_shouldReturn() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1796497/activities/4184319")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.licenseeActivityId").isEmpty())
        .andExpect(jsonPath("$.species").isArray())
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Activities SU should succeed")
  void getOpeningActivitiesActivitiesDetails_typeSU_shouldReturn() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1796497/activities/4184320")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.licenseeActivityId").value(4527))
        .andExpect(jsonPath("$.treatedAmount").value(6.3))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Activities PR should succeed")
  void getOpeningActivitiesActivitiesDetails_typePR_shouldReturn() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1524010/activities/3306979")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.licenseeActivityId").isEmpty())
        .andExpect(jsonPath("$.intraAgencyNumber").value("PR14LMN001"))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Activities JS should succeed")
  void getOpeningActivitiesActivitiesDetails_typeJS_shouldReturn() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1524010/activities/2930470")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.licenseeActivityId").isEmpty())
        .andExpect(jsonPath("$.intraAgencyNumber").value("SU12LMN013"))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Activities SP should succeed")
  void getOpeningActivitiesActivitiesDetails_typeSP_shouldReturn() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1009974/activities/1683934")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.licenseeActivityId").value(715011423))
        .andExpect(jsonPath("$.treatedAmount").value(0.5))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Activities General should succeed")
  void getOpeningActivitiesActivitiesDetails_typeGeneral_shouldReturn() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1009974/activities/3098703")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.licenseeActivityId").value(715042147))
        .andExpect(jsonPath("$.treatedAmount").value(25.5))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Tenures listAll should succeed")
  void getOpeningTenures_listAll_shouldReturn() throws Exception {

    mockMvc
        .perform(
            get("/api/openings/1589595/tenures")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").value("21"))
        .andExpect(jsonPath("$.content[0].id").value(258063))
        .andExpect(jsonPath("$.primary.id").value(258073))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Tenures list page should succeed")
  void getOpeningTenures_listPage_shouldReturn() throws Exception {

    mockMvc
        .perform(
            get("/api/openings/1589595/tenures")
                .param("page", "3")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("3"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").value("21"))
        .andExpect(jsonPath("$.totalUnfiltered").value("21"))
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content").isEmpty())
        .andExpect(jsonPath("$.primary.id").value(258073))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Tenures filter should succeed")
  void getOpeningTenures_listFilter_shouldReturn() throws Exception {

    mockMvc
        .perform(
            get("/api/openings/1589595/tenures")
                .param("filter", "073")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").value("1"))
        .andExpect(jsonPath("$.totalUnfiltered").value("21"))
        .andExpect(jsonPath("$.content[0].id").value(258114))
        .andExpect(jsonPath("$.primary.id").value(258073))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Forest Cover listAll should succeed")
  void getOpeningForestCover_listAll_shouldReturn() throws Exception {

    mockMvc
        .perform(
            get("/api/openings/60000/cover")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.[3].coverId").value(2638622))
        .andExpect(jsonPath("$.[3].polygonId").value("D4"))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Forest Cover details should succeed")
  void getOpeningForestCover_details_shouldReturn() throws Exception {

    mockMvc
        .perform(
            get("/api/openings/60000/cover/2638620")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.polygon.forestCoverId").value(2638620))
        .andExpect(jsonPath("$.unmapped").isEmpty())
        .andExpect(jsonPath("$.layers[0].totalWellSpaced").value(1164))
        .andExpect(jsonPath("$.layers[1].damage[0].damageAgent.code").value("IWS"))
        .andReturn();
  }

  @Test
  @DisplayName("Get Opening Forest Cover History Overview - empty list should return empty array")
  void getOpeningForestCoverHistoryOverview_empty_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1/cover/history/overview")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType("application/problem+json"))
        .andExpect(jsonPath("$.type").value("about:blank"))
        .andExpect(jsonPath("$.title").value("Not Found"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.detail").value("Forest cover history overview list for opening with id 1 record(s) not found!"))
        .andExpect(jsonPath("$.instance").value("/api/openings/1/cover/history/overview"));
  }

  @Test
  @DisplayName("Get Opening Forest Cover History Overview - non-empty should return expected list")
  void getOpeningForestCoverHistoryOverview_nonEmpty_shouldReturnList() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/101017/cover/history/overview")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        // Entry 0
        .andExpect(jsonPath("$[0].updateTimestamp").value("2012-05-02T00:00:00"))
        .andExpect(jsonPath("$[0].np").value(1.0))
        .andExpect(jsonPath("$[0].nsr").value(0.0))
        .andExpect(jsonPath("$[0].imm").value(15.7))
        .andExpect(jsonPath("$[0].other").value(0.0))
        .andExpect(jsonPath("$[0].total").value(16.7))
        .andExpect(jsonPath("$[0].hasDetails").value(true))
        .andExpect(jsonPath("$[0].isCurrent").value(true))
        .andExpect(jsonPath("$[0].isOldest").value(false))
        // Entry 1
        .andExpect(jsonPath("$[1].updateTimestamp").value("2004-11-29T10:08:00"))
        .andExpect(jsonPath("$[1].np").value(1.0))
        .andExpect(jsonPath("$[1].nsr").value(0.0))
        .andExpect(jsonPath("$[1].imm").value(15.6))
        .andExpect(jsonPath("$[1].other").value(0.0))
        .andExpect(jsonPath("$[1].total").value(16.6))
        .andExpect(jsonPath("$[1].hasDetails").value(true))
        .andExpect(jsonPath("$[1].isCurrent").value(false))
        .andExpect(jsonPath("$[1].isOldest").value(false))
        // Entry 2
        .andExpect(jsonPath("$[2].updateTimestamp").value("2004-10-01T00:00:01"))
        .andExpect(jsonPath("$[2].np").value(1.0))
        .andExpect(jsonPath("$[2].nsr").value(0.0))
        .andExpect(jsonPath("$[2].imm").value(15.6))
        .andExpect(jsonPath("$[2].other").value(0.0))
        .andExpect(jsonPath("$[2].total").value(16.6))
        .andExpect(jsonPath("$[2].hasDetails").value(false))
        .andExpect(jsonPath("$[2].isCurrent").value(false))
        .andExpect(jsonPath("$[2].isOldest").value(false))
        // Entry 3
        .andExpect(jsonPath("$[3].updateTimestamp").value("2002-06-11T00:00:00"))
        .andExpect(jsonPath("$[3].np").value(1.3))
        .andExpect(jsonPath("$[3].nsr").value(7.9))
        .andExpect(jsonPath("$[3].imm").value(7.4))
        .andExpect(jsonPath("$[3].other").value(0.0))
        .andExpect(jsonPath("$[3].total").value(16.6))
        .andExpect(jsonPath("$[3].hasDetails").value(true))
        .andExpect(jsonPath("$[3].isCurrent").value(false))
        .andExpect(jsonPath("$[3].isOldest").value(false))
        // Entry 4
        .andExpect(jsonPath("$[4].updateTimestamp").value("2002-06-04T00:00:00"))
        .andExpect(jsonPath("$[4].np").value(1.3))
        .andExpect(jsonPath("$[4].nsr").value(7.9))
        .andExpect(jsonPath("$[4].imm").value(7.4))
        .andExpect(jsonPath("$[4].other").value(0.0))
        .andExpect(jsonPath("$[4].total").value(16.6))
        .andExpect(jsonPath("$[4].hasDetails").value(false))
        .andExpect(jsonPath("$[4].isCurrent").value(false))
        .andExpect(jsonPath("$[4].isOldest").value(false))
        // Entry 5
        .andExpect(jsonPath("$[5].updateTimestamp").value("2002-06-03T00:00:00"))
        .andExpect(jsonPath("$[5].np").value(1.3))
        .andExpect(jsonPath("$[5].nsr").value(7.9))
        .andExpect(jsonPath("$[5].imm").value(7.4))
        .andExpect(jsonPath("$[5].other").value(0.0))
        .andExpect(jsonPath("$[5].total").value(16.6))
        .andExpect(jsonPath("$[5].hasDetails").value(false))
        .andExpect(jsonPath("$[5].isCurrent").value(false))
        .andExpect(jsonPath("$[5].isOldest").value(true));
  }

  @Test
  @DisplayName("Get Opening Forest Cover History - empty list should return 404 and empty array")
  void getOpeningForestCoverHistory_empty_shouldReturn404() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1/cover/history")
                .param("updateDate", "2020-01-01")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType("application/problem+json"))
        .andExpect(jsonPath("$.type").value("about:blank"))
        .andExpect(jsonPath("$.title").value("Not Found"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.detail").value("Forest cover history list for opening with id 1 and update date 2004-11-29 record(s) not found!"))
        .andExpect(jsonPath("$.instance").value("/api/openings/1/cover/history"));
  }

  @Test
  @DisplayName("Get Opening Forest Cover History - missing updateDate should return 400")
  void getOpeningForestCoverHistory_missingUpdateDate_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/101017/cover/history")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/problem+json"))
        .andExpect(jsonPath("$.type").value("about:blank"))
        .andExpect(jsonPath("$.title").value("Bad Request"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.detail").value("Required parameter 'updateDate' is not present."))
        .andExpect(jsonPath("$.instance").value("/api/openings/101017/cover/history"));
  }

  @Test
  @DisplayName("Get Opening Forest Cover History - non-empty for opening 101017 and updateDate 2004-11-29")
  void getOpeningForestCoverHistory_nonEmpty_shouldReturnList() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/101017/cover/history")
                .param("updateDate", "2004-11-29")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        // Entry 0
        .andExpect(jsonPath("$[0].coverId").value(1021182))
        .andExpect(jsonPath("$[0].archiveDate").value("2012-05-02"))
        .andExpect(jsonPath("$[0].polygonId").value("A"))
        .andExpect(jsonPath("$[0].standardUnitId").value("A"))
        .andExpect(jsonPath("$[0].grossArea").value(8.1))
        .andExpect(jsonPath("$[0].netArea").value(8.1))
        .andExpect(jsonPath("$[0].status.code").value("IMM"))
        .andExpect(jsonPath("$[0].coverType.code").value("ART"))
        .andExpect(jsonPath("$[0].inventoryLayer.species[0].code").value("FDC"))
        .andExpect(jsonPath("$[0].inventoryLayer.total").value(1136.0))
        .andExpect(jsonPath("$[0].referenceYear").value(2004))
        .andExpect(jsonPath("$[0].isSingleLayer").value(true))
        .andExpect(jsonPath("$[0].hasReserve").value(true))
        // Entry 1
        .andExpect(jsonPath("$[1].coverId").value(1021183))
        .andExpect(jsonPath("$[1].polygonId").value("B"))
        .andExpect(jsonPath("$[1].standardUnitId").value("B"))
        .andExpect(jsonPath("$[1].grossArea").value(1.1))
        .andExpect(jsonPath("$[1].netArea").value(1.1))
        .andExpect(jsonPath("$[1].status.code").value("IMM"))
        .andExpect(jsonPath("$[1].coverType.code").value("NAT"))
        .andExpect(jsonPath("$[1].inventoryLayer.species[0].code").value("FDC"))
        .andExpect(jsonPath("$[1].inventoryLayer.total").value(500.0))
        .andExpect(jsonPath("$[1].referenceYear").value(2000))
        .andExpect(jsonPath("$[1].isSingleLayer").value(true))
        .andExpect(jsonPath("$[1].hasReserve").value(true))
        // Entry 2
        .andExpect(jsonPath("$[2].coverId").value(1021184))
        .andExpect(jsonPath("$[2].polygonId").value("C"))
        .andExpect(jsonPath("$[2].standardUnitId").doesNotExist())
        .andExpect(jsonPath("$[2].grossArea").value(1.4))
        .andExpect(jsonPath("$[2].netArea").value(1.4))
        .andExpect(jsonPath("$[2].status.code").value("IMM"))
        .andExpect(jsonPath("$[2].coverType.code").value("NAT"))
        .andExpect(jsonPath("$[2].inventoryLayer.species[0].code").value("FDC"))
        .andExpect(jsonPath("$[2].inventoryLayer.total").value(500.0))
        .andExpect(jsonPath("$[2].referenceYear").value(2000))
        .andExpect(jsonPath("$[2].isSingleLayer").value(true))
        .andExpect(jsonPath("$[2].hasReserve").value(true))
        // Entry 3
        .andExpect(jsonPath("$[3].coverId").value(1021185))
        .andExpect(jsonPath("$[3].polygonId").value("D"))
        .andExpect(jsonPath("$[3].standardUnitId").doesNotExist())
        .andExpect(jsonPath("$[3].grossArea").value(1.3))
        .andExpect(jsonPath("$[3].netArea").value(1.3))
        .andExpect(jsonPath("$[3].status.code").value("IMM"))
        .andExpect(jsonPath("$[3].coverType.code").value("NAT"))
        .andExpect(jsonPath("$[3].inventoryLayer.species[0].code").value("FDC"))
        .andExpect(jsonPath("$[3].referenceYear").value(2000))
        .andExpect(jsonPath("$[3].isSingleLayer").value(true))
        .andExpect(jsonPath("$[3].hasReserve").value(true))
        // Entry 4
        .andExpect(jsonPath("$[4].coverId").value(1021186))
        .andExpect(jsonPath("$[4].polygonId").value("E"))
        .andExpect(jsonPath("$[4].standardUnitId").doesNotExist())
        .andExpect(jsonPath("$[4].grossArea").value(3.7))
        .andExpect(jsonPath("$[4].netArea").value(3.7))
        .andExpect(jsonPath("$[4].status.code").value("IMM"))
        .andExpect(jsonPath("$[4].coverType.code").value("NAT"))
        .andExpect(jsonPath("$[4].inventoryLayer.species[0].code").value("FDC"))
        .andExpect(jsonPath("$[4].inventoryLayer.total").value(500.0))
        .andExpect(jsonPath("$[4].referenceYear").value(2000))
        .andExpect(jsonPath("$[4].isSingleLayer").value(true))
        .andExpect(jsonPath("$[4].hasReserve").value(true))
        // Entry 5
        .andExpect(jsonPath("$[5].coverId").value(1021187))
        .andExpect(jsonPath("$[5].polygonId").value("F"))
        .andExpect(jsonPath("$[5].standardUnitId").doesNotExist())
        .andExpect(jsonPath("$[5].grossArea").value(1.0))
        .andExpect(jsonPath("$[5].netArea").value(1.0))
        .andExpect(jsonPath("$[5].status.code").value("NP"))
        .andExpect(jsonPath("$[5].coverType.code").value("UNN"))
        .andExpect(jsonPath("$[5].inventoryLayer.species[0].code").doesNotExist())
        .andExpect(jsonPath("$[5].referenceYear").value(2001))
        .andExpect(jsonPath("$[5].isSingleLayer").value(true))
        .andExpect(jsonPath("$[5].hasReserve").value(true));
  }

  @Test
  @DisplayName("Get Opening Forest Cover History - filtered by mainSearchTerm for opening 101017 and updateDate 2004-11-29")
  void getOpeningForestCoverHistory_filtered_shouldReturnFilteredList() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/101017/cover/history")
                .param("updateDate", "2004-11-29")
                .param("mainSearchTerm", "1.4")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].coverId").value(1021184))
        .andExpect(jsonPath("$[0].archiveDate").value("2012-05-02"))
        .andExpect(jsonPath("$[0].polygonId").value("C"))
        .andExpect(jsonPath("$[0].standardUnitId").doesNotExist())
        .andExpect(jsonPath("$[0].grossArea").value(1.4))
        .andExpect(jsonPath("$[0].netArea").value(1.4))
        .andExpect(jsonPath("$[0].status.code").value("IMM"))
        .andExpect(jsonPath("$[0].coverType.code").value("NAT"))
        .andExpect(jsonPath("$[0].inventoryLayer.species[0].code").value("FDC"))
        .andExpect(jsonPath("$[0].inventoryLayer.total").value(500.0))
        .andExpect(jsonPath("$[0].referenceYear").value(2000))
        .andExpect(jsonPath("$[0].isSingleLayer").value(true))
        .andExpect(jsonPath("$[0].hasReserve").value(true));
  }

  @Test
  @DisplayName("Get Forest Cover History Details - not found should return 404")
  void getForestCoverHistoryDetails_notFound_shouldReturn404() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/1/cover/history/1")
                .param("archiveDate", "2000-01-01")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType("application/problem+json"))
        .andExpect(jsonPath("$.type").value("about:blank"))
        .andExpect(jsonPath("$.title").value("Not Found"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.detail").value("Forest cover history polygon with id 1 and archive date 2000-01-01 record(s) not found!"))
        .andExpect(jsonPath("$.instance").value("/api/openings/1/cover/history/1"));;
  }

  @Test
  @DisplayName("Get Forest Cover History Details - missing archiveDate should return 400")
  void getForestCoverHistoryDetails_missingArchiveDate_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/101017/cover/history/1021182")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/problem+json"))
        .andExpect(jsonPath("$.type").value("about:blank"))
        .andExpect(jsonPath("$.title").value("Bad Request"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.detail").value("Required parameter 'archiveDate' is not present."))
        .andExpect(jsonPath("$.instance").value("/api/openings/101017/cover/history/1021182"));
  }

  @Test
  @DisplayName("Get Forest Cover History Details - valid request should return details")
  void getForestCoverHistoryDetails_valid_shouldReturnDetails() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/101017/cover/history/1021182")
                .param("archiveDate", "2012-05-02")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.polygon.forestCoverId").value(1021182))
        .andExpect(jsonPath("$.polygon.reserve.code").value("N"))
        .andExpect(jsonPath("$.polygon.reserve.description").value("No Reserve"))
        .andExpect(jsonPath("$.polygon.siteIndex").value(32))
        .andExpect(jsonPath("$.polygon.siteIndexSource.code").value("H"))
        .andExpect(jsonPath("$.polygon.siteIndexSource.description").value("SI from stand before harvest"))
        .andExpect(jsonPath("$.isSingleLayer").value(true))
        .andExpect(jsonPath("$.unmapped").isArray())
        .andExpect(jsonPath("$.layers[0].layerId").value(1032926))
        .andExpect(jsonPath("$.layers[0].layer.code").value("I"))
        .andExpect(jsonPath("$.layers[0].layer.description").value("Inventory Layer"))
        .andExpect(jsonPath("$.layers[0].totalStems").value(1136))
        .andExpect(jsonPath("$.layers[0].species[0].species.code").value("FDC"))
        .andExpect(jsonPath("$.layers[0].species[0].percentage").value(49))
        .andExpect(jsonPath("$.layers[0].species[0].averageAge").value(3))
        .andExpect(jsonPath("$.layers[0].species[0].averageHeight").value(0.2))
        .andExpect(jsonPath("$.layers[1].layerId").value(1032927))
        .andExpect(jsonPath("$.layers[1].layer.code").value("S"))
        .andExpect(jsonPath("$.layers[1].layer.description").value("Silviculture Layer - even aged"))
        .andExpect(jsonPath("$.layers[1].wellSpaced").value(582))
        .andExpect(jsonPath("$.layers[1].species[0].species.code").value("FDC"))
        .andExpect(jsonPath("$.layers[1].species[0].percentage").value(76))
        .andExpect(jsonPath("$.layers[1].species[0].averageAge").value(3))
        .andExpect(jsonPath("$.layers[1].species[0].averageHeight").value(0.3));
  }


  @Test
  @DisplayName("Get attachments metadata by openingId should return list")
  void getAttachmentsMetadata_shouldReturnList() throws Exception {
    Long openingId = 1589595L;

    mockMvc
        .perform(
            get("/api/openings/" + openingId + "/attachments").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", Matchers.greaterThanOrEqualTo(1)))
        .andExpect(jsonPath("$[0].openingId").value(openingId))
        .andExpect(jsonPath("$[0].attachmentGuid").isNotEmpty());
  }

  @Test
  @DisplayName("Get attachment by GUID should return presigned URL")
  void getAttachmentByGuid_shouldReturnPresignedUrl() throws Exception {
    Long openingId = 1589595L;

    MvcResult result =
        mockMvc
            .perform(
                get("/api/openings/" + openingId + "/attachments")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].attachmentGuid").isNotEmpty())
            .andReturn();

    String json = result.getResponse().getContentAsString();
    String guid = JsonPath.read(json, "$[0].attachmentGuid");

    mockMvc
        .perform(
            get("/api/openings/" + openingId + "/attachments/" + guid)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(Matchers.startsWith("http")));
  }

  @Test
  @DisplayName("Get attachment by non-existing GUID should return 404")
  void getAttachmentByGuid_shouldReturnNotFound() throws Exception {
    UUID nonExistentGuid = UUID.randomUUID();

    mockMvc
        .perform(
            get("/api/openings/1589595/attachments/" + nonExistentGuid)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
