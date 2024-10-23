package ca.bc.gov.restapi.results.oracle.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WithMockJwt(value = "ttester")
@AutoConfigureMockMvc
@DisplayName("Integrated Test | Opening Endpoint")
class OpeningEndpointTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Request a list of recent openings for the home screen")
  void getRecentOpenings_fetchPaginated_shouldSucceed() throws Exception {
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
        .andExpect(jsonPath("$.data[0].openingId").value("103"))
        .andExpect(jsonPath("$.data[0].fileId").value("TFL49"))
        .andExpect(jsonPath("$.data[0].cuttingPermit").value("14T"))
        .andExpect(jsonPath("$.data[0].timberMark").value("49/14S"))
        .andExpect(jsonPath("$.data[0].cutBlock").value("14-71"))
        .andExpect(jsonPath("$.data[0].grossAreaHa").value("14.9"))
        .andExpect(jsonPath("$.data[0].status.code").value(OpeningStatusEnum.APP.getCode()))
        .andExpect(
            jsonPath("$.data[0].status.description").value(OpeningStatusEnum.APP.getDescription()))
        .andExpect(jsonPath("$.data[0].category.code").value(OpeningCategoryEnum.FTML.getCode()))
        .andExpect(
            jsonPath("$.data[0].category.description")
                .value(OpeningCategoryEnum.FTML.getDescription()))
        .andExpect(jsonPath("$.data[0].disturbanceStart").value("2024-01-25"))
        .andReturn();
  }
}
