package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpeningsLastYearRepository extends JpaRepository<OpeningsLastYearEntity, String> {}
