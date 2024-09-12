package ca.bc.gov.restapi.results.common.endpoint;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ForestClientEndpoint.class)
@WithMockUser(roles = "user_read")
class ForestClientEndpointTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ForestClientService forestClientService;

  @Test
  @DisplayName("Get forest client happy path should succeed")
  void getForestClient_happyPath_shouldSucceed() throws Exception {
    String clientNumber = "149081";

    ForestClientDto clientDto =
        new ForestClientDto(
            clientNumber,
            "WESTERN FOREST PRODUCTS INC.",
            null,
            null,
            ForestClientStatusEnum.ACTIVE,
            ForestClientTypeEnum.CORPORATION,
            "WFP");

    when(forestClientService.getClientByNumber(clientNumber)).thenReturn(Optional.of(clientDto));

    ForestClientStatusEnum active =
        ForestClientStatusEnum.of(clientDto.clientStatusCode().getCode());
    ForestClientTypeEnum type = ForestClientTypeEnum.of(clientDto.clientTypeCode().getCode());

    mockMvc
        .perform(
            get("/api/forest-clients/{clientNumber}", clientNumber)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.clientNumber").value(clientNumber))
        .andExpect(jsonPath("$.clientName").value(clientDto.clientName()))
        .andExpect(jsonPath("$.legalFirstName").value(clientDto.legalFirstName()))
        .andExpect(jsonPath("$.legalMiddleName").value(clientDto.legalMiddleName()))
        .andExpect(jsonPath("$.clientStatusCode.code").value(active.getCode()))
        .andExpect(jsonPath("$.clientTypeCode.code").value(type.getCode().toString()))
        .andExpect(jsonPath("$.acronym").value(clientDto.acronym()))
        .andReturn();
  }

  @Test
  @DisplayName("Get forest client not found should succeed")
  void getForestClient_notFound_shouldSucceed() throws Exception {
    String clientNumber = "111";

    when(forestClientService.getClientByNumber(clientNumber)).thenReturn(Optional.empty());

    mockMvc
        .perform(
            get("/api/forest-clients/{clientNumber}", clientNumber)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }
}
