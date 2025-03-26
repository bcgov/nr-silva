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

  @Autowired
  private MockMvc mockMvc;

  @RegisterExtension
  static WireMockExtension clientApiStub = WireMockExtension
      .newInstance()
      .options(
          wireMockConfig()
              .port(10000)
              .notifier(new WiremockLogNotifier())
              .asynchronousResponseEnabled(true)
              .stubRequestLoggingDisabled(false)
      )
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
            .willReturn(okJson("""
                {
                  "clientNumber": "00000003",
                  "clientName": "MINISTRY OF FORESTS",
                  "legalFirstName": null,
                  "legalMiddleName": null,
                  "clientStatusCode": "ACT",
                  "clientTypeCode": "F",
                  "acronym": "MOF"
                }
                """)
            )
    );

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

}
