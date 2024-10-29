package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntityId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface holds methods for handling {@link UserOpeningEntity} data in the database.
 */
public interface UserOpeningRepository
    extends JpaRepository<UserOpeningEntity, UserOpeningEntityId> {

  List<UserOpeningEntity> findAllByUserId(String userId);

}
