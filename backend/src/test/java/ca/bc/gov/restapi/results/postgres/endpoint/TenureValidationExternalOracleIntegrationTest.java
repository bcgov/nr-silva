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
import ca.bc.gov.restapi.results.extensions.CustomOracleContainer;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.testconfig.RealTenureEndToEndTestConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import oracle.jdbc.pool.OracleDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dual-database integration tests running {@code server.primary-db=postgres} with live external
 * Oracle FTA tables ({@code THE.CUT_BLOCK}, {@code THE.CUT_BLOCK_CLIENT}) in a {@link
 * CustomOracleContainer}.
 */
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Tenure Validation End-to-End (Postgres + External Oracle)")
@AutoConfigureMockMvc
@Import(RealTenureEndToEndTestConfig.class)
@WithMockJwt(cognitoGroups = {"Submitter_00010006"})
@Transactional
class TenureValidationExternalOracleIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  private static final String CLIENT_NUMBER = "00010006";
  private static final long ORACLE_ONLY_CB_SKEY_1 = 999888L;
  private static final long ORACLE_ONLY_CB_SKEY_2 = 999889L;
  private static final long ORACLE_ONLY_CBLK_CLIENT_SKEY_1 = 9998880L;
  private static final long ORACLE_ONLY_CBLK_CLIENT_SKEY_2 = 9998890L;

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

  static final CustomOracleContainer externalOracle = new CustomOracleContainer();
  static final NamedParameterJdbcTemplate oracleRwJdbcTemplate;

  static {
    externalOracle.start();
    Flyway.configure()
        .dataSource(
            externalOracle.getJdbcUrl(),
            externalOracle.getUsername(),
            externalOracle.getPassword())
        .locations("classpath:migration/oracle")
        .schemas("THE")
        .baselineOnMigrate(true)
        .load()
        .migrate();
    try {
      OracleDataSource ds = new OracleDataSource();
      ds.setURL(externalOracle.getJdbcUrl());
      ds.setUser(externalOracle.getUsername());
      ds.setPassword(externalOracle.getPassword());
      oracleRwJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to initialize external Oracle test JDBC client", e);
    }
  }

  @DynamicPropertySource
  static void registerExternalOracleProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.oracle.jdbcUrl", externalOracle::getJdbcUrl);
    registry.add("spring.oracle.username", externalOracle::getUsername);
    registry.add("spring.oracle.password", externalOracle::getPassword);
    registry.add("silva.integrations.external-oracle.hikari.jdbc-url", externalOracle::getJdbcUrl);
    registry.add("silva.integrations.external-oracle.hikari.username", externalOracle::getUsername);
    registry.add("silva.integrations.external-oracle.hikari.password", externalOracle::getPassword);
    registry.add("silva.integrations.external-oracle.enabled", () -> "true");
    registry.add("EXTERNAL_ORACLE_ENABLED", () -> "true");
    registry.add("silva.integrations.tenure.provider", () -> "oracle");
  }

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
  @Autowired private ObjectMapper objectMapper;
  @MockitoBean private VirusScanService virusScanService;

  @BeforeEach
  void seedOracleOnlyCutBlocks() {
    cleanupOracleOnlyCutBlocks();
    insertOracleCutBlock(ORACLE_ONLY_CB_SKEY_1, "ORA999", "01", "BLK88");
    insertOracleCutBlockClient(ORACLE_ONLY_CBLK_CLIENT_SKEY_1, ORACLE_ONLY_CB_SKEY_1);
    // Space-padded CUTTING_PERMIT_ID matching legacy Oracle rows for null input
    insertOracleCutBlock(ORACLE_ONLY_CB_SKEY_2, "ORA999", " ", "BLK89");
    insertOracleCutBlockClient(ORACLE_ONLY_CBLK_CLIENT_SKEY_2, ORACLE_ONLY_CB_SKEY_2);
  }

  @AfterEach
  void tearDownOracleOnlyCutBlocks() {
    cleanupOracleOnlyCutBlocks();
  }

  @Test
  @DisplayName("POST /api/tenures/validate queries live external Oracle FTA tables")
  void validateTenures_externalOracleProvider_validatesAgainstLiveOracleAndPostgres()
      throws Exception {
    // 1. Seeded Oracle block (A72929/001/028) passes external FTA verification & licensee check
    //    AND already-allocated A72929/001/024 is caught by local Postgres CBOA duplicate check.
    mockMvc
        .perform(
            post("/api/tenures/validate?clientNumber=" + CLIENT_NUMBER)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [
                      {
                        "fileId": "ORA999",
                        "cuttingPermit": "01",
                        "cutBlock": "BLK88",
                        "isPrimary": true
                      },
                      {
                        "fileId": "ORA999",
                        "cuttingPermit": null,
                        "cutBlock": "BLK89",
                        "isPrimary": false
                      }
                    ]
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(true))
        .andExpect(jsonPath("$.tenures[0].timberMark").value("AY5001"))
        .andExpect(jsonPath("$.tenures[1].timberMark").value("AY5001"));

    // 2. Duplicate allocation in Postgres + missing block + unauthorized client in Oracle
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
                      },
                      {
                        "fileId": "TFL47",
                        "cuttingPermit": "12K",
                        "cutBlock": "12-11",
                        "isPrimary": false
                      },
                      {
                        "fileId": "NOFILE",
                        "cuttingPermit": "01",
                        "cutBlock": "NOBLK",
                        "isPrimary": false
                      }
                    ]
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(false))
        .andExpect(jsonPath("$.validationResults[0].errorCode").value("TENURE_DUPLICATE_OPENING"))
        .andExpect(jsonPath("$.validationResults[1].errorCode").value("CLIENT_NOT_LICENSEE"))
        .andExpect(jsonPath("$.validationResults[2].errorCode").value("TENURE_NOT_FOUND"));
  }

  @Test
  @DisplayName(
      "Create and update opening with external Oracle-only cut blocks persists cb_skey in"
          + " Postgres")
  void createAndUpdateOpening_externalOracleOnlyCutBlocks_persistsWithoutPostgresCutBlock()
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
          "clientNumber": "00010006",
          "orgUnitCode": "DAS",
          "openingCategoryCode": "FTML",
          "licenseeOpeningId": "E2E-ORA-1",
          "tenures": [
            {"fileId": "ORA999", "cuttingPermit": "01", "cutBlock": "BLK88", "isPrimary": true}
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

    JsonNode createdJson = objectMapper.readTree(createResult.getResponse().getContentAsString());
    long openingId = createdJson.get("openingId").asLong();

    // Oracle-only block cb_skey=999888 is NOT in Postgres silva.cut_block
    Integer postgresCutBlockCount =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM silva.cut_block WHERE cb_skey = ?",
            Integer.class,
            ORACLE_ONLY_CB_SKEY_1);
    assertThat(postgresCutBlockCount).isZero();

    Map<String, Object> cboaRow =
        jdbcTemplate.queryForMap(
            "SELECT cut_block_open_admin_id, cb_skey, timber_mark, revision_count "
                + "FROM silva.cut_block_open_admin WHERE opening_id = ? AND cut_block_id = 'BLK88'",
            openingId);
    assertThat(((Number) cboaRow.get("cb_skey")).longValue()).isEqualTo(ORACLE_ONLY_CB_SKEY_1);
    assertThat(cboaRow.get("timber_mark")).isEqualTo("AY5001");

    long existingCboaId = ((Number) cboaRow.get("cut_block_open_admin_id")).longValue();
    int existingRevision = ((Number) cboaRow.get("revision_count")).intValue();

    // PUT /api/openings/{openingId}/tenures adding space-padded Oracle block ORA999/null/BLK89
    String updatePayload =
        """
        [
          {
            "cboaId": %d,
            "revisionCount": %d,
            "fileId": "ORA999",
            "cuttingPermit": "01",
            "cutBlock": "BLK88",
            "isPrimary": true
          },
          {
            "cboaId": null,
            "revisionCount": null,
            "fileId": "ORA999",
            "cuttingPermit": null,
            "cutBlock": "BLK89",
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

    Map<String, Object> secondRow =
        jdbcTemplate.queryForMap(
            "SELECT cb_skey, timber_mark FROM silva.cut_block_open_admin "
                + "WHERE opening_id = ? AND cut_block_id = 'BLK89'",
            openingId);
    assertThat(((Number) secondRow.get("cb_skey")).longValue()).isEqualTo(ORACLE_ONLY_CB_SKEY_2);
    assertThat(secondRow.get("timber_mark")).isEqualTo("AY5001");
  }

  private static void insertOracleCutBlock(
      long cbSkey, String fileId, String cuttingPermitId, String cutBlockId) {
    String sql =
        """
        INSERT INTO THE.CUT_BLOCK (
          CB_SKEY, HVA_SKEY, FOREST_FILE_ID, CUTTING_PERMIT_ID, TIMBER_MARK, CUT_BLOCK_ID,
          SP_EXEMPT_IND, BLOCK_STATUS_ST, REVISION_COUNT, ENTRY_USERID, ENTRY_TIMESTAMP,
          UPDATE_USERID, UPDATE_TIMESTAMP, IS_WASTE_ASSESSMENT_REQUIRED
        ) VALUES (
          :cbSkey, 114530, :fileId, :cuttingPermitId, 'AY5001', :cutBlockId,
          'N', 'S', 1, 'TEST_USER', SYSDATE, 'TEST_USER', SYSDATE, 'U'
        )
        """;
    oracleRwJdbcTemplate.update(
        sql,
        Map.of(
            "cbSkey", cbSkey,
            "fileId", fileId,
            "cuttingPermitId", cuttingPermitId,
            "cutBlockId", cutBlockId));
  }

  private static void insertOracleCutBlockClient(long clientSkey, long cbSkey) {
    String sql =
        """
        INSERT INTO THE.CUT_BLOCK_CLIENT (
          CBLK_CLIENT_SKEY, CB_SKEY, CUT_BLOCK_CLIENT_TYPE_CODE, LICENSEE_START_DATE,
          CLIENT_LOCN_CODE, CLIENT_NUMBER, REVISION_COUNT, ENTRY_USERID, ENTRY_TIMESTAMP,
          UPDATE_USERID, UPDATE_TIMESTAMP
        ) VALUES (
          :clientSkey, :cbSkey, 'O', SYSDATE, '00', :clientNumber,
          1, 'TEST_USER', SYSDATE, 'TEST_USER', SYSDATE
        )
        """;
    oracleRwJdbcTemplate.update(
        sql,
        Map.of(
            "clientSkey", clientSkey,
            "cbSkey", cbSkey,
            "clientNumber", CLIENT_NUMBER));
  }

  private static void cleanupOracleOnlyCutBlocks() {
    oracleRwJdbcTemplate.update(
        "DELETE FROM THE.CUT_BLOCK_CLIENT WHERE CB_SKEY IN (:cb1, :cb2)",
        Map.of("cb1", ORACLE_ONLY_CB_SKEY_1, "cb2", ORACLE_ONLY_CB_SKEY_2));
    oracleRwJdbcTemplate.update(
        "DELETE FROM THE.CUT_BLOCK WHERE CB_SKEY IN (:cb1, :cb2)",
        Map.of("cb1", ORACLE_ONLY_CB_SKEY_1, "cb2", ORACLE_ONLY_CB_SKEY_2));
  }
}
