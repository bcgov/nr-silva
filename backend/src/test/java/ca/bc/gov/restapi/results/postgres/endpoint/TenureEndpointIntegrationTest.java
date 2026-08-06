package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.config.TenureEndpointTestConfig;
import ca.bc.gov.restapi.results.postgres.dto.DuplicateConflictDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResultDto;
import ca.bc.gov.restapi.results.postgres.service.TenureValidationService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Integration Test | Tenure Endpoint")
@AutoConfigureMockMvc
@Import(TenureEndpointTestConfig.class)
class TenureEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private TenureValidationService tenureValidationService;

  private static final String VALID_TENURES_JSON =
      """
      [
        {"fileId": "TFL001", "cuttingPermit": "CP1", "cutBlock": "CB001", "isPrimary": true},
        {"fileId": "TFL002", "cuttingPermit": null, "cutBlock": "CB002", "isPrimary": false}
      ]
      """;

  @BeforeEach
  void resetMocks() {
    Mockito.reset(tenureValidationService);
  }

  @Test
  @WithMockJwt
  @DisplayName("Valid tenure list should return 200 with isValid=true")
  void validateTenures_validList_returns200WithIsValidTrue() throws Exception {
    TenureValidationResponseDto response =
        new TenureValidationResponseDto(
            List.of(
                new TenureValidationResultDto(0, true, null),
                new TenureValidationResultDto(1, true, null)),
            List.of(),
            true);
    when(tenureValidationService.validateTenures(any())).thenReturn(response);

    mockMvc
        .perform(
            post("/api/tenures/validate")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(VALID_TENURES_JSON.getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(true))
        .andExpect(jsonPath("$.duplicateConflicts").isEmpty())
        .andExpect(jsonPath("$.validationResults[0].isValid").value(true))
        .andExpect(jsonPath("$.validationResults[1].isValid").value(true));
  }

  @Test
  @WithMockJwt
  @DisplayName("Duplicate tenures should return 200 with isValid=false and conflicts populated")
  void validateTenures_withDuplicate_returns200IsValidFalse() throws Exception {
    TenureValidationResponseDto response =
        new TenureValidationResponseDto(
            List.of(
                new TenureValidationResultDto(0, true, null),
                new TenureValidationResultDto(1, true, null)),
            List.of(new DuplicateConflictDto(List.of(0, 1), "fileId=TFL001, cutBlock=CB001")),
            false);
    when(tenureValidationService.validateTenures(any())).thenReturn(response);

    mockMvc
        .perform(
            post("/api/tenures/validate")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(VALID_TENURES_JSON.getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(false))
        .andExpect(jsonPath("$.duplicateConflicts").isNotEmpty())
        .andExpect(
            jsonPath("$.duplicateConflicts[0].reason")
                .value("fileId=TFL001, cuttingPermit=CP1, cutBlock=CB001"));
  }

  @Test
  @WithMockJwt
  @DisplayName("Request with bean validation violation should return 400")
  void validateTenures_withBeanValidationError_returns400() throws Exception {
    String body = """
        [{"fileId": "", "cutBlock": "CB001", "isPrimary": true}]
        """;

    mockMvc
        .perform(
            post("/api/tenures/validate")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body.getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockJwt
  @DisplayName("Empty tenure list should return 200")
  void validateTenures_emptyList_returns200() throws Exception {
    TenureValidationResponseDto response =
        new TenureValidationResponseDto(List.of(), List.of(), true);
    when(tenureValidationService.validateTenures(any())).thenReturn(response);

    mockMvc
        .perform(
            post("/api/tenures/validate")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("[]".getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(true));
  }

  @Test
  @WithMockJwt
  @DisplayName("Request without CSRF token should return 403")
  void validateTenures_withoutCsrf_returns403() throws Exception {
    mockMvc
        .perform(
            post("/api/tenures/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(VALID_TENURES_JSON.getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Unauthenticated request should return 401")
  void validateTenures_unauthenticated_returns401() throws Exception {
    mockMvc
        .perform(
            post("/api/tenures/validate")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(VALID_TENURES_JSON.getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isUnauthorized());
  }
}
