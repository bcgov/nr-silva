package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.exception.UserOpeningNotFoundException;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntityId;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsActivityRepository;
import ca.bc.gov.restapi.results.postgres.repository.UserOpeningRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;

/**
 * This class contains methods for handling User favourite Openings.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserOpeningService {

  private final LoggedUserService loggedUserService;

  private final UserOpeningRepository userOpeningRepository;

  private final OpeningsActivityRepository openingsActivityRepository;

  private final PrettyTime prettyTime = new PrettyTime();

  /**
   * Gets user's tracked Openings.
   *
   * @return A list of {@link MyRecentActionsRequestsDto} containing the found records.
   */
  public List<MyRecentActionsRequestsDto> getUserTrackedOpenings() {
    log.info("Getting all user openings for the Track openings table");

    List<UserOpeningEntity> userList = userOpeningRepository.findAllByUserId(
        loggedUserService.getLoggedUserId());

    if (userList.isEmpty()) {
      log.info("No saved openings for the current user!");
      return List.of();
    }

    List<Long> openingIds = userList.stream().map(UserOpeningEntity::getOpeningId).toList();
    List<OpeningsActivityEntity> openingActivities =
        openingsActivityRepository.findAllByOpeningIdIn(openingIds);

    if (openingActivities.isEmpty()) {
      log.info("No records found on the opening activity table for the opening ID list!");
      return List.of();
    }

    return
        openingActivities
            .stream()
            .limit(3) // Best approach would be to limit the number of records in the query
            .map(
                activityEntity ->
                    new MyRecentActionsRequestsDto(
                        activityEntity.getActivityTypeDesc(),
                        activityEntity.getOpeningId(),
                        activityEntity.getStatusCode(),
                        activityEntity.getStatusDesc(),
                        prettyTime.format(activityEntity.getLastUpdated()),
                        activityEntity.getLastUpdated())
            )
            .toList();
  }

  /**
   * Saves one or more Openings IDs to an user.
   *
   * @param openingId The opening ID.
   */
  @Transactional
  public void saveOpeningToUser(Long openingId) {
    log.info("Opening ID to save in the user favourites: {}", openingId);
    userOpeningRepository.saveAndFlush(
        new UserOpeningEntity(loggedUserService.getLoggedUserId(), openingId));
    log.info("Opening ID saved in the user's favourites!");
  }

  /**
   * Deletes one or more user opening from favourite.
   *
   * @param openingId The opening ID.
   */
  @Transactional
  public void deleteOpeningFromUserFavourite(Long openingId) {
    log.info("Opening ID to delete from the user's favourites: {}", openingId);

    Optional<UserOpeningEntity> userOpeningsOp = userOpeningRepository
        .findById(new UserOpeningEntityId(
                loggedUserService.getLoggedUserId(),
                openingId
            )
        );

    if (userOpeningsOp.isEmpty()) {
      log.info("Opening id {} not found in the user's favourite list!", openingId);
      throw new UserOpeningNotFoundException();
    }

    userOpeningRepository.delete(userOpeningsOp.get());
    userOpeningRepository.flush();
    log.info("Opening ID deleted from the favourites!");
  }
}
