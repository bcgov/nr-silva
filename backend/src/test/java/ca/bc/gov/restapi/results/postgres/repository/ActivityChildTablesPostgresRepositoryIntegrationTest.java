package ca.bc.gov.restapi.results.postgres.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentGeometryEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTuFundSrceEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTuObjectiveEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Activity child table PostgreSQL repositories")
@Transactional
class ActivityChildTablesPostgresRepositoryIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  private static final String PLANNED = "N";
  private static final String ACTUAL = "Y";

  @Autowired private ActivityTuObjectivePostgresRepository objectiveRepository;
  @Autowired private ActivityTuFundSrcePostgresRepository fundSrceRepository;
  @Autowired private ActivityTreatmentGeometryPostgresRepository geometryRepository;
  @Autowired private ActivityTreatmentUnitPostgresRepository activityRepository;
  @Autowired private JdbcTemplate jdbcTemplate;

  private Long existingActivityId() {
    return activityRepository
        .findByOpeningIdAndSilvBaseCode(1589595L, "DN")
        .get(0)
        .getId();
  }

  @Test
  @DisplayName("Persists more than three objectives per planned and actual set")
  void objectives_persistFiveForPlannedAndActualSets() {
    Long atuId = existingActivityId();
    List<String> codes =
        jdbcTemplate.queryForList(
            "SELECT silv_objective_code FROM silva.silv_objective_code ORDER BY 1 LIMIT 5",
            String.class);
    assertThat(codes).hasSize(5);

    for (String resultsInd : List.of(PLANNED, ACTUAL)) {
      for (short i = 0; i < codes.size(); i++) {
        objectiveRepository.save(
            ActivityTuObjectiveEntity.builder()
                .activityTreatmentUnitId(atuId)
                .resultsInd(resultsInd)
                .silvObjectiveCode(codes.get(i))
                .objectiveOrder((short) (i + 1))
                .entryUserid("TEST")
                .entryTimestamp(LocalDateTime.now())
                .build());
      }
    }
    objectiveRepository.flush();

    List<String> stored =
        jdbcTemplate.queryForList(
            "SELECT silv_objective_code FROM silva.activity_tu_objective "
                + "WHERE activity_treatment_unit_id = ? AND results_ind = ? "
                + "ORDER BY objective_order",
            String.class,
            atuId,
            PLANNED);
    assertThat(stored).containsExactlyElementsOf(codes);
  }

  @Test
  @DisplayName("Persists several funding sources for one activity")
  void fundingSources_persistMultiple() {
    Long atuId = existingActivityId();
    List<String> codes =
        jdbcTemplate.queryForList(
            "SELECT silv_fund_srce_code FROM silva.silv_fund_srce_code ORDER BY 1 LIMIT 3",
            String.class);
    assertThat(codes).hasSize(3);

    for (short i = 0; i < codes.size(); i++) {
      fundSrceRepository.save(
          ActivityTuFundSrceEntity.builder()
              .activityTreatmentUnitId(atuId)
              .resultsInd(PLANNED)
              .silvFundSrceCode(codes.get(i))
              .fundSrceOrder((short) (i + 1))
              .entryUserid("TEST")
              .entryTimestamp(LocalDateTime.now())
              .build());
    }
    fundSrceRepository.flush();

    Integer count =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM silva.activity_tu_fund_srce WHERE activity_treatment_unit_id = ?",
            Integer.class,
            atuId);
    assertThat(count).isEqualTo(3);
  }

  @Test
  @DisplayName("Persists an activity geometry in EPSG:3005")
  void geometry_persistsInBcAlbers() {
    Long atuId = existingActivityId();
    GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 3005);
    var polygon =
        factory.createPolygon(
            new Coordinate[] {
              new Coordinate(1000000, 600000),
              new Coordinate(1000100, 600000),
              new Coordinate(1000100, 600100),
              new Coordinate(1000000, 600100),
              new Coordinate(1000000, 600000)
            });
    LocalDateTime now = LocalDateTime.now();

    geometryRepository.saveAndFlush(
        ActivityTreatmentGeometryEntity.builder()
            .activityTreatmentUnitId(atuId)
            .isNew(true)
            .geometry(polygon)
            .featureArea(new BigDecimal("1.0000"))
            .featurePerimeter(new BigDecimal("400.0000"))
            .featureClassSkey(2411L)
            .entryUserId("TEST")
            .entryTimestamp(now)
            .updateUserId("TEST")
            .updateTimestamp(now)
            .revisionCount(1)
            .build());

    Integer srid =
        jdbcTemplate.queryForObject(
            "SELECT ST_SRID(geometry) FROM silva.activity_treatment_geometry "
                + "WHERE activity_treatment_unit_id = ?",
            Integer.class,
            atuId);
    assertThat(srid).isEqualTo(3005);
  }

  @Test
  @DisplayName("Activity and comment id sequences exist and increase")
  void sequences_existAndIncrease() {
    for (String sequence :
        List.of("silva.activity_treatment_unit_id_seq", "silva.silviculture_comment_id_seq")) {
      Long first =
          jdbcTemplate.queryForObject("SELECT nextval('" + sequence + "')", Long.class);
      Long second =
          jdbcTemplate.queryForObject("SELECT nextval('" + sequence + "')", Long.class);
      assertThat(second).isGreaterThan(first);
    }
  }
}
