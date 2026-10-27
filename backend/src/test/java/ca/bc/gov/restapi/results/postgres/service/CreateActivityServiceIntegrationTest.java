package ca.bc.gov.restapi.results.postgres.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsActivitiesService;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.dto.CreateActivityRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateActivityResponseDto;
import ca.bc.gov.restapi.results.postgres.enums.ActivityStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(cognitoGroups = {"Submitter_00010003"})
@Transactional
@DisplayName("Integration Test | Create activity service | Postgres")
class CreateActivityServiceIntegrationTest extends AbstractTestContainerIntegrationTest {

  // Opening with a prime licence gross area of 79.4 ha, org unit 1 and licensee client 00010003
  private static final Long OPENING_ID = 1004185L;
  // Opening without any licensee or obligation holder client
  private static final Long OPENING_WITHOUT_CLIENTS = 1480130L;
  private static final String SQUARE_GEOJSON =
      """
      {"type":"FeatureCollection","crs":{"properties":{"name":"EPSG:3005"}},
       "features":[{"type":"Feature","properties":{},"geometry":{"type":"Polygon",
       "coordinates":[[[850000,1500000],[850100,1500000],[850100,1500100],
                        [850000,1500100],[850000,1500000]]]}}]}
      """;

  @Autowired private CreateActivityService service;
  @Autowired private OpeningDetailsActivitiesService activitiesService;
  @Autowired private SilvicultureCommentRepository commentRepository;
  @Autowired private JdbcTemplate jdbcTemplate;
  @PersistenceContext private EntityManager entityManager;

  @TestBean private SpatialFileService spatialFileService;

  static SpatialFileService spatialFileService() {
    return new SpatialFileService();
  }

  private CreateActivityRequestDto planned(String baseCode, String comment) {
    return new CreateActivityRequestDto(
        ActivityStatus.PLANNED,
        baseCode,
        "CG",
        "BAGS",
        List.of("A", "AC"),
        List.of("BCP", "BCT"),
        "PRJ-1",
        "LIC-1",
        comment,
        LocalDate.now().plusDays(30),
        new BigDecimal("12.5"),
        new BigDecimal("1500"),
        null,
        null,
        null);
  }

  private CreateActivityRequestDto completed() {
    return new CreateActivityRequestDto(
        ActivityStatus.COMPLETED,
        "HC",
        "CG",
        "BAGS",
        List.of("A", "AC", "AHM", "ALP", "AMD"),
        List.of("BCP", "BCT", "CBI"),
        null,
        null,
        null,
        LocalDate.now().plusDays(10),
        new BigDecimal("30.0"),
        null,
        LocalDate.now().minusDays(5),
        new BigDecimal("28.5"),
        new BigDecimal("1200"));
  }

  private Map<String, Object> activityRow(Long activityId) {
    entityManager.flush();
    return jdbcTemplate.queryForMap(
        "SELECT * FROM silva.activity_treatment_unit WHERE activity_treatment_unit_id = ?",
        activityId);
  }

  private List<String> codes(String table, String column, String order, Long id, String ind) {
    entityManager.flush();
    return jdbcTemplate.queryForList(
        "SELECT " + column + " FROM silva." + table
            + " WHERE activity_treatment_unit_id = ? AND results_ind = ? ORDER BY " + order,
        String.class,
        id,
        ind);
  }

