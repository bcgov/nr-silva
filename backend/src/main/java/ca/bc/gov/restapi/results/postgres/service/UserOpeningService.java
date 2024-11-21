package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.exception.OpeningNotFoundException;
import ca.bc.gov.restapi.results.common.exception.UserFavoriteNotFoundException;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntityId;
import ca.bc.gov.restapi.results.postgres.repository.UserOpeningRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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

  private final OpeningRepository openingRepository;

  public List<Long> listUserFavoriteOpenings() {
    log.info("Loading user favorite openings for {}", loggedUserService.getLoggedUserId());

    List<UserOpeningEntity> userList = userOpeningRepository
        .findAllByUserId(loggedUserService.getLoggedUserId(), PageRequest.of(0, 32));

    if (userList.isEmpty()) {
      log.info("No saved openings for {}", loggedUserService.getLoggedUserId());
      return List.of();
    }

    return
        userList
            .stream()
            .map(UserOpeningEntity::getOpeningId)
            .toList();
  }

  public List<Long> checkForFavorites(List<Long> openingIds) {
    log.info("Checking {} favorite for openings from the following list of openings {}",
        loggedUserService.getLoggedUserId(),
        openingIds
    );

    return userOpeningRepository
        .findAllByUserIdAndOpeningIdIn(loggedUserService.getLoggedUserId(), openingIds)
        .stream()
        .map(UserOpeningEntity::getOpeningId)
        .toList();
  }

  @Transactional
  public void addUserFavoriteOpening(Long openingId) {
    log.info("Adding opening ID {} as favorite for user {}", openingId,
        loggedUserService.getLoggedUserId());

    if (openingRepository.findById(openingId).isEmpty()) {
      log.info("Opening ID not found: {}", openingId);
      throw new OpeningNotFoundException();
    }

    log.info("Opening ID {} added as favorite for user {}", openingId,
        loggedUserService.getLoggedUserId());
    userOpeningRepository.saveAndFlush(
        new UserOpeningEntity(
            loggedUserService.getLoggedUserId(),
            openingId
        )
    );
  }

  @Transactional
  public void removeUserFavoriteOpening(Long openingId) {
    log.info("Removing opening ID {} from the favorites for user {}", openingId,
        loggedUserService.getLoggedUserId());
    userOpeningRepository.findById(
        new UserOpeningEntityId(
            loggedUserService.getLoggedUserId(),
            openingId
        )
    ).ifPresentOrElse(
        userOpening -> {
          userOpeningRepository.delete(userOpening);
          userOpeningRepository.flush();
          log.info("Opening ID deleted from the favourites!");
        },
        () -> {
          log.info("Opening id {} not found in the user's favourite list!", openingId);
          throw new UserFavoriteNotFoundException();
        }
    );
  }
}
