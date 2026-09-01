package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.config.TenureEndpointTestConfig;
import ca.bc.gov.restapi.results.postgres.dto.DuplicateConflictDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureRemovalValidationResultDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureUpdateValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResultDto;
import ca.bc.gov.restapi.results.postgres.enums.TenureValidationErrorCode;
import ca.bc.gov.restapi.results.postgres.service.TenureValidationService;
import ca.bc.gov.restapi.results.postgres.service.UpdateTenuresService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Integration Test | Tenure Endpoint")
@AutoConfigureMockMvc
@Import(TenureEndpointTestConfig.class)
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
class TenureEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private TenureValidationService tenureValidationService;
  @Autowired private UpdateTenuresService updateTenuresService;

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
    Mockito.reset(updateTenuresService);
  }

  @Test
  @WithMockJwt
  @DisplayName("Update tenure list succeeds with 204")
  void updateTenures_validList_returns204() throws Exception {
    when(updateTenuresService.updateTenures(any(), anyString(), any()))
        .thenReturn(Optional.empty());

    mockMvc
        .perform(
            put("/api/openings/101/tenures?clientNumber=12345678")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [{"cboaId": 1, "revisionCount": 2, "fileId": "TFL001", "cuttingPermit": "CP1", "cutBlock": "CB001", "isPrimary": true}]
                    """))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockJwt
  @DisplayName("Update tenure list returns structured 422 validation")
  void updateTenures_validationFailure_returns422() throws Exception {
    TenureValidationResponseDto validation =
        new TenureValidationResponseDto(
            List.of(
                new TenureValidationResultDto(
                    0,
                    false,
                    TenureValidationErrorCode.STALE_TENURE,
                    "Tenure was changed by another request")),
            List.of(),
            false,
            List.of(),
            Map.of());
    when(updateTenuresService.updateTenures(any(), anyString(), any()))
        .thenReturn(
            Optional.of(
                new TenureUpdateValidationResponseDto(
                    validation,
                    List.of(
                        new TenureRemovalValidationResultDto(
                            1L,
                            TenureValidationErrorCode.DISTURBANCE_EXISTS,
                            "Tenure cannot be removed while a disturbance activity exists")))));

    mockMvc
        .perform(
            put("/api/openings/101/tenures?clientNumber=12345678")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [{"cboaId": 1, "revisionCount": 1, "fileId": "TFL001", "cuttingPermit": null, "cutBlock": "CB001", "isPrimary": true}]
                    """))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(
            jsonPath("$.tenureValidation.validationResults[0].errorCode").value("STALE_TENURE"))
        .andExpect(jsonPath("$.removalErrors[0].cboaId").value(1))
        .andExpect(jsonPath("$.removalErrors[0].errorCode").value("DISTURBANCE_EXISTS"));
  }

  @Test
  @WithMockJwt
  @DisplayName("Valid tenure list should return 200 with isValid=true")
  void validateTenures_validList_returns200WithIsValidTrue() throws Exception {
    TenureValidationResponseDto response =
        new TenureValidationResponseDto(
            List.of(
                new TenureValidationResultDto(0, true, null, null),
                new TenureValidationResultDto(1, true, null, null)),
            List.of(),
            true,
            List.of(
                new TenureDto("TFL001", "CP1", "CB001", true, "TM001"),
                new TenureDto("TFL002", null, "CB002", false, "TM001")),
            Map.of());
    when(tenureValidationService.validateTenures(any(), anyString())).thenReturn(response);

    mockMvc
        .perform(
            post("/api/tenures/validate?clientNumber=12345678")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(VALID_TENURES_JSON.getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(true))
        .andExpect(jsonPath("$.duplicateConflicts").isEmpty())
        .andExpect(jsonPath("$.validationResults[0].isValid").value(true))
        .andExpect(jsonPath("$.validationResults[1].isValid").value(true))
        .andExpect(jsonPath("$.tenures[0].timberMark").value("TM001"))
        .andExpect(jsonPath("$.tenures[1].timberMark").value("TM001"));
  }

  @Test
  @WithMockJwt
  @DisplayName("Duplicate tenures should return 200 with isValid=false and conflicts populated")
  void validateTenures_withDuplicate_returns200IsValidFalse() throws Exception {
    TenureValidationResponseDto response =
        new TenureValidationResponseDto(
            List.of(
                new TenureValidationResultDto(0, true, null, null),
                new TenureValidationResultDto(1, true, null, null)),
            List.of(
                new DuplicateConflictDto(
                    List.of(0, 1),
                    "fileId=TFL001, cuttingPermit=CP1, cutBlock=CB001",
                    TenureValidationErrorCode.DUPLICATE_IN_REQUEST)),
            false,
            List.of(),
            Map.of());
    when(tenureValidationService.validateTenures(any(), anyString())).thenReturn(response);

    mockMvc
        .perform(
            post("/api/tenures/validate?clientNumber=12345678")
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
            post("/api/tenures/validate?clientNumber=12345678")
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
        new TenureValidationResponseDto(List.of(), List.of(), true, List.of(), Map.of());
    when(tenureValidationService.validateTenures(any(), anyString())).thenReturn(response);

    mockMvc
        .perform(
            post("/api/tenures/validate?clientNumber=12345678")
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
