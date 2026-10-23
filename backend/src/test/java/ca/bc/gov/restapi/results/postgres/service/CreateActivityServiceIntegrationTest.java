package ca.bc.gov.restapi.results.postgres.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt
@Transactional
@DisplayName("Integration Test | Create activity service | Postgres")
class CreateActivityServiceIntegrationTest extends AbstractTestContainerIntegrationTest {

  // Opening with a prime licence gross area of 79.4 ha and org unit 1
  private static final Long OPENING_ID = 1004185L;
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
        OPENING_ID,
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
        OPENING_ID,
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

    CreateActivityResponseDto response = service.createActivity(planned("HC", "  first comment "));

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
    CreateActivityResponseDto response = service.createActivity(completed());

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
    Long id = service.createActivity(completed()).activityId();
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
    Long id = service.createActivity(planned("HC", "  first comment ")).activityId();
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
    Long id = service.createActivity(planned("HC", "   ")).activityId();
    entityManager.flush();

    assertThat(commentRepository.getCommentById(null, id, null, null, null)).isEmpty();
  }

  @Test
  @DisplayName("Numbers activities under the opening in sequence")
  void createActivity_twice_incrementsSequenceNumber() {
    Long first = service.createActivity(planned("HC", null)).activityId();
    Long second = service.createActivity(planned("FE", null)).activityId();

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

    assertThatThrownBy(() -> service.createActivity(planned("PL", "comment")))
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
                planned("HC", null), "activity.geojson", SQUARE_GEOJSON.getBytes(UTF_8))
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

    Long id = service.createActivity(planned("HC", null), "activity.geojson", file).activityId();
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
    Long id = service.createActivity(planned("HC", null)).activityId();
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
                    planned("HC", "comment"), "bad.geojson", "not a spatial file".getBytes(UTF_8)))
        .isInstanceOf(ResponseStatusException.class);

    entityManager.flush();
    Integer after =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM silva.activity_treatment_unit", Integer.class);
    assertThat(after).isEqualTo(before);
  }
}
