package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
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
      @RequestParam(value = "type", required = false, defaultValue = "all") String type) {
    return codeService.findAllOrgUnits("district".equalsIgnoreCase(type));
  }

  /**
   * Get all silv base codes.
   *
   * @return List of {@link CodeDescriptionDto} with found silv base codes.
   */
  @GetMapping("/silv-base")
  public List<CodeDescriptionDto> getSilvBaseCodes() {
    return codeService.getAllSilvBaseCode();
  }

  /**
   * Get all silv technique codes.
   *
   * @return List of {@link CodeDescriptionDto} with found silv technique codes.
   */
  @GetMapping("/silv-technique")
  public List<CodeDescriptionDto> getSilvTechniqueCodes() {
    return codeService.getAllSilvTechniqueCode();
  }

  /**
   * Get all silv method codes.
   *
   * @return List of {@link CodeDescriptionDto} with found silv method codes.
   */
  @GetMapping("/silv-method")
  public List<CodeDescriptionDto> getSilvMethodCodes() {
    return codeService.getAllSilvMethodCode();
  }

  /**
   * Get all silv objective codes.
   *
   * @return List of {@link CodeDescriptionDto} with found silv objective codes.
   */
  @GetMapping("/silv-objective")
  public List<CodeDescriptionDto> getSilvObjectiveCodes() {
    return codeService.getAllSilvObjectiveCode();
  }

  /**
   * Get all silv fund source codes.
   *
   * @return List of {@link CodeDescriptionDto} with found silv fund source codes.
   */
  @GetMapping("/silv-fund-source")
  public List<CodeDescriptionDto> getSilvFundSourceCodes() {
    return codeService.getAllSilvFundSrceCode();
  }

  /**
   * Get all silv system codes.
   *
   * @return List of {@link CodeDescriptionDto} with found silv system codes.
   */
  @GetMapping("/silv-system")
  public List<CodeDescriptionDto> getSilvSystemCodes() {
    return codeService.getAllSilvSystemCode();
  }

  /**
   * Get all silv system variant codes.
   *
   * @return List of {@link CodeDescriptionDto} with found silv system variant codes.
   */
  @GetMapping("/silv-system-variant")
  public List<CodeDescriptionDto> getSilvSystemVariantCodes() {
    return codeService.getAllSilvSystemVariantCode();
  }

  /**
   * Get all silv cut phase codes.
   *
   * @return List of {@link CodeDescriptionDto} with found silv cut phase codes.
   */
  @GetMapping("/silv-cut-phase")
  public List<CodeDescriptionDto> getSilvCutPhaseCodes() {
    return codeService.getAllSilvCutPhaseCode();
  }

  /**
   * Get all disturbance codes.
   *
   * @return List of {@link CodeDescriptionDto} with found disturbance codes.
   */
  @GetMapping("/disturbance")
  public List<CodeDescriptionDto> getDisturbanceCodes() {
    return codeService.getAllDisturbanceCode();
  }

  /**
   * Get all silv damage agent codes.
   *
   * @return List of {@link CodeDescriptionDto} with found silv damage agent codes.
   */
  @GetMapping("/silv-damage-agent")
  public List<CodeDescriptionDto> getSilvDamageAgentCodes() {
    return codeService.getAllSilvDamageAgentCode();
  }

  /**
   * Get all stocking status codes.
   *
   * @return List of {@link CodeDescriptionDto} with found stocking status codes.
   */
  @GetMapping("/stocking-status")
  public List<CodeDescriptionDto> getStockingStatusCodes() {
    return codeService.getAllStockingStatusCode();
  }

  /**
   * Get all stocking type codes.
   *
   * @return List of {@link CodeDescriptionDto} with found stocking type codes.
   */
  @GetMapping("/stocking-type")
  public List<CodeDescriptionDto> getStockingTypeCodes() {
    return codeService.getAllStockingTypeCode();
  }

  /**
   * Get all silv tree species codes.
   *
   * @return List of {@link CodeDescriptionDto} with found silv tree species codes.
   */
  @GetMapping("/silv-tree-species")
  public List<CodeDescriptionDto> getSilvTreeSpeciesCodes() {
    return codeService.getAllSilvTreeSpeciesCode();
  }
}
