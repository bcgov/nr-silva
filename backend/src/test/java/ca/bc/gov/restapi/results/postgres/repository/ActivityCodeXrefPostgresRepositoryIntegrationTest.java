package ca.bc.gov.restapi.results.postgres.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Activity code xref repository")
class ActivityCodeXrefPostgresRepositoryIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @Autowired private ActivityCodeXrefPostgresRepository repository;
  @Autowired private JdbcTemplate jdbcTemplate;

  private long count(String table) {
    return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM silva." + table, Long.class);
  }

  @Test
  @DisplayName("Migration seeds the same rows as the RESULTS tables")
  void migration_seedsAllRows() {
    assertThat(count("silv_activity_xref")).isEqualTo(612);
    assertThat(count("silv_base_objective_xref")).isEqualTo(3192);
    assertThat(count("silv_base_funding_xref")).isEqualTo(1595);
  }

  @Test
  @DisplayName("Matches the base, technique and method combination exactly")
  void existsBaseTechniqueMethod_matchesExactCombination() {
    assertThat(repository.existsBaseTechniqueMethod("FE", "CG", "BAGS")).isTrue();
    assertThat(repository.existsBaseTechniqueMethod("HC", "CG", "BAGS")).isFalse();
    assertThat(repository.existsBaseTechniqueMethod("FE", "CG", "SINJE")).isFalse();
  }

  @Test
  @DisplayName("Treats a missing technique or method as part of the combination")
  void existsBaseTechniqueMethod_nullsMatchOnlyNullRows() {
    assertThat(repository.existsBaseTechniqueMethod("FE", null, null)).isTrue();
    assertThat(repository.existsBaseTechniqueMethod("FE", "CG", null)).isTrue();
    assertThat(repository.existsBaseTechniqueMethod("FE", null, "BAGS")).isFalse();
  }

  @Test
  @DisplayName("Finds whether a technique is used with a base at all")
  void existsBaseTechnique_anyMethod() {
    assertThat(repository.existsBaseTechnique("FE", "CG")).isTrue();
    assertThat(repository.existsBaseTechnique("HC", "CG")).isFalse();
  }

  @Test
  @DisplayName("Returns only the objectives allowed for the base")
  void findObjectivesForBase_filtersUnknownCodes() {
    assertThat(repository.findObjectivesForBase("FE", List.of("A", "AC", "ZZZ")))
        .isEqualTo(Set.of("A", "AC"));
    assertThat(repository.findObjectivesForBase("FE", List.of())).isEmpty();
  }

  @Test
  @DisplayName("Returns only the funding sources allowed for the base")
  void findFundingSourcesForBase_filtersUnknownCodes() {
    assertThat(repository.findFundingSourcesForBase("AU", List.of("BCP", "FIP", "ZZZ")))
        .isEqualTo(Set.of("BCP", "FIP"));
    assertThat(repository.findFundingSourcesForBase("DN", List.of("BCP", "FIP")))
        .isEqualTo(Set.of("BCP"));
  }
}
