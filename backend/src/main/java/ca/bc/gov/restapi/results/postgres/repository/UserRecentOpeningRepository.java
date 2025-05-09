package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntityId;
import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecentOpeningRepository extends
    JpaRepository<UserRecentOpeningEntity, UserOpeningEntityId> {

  Page<UserRecentOpeningEntity> findByUserIdOrderByLastViewedDesc(String userId, Pageable pageable);
}
