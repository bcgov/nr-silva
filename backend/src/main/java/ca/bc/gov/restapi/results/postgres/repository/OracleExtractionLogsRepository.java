package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.OracleExtractionLogsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** This interface provides access to the OracleExtractionLogsEntity table on Postgres database. */
public interface OracleExtractionLogsRepository
    extends JpaRepository<OracleExtractionLogsEntity, Long> {}
