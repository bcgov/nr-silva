package ca.bc.gov.restapi.results.postgres.endpoint;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.testconfig.RealTenureEndToEndTestConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import jakarta.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * End-to-end integration tests exercising {@code POST /api/tenures/validate}, {@code POST
 * /api/openings}, and {@code PUT /api/openings/{openingId}/tenures} through the real {@link
 * ca.bc.gov.restapi.results.common.provider.DelegatingTenureProvider} and {@link
 * ca.bc.gov.restapi.results.common.provider.mock.MockTenureAdapter} against PostgreSQL.
 */
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Tenure Validation End-to-End (Postgres + Mock)")
@AutoConfigureMockMvc
@Import(RealTenureEndToEndTestConfig.class)
@TestPropertySource(properties = "silva.integrations.tenure.provider=mock")
@WithMockJwt(cognitoGroups = {"Submitter_00010003"})
@Transactional
class TenureValidationEndToEndIntegrationTest extends AbstractTestContainerIntegrationTest {

  private static final String CLIENT_NUMBER = "00010003";
  private static final String OVERRIDE_PROPERTY_SOURCE = "tenure-e2e-runtime-override";
  private static final String TENURE_PROVIDER_PROP = "silva.integrations.tenure.provider";

  private static final String SQUARE_EPSG_3005_GEOJSON =
      """
      {"type":"FeatureCollection","crs":{"properties":{"name":"EPSG:3005"}},
       "features":[{"type":"Feature","properties":{},"geometry":{"type":"Polygon",
       "coordinates":[[[850000,1500000],[850100,1500000],[850100,1500100],
                        [850000,1500100],[850000,1500000]]]}}]}
      """;

  private static final String MAPSHEET_WFS_RESPONSE =
      """
      {
        "type": "FeatureCollection",
        "features": [{
          "type": "Feature",
          "geometry": null,
          "properties": {"MAP_TILE": "092L057"}
        }]
      }
      """;

  @RegisterExtension
  static WireMockExtension openMapsStub =
      WireMockExtension.newInstance()
          .options(
              wireMockConfig()
                  .port(10001)
                  .notifier(new WiremockLogNotifier())
                  .asynchronousResponseEnabled(true))
          .build();

  @Autowired private MockMvc mockMvc;
  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private EntityManager entityManager;
  @Autowired private ConfigurableEnvironment environment;
  @Autowired private ObjectMapper objectMapper;
  @MockitoBean private VirusScanService virusScanService;

  @AfterEach
  void clearPropertyOverrides() {
    environment.getPropertySources().remove(OVERRIDE_PROPERTY_SOURCE);
  }

