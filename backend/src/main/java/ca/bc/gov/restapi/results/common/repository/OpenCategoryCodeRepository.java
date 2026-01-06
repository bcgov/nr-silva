package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.projection.OpenCategoryCodeProjection;
import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;

import java.time.LocalDate;
import java.util.List;

public interface OpenCategoryCodeRepository {
  List<OpenCategoryCodeProjection> findAllBy();
  List<OpenCategoryCodeProjection> findAllByExpiryDateAfter(LocalDate now);
}
