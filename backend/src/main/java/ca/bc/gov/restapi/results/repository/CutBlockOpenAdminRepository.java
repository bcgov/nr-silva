package ca.bc.gov.restapi.results.repository;

import ca.bc.gov.restapi.results.entity.CutBlockOpenAdminEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CutBlockOpenAdminRepository extends JpaRepository<CutBlockOpenAdminEntity, Long> {

  List<CutBlockOpenAdminEntity> findAllByOpeningId(Long openingId);
}