  @Test
  @DisplayName("POST /api/tenures/validate validates mock fixtures, failures, and duplicates")
  void validateTenures_mockProviderCases_returnsExpectedResults() throws Exception {
    // 1. Valid synthetic fixtures (both with and without cuttingPermit)
    mockMvc
        .perform(
            post("/api/tenures/validate?clientNumber=" + CLIENT_NUMBER)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [
                      {
                        "fileId": "TFL001",
                        "cuttingPermit": "CP1",
                        "cutBlock": "CB001",
                        "isPrimary": true
                      },
                      {
                        "fileId": "TFL002",
                        "cuttingPermit": null,
                        "cutBlock": "CB002",
                        "isPrimary": false
                      }
                    ]
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(true))
        .andExpect(jsonPath("$.tenures.length()").value(2))
        .andExpect(jsonPath("$.tenures[0].timberMark").value("47/12K"))
        .andExpect(jsonPath("$.tenures[1].timberMark").value("47/12M"));

    // 2. Non-existent, wrong licensee, and bean validation (400) cases
    mockMvc
        .perform(
            post("/api/tenures/validate?clientNumber=" + CLIENT_NUMBER)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [
                      {
                        "fileId": "TFL999",
                        "cuttingPermit": null,
                        "cutBlock": "CB999",
                        "isPrimary": true
                      },
                      {
                        "fileId": "TFL01",
                        "cuttingPermit": "104",
                        "cutBlock": "04",
                        "isPrimary": false
                      }
                    ]
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(false))
        .andExpect(jsonPath("$.validationResults[0].errorCode").value("TENURE_NOT_FOUND"))
        .andExpect(jsonPath("$.validationResults[1].errorCode").value("CLIENT_NOT_LICENSEE"))
        .andExpect(jsonPath("$.tenures").isEmpty());

    mockMvc
        .perform(
            post("/api/tenures/validate?clientNumber=" + CLIENT_NUMBER)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [
                      {
                        "fileId": " ",
                        "cuttingPermit": null,
                        "cutBlock": "CB001",
                        "isPrimary": false
                      }
                    ]
                    """))
        .andExpect(status().isBadRequest());

    // 3. Duplicate in request (case-insensitive)
    mockMvc
        .perform(
            post("/api/tenures/validate?clientNumber=" + CLIENT_NUMBER)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [
                      {
                        "fileId": "TFL001",
                        "cuttingPermit": "CP1",
                        "cutBlock": "CB001",
                        "isPrimary": true
                      },
                      {
                        "fileId": "tfl001",
                        "cuttingPermit": "cp1",
                        "cutBlock": "cb001",
                        "isPrimary": false
                      }
                    ]
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(false))
        .andExpect(jsonPath("$.duplicateConflicts[0].conflictCode").value("DUPLICATE_IN_REQUEST"));
  }

  @Test
  @DisplayName("POST /api/tenures/validate enforces client authorization (403)")
  void validateTenures_unauthorizedClient_returns403() throws Exception {
    mockMvc
        .perform(
            post("/api/tenures/validate?clientNumber=00099999")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [
                      {
                        "fileId": "TFL001",
                        "cuttingPermit": "CP1",
                        "cutBlock": "CB001",
                        "isPrimary": true
                      }
                    ]
                    """))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName(
      "Runtime switch to oracle provider with EXTERNAL_ORACLE_ENABLED=false returns 503"
          + " ProblemDetail")
  void validateTenures_runtimeOracleDisabled_returns503ProblemDetail() throws Exception {
    environment
        .getPropertySources()
        .addFirst(
            new MapPropertySource(
                OVERRIDE_PROPERTY_SOURCE, Map.of(TENURE_PROVIDER_PROP, "oracle")));

    mockMvc
        .perform(
            post("/api/tenures/validate?clientNumber=" + CLIENT_NUMBER)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [
                      {
                        "fileId": "A72929",
                        "cuttingPermit": "001",
                        "cutBlock": "028",
                        "isPrimary": true
                      }
                    ]
                    """))
        .andExpect(status().isServiceUnavailable())
        .andExpect(jsonPath("$.title").value("External Service Error"))
        .andExpect(jsonPath("$.code").value("EXTERNAL_SERVICE_UNAVAILABLE"))
        .andExpect(jsonPath("$.serviceName").value("FTA"));
  }

  @Test
  @DisplayName(
      "Create opening, duplicate check, and update tenures persist external cb_skey without"
          + " cboa_cb_fk")
  void createAndUpdateOpening_withSyntheticMockTenures_persistsWithoutCboaCutBlockFk()
      throws Exception {
    openMapsStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("typeName", equalTo("WHSE_BASEMAPPING.BCGS_20K_GRID"))
            .willReturn(okJson(MAPSHEET_WFS_RESPONSE)));

    String createPayload =
        """
        {
          "openingGrossArea": 1.0,
          "maxAllowablePermAccessPerc": 5.0,
          "clientNumber": "00010003",
          "orgUnitCode": "DAS",
          "openingCategoryCode": "FTML",
          "licenseeOpeningId": "E2E-MOCK-1",
          "tenures": [
            {"fileId": "TFL001", "cuttingPermit": "CP1", "cutBlock": "CB001", "isPrimary": true}
          ]
        }
        """;

    MockMultipartFile dataPart =
        new MockMultipartFile(
            "data",
            "data.json",
            MediaType.APPLICATION_JSON_VALUE,
            createPayload.getBytes(StandardCharsets.UTF_8));
    MockMultipartFile filePart =
        new MockMultipartFile(
            "file",
            "opening.geojson",
            MediaType.APPLICATION_JSON_VALUE,
            SQUARE_EPSG_3005_GEOJSON.getBytes(StandardCharsets.UTF_8));

    MvcResult createResult =
        mockMvc
            .perform(
                multipart("/api/openings")
                    .file(dataPart)
                    .file(filePart)
                    .with(csrf().asHeader())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.openingId").isNumber())
            .andReturn();

    // Flush and clear Hibernate session so JdbcTemplate sees the reconciled revision_count=2.
    entityManager.flush();
    entityManager.clear();

    JsonNode createdJson = objectMapper.readTree(createResult.getResponse().getContentAsString());
    long openingId = createdJson.get("openingId").asLong();

    // Confirm cb_skey=1001 does NOT exist in replicated silva.cut_block yet persisted cleanly.
    Integer cutBlockCount =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM silva.cut_block WHERE cb_skey = 1001", Integer.class);
    assertThat(cutBlockCount).isZero();

    Map<String, Object> cboaRow =
        jdbcTemplate.queryForMap(
            "SELECT cut_block_open_admin_id, cb_skey, timber_mark, revision_count "
                + "FROM silva.cut_block_open_admin WHERE opening_id = ? AND cut_block_id = 'CB001'",
            openingId);
    assertThat(((Number) cboaRow.get("cb_skey")).longValue()).isEqualTo(1001L);
    assertThat(cboaRow.get("timber_mark")).isEqualTo("47/12K");

    long existingCboaId = ((Number) cboaRow.get("cut_block_open_admin_id")).longValue();
    int existingRevision = ((Number) cboaRow.get("revision_count")).intValue();
    assertThat(existingRevision).isEqualTo(2);

    // Re-validating with lowercase identifiers still detects TENURE_DUPLICATE_OPENING.
    mockMvc
        .perform(
            post("/api/tenures/validate?clientNumber=" + CLIENT_NUMBER)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [
                      {
                        "fileId": "tfl001",
                        "cuttingPermit": "cp1",
                        "cutBlock": "cb001",
                        "isPrimary": true
                      }
                    ]
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(false))
        .andExpect(jsonPath("$.validationResults[0].errorCode").value("TENURE_DUPLICATE_OPENING"));

    // PUT /api/openings/{openingId}/tenures adding second synthetic block TFL002/null/CB002.
    String updatePayload =
        """
        [
          {
            "cboaId": %d,
            "revisionCount": %d,
            "fileId": "TFL001",
            "cuttingPermit": "CP1",
            "cutBlock": "CB001",
            "isPrimary": true
          },
          {
            "cboaId": null,
            "revisionCount": null,
            "fileId": "TFL002",
            "cuttingPermit": null,
            "cutBlock": "CB002",
            "isPrimary": false
          }
        ]
        """
            .formatted(existingCboaId, existingRevision);

    mockMvc
        .perform(
            put("/api/openings/{openingId}/tenures?clientNumber=" + CLIENT_NUMBER, openingId)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
        .andExpect(status().isNoContent());

    entityManager.flush();
    entityManager.clear();

    Map<String, Object> secondCboaRow =
        jdbcTemplate.queryForMap(
            "SELECT cb_skey, timber_mark FROM silva.cut_block_open_admin "
                + "WHERE opening_id = ? AND cut_block_id = 'CB002'",
            openingId);
    assertThat(((Number) secondCboaRow.get("cb_skey")).longValue()).isEqualTo(1002L);
    assertThat(secondCboaRow.get("timber_mark")).isEqualTo("47/12M");

    int updatedRevision =
        jdbcTemplate.queryForObject(
            "SELECT revision_count FROM silva.cut_block_open_admin "
                + "WHERE cut_block_open_admin_id = ?",
            Integer.class,
            existingCboaId);

    // Unauthorized added block is rejected with 422 via TenureProvider.
    String invalidUpdatePayload =
        """
        [
          {
            "cboaId": %d,
            "revisionCount": %d,
            "fileId": "TFL001",
            "cuttingPermit": "CP1",
            "cutBlock": "CB001",
            "isPrimary": true
          },
          {
            "cboaId": null,
            "revisionCount": null,
            "fileId": "TFL01",
            "cuttingPermit": "104",
            "cutBlock": "04",
            "isPrimary": false
          }
        ]
        """
            .formatted(existingCboaId, updatedRevision);

    mockMvc
        .perform(
            put("/api/openings/{openingId}/tenures?clientNumber=" + CLIENT_NUMBER, openingId)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUpdatePayload))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(
            jsonPath("$.tenureValidation.validationResults[1].errorCode")
                .value("CLIENT_NOT_LICENSEE"));
  }
}
