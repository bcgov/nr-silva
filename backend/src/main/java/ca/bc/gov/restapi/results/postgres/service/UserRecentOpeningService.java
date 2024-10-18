package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.postgres.dto.UserRecentOpeningDto;
import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.UserRecentOpeningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRecentOpeningService {

    private final LoggedUserService loggedUserService;
    private final UserRecentOpeningRepository userRecentOpeningRepository;

    /**
     * Stores the opening viewed by the user and returns the DTO.
     *
     * @param openingId The ID of the opening viewed by the user.
     * @return A DTO with userId, openingId, and lastViewed timestamp.
     */
    public UserRecentOpeningDto storeViewedOpening(String openingId) {
        String userId = loggedUserService.getLoggedUserId();
        LocalDateTime lastViewed = LocalDateTime.now();
        
        // Verify that the openingId String contains numbers only and no spaces
        if (!openingId.matches("^[0-9]*$")) {
            throw new IllegalArgumentException("Opening ID must contain numbers only!");
        }

        // Check if the user has already viewed this opening
        UserRecentOpeningEntity existingEntity = userRecentOpeningRepository.findByUserIdAndOpeningId(userId, openingId);
        
        if (existingEntity != null) {
            // Update the last viewed timestamp for the existing record
            existingEntity.setLastViewed(lastViewed);
            userRecentOpeningRepository.save(existingEntity);  // Save the updated entity
        } else {
            // Create a new entity if this openingId is being viewed for the first time
            UserRecentOpeningEntity newEntity = new UserRecentOpeningEntity(null, userId, openingId, lastViewed);
            userRecentOpeningRepository.save(newEntity);  // Save the new entity
        }
    
        // Return the DTO
        return new UserRecentOpeningDto(userId, openingId, lastViewed);
    }
}
