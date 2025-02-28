package ca.bc.gov.restapi.results.oracle.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
class OpeningSearchEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Opening search happy path should succeed")
  void openingSearch_happyPath_shouldSucceed() throws Exception {
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
            get("/api/openings/search?mainSearchTerm=101")
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