  @Test
  @DisplayName("Creates a planned activity with its planned set of values")
  void createActivity_planned_savesPlannedSet() {
    Integer revisionBefore =
        jdbcTemplate.queryForObject(
            "SELECT revision_count FROM silva.opening WHERE opening_id = ?",
            Integer.class,
            OPENING_ID);
    Integer maxSeq =
        jdbcTemplate.queryForObject(
            "SELECT MAX(activity_tu_seq_no) FROM silva.activity_treatment_unit "
                + "WHERE opening_id = ?",
            Integer.class,
            OPENING_ID);

    CreateActivityResponseDto response =
        service.createActivity(OPENING_ID, planned("HC", "  first comment "));

    assertThat(response.openingId()).isEqualTo(OPENING_ID);
    assertThat(response.status()).isEqualTo(ActivityStatus.PLANNED);
    assertThat(response.baseCode()).isEqualTo("HC");

    Map<String, Object> row = activityRow(response.activityId());
    assertThat(row)
        .containsEntry("opening_id", OPENING_ID)
        .containsEntry("silv_base_code", "HC")
        .containsEntry("results_ind", "N")
        .containsEntry("silv_activity_measurement_code", "HA")
        .containsEntry("activity_licensee_id", "LIC-1")
        .containsEntry("fia_project_id", "PRJ-1")
        .containsEntry("plan_silv_technique_code", "CG")
        .containsEntry("plan_silv_method_code", "BAGS")
        .containsEntry("planned_treatment_cost", 1500)
        .containsEntry("activity_tu_seq_no", maxSeq + 1)
        .containsEntry("org_unit_no", 1L)
        .containsEntry("revision_count", 1);
    assertThat(row.get("planned_treatment_amount")).isEqualTo(new BigDecimal("12.5"));
    assertThat(row.get("atu_completion_date")).isNull();
    assertThat(row.get("silv_technique_code")).isNull();
    assertThat(row.get("silv_objective_code_1")).isNull();
    assertThat((String) row.get("entry_userid")).startsWith("IDIR\\");

    Long id = response.activityId();
    assertThat(codes("activity_tu_objective", "silv_objective_code", "objective_order", id, "N"))
        .containsExactly("A", "AC");
    assertThat(codes("activity_tu_fund_srce", "silv_fund_srce_code", "fund_srce_order", id, "N"))
        .containsExactly("BCP", "BCT");
    assertThat(codes("activity_tu_objective", "silv_objective_code", "objective_order", id, "Y"))
        .isEmpty();

    Integer revisionAfter =
        jdbcTemplate.queryForObject(
            "SELECT revision_count FROM silva.opening WHERE opening_id = ?",
            Integer.class,
            OPENING_ID);
    assertThat(revisionAfter).isEqualTo(revisionBefore + 1);
  }

  @Test
  @DisplayName("Creates a completed activity with the actual set of values")
  void createActivity_completed_savesActualSet() {
    CreateActivityResponseDto response = service.createActivity(OPENING_ID, completed());

    Map<String, Object> row = activityRow(response.activityId());
    assertThat(row)
        .containsEntry("results_ind", "Y")
        .containsEntry("silv_technique_code", "CG")
        .containsEntry("silv_method_code", "BAGS")
        .containsEntry("actual_treatment_cost", 1200);
    assertThat(row.get("treatment_amount")).isEqualTo(new BigDecimal("28.5"));
    assertThat(row.get("planned_treatment_amount")).isEqualTo(new BigDecimal("30.0"));
    assertThat(row.get("atu_completion_date")).isNotNull();
    assertThat(row.get("plan_silv_technique_code")).isNull();

    Long id = response.activityId();
    assertThat(codes("activity_tu_objective", "silv_objective_code", "objective_order", id, "Y"))
        .containsExactly("A", "AC", "AHM", "ALP", "AMD");
    assertThat(codes("activity_tu_fund_srce", "silv_fund_srce_code", "fund_srce_order", id, "Y"))
        .containsExactly("BCP", "BCT", "CBI");
  }

  @Test
  @DisplayName("The new activity shows its objectives and funding in the opening activities list")
  void createActivity_completed_isReadFromChildTables() {
    Long id = service.createActivity(OPENING_ID, completed()).activityId();
    entityManager.flush();

    var activity =
        activitiesService
            .getOpeningActivitiesActivities(OPENING_ID, null, PageRequest.of(0, 200))
            .getContent()
            .stream()
            .filter(dto -> id.equals(dto.atuId()))
            .findFirst()
            .orElseThrow();

    assertThat(activity.status().code()).isEqualTo("CPT");
    assertThat(activity.objectives())
        .extracting("code")
        .containsExactly("A", "AC", "AHM", "ALP", "AMD");
    assertThat(activity.fundingSources()).extracting("code").containsExactly("BCP", "BCT", "CBI");
  }

