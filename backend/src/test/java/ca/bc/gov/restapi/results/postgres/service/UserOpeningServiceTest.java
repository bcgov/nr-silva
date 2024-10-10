package ca.bc.gov.restapi.results.postgres.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.exception.UserOpeningNotFoundException;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsActivityRepository;
import ca.bc.gov.restapi.results.postgres.repository.UserOpeningRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserOpeningServiceTest {

  @Mock LoggedUserService loggedUserService;

  @Mock UserOpeningRepository userOpeningRepository;

  @Mock OpeningsActivityRepository openingsActivityRepository;

  private UserOpeningService userOpeningService;

  private static final String USER_ID = "TEST";

  @BeforeEach
  void setup() {
    this.userOpeningService =
        new UserOpeningService(
            loggedUserService, userOpeningRepository, openingsActivityRepository);
  }

  @Test
  @DisplayName("Get user tracked openings happy path should succeed")
  void getUserTrackedOpenings_happyPath_shouldSucceed() {
    when(loggedUserService.getLoggedUserId()).thenReturn(USER_ID);

    UserOpeningEntity entity = new UserOpeningEntity();
    entity.setUserId(USER_ID);
    entity.setOpeningId(223344L);

    when(userOpeningRepository.findAllByUserId(USER_ID)).thenReturn(List.of(entity));

    LocalDateTime now = LocalDateTime.now().minusMinutes(2);
    OpeningsActivityEntity openingEntity = new OpeningsActivityEntity();
    openingEntity.setOpeningId(entity.getOpeningId());
    openingEntity.setActivityTypeCode("UPD");
    openingEntity.setActivityTypeDesc("Update");
    openingEntity.setStatusCode("APP");
    openingEntity.setStatusDesc("Approved");
    openingEntity.setLastUpdated(now);
    openingEntity.setEntryUserid(USER_ID);

    when(openingsActivityRepository.findAllByOpeningIdIsIn(List.of(223344L)))
        .thenReturn(List.of(openingEntity));

    List<MyRecentActionsRequestsDto> openings = userOpeningService.getUserTrackedOpenings();

    Assertions.assertFalse(openings.isEmpty());
    Assertions.assertEquals("Update", openings.get(0).activityType());
    Assertions.assertEquals(223344L, openings.get(0).openingId());
    Assertions.assertEquals("APP", openings.get(0).statusCode());
    Assertions.assertEquals("Approved", openings.get(0).statusDescription());
    Assertions.assertEquals("2 minutes ago", openings.get(0).lastUpdatedLabel());
  }

  @Test
  @DisplayName("Get user tracked openings no data should succeed")
  void getUserTrackedOpenings_noData_shouldSucceed() {
    when(loggedUserService.getLoggedUserId()).thenReturn(USER_ID);

    when(userOpeningRepository.findAllByUserId(USER_ID)).thenReturn(List.of());

    List<MyRecentActionsRequestsDto> openings = userOpeningService.getUserTrackedOpenings();

    Assertions.assertTrue(openings.isEmpty());
  }

  @Test
  @DisplayName("Save opening to user happy path should succeed")
  void saveOpeningToUser_happyPath_shouldSucceed() {
    when(loggedUserService.getLoggedUserId()).thenReturn(USER_ID);
    when(userOpeningRepository.saveAndFlush(any())).thenReturn(new UserOpeningEntity());
    userOpeningService.saveOpeningToUser(112233L);
  }

  @Test
  @DisplayName("Delete opening from user's favourite happy path should succeed")
  void deleteOpeningFromUserFavourite_happyPath_shouldSucceed() {
    when(loggedUserService.getLoggedUserId()).thenReturn(USER_ID);

    UserOpeningEntity userEntity = new UserOpeningEntity();
    when(userOpeningRepository.findById(any())).thenReturn(Optional.of(userEntity));

    doNothing().when(userOpeningRepository).delete(any());
    doNothing().when(userOpeningRepository).flush();

    userOpeningService.deleteOpeningFromUserFavourite(112233L);
  }

  @Test
  @DisplayName("Delete opening from user's favourite not found should fail")
  void deleteOpeningFromUserFavourite_notFound_shouldFail() {
    when(loggedUserService.getLoggedUserId()).thenReturn(USER_ID);
    when(userOpeningRepository.findById(any())).thenReturn(Optional.empty());

    Assertions.assertThrows(
        UserOpeningNotFoundException.class,
        () -> {
          userOpeningService.deleteOpeningFromUserFavourite(112233L);
        });
  }
}
