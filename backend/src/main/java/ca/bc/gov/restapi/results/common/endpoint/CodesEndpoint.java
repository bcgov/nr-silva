package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.enums.OrgUnitTypeParam;
import ca.bc.gov.restapi.results.common.service.CodeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The Codes endpoint. Returns all the codes used by the frontend. */
@RestController
@RequestMapping("/api/codes")
@RequiredArgsConstructor
public class CodesEndpoint {

  private final CodeService codeService;

  /**
   * Get all opening categories. Optionally you can ask for the expired ones.
   *
   * @param includeExpired Query param to include expired categories.
   * @return List of {@link CodeDescriptionDto} with found categories.
   */
  @GetMapping("/opening-categories")
  public List<CodeDescriptionDto> getOpeningCategories(
      @RequestParam(value = "includeExpired", required = false, defaultValue = "true")
          Boolean includeExpired) {
    boolean addExpired = Boolean.TRUE.equals(includeExpired);
    return codeService.findAllCategories(addExpired);
  }

  /**
   * Get the org units list for the openings search API.
   *
   * <p>Without a {@code type} parameter (or {@code type=all}), returns all active non-expired org
   * units: districts ({@code D*}), regions, and headquarters ({@code H*}) units, ordered D → R → H.
   * Pass {@code type=district} to return only district-level org units ({@code D*}, length 3).
   *
   * @param type Optional filter — {@code district} for districts only; omit or {@code all} for the
   *     full list.
   * @return List of {@link CodeDescriptionDto} with matching org units.
   */
  @GetMapping("/org-units")
  public List<CodeDescriptionDto> getOpeningOrgUnits(
      @RequestParam(value = "type", required = false, defaultValue = "all") OrgUnitTypeParam type) {
    return codeService.findAllOrgUnits(OrgUnitTypeParam.DISTRICT == type);
  }

  /**
   * Get all silv base codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found silv base codes.
   */
  @GetMapping("/silv-base")
  public List<CodeDescriptionDto> getSilvBaseCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllSilvBaseCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all silv technique codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found silv technique codes.
   */
  @GetMapping("/silv-technique")
  public List<CodeDescriptionDto> getSilvTechniqueCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllSilvTechniqueCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all silv method codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found silv method codes.
   */
  @GetMapping("/silv-method")
  public List<CodeDescriptionDto> getSilvMethodCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllSilvMethodCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all silv objective codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found silv objective codes.
   */
  @GetMapping("/silv-objective")
  public List<CodeDescriptionDto> getSilvObjectiveCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllSilvObjectiveCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all silv fund source codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found silv fund source codes.
   */
  @GetMapping("/silv-fund-source")
  public List<CodeDescriptionDto> getSilvFundSourceCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllSilvFundSrceCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all opening status codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found opening status codes.
   */
  @GetMapping("/opening-status")
  public List<CodeDescriptionDto> getOpeningStatusCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllOpenStatusCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all silv system codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found silv system codes.
   */
  @GetMapping("/silv-system")
  public List<CodeDescriptionDto> getSilvSystemCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllSilvSystemCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all silv system variant codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found silv system variant codes.
   */
  @GetMapping("/silv-system-variant")
  public List<CodeDescriptionDto> getSilvSystemVariantCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllSilvSystemVariantCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all silv cut phase codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found silv cut phase codes.
   */
  @GetMapping("/silv-cut-phase")
  public List<CodeDescriptionDto> getSilvCutPhaseCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllSilvCutPhaseCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all disturbance codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found disturbance codes.
   */
  @GetMapping("/disturbance")
  public List<CodeDescriptionDto> getDisturbanceCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllDisturbanceCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all silv damage agent codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found silv damage agent codes.
   */
  @GetMapping("/silv-damage-agent")
  public List<CodeDescriptionDto> getSilvDamageAgentCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllSilvDamageAgentCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all stocking status codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found stocking status codes.
   */
  @GetMapping("/stocking-status")
  public List<CodeDescriptionDto> getStockingStatusCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllStockingStatusCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all stocking type codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found stocking type codes.
   */
  @GetMapping("/stocking-type")
  public List<CodeDescriptionDto> getStockingTypeCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllStockingTypeCode(Boolean.TRUE.equals(effectiveOnly));
  }

  /**
   * Get all silv tree species codes.
   *
   * @param effectiveOnly Query param to filter only effective codes.
   * @return List of {@link CodeDescriptionDto} with found silv tree species codes.
   */
  @GetMapping("/silv-tree-species")
  public List<CodeDescriptionDto> getSilvTreeSpeciesCodes(
      @RequestParam(value = "effectiveOnly", required = false, defaultValue = "false")
          Boolean effectiveOnly) {
    return codeService.getAllSilvTreeSpeciesCode(Boolean.TRUE.equals(effectiveOnly));
  }
}