  @Test
  @DisplayName("Saves the comment as an activity comment")
  void createActivity_withComment_savesLinkedComment() {
    Long id =
        service.createActivity(OPENING_ID, planned("HC", "  first comment ")).activityId();
    entityManager.flush();

    var comments = commentRepository.getCommentById(null, id, null, null, null);

    assertThat(comments).hasSize(1);
    assertThat(comments.get(0).getCommentText()).isEqualTo("first comment");
    assertThat(comments.get(0).getCommentSourceCode()).isEqualTo("RSLT");
    assertThat(comments.get(0).getCommentTypeCode()).isEqualTo("GENERAL");
  }

  @Test
  @DisplayName("Does not save a comment when it is blank")
  void createActivity_blankComment_savesNoComment() {
    Long id = service.createActivity(OPENING_ID, planned("HC", "   ")).activityId();
    entityManager.flush();

    assertThat(commentRepository.getCommentById(null, id, null, null, null)).isEmpty();
  }

  @Test
  @DisplayName("Numbers activities under the opening in sequence")
  void createActivity_twice_incrementsSequenceNumber() {
    Long first = service.createActivity(OPENING_ID, planned("HC", null)).activityId();
    Long second = service.createActivity(OPENING_ID, planned("FE", null)).activityId();

    Integer firstSeq = (Integer) activityRow(first).get("activity_tu_seq_no");
    Integer secondSeq = (Integer) activityRow(second).get("activity_tu_seq_no");

    assertThat(secondSeq).isEqualTo(firstSeq + 1);
    assertThat(second).isGreaterThan(first);
  }

  @Test
  @DisplayName("Saves nothing when the request is invalid")
  void createActivity_invalid_savesNothing() {
    Integer before =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM silva.activity_treatment_unit", Integer.class);

    assertThatThrownBy(() -> service.createActivity(OPENING_ID, planned("PL", "comment")))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("baseCode: must be one of");

    entityManager.flush();
    Integer after =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM silva.activity_treatment_unit", Integer.class);
    assertThat(after).isEqualTo(before);
  }

  @Test
  @DisplayName("Saves the spatial file as the activity geometry in EPSG:3005")
  void createActivity_withSpatialFile_savesGeometry() {
    Long id =
        service
            .createActivity(
                OPENING_ID,
                planned("HC", null),
                "activity.geojson",
                SQUARE_GEOJSON.getBytes(UTF_8))
            .activityId();
    entityManager.flush();

    Map<String, Object> row =
        jdbcTemplate.queryForMap(
            "SELECT ST_SRID(geometry) AS srid, feature_area, feature_perimeter, "
                + "feature_class_skey, entry_userid, revision_count "
                + "FROM silva.activity_treatment_geometry WHERE activity_treatment_unit_id = ?",
            id);
    assertThat(row.get("srid")).isEqualTo(3005);
    assertThat(row.get("feature_class_skey")).isEqualTo(2411L);
    assertThat(row.get("revision_count")).isEqualTo(1);
    assertThat((String) row.get("entry_userid")).startsWith("IDIR\\");
    // A 100 m x 100 m square, within the error of reprojecting to EPSG:4326 and back
    assertThat((BigDecimal) row.get("feature_area"))
        .isCloseTo(new BigDecimal("1.0"), within(new BigDecimal("0.05")));
    assertThat((BigDecimal) row.get("feature_perimeter"))
        .isCloseTo(new BigDecimal("400"), within(new BigDecimal("5")));
  }

