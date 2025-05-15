package ca.bc.gov.restapi.results.oracle.endpoint;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WithMockJwt
@DisplayName("Integrated Test | Opening Search Endpoint")
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
        .andExpect(jsonPath("$[0].stocking.ssid").value(1013720L))
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
  @DisplayName("Get Opening Stocking Details by non-existing openingId should return 404")
  void getOpeningStockingDetails_nonExistingOpeningId_shouldReturn404() throws Exception {

    mockMvc
        .perform(
            get("/api/openings/999999999/ssu")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
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
        .andExpect(jsonPath("$.species[0].numberBeyondTransferLimit").value(0))
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
}
