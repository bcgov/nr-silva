package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.entity.BaseCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;

@NoRepositoryBean
public interface OpeningStatusCodeRepository<T extends BaseCodeEntity> extends JpaRepository<T, String> {
  @NonNull
  List<T> findAll();
}
