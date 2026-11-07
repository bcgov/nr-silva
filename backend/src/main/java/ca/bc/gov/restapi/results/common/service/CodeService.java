package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import java.util.List;

public interface CodeService {
  List<CodeDescriptionDto> getAllSilvBaseCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllSilvBaseCode() {
    return getAllSilvBaseCode(false);
  }

  List<CodeDescriptionDto> getAllSilvTechniqueCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllSilvTechniqueCode() {
    return getAllSilvTechniqueCode(false);
  }

  List<CodeDescriptionDto> getAllSilvMethodCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllSilvMethodCode() {
    return getAllSilvMethodCode(false);
  }

  List<CodeDescriptionDto> getAllSilvObjectiveCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllSilvObjectiveCode() {
    return getAllSilvObjectiveCode(false);
  }

  List<CodeDescriptionDto> getAllSilvFundSrceCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllSilvFundSrceCode() {
    return getAllSilvFundSrceCode(false);
  }

  List<CodeDescriptionDto> findAllCategories(boolean includeExpired);

  List<CodeDescriptionDto> findAllOrgUnits(boolean districtsOnly);

  List<CodeDescriptionDto> getAllOpenStatusCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllOpenStatusCode() {
    return getAllOpenStatusCode(false);
  }

  List<CodeDescriptionDto> getAllDisturbanceCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllDisturbanceCode() {
    return getAllDisturbanceCode(false);
  }

  List<CodeDescriptionDto> getAllSilvSystemCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllSilvSystemCode() {
    return getAllSilvSystemCode(false);
  }

  List<CodeDescriptionDto> getAllSilvSystemVariantCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllSilvSystemVariantCode() {
    return getAllSilvSystemVariantCode(false);
  }

  List<CodeDescriptionDto> getAllSilvCutPhaseCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllSilvCutPhaseCode() {
    return getAllSilvCutPhaseCode(false);
  }

  List<CodeDescriptionDto> getAllSilvDamageAgentCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllSilvDamageAgentCode() {
    return getAllSilvDamageAgentCode(false);
  }

  List<CodeDescriptionDto> getAllStockingStatusCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllStockingStatusCode() {
    return getAllStockingStatusCode(false);
  }

  List<CodeDescriptionDto> getAllStockingTypeCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllStockingTypeCode() {
    return getAllStockingTypeCode(false);
  }

  List<CodeDescriptionDto> getAllSilvTreeSpeciesCode(boolean effectiveOnly);

  default List<CodeDescriptionDto> getAllSilvTreeSpeciesCode() {
    return getAllSilvTreeSpeciesCode(false);
  }
}

