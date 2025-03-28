package ca.bc.gov.restapi.results.oracle.service;


import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneProjection;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * This class holds methods for fetching and handling {@link OpeningEntity} in general.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningService {

  private final OpeningRepository openingRepository;

  public Optional<OpeningTombstoneProjection> getOpeningTombstone(Long openingId) {
    return
        openingRepository
            .getOpeningTombstoneByOpeningId(openingId)
            .stream()
            .findFirst();
  }
}
