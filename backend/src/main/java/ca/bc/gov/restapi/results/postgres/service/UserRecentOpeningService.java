package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.exception.InvalidOpeningIdException;
import ca.bc.gov.restapi.results.common.exception.OpeningNotFoundException;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import ca.bc.gov.restapi.results.postgres.dto.UserRecentOpeningDto;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntityId;
import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.UserRecentOpeningRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    if (openingId == null) {
      log.info("Opening ID is null");
      throw new InvalidOpeningIdException();
    }

    if (!openingRepository.existsById(openingId)) {
      log.info("Opening ID not found: {}", openingId);
      throw new OpeningNotFoundException();
    }

    UserRecentOpeningEntity recentOpening =
        userRecentOpeningRepository.saveAndFlush(
            userRecentOpeningRepository
                .findById(new UserOpeningEntityId(loggedUserService.getLoggedUserId(), openingId))
                .map(entity -> entity.withLastViewed(LocalDateTime.now()))
                .orElse(
                    new UserRecentOpeningEntity(
                        loggedUserService.getLoggedUserId(),
                        openingId,
                        LocalDateTime.now()
                    )
                )
        );

    // Return the DTO
    return new UserRecentOpeningDto(
        recentOpening.getUserId(),
        recentOpening.getOpeningId(),
        recentOpening.getLastViewed()
    );
  }

  public Page<OpeningSearchResponseDto> getAllRecentOpeningsForUser(Pageable pageable) {
    String userId = loggedUserService.getLoggedUserId();

    // Fetch recent openings for the user
    Page<UserRecentOpeningEntity> recentOpenings = userRecentOpeningRepository
        .findByUserIdOrderByLastViewedDesc(userId, pageable);

    // Extract opening IDs as String
    Map<Long, LocalDateTime> openingIds = recentOpenings
        .getContent()
        .stream()
        .collect(
            Collectors
                .toMap(
                    UserRecentOpeningEntity::getOpeningId,
                    UserRecentOpeningEntity::getLastViewed
                )
        );
    log.info("User with the userId {} has the following openingIds {}", userId, openingIds);

    if (openingIds.isEmpty()) {
      // Ensure an empty data list instead of null
      return new PageImpl<>(List.of(), pageable, 0);
    }

    Page<OpeningSearchResponseDto> pageResult =
        openingService.parsePageResult(
            openingRepository
                .searchBy(
                    new OpeningSearchFiltersDto(),
                    new ArrayList<>(openingIds.keySet()),
                    // Here it really doesn't matter, if we set the page as first, 
                    // because it will be just for the current page anyway                    
                    PageRequest.of(0, openingIds.size())
                )
        );

    return
        new PageImpl<>(
            pageResult
                .get()
                .map(result -> result.withLastViewDate(
                    openingIds.get(result.getOpeningId().longValue())))
                .sorted(Comparator.comparing(OpeningSearchResponseDto::getLastViewDate).reversed())
                .toList(),
            recentOpenings.getPageable(),
            recentOpenings.getTotalPages()
        );
  }


}
