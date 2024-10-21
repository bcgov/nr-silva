package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.service.OpeningRecentViewService;
import ca.bc.gov.restapi.results.postgres.dto.UserRecentOpeningDto;
import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.UserRecentOpeningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRecentOpeningService {

    private final LoggedUserService loggedUserService;
    private final UserRecentOpeningRepository userRecentOpeningRepository;
    private final OpeningRecentViewService openingRecentViewService;

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

    /**
     * Retrieves the recent openings viewed by the logged-in user, limited by the provided limit.
     *
     * @param limit The maximum number of recent openings to retrieve.
     * @return A list of opening IDs the user has viewed, sorted by last viewed in descending order.
     */
    public PaginatedResult<OpeningSearchResponseDto> getAllRecentOpeningsForUser(int limit) {
        String userId = loggedUserService.getLoggedUserId();
        Pageable pageable = PageRequest.of(0, limit); // PageRequest object to apply limit
    
        // Fetch recent openings for the user
        List<UserRecentOpeningEntity> recentOpenings = userRecentOpeningRepository
                .findByUserIdOrderByLastViewedDesc(userId, pageable);
    
        // Extract opening IDs as String
        List<String> openingIds = recentOpenings.stream()
                .map(opening -> String.valueOf(opening.getOpeningId())) // Convert Integer to String
                .collect(Collectors.toList());

        System.out.println("Opening IDs: " + openingIds);
        // Call the service method to fetch opening details
        return openingRecentViewService.getOpeningsByIds(openingIds);
    }
    
}
