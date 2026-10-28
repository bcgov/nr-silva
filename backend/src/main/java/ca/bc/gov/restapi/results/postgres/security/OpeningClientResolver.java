package ca.bc.gov.restapi.results.postgres.security;

import ca.bc.gov.restapi.results.postgres.repository.CutBlockClientPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.ForestFileClientPostgresRepository;
import java.util.Set;
import java.util.TreeSet;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Finds the clients that have access to an opening, following the RESULTS rule for who can update
 * an opening: the licensee of the forest file (type A) and the holders of the silviculture
 * obligation (O) or responsibility (R) on the cut blocks of its tenures.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class OpeningClientResolver {

  private final ForestFileClientPostgresRepository forestFileClientRepository;
  private final CutBlockClientPostgresRepository cutBlockClientRepository;

  /**
   * Returns the client numbers with access to the opening.
   *
   * @param openingId the opening
   * @return the client numbers; empty when the opening has none
   */
  public Set<String> clientNumbersOf(Long openingId) {
    Set<String> clients = new TreeSet<>();
    clients.addAll(forestFileClientRepository.findLicenseeClientNumbersByOpeningId(openingId));
    clients.addAll(cutBlockClientRepository.findObligationClientNumbersByOpeningId(openingId));
    return clients;
  }
}
