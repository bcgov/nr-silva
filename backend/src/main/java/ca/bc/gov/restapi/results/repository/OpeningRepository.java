package ca.bc.gov.restapi.results.repository;

import ca.bc.gov.restapi.results.entity.OpeningEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** This interface allows the service to fetch and save data into the database. */
public interface OpeningRepository extends JpaRepository<OpeningEntity, String> {}
