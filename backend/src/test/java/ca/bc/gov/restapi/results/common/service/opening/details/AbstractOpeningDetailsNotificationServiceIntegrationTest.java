package ca.bc.gov.restapi.results.common.service.opening.details;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsNotificationDto;
import ca.bc.gov.restapi.results.common.enums.OpeningDetailsNotificationStatusEnum;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DisplayName("Integrated Test | Opening Details Notification Service | Contract")
public abstract class AbstractOpeningDetailsNotificationServiceIntegrationTest<T extends OpeningDetailsNotificationService> extends
    AbstractTestContainerIntegrationTest {

  @Autowired
  protected OpeningDetailsNotificationService openingDetailsNotificationService;

  @Test
  @DisplayName("getNotifications for openingId 101017 should return correct notifications")
  void getNotifications_openingId101017_shouldReturnCorrectNotifications() {
    // Arrange
    Long openingId = 101017L;

    // Act
    List<OpeningDetailsNotificationDto> result = openingDetailsNotificationService.getNotifications(openingId);

    // Assert
    Assertions.assertEquals(1, result.size(), "Result size should match the number of notifications");

    OpeningDetailsNotificationDto notification1 = result.get(0);
    Assertions.assertEquals("Regeneration milestone reminder for standard unit \"A, B\"", notification1.title(), "Title should match");
    Assertions.assertEquals("Please update your forest cover.", notification1.description(), "Description should match");
    Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.INFO, notification1.status(), "Status should match");
  }
}
