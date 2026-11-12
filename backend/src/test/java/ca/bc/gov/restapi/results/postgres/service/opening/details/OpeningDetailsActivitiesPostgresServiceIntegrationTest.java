package ca.bc.gov.restapi.results.postgres.service.opening.details;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsActivitiesActivitiesDto;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsActivitiesService;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt
@Transactional
@DisplayName("Integration Test | Opening activities list | Postgres objectives and funding")
class OpeningDetailsActivitiesPostgresServiceIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  private static final Long OPENING_ID = 1796497L;
  // Completed activity with legacy funding XXX and no legacy objectives
  private static final Long COMPLETED = 4184320L;
  // Planned activity with legacy objective CE and no legacy funding
  private static final Long PLANNED_LEGACY_OBJECTIVE = 4184319L;

  @Autowired private OpeningDetailsActivitiesService service;
  @Autowired private JdbcTemplate jdbcTemplate;

  private void addObjective(Long atuId, String resultsInd, String code, int order) {
    jdbcTemplate.update(
        "INSERT INTO silva.activity_tu_objective (activity_treatment_unit_id, results_ind, "
            + "silv_objective_code, objective_order, entry_userid, entry_timestamp) "
            + "VALUES (?, ?, ?, ?, 'TEST', now())",
        atuId,
        resultsInd,
        code,
        order);
  }

  private void addFunding(Long atuId, String resultsInd, String code, int order) {
    jdbcTemplate.update(
        "INSERT INTO silva.activity_tu_fund_srce (activity_treatment_unit_id, results_ind, "
            + "silv_fund_srce_code, fund_srce_order, entry_userid, entry_timestamp) "
            + "VALUES (?, ?, ?, ?, 'TEST', now())",
        atuId,
        resultsInd,
        code,
        order);
  }

  private void clearLegacyFunding(Long atuId) {
    jdbcTemplate.update(
        "UPDATE silva.activity_treatment_unit SET silv_fund_srce_code = NULL "
            + "WHERE activity_treatment_unit_id = ?",
        atuId);
  }

  private OpeningDetailsActivitiesActivitiesDto activity(Long atuId) {
    return service
        .getOpeningActivitiesActivities(OPENING_ID, null, PageRequest.of(0, 50))
        .getContent()
        .stream()
        .filter(dto -> atuId.equals(dto.atuId()))
        .findFirst()
        .orElseThrow();
  }

  private List<String> codes(List<CodeDescriptionDto> values) {
    return values.stream().map(CodeDescriptionDto::code).toList();
  }

  @Test
  @DisplayName("Reads five objectives and several funding sources from the child tables")
  void activities_withoutLegacyValues_readChildTables() {
    List<String> objectives = List.of("2BT", "A", "AC", "AHM", "ALP");
    clearLegacyFunding(COMPLETED);
    for (int i = 0; i < objectives.size(); i++) {
      addObjective(COMPLETED, "Y", objectives.get(i), i + 1);
    }
    addObjective(COMPLETED, "N", "AMD", 1);
    addFunding(COMPLETED, "Y", "BCP", 1);
    addFunding(COMPLETED, "Y", "BCT", 2);

    OpeningDetailsActivitiesActivitiesDto dto = activity(COMPLETED);

    assertThat(codes(dto.objectives())).containsExactlyElementsOf(objectives);
    assertThat(dto.objectives().get(0).description()).isEqualTo("2 Billion Trees");
    assertThat(dto.objective1().code()).isEqualTo("2BT");
    assertThat(dto.objective2().code()).isEqualTo("A");
    assertThat(dto.objective3().code()).isEqualTo("AC");
    assertThat(codes(dto.fundingSources())).containsExactly("BCP", "BCT");
    assertThat(dto.funding().code()).isEqualTo("BCP");
  }

  @Test
  @DisplayName("Reads the planned set of a planned activity")
  void activities_planned_readPlannedChildSet() {
    addFunding(PLANNED_LEGACY_OBJECTIVE, "N", "BCP", 1);
    addFunding(PLANNED_LEGACY_OBJECTIVE, "Y", "BCT", 1);

    OpeningDetailsActivitiesActivitiesDto dto = activity(PLANNED_LEGACY_OBJECTIVE);

    assertThat(codes(dto.fundingSources())).containsExactly("BCP");
  }

  @Test
  @DisplayName("Legacy columns win over child tables when they have a value")
  void activities_withLegacyValues_ignoreChildTables() {
    addObjective(PLANNED_LEGACY_OBJECTIVE, "N", "AMD", 1);
    addFunding(COMPLETED, "Y", "BCP", 1);

    OpeningDetailsActivitiesActivitiesDto planned = activity(PLANNED_LEGACY_OBJECTIVE);
    OpeningDetailsActivitiesActivitiesDto completed = activity(COMPLETED);

    assertThat(codes(planned.objectives())).containsExactly("CE");
    assertThat(codes(completed.fundingSources())).containsExactly("XXX");
  }

  @Test
  @DisplayName("Sorts by the funding source and objective shown, including child table values")
  void activities_sortByChildTableValues_followsDisplayedValues() {
    clearLegacyFunding(COMPLETED);
    addFunding(COMPLETED, "Y", "BCT", 1);
    addFunding(PLANNED_LEGACY_OBJECTIVE, "N", "BCP", 1);
    addObjective(COMPLETED, "Y", "A", 1);

    // funding: planned BCP < completed BCT; objective1: completed A < planned legacy CE
    assertThat(positions("funding", Sort.Direction.ASC))
        .containsExactly(PLANNED_LEGACY_OBJECTIVE, COMPLETED);
    assertThat(positions("funding", Sort.Direction.DESC))
        .containsExactly(COMPLETED, PLANNED_LEGACY_OBJECTIVE);
    assertThat(positions("objective1", Sort.Direction.ASC))
        .containsExactly(COMPLETED, PLANNED_LEGACY_OBJECTIVE);
    assertThat(positions("objective1", Sort.Direction.DESC))
        .containsExactly(PLANNED_LEGACY_OBJECTIVE, COMPLETED);
  }

  @Test
  @DisplayName("Sorts planned activities by their planned technique, method and area")
  void activities_sortPlannedValues_usesDisplayedColumns() {
    jdbcTemplate.update(
        "UPDATE silva.activity_treatment_unit SET plan_silv_technique_code = 'CG', "
            + "plan_silv_method_code = 'BAGS', planned_treatment_amount = 0.1, "
            + "treatment_amount = NULL WHERE activity_treatment_unit_id = ?",
        PLANNED_LEGACY_OBJECTIVE);
    jdbcTemplate.update(
        "UPDATE silva.activity_treatment_unit SET silv_technique_code = 'BU', "
            + "silv_method_code = 'MACH', treatment_amount = 999 "
            + "WHERE activity_treatment_unit_id = ?",
        COMPLETED);

    // tech: completed BU < planned CG; method: planned BAGS < completed MACH; area: 0.1 < 999
    assertThat(positions("tech", Sort.Direction.ASC))
        .containsExactly(COMPLETED, PLANNED_LEGACY_OBJECTIVE);
    assertThat(positions("method", Sort.Direction.ASC))
        .containsExactly(PLANNED_LEGACY_OBJECTIVE, COMPLETED);
    assertThat(positions("area", Sort.Direction.ASC))
        .containsExactly(PLANNED_LEGACY_OBJECTIVE, COMPLETED);
  }

  // Ids of the two activities under test, in the order the sorted list returns them
  private List<Long> positions(String property, Sort.Direction direction) {
    return service
        .getOpeningActivitiesActivities(
            OPENING_ID, null, PageRequest.of(0, 50, Sort.by(direction, property)))
        .getContent()
        .stream()
        .map(OpeningDetailsActivitiesActivitiesDto::atuId)
        .filter(id -> COMPLETED.equals(id) || PLANNED_LEGACY_OBJECTIVE.equals(id))
        .toList();
  }

  @Test
  @DisplayName("Activities without any values return empty lists")
  void activities_withoutAnyValues_returnEmptyLists() {
    clearLegacyFunding(COMPLETED);
    OpeningDetailsActivitiesActivitiesDto dto = activity(COMPLETED);

    assertThat(dto.objectives()).isEmpty();
    assertThat(dto.fundingSources()).isEmpty();
    assertThat(dto.objective1().code()).isNull();
    assertThat(dto.funding().code()).isNull();
  }

  @Test
  @DisplayName("Main search term matches child table objectives and funding in list and count")
  void activities_searchTerm_matchesChildTableValues() {
    clearLegacyFunding(COMPLETED);
    addObjective(COMPLETED, "Y", "2BT", 1);
    addFunding(COMPLETED, "Y", "BCP", 1);
    addObjective(COMPLETED, "N", "AMD", 1);
    // Control: the planned activity has AMD in its active (planned) set, so the search is not empty
    addObjective(PLANNED_LEGACY_OBJECTIVE, "N", "AMD", 1);

    var byObjective =
        service.getOpeningActivitiesActivities(OPENING_ID, "BILLION", PageRequest.of(0, 50));
    var byFunding =
        service.getOpeningActivitiesActivities(OPENING_ID, "BC PARKS", PageRequest.of(0, 50));
    var byOtherSet =
        service.getOpeningActivitiesActivities(OPENING_ID, "AMENDMENT", PageRequest.of(0, 50));

    assertThat(byObjective.getContent()).extracting("atuId").contains(COMPLETED);
    assertThat(byObjective.getTotalElements()).isEqualTo(byObjective.getContent().size());
    assertThat(byFunding.getContent()).extracting("atuId").contains(COMPLETED);
    assertThat(byFunding.getTotalElements()).isEqualTo(byFunding.getContent().size());
    assertThat(byOtherSet.getContent())
        .extracting("atuId")
        .isNotEmpty()
        .contains(PLANNED_LEGACY_OBJECTIVE)
        .doesNotContain(COMPLETED);
  }

  @Test
  @DisplayName("Numeric search term matches the planned area of a planned activity in list and count")
  void activities_searchTerm_matchesPlannedArea() {
    jdbcTemplate.update(
        "UPDATE silva.activity_treatment_unit SET treatment_amount = NULL, "
            + "planned_treatment_amount = 12345.6 WHERE activity_treatment_unit_id = ?",
        PLANNED_LEGACY_OBJECTIVE);

    var byArea =
        service.getOpeningActivitiesActivities(OPENING_ID, "12345.6", PageRequest.of(0, 50));

    assertThat(byArea.getContent()).extracting("atuId").containsExactly(PLANNED_LEGACY_OBJECTIVE);
    assertThat(byArea.getTotalElements()).isEqualTo(1);
  }
}
