package ca.bc.gov.restapi.results.oracle.service;


import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningTombstoneDto;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
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
  private final CutBlockOpenAdminService cutBlockOpenAdminService;
  private final LoggedUserService loggedUserService;
  private final ForestClientApiProvider forestClientApiProvider;
  private final UserOpeningService userOpeningService;


  public OpeningTombstoneDto getOpeningTombstone(String openingId) {
    return null;
  }
}
