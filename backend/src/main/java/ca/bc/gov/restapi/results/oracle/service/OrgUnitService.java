package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.repository.OrgUnitRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** This class contains methods to handle Org Units. */
@Slf4j
@Service
@AllArgsConstructor
public class OrgUnitService {

  private final OrgUnitRepository orgUnitRepository;

  @Value("${ca.bc.gov.nrs.results.opening-search.org-units}")
  private String[] orgUnitsFromProps;

  /**
   * Find all Org Units for the Openings Search.
   *
   * @return List of {@link OrgUnitEntity} with found categories.
   */
  public List<OrgUnitEntity> findAllOrgUnits() {
    log.info("Getting all org units for the search openings");

    if (Objects.isNull(orgUnitsFromProps) || orgUnitsFromProps.length == 0) {
      log.warn("No Org Units from the properties file.");
      return List.of();
    }

    List<OrgUnitEntity> orgUnits = orgUnitRepository.findAllByOrgUnitCodeIn(orgUnitsFromProps);

    log.info("Found {} org units by codes", orgUnits.size());
    return orgUnits;
  }

  /**
   * Find all Org Units by code.
   *
   * @param orgUnitCodes Org Unit codes to search for.
   * @return List of {@link OrgUnitEntity} with found categories.
   */
  public List<OrgUnitEntity> findAllOrgUnitsByCode(String[] orgUnitCodes) {
    log.info("Getting all org units by codes: {}", Arrays.toString(orgUnitCodes));

    List<OrgUnitEntity> orgUnits = orgUnitRepository.findAllByOrgUnitCodeIn(orgUnitCodes);
    log.info("Found {} org units by codes", orgUnits.size());
    return orgUnits;
  }
}
