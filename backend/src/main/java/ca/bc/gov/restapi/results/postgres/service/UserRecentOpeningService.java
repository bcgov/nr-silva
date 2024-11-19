package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.exception.OpeningNotFoundException;
import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import ca.bc.gov.restapi.results.postgres.dto.UserRecentOpeningDto;
import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.UserRecentOpeningRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRecentOpeningService {

    private final LoggedUserService loggedUserService;
    private final UserRecentOpeningRepository userRecentOpeningRepository;
    private final OpeningService openingService;
    private final OpeningRepository openingRepository;

    @Transactional
    public UserRecentOpeningDto storeViewedOpening(Long openingId) {
        log.info("Adding opening ID {} as recently viewed for user {}", openingId,
            loggedUserService.getLoggedUserId());

        if (openingRepository.findById(openingId).isEmpty()) {
            log.info("Opening ID not found: {}", openingId);
            throw new OpeningNotFoundException();
        }

        LocalDateTime lastViewed = LocalDateTime.now();

        userRecentOpeningRepository.saveAndFlush(
        userRecentOpeningRepository
            .findByUserIdAndOpeningId(loggedUserService.getLoggedUserId(), openingId)
            .map(entity -> entity.withLastViewed(lastViewed))
            .orElse(
                new UserRecentOpeningEntity(null,loggedUserService.getLoggedUserId(),openingId,lastViewed)
            )
        );
    
        // Return the DTO
        return new UserRecentOpeningDto(
            loggedUserService.getLoggedUserId(),
            openingId,
            lastViewed
        );
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
        Page<UserRecentOpeningEntity> recentOpenings = userRecentOpeningRepository
                .findByUserIdOrderByLastViewedDesc(userId, pageable);
    
        // Extract opening IDs as String
        Map<Long, LocalDateTime> openingIds = recentOpenings.getContent().stream()
                .collect(Collectors.toMap(UserRecentOpeningEntity::getOpeningId, UserRecentOpeningEntity::getLastViewed));
        log.info("User with the userId {} has the following openingIds {}", userId, openingIds);
    
        if (openingIds.isEmpty()) {
            // Ensure an empty data list instead of null
            return new PaginatedResult<OpeningSearchResponseDto>()
                    .withData(Collections.emptyList());
        }
    
        PaginatedResult<OpeningSearchResponseDto> pageResult =
            openingService
                .openingSearch(
                    new OpeningSearchFiltersDto(new ArrayList<>(openingIds.keySet())),
                    new PaginationParameters(0, 10)
                );
    
        return pageResult
            .withData(
                pageResult
                    .getData()
                    .stream()
                    .map(result -> result.withLastViewDate(openingIds.get(result.getOpeningId().longValue())))
                    .sorted(Comparator.comparing(OpeningSearchResponseDto::getLastViewDate).reversed())
                    .toList()
            );
    }
    
    
}
