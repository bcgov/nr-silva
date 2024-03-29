package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** This interface provides access to the database for the OpeningsLastYearEntity entity. */
public interface OpeningsLastYearRepository extends JpaRepository<OpeningsLastYearEntity, String> {}
