package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockOpenAdminPostgresRepository;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** Finds the gross area of an opening in hectares. */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class OpeningGrossAreaResolver {

  private static final String PRIME_LICENCE = "Y";

  private final CutBlockOpenAdminPostgresRepository cutBlockOpenAdminRepository;

  /**
   * Returns the gross area stored on the opening and, for openings that do not have one, the one
   * of the prime licence tenure.
   *
   * @param opening the opening
   * @return the gross area, or null when it is not known
   */
  public BigDecimal resolve(OpeningEntity opening) {
    if (opening.getOpeningGrossArea() != null) {
      return opening.getOpeningGrossArea();
    }
    return cutBlockOpenAdminRepository.findAllByOpeningId(opening.getId()).stream()
        .filter(tenure -> PRIME_LICENCE.equals(tenure.getOpeningPrimeLicenceInd()))
        .map(CutBlockOpenAdminEntity::getOpeningGrossArea)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }
}
