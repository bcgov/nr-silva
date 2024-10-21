package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecentOpeningRepository extends JpaRepository<UserRecentOpeningEntity, Long> {
    UserRecentOpeningEntity findByUserIdAndOpeningId(String userId, String openingId);
    // Add a method to fetch recent openings for a user with a limit and sorting by last_viewed in descending order
    List<UserRecentOpeningEntity> findByUserIdOrderByLastViewedDesc(String userId, Pageable pageable);
}
