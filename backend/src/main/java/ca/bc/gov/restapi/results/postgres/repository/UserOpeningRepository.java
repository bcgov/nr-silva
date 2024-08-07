package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntityId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** This interface holds methods for handling {@link UserOpeningEntity} data in the database. */
public interface UserOpeningRepository
    extends JpaRepository<UserOpeningEntity, UserOpeningEntityId> {

  List<UserOpeningEntity> findAllByUserId(String userId);

  @Query("from UserOpeningEntity o where o.openingId in ?1 and o.userId = ?2")
  List<UserOpeningEntity> findAllByOpeningIdInAndUserId(List<String> openingIdList, String userId);
}
