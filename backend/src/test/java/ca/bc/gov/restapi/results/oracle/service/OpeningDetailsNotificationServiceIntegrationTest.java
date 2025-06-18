package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsNotificationDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningDetailsNotificationStatusEnum;
import ca.bc.gov.restapi.results.oracle.service.opening.details.OpeningDetailsNotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DisplayName("Integrated Test | Opening Details Notification Service")
public class OpeningDetailsNotificationServiceIntegrationTest extends AbstractTestContainerIntegrationTest {

    @Autowired
    private OpeningDetailsNotificationService openingDetailsNotificationService;

    @Test
    @DisplayName("getNotifications for openingId 101017 should return correct notifications")
    void getNotifications_openingId101017_shouldReturnCorrectNotifications() {
        // Arrange
        Long openingId = 101017L;

        // Act
        List<OpeningDetailsNotificationDto> result = openingDetailsNotificationService.getNotifications(openingId);

        // Assert
        Assertions.assertEquals(2, result.size(), "Result size should match the number of notifications");

        // Verify first notification
        OpeningDetailsNotificationDto notification1 = result.get(0);
        Assertions.assertEquals("Overdue milestone detected for standard unit \"A\"", notification1.title(), "Title should match");
        Assertions.assertEquals("Immediate action required!", notification1.description(), "Description should match");
        Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.ERROR, notification1.status(), "Status should match");

        // Verify second notification
        OpeningDetailsNotificationDto notification2 = result.get(1);
        Assertions.assertEquals("Overdue milestone detected for standard unit \"B\"", notification2.title(), "Title should match");
        Assertions.assertEquals("Immediate action required!", notification2.description(), "Description should match");
        Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.ERROR, notification2.status(), "Status should match");
    }
}
