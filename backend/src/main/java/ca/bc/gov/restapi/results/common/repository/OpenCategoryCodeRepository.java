package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.entity.GenericCodeEntity;
import ca.bc.gov.restapi.results.common.projection.OpenCategoryCodeProjection;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

@NoRepositoryBean
public interface OpenCategoryCodeRepository<T extends GenericCodeEntity>
    extends JpaRepository<T, String> {
  List<OpenCategoryCodeProjection> findAllBy();

  List<OpenCategoryCodeProjection> findAllByExpiryDateAfter(LocalDate now);

  @NonNull
  List<T> findAll();
}
