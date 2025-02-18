package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.exception.UserFavoriteNotFoundException;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.UserOpeningRepository;
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

  @Mock
  LoggedUserService loggedUserService;

  @Mock
  UserOpeningRepository userOpeningRepository;

  @Mock
  OpeningRepository openingRepository;

  private UserOpeningService userOpeningService;

  private static final String USER_ID = "TEST";

  @BeforeEach
  void setup() {
    this.userOpeningService =
        new UserOpeningService(
            loggedUserService, userOpeningRepository,
            openingRepository);
  }

  @Test
  @DisplayName("Save opening to user happy path should succeed")
  void addUser_FavoriteOpening_happyPath_shouldSucceed() {
    when(loggedUserService.getLoggedUserId()).thenReturn(USER_ID);
    when(openingRepository.findById(any())).thenReturn(Optional.of(new OpeningEntity()));
    when(userOpeningRepository.saveAndFlush(any())).thenReturn(new UserOpeningEntity());
    userOpeningService.addUserFavoriteOpening(112233L);
  }

  @Test
  @DisplayName("Delete opening from user's favourite happy path should succeed")
  void removeUserFavoriteOpening_happyPath_shouldSucceed() {
    when(loggedUserService.getLoggedUserId()).thenReturn(USER_ID);

    UserOpeningEntity userEntity = new UserOpeningEntity();
    when(userOpeningRepository.findById(any())).thenReturn(Optional.of(userEntity));

    doNothing().when(userOpeningRepository).delete(any());
    doNothing().when(userOpeningRepository).flush();

    userOpeningService.removeUserFavoriteOpening(112233L);
  }

  @Test
  @DisplayName("Delete opening from user's favourite not found should fail")
  void removeUserFavoriteOpening_notFound_shouldFail() {
    when(loggedUserService.getLoggedUserId()).thenReturn(USER_ID);
    when(userOpeningRepository.findById(any())).thenReturn(Optional.empty());

    Assertions.assertThrows(
        UserFavoriteNotFoundException.class,
        () -> {
          userOpeningService.removeUserFavoriteOpening(112233L);
        });
  }

  @Test
  @DisplayName("List user favourite openings happy path should succeed")
  void listUserFavoriteOpenings_happyPath_shouldSucceed() {
    when(loggedUserService.getLoggedUserId()).thenReturn(USER_ID);
    when(userOpeningRepository.findAllByUserId(any(), any())).thenReturn(
        List.of(new UserOpeningEntity()));
    userOpeningService.listUserFavoriteOpenings();
  }

  @Test
  @DisplayName("Check for favorites happy path should succeed")
  void checkForFavorites_happyPath_shouldSucceed() {
    when(loggedUserService.getLoggedUserId()).thenReturn(USER_ID);
    when(userOpeningRepository.findAllByUserIdAndOpeningIdIn(any(), any())).thenReturn(
        List.of(new UserOpeningEntity(USER_ID, 112233L)));
    assertThat(userOpeningService.checkForFavorites(List.of(112233L)))
        .isNotNull()
        .isNotEmpty()
        .hasSize(1)
        .contains(112233L);
  }
}