  @Test
  @DisplayName("Saves a GeoJSON file in EPSG:4326")
  void createActivity_withLongitudeLatitudeFile_savesGeometry() throws IOException {
    byte[] file = new ClassPathResource("spatial/activity-1527650.geojson").getContentAsByteArray();

    Long id =
        service.createActivity(OPENING_ID, planned("HC", null), "activity.geojson", file)
            .activityId();
    entityManager.flush();

    BigDecimal area =
        jdbcTemplate.queryForObject(
            "SELECT feature_area FROM silva.activity_treatment_geometry "
                + "WHERE activity_treatment_unit_id = ?",
            BigDecimal.class,
            id);
    assertThat(area).isPositive();
  }

  @Test
  @DisplayName("Saves no geometry when there is no file")
  void createActivity_withoutFile_savesNoGeometry() {
    Long id = service.createActivity(OPENING_ID, planned("HC", null)).activityId();
    entityManager.flush();

    Integer count =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM silva.activity_treatment_geometry "
                + "WHERE activity_treatment_unit_id = ?",
            Integer.class,
            id);
    assertThat(count).isZero();
  }

  @Test
  @DisplayName("Saves nothing when the spatial file is invalid")
  void createActivity_invalidFile_savesNothing() {
    Integer before =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM silva.activity_treatment_unit", Integer.class);

    assertThatThrownBy(
            () ->
                service.createActivity(
                    OPENING_ID,
                    planned("HC", "comment"),
                    "bad.geojson",
                    "not a spatial file".getBytes(UTF_8)))
        .isInstanceOf(ResponseStatusException.class);

    entityManager.flush();
    Integer after =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM silva.activity_treatment_unit", Integer.class);
    assertThat(after).isEqualTo(before);
  }

  private int activityCount() {
    entityManager.flush();
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM silva.activity_treatment_unit", Integer.class);
  }

  @Test
  @WithMockJwt(cognitoGroups = {"Submitter_00099999"})
  @DisplayName("Rejects a user with a role only for another client and saves nothing")
  void createActivity_roleForAnotherClient_forbidden() {
    int before = activityCount();

    assertThatThrownBy(() -> service.createActivity(OPENING_ID, planned("HC", null)))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            exception -> assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN));

    assertThat(activityCount()).isEqualTo(before);
  }

  @Test
  @WithMockJwt(cognitoGroups = {"Viewer"})
  @DisplayName("Rejects a user with only the viewer role")
  void createActivity_viewerOnly_forbidden() {
    assertThatThrownBy(() -> service.createActivity(OPENING_ID, planned("HC", null)))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            exception -> assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN));
  }

  @Test
  @DisplayName("Rejects everyone for an opening without clients")
  void createActivity_openingWithoutClients_forbidden() {
    assertThatThrownBy(() -> service.createActivity(OPENING_WITHOUT_CLIENTS, planned("HC", null)))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            exception -> assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN));
  }

  @Test
  @WithMockJwt(cognitoGroups = {"Planner_00010004"})
  @DisplayName("Allows a holder of the obligation on the cut block")
  void createActivity_obligationHolder_allowed() {
    jdbcTemplate.update(
        "INSERT INTO silva.cut_block_client (cblk_client_skey, cb_skey, "
            + "cut_block_client_type_code, client_locn_code, client_number, revision_count, "
            + "entry_userid, entry_timestamp, update_userid, update_timestamp) "
            + "VALUES (90000001, 189945, 'O', '00', '00010004', 1, 'TEST', now(), 'TEST', now())");

    assertThat(service.createActivity(OPENING_ID, planned("HC", null)).activityId()).isNotNull();
  }

  @Test
  @WithMockJwt(cognitoGroups = {})
  @DisplayName("Reports an unknown opening as not found before checking access")
  void createActivity_unknownOpening_notFoundBeforeForbidden() {
    assertThatThrownBy(() -> service.createActivity(-1L, planned("HC", null)))
        .isInstanceOf(NotFoundGenericException.class);
  }

  @Test
  @WithMockJwt(cognitoGroups = {"Submitter_00099999"})
  @DisplayName("Checks access before validating the request")
  void createActivity_forbiddenAndInvalid_forbidden() {
    assertThatThrownBy(() -> service.createActivity(OPENING_ID, planned("PL", "comment")))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            exception -> assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN));
  }
}
