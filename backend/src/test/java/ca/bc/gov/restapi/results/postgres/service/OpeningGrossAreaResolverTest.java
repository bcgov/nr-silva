package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.postgres.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockOpenAdminPostgresRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | OpeningGrossAreaResolver")
class OpeningGrossAreaResolverTest {

  private static final Long OPENING_ID = 1001L;

  @Mock private CutBlockOpenAdminPostgresRepository cutBlockOpenAdminRepository;
  @InjectMocks private OpeningGrossAreaResolver resolver;

  private CutBlockOpenAdminEntity tenure(String primeLicence, String grossArea) {
    return CutBlockOpenAdminEntity.builder()
        .openingPrimeLicenceInd(primeLicence)
        .openingGrossArea(grossArea == null ? null : new BigDecimal(grossArea))
        .build();
  }

  @Test
  @DisplayName("Uses the gross area stored on the opening")
  void resolve_openingValue_winsOverTenures() {
    OpeningEntity opening =
        OpeningEntity.builder().id(OPENING_ID).openingGrossArea(new BigDecimal("12.5")).build();

    assertThat(resolver.resolve(opening)).isEqualByComparingTo("12.5");
    verifyNoInteractions(cutBlockOpenAdminRepository);
  }

  @Test
  @DisplayName("Falls back to the prime licence gross area")
  void resolve_noOpeningValue_usesPrimeLicence() {
    OpeningEntity opening = OpeningEntity.builder().id(OPENING_ID).build();
    when(cutBlockOpenAdminRepository.findAllByOpeningId(OPENING_ID))
        .thenReturn(List.of(tenure("N", "999.0"), tenure("Y", "100.0")));

    assertThat(resolver.resolve(opening)).isEqualByComparingTo("100.0");
  }

  @Test
  @DisplayName("Skips a prime licence without a gross area")
  void resolve_primeLicenceWithoutArea_returnsNull() {
    OpeningEntity opening = OpeningEntity.builder().id(OPENING_ID).build();
    when(cutBlockOpenAdminRepository.findAllByOpeningId(OPENING_ID))
        .thenReturn(List.of(tenure("N", "999.0"), tenure("Y", null)));

    assertThat(resolver.resolve(opening)).isNull();
  }

  @Test
  @DisplayName("Returns null when no gross area is known")
  void resolve_nothingKnown_returnsNull() {
    OpeningEntity opening = OpeningEntity.builder().id(OPENING_ID).build();
    when(cutBlockOpenAdminRepository.findAllByOpeningId(OPENING_ID)).thenReturn(List.of());

    assertThat(resolver.resolve(opening)).isNull();
  }
}
