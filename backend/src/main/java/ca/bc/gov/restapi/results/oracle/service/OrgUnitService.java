package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.repository.OrgUnitRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** This class contains methods to handle Org Units. */
@Slf4j
@Service
@AllArgsConstructor
public class OrgUnitService {

  private final OrgUnitRepository orgUnitRepository;

  /**
   * Find all Org Units. Option to include expired ones.
   *
   * @param includeExpired True to include expired, false otherwise.
   * @return List of {@link OrgUnitEntity} with found categories.
   */
  public List<OrgUnitEntity> findAllOrgUnits(boolean includeExpired) {
    log.info("Getting all org units. Include expired: {}", includeExpired);

    if (includeExpired) {
      List<OrgUnitEntity> orgUnits = orgUnitRepository.findAll();
      log.info("Found {} org units (including expired)", orgUnits.size());
      return orgUnits;
    }

    List<OrgUnitEntity> orgUnits = orgUnitRepository.findAllByExpiryDateAfter(LocalDate.now());
    log.info("Found {} org units (excluding expired)", orgUnits.size());
    return orgUnits;
  }

  /**
   * Find all Org Units by code.
   *
   * @param orgUnitCodes Org Unit codes to search for.
   * @return List of {@link OrgUnitEntity} with found categories.
   */
  public List<OrgUnitEntity> findAllOrgUnitsByCode(List<String> orgUnitCodes) {
    log.info("Getting all org units by codes: {}", orgUnitCodes);

    List<OrgUnitEntity> orgUnits = orgUnitRepository.findAllByOrgUnitCodeIn(orgUnitCodes);
    log.info("Found {} org units by codes", orgUnits.size());
    return orgUnits;
  }
}
