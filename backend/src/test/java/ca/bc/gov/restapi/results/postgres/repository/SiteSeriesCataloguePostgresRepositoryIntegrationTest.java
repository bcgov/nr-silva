package ca.bc.gov.restapi.results.postgres.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Site series catalogue PostgreSQL repository")
class SiteSeriesCataloguePostgresRepositoryIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @Autowired private SiteSeriesCataloguePostgresRepository repository;

  @Test
  @DisplayName("Finds an exact BEC combination including a variant")
  void findMatchingBecCombo_exactVariant_returnsMatch() {
    assertThat(repository.findMatchingBecCombo("CWH", "wh", "1", null, "01")).hasSize(1);
  }

  @Test
  @DisplayName("Matches nullable variant and phase values")
  void findMatchingBecCombo_nullVariantAndPhase_returnsMatch() {
    assertThat(repository.findMatchingBecCombo("CWH", "xm", null, null, "01")).hasSize(1);
  }

  @Test
  @DisplayName("Rejects an unknown site series combination")
  void findMatchingBecCombo_unknownSiteSeries_returnsEmpty() {
    assertThat(repository.findMatchingBecCombo("CWH", "wh", "1", null, "99")).isEmpty();
  }
}
