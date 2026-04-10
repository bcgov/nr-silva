package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.dto.StandardUnitSearchFilterDto;
import ca.bc.gov.restapi.results.common.projection.StandardUnitSearchProjection;
import java.util.List;

public interface StandardUnitRepository {
  List<StandardUnitSearchProjection> standardsUnitSearch(
      StandardUnitSearchFilterDto filters, long offset, long size);
}
