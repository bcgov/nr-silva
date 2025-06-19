package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsNotificationDto;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingNotificationProjection;
import ca.bc.gov.restapi.results.oracle.enums.OpeningDetailsNotificationStatusEnum;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.service.opening.details.OpeningDetailsNotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Opening Details Notification Service")
public class OpeningDetailsNotificationServiceTest {

    @Mock
    OpeningRepository openingRepository;
    private OpeningDetailsNotificationService openingDetailsNotificationService;

    @BeforeEach
    void setUp() {
        openingDetailsNotificationService = new OpeningDetailsNotificationService(openingRepository);
    }

    @Test
    @DisplayName("getNotifications returns notifications with valid milestone type codes")
    void getNotifications_validMilestoneTypeCodes_shouldReturnNotifications() {
        Long openingId = 1L;

        OpeningStockingNotificationProjection projection1 = mock(OpeningStockingNotificationProjection.class);
        when(projection1.getNotificationType()).thenReturn("ERROR");
        when(projection1.getStandardsUnitId()).thenReturn("A");

        OpeningStockingNotificationProjection projection2 = mock(OpeningStockingNotificationProjection.class);
        when(projection2.getNotificationType()).thenReturn("WARNING");
        when(projection2.getSilvMilestoneTypeCode()).thenReturn("RG");
        when(projection2.getStandardsUnitId()).thenReturn("B");

        when(openingRepository.getOpeningStockingNotificationsByOpeningId(openingId))
                .thenReturn(List.of(projection1, projection2));

        List<OpeningDetailsNotificationDto> result = openingDetailsNotificationService.getNotifications(openingId);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Overdue milestone detected for standard unit \"A\"", result.get(1).title());
        Assertions.assertEquals("Immediate action required!", result.get(1).description());
        Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.ERROR, result.get(1).status());

        Assertions.assertEquals("Upcoming milestone detected for standard unit \"B\"", result.get(0).title());
        Assertions.assertEquals("Monitor progress closely to declare your Regeneration in time!", result.get(0).description());
        Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.WARNING, result.get(0).status());
    }

    @Test
    @DisplayName("getNotifications handles multiple standard units correctly")
    void getNotifications_multipleStandardUnits_shouldReturnCombinedNotification() {
        Long openingId = 1L;

        OpeningStockingNotificationProjection projection1 = mock(OpeningStockingNotificationProjection.class);
        when(projection1.getNotificationType()).thenReturn("WARNING");
        when(projection1.getSilvMilestoneTypeCode()).thenReturn("PH");
        when(projection1.getStandardsUnitId()).thenReturn("A");

        OpeningStockingNotificationProjection projection2 = mock(OpeningStockingNotificationProjection.class);
        when(projection2.getNotificationType()).thenReturn("WARNING");
        when(projection2.getSilvMilestoneTypeCode()).thenReturn("PH");
        when(projection2.getStandardsUnitId()).thenReturn("C");

        when(openingRepository.getOpeningStockingNotificationsByOpeningId(openingId))
                .thenReturn(List.of(projection1, projection2));

        List<OpeningDetailsNotificationDto> result = openingDetailsNotificationService.getNotifications(openingId);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Upcoming milestone detected for standard unit \"A, C\"", result.get(0).title());
        Assertions.assertEquals("Monitor progress closely to declare your Post Harvest in time!", result.get(0).description());
        Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.WARNING, result.get(0).status());
    }

    @Test
    @DisplayName("getNotifications returns empty list when no projections are found")
    void getNotifications_noProjections_shouldReturnEmptyList() {
        Long openingId = 1L;

        when(openingRepository.getOpeningStockingNotificationsByOpeningId(openingId))
                .thenReturn(List.of());

        List<OpeningDetailsNotificationDto> result = openingDetailsNotificationService.getNotifications(openingId);

        Assertions.assertNotNull(result, "Result should not be null");
        Assertions.assertTrue(result.isEmpty(), "Result should be an empty list");
    }

}
