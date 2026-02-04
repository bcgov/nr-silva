package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.entity.BaseCodeEntity;
import ca.bc.gov.restapi.results.common.projection.OpenCategoryCodeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;

@NoRepositoryBean
public interface OpenCategoryCodeRepository<T extends BaseCodeEntity> extends JpaRepository<T, String> {
  List<OpenCategoryCodeProjection> findAllBy();
  List<OpenCategoryCodeProjection> findAllByExpiryDateAfter(LocalDate now);

  @NonNull
  List<T> findAll();
}
