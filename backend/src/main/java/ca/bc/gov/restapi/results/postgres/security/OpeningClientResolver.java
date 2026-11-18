package ca.bc.gov.restapi.results.postgres.security;

import ca.bc.gov.restapi.results.postgres.repository.ForestFileClientPostgresRepository;
import java.util.Set;
import java.util.TreeSet;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Finds the clients that have access to an opening: the licensee (type A) of the forest file of
 * the opening's primary tenure, which is the client created the opening under.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class OpeningClientResolver {

  private final ForestFileClientPostgresRepository forestFileClientRepository;

  /**
   * Returns the client numbers with access to the opening.
   *
   * @param openingId the opening
   * @return the client numbers; empty when the opening has none
   */
  public Set<String> clientNumbersOf(Long openingId) {
    return new TreeSet<>(
        forestFileClientRepository.findLicenseeClientNumbersByOpeningId(openingId));
  }
}
