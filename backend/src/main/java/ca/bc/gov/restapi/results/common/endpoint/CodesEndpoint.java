package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.service.OpenCategoryCodeService;
import ca.bc.gov.restapi.results.common.service.OrgUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The Codes endpoint. Returns all the codes used by the frontend.
 */
@RestController
@RequestMapping("/api/codes")
@RequiredArgsConstructor
public class CodesEndpoint {

  private final OpenCategoryCodeService openCategoryCodeService;
  private final OrgUnitService orgUnitService;


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
    return openCategoryCodeService.findAllCategories(addExpired);
  }

  /**
   * Get the Org units list for the openings search API.
   *
   * @return List of OrgUnitEntity with found org units.
   */
  @GetMapping("/org-units")
  public List<CodeDescriptionDto> getOpeningOrgUnits() {
    return orgUnitService.findAllOrgUnits();
  }
}
