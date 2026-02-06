package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.entity.GenericCodeEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

@NoRepositoryBean
public interface GenericCodeRepository<T extends GenericCodeEntity>
    extends JpaRepository<T, String> {

  @NonNull
  @Override
  List<T> findAll();

  List<T> findAllByExpiryDateAfter(LocalDate now);
}
