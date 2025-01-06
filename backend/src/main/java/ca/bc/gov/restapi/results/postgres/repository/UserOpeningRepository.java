package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntityId;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface holds methods for handling {@link UserOpeningEntity} data in the database.
 */
@Repository
public interface UserOpeningRepository
    extends JpaRepository<UserOpeningEntity, UserOpeningEntityId> {

  List<UserOpeningEntity> findAllByUserId(String userId, Pageable page);

  List<UserOpeningEntity> findAllByUserIdAndOpeningIdIn(String userId,List<Long> openingIds);

}
