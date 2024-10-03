package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** This class contains methods to handle Org Units. */
@Slf4j
@Service
@AllArgsConstructor
public class OrgUnitService {

  /**
   * Find all Org Units. Option to include expired ones.
   *
   * @return List of {@link OrgUnitEntity} with found categories.
   */
  public List<OrgUnitEntity> findAllOrgUnits() {
    log.info("Getting all org units from BC GW");

    log.info("Found {} org units ", 0);
    return List.of();
  }
}
