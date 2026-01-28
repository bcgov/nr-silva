package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.common.service.OrgUnitService;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractOrgUnitService implements OrgUnitService {

  protected final OrgUnitRepository orgUnitRepository;
  protected final SilvaConfiguration silvaConfiguration;

  /**
   * Find all Org Units for the Openings Search.
   *
   * @return List of {@link OrgUnitEntity} with found categories.
   */
  @Override
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
