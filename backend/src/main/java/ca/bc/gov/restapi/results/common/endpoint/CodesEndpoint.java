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
   * Get the Org units list for the openings search API.
   *
   * @return List of OrgUnitEntity with found org units.
   */
  @GetMapping("/org-units")
  public List<CodeDescriptionDto> getOpeningOrgUnits() {
    return codeService.findAllOrgUnits();
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
}
