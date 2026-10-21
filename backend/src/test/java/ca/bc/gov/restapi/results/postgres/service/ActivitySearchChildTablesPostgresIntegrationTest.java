package ca.bc.gov.restapi.results.postgres.service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchFiltersDto;
import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchResponseDto;
import ca.bc.gov.restapi.results.common.service.ActivityService;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
@Transactional
@DisplayName("Integration Test | Activity search | Postgres objectives and funding child tables")
class ActivitySearchChildTablesPostgresIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @RegisterExtension
  static WireMockExtension clientApiStub =
      WireMockExtension.newInstance()
          .options(
              wireMockConfig()
                  .port(10000)
                  .notifier(new WiremockLogNotifier())
                  .asynchronousResponseEnabled(true)
                  .stubRequestLoggingDisabled(false))
          .configureStaticDsl(true)
          .build();

  // Completed activity; legacy funding XXX and no legacy objectives
  private static final Long COMPLETED = 4184320L;
  // Planned activity with legacy plan objective CE
  private static final Long PLANNED_LEGACY_OBJECTIVE = 4184319L;

  @Autowired private ActivityService activityService;
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

  private List<ActivitySearchResponseDto> search(
      List<String> objectives, List<String> fundingSources) {
    ActivitySearchFiltersDto filters =
        new ActivitySearchFiltersDto(
            null, null, null, null, objectives, fundingSources, null, null, null, null, null, null,
            null, null);
    return activityService.searchActivity(filters, PageRequest.of(0, 500)).getContent();
  }

  private boolean contains(List<ActivitySearchResponseDto> results, Long atuId) {
    return results.stream().anyMatch(result -> atuId.equals(result.activityId()));
  }

  @Test
  @DisplayName("Objective filter finds activities by child table objectives")
  void search_objectiveFilter_findsChildTableObjectives() {
    addObjective(COMPLETED, "Y", "A", 1);
    addObjective(COMPLETED, "Y", "AC", 2);
    addObjective(COMPLETED, "N", "AHM", 1);

    assertThat(contains(search(List.of("AC"), null), COMPLETED)).isTrue();
    assertThat(contains(search(List.of("A"), null), COMPLETED)).isTrue();
    // The planned set does not apply to a completed activity
    assertThat(contains(search(List.of("AHM"), null), COMPLETED)).isFalse();
  }

  @Test
  @DisplayName("Funding filter finds activities by child table funding and shows the first one")
  void search_fundingFilter_findsChildTableFunding() {
    clearLegacyFunding(COMPLETED);
    addFunding(COMPLETED, "Y", "BCP", 1);
    addFunding(COMPLETED, "Y", "BCT", 2);

    List<ActivitySearchResponseDto> byLastFunding = search(null, List.of("BCT"));

    assertThat(contains(byLastFunding, COMPLETED)).isTrue();
    assertThat(
            byLastFunding.stream()
                .filter(result -> COMPLETED.equals(result.activityId()))
                .findFirst()
                .orElseThrow()
                .fundingSource()
                .code())
        .isEqualTo("BCP");
    assertThat(contains(search(null, List.of("CBI")), COMPLETED)).isFalse();
  }

  @Test
  @DisplayName("Legacy columns win over child tables when they have a value")
  void search_legacyValues_ignoreChildTables() {
    addObjective(PLANNED_LEGACY_OBJECTIVE, "N", "AMD", 1);
    addFunding(COMPLETED, "Y", "BCP", 1);

    assertThat(contains(search(List.of("AMD"), null), PLANNED_LEGACY_OBJECTIVE)).isFalse();
    assertThat(contains(search(null, List.of("BCP")), COMPLETED)).isFalse();
    assertThat(contains(search(null, List.of("XXX")), COMPLETED)).isTrue();
  }
}
