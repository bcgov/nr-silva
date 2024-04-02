package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.exception.OpeningNotFoundException;
import ca.bc.gov.restapi.results.common.exception.UserOpeningNotFoundException;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.postgres.dto.UserOpeningCreateDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsLastYearRepository;
import ca.bc.gov.restapi.results.postgres.repository.UserOpeningRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** This class contains methods for handling User favourite Openings. */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserOpeningService {

  private final LoggedUserService loggedUserService;

  private final UserOpeningRepository userOpeningRepository;

  private final OpeningsLastYearRepository openingsLastYearRepository;

  /**
   * Gets all openings saved for an user.
   *
   * @return A list of {@link UserOpeningEntity} containing the found records.
   */
  public List<UserOpeningEntity> getAllUserOpenings() {
    String userId = loggedUserService.getLoggedUserId();
    return userOpeningRepository.findAllByUserId(userId);
  }

  /**
   * Saves one or more Openings IDs to an user.
   *
   * @param createDtos List with one ore more opening IDs.
   */
  @Transactional
  public void saveOpeningsToUser(List<UserOpeningCreateDto> createDtos) {
    log.info("{} Opening IDs to save in the favourites!", createDtos.size());

    // validate all openings
    List<String> openingIdList = createDtos.stream().map(UserOpeningCreateDto::openingId).toList();

    log.info("Looking for Openings in the spar.openings_last_year table!");
    List<OpeningsLastYearEntity> entities =
        openingsLastYearRepository.findAllByOpeningIdInList(openingIdList);

    if (entities.size() < createDtos.size()) {
      log.info(
          "Result list ({}) contains less records than Openings IDs received ({})!",
          entities.size(),
          createDtos.size());
      throw new OpeningNotFoundException();
    }

    final String userId = loggedUserService.getLoggedUserId();

    List<UserOpeningEntity> entitiesToSave = new ArrayList<>();
    createDtos.forEach(
        dto -> {
          UserOpeningEntity entity = new UserOpeningEntity();
          entity.setUserId(userId);
          entity.setOpeningId(dto.openingId());
          entitiesToSave.add(entity);
        });

    userOpeningRepository.saveAllAndFlush(entitiesToSave);
    log.info("Opening IDs saved in the favourites!");
  }

  /**
   * Deletes one or more user opening from favourite.
   *
   * @param openingIds List with opening IDs to be deleted.
   */
  @Transactional
  public void deleteOpeningsFromUserFavourite(List<String> openingIds) {
    log.info("{} Opening IDs to delete from the favourites!", openingIds.size());
    String userId = loggedUserService.getLoggedUserId();

    List<UserOpeningEntity> userOpenings =
        userOpeningRepository.findAllByOpeningIdInAndUserId(openingIds, userId);

    if (userOpenings.size() < openingIds.size()) {
      log.info(
          "UserOpeningEntity result list ({}) contains less records than IDs received ({})!",
          userOpenings.size(),
          openingIds.size());
      throw new UserOpeningNotFoundException();
    }

    userOpeningRepository.deleteAll(userOpenings);
    log.info("Opening IDs deleted from the favourites!");
  }
}
