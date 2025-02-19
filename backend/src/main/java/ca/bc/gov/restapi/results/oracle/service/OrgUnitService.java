package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.repository.OrgUnitRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * This class contains methods to handle Org Units.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgUnitService {

  private final OrgUnitRepository orgUnitRepository;
  private final SilvaConfiguration silvaConfiguration;

  /**
   * Find all Org Units for the Openings Search.
   *
   * @return List of {@link OrgUnitEntity} with found categories.
   */
  public List<CodeDescriptionDto> findAllOrgUnits() {
    log.info("Getting all org units for the search openings");

    if (Objects.isNull(silvaConfiguration.getOrgUnits())
        || silvaConfiguration.getOrgUnits().isEmpty()
    ) {
      log.warn("No Org Units from the properties file.");
      return List.of();
    }

    List<CodeDescriptionDto> orgUnits = orgUnitRepository
        .findAllByOrgUnitCodeIn(silvaConfiguration.getOrgUnits())
        .stream()
        .map(orgUnit -> new CodeDescriptionDto(orgUnit.getOrgUnitCode(), orgUnit.getOrgUnitName()))
        .toList();

    log.info("Found {} org units by codes", orgUnits.size());
    return orgUnits;
  }
}
