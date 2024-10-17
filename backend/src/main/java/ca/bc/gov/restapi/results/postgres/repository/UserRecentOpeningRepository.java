package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecentOpeningRepository extends JpaRepository<UserRecentOpeningEntity, Long> {
    UserRecentOpeningEntity findByUserIdAndOpeningId(String userId, String openingId);
}
