package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;

import java.util.List;

public interface OpenCategoryCodeService {
  List<CodeDescriptionDto> findAllCategories(boolean includeExpired);
}
