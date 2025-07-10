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
        when(projection1.getNotificationType()).thenReturn(OpeningDetailsNotificationStatusEnum.ERROR.toString());
        when(projection1.getSilvMilestoneTypeCode()).thenReturn("FG");
        when(projection1.getStandardsUnitId()).thenReturn("A");

        OpeningStockingNotificationProjection projection2 = mock(OpeningStockingNotificationProjection.class);
        when(projection2.getNotificationType()).thenReturn(OpeningDetailsNotificationStatusEnum.WARNING.toString());
        when(projection2.getSilvMilestoneTypeCode()).thenReturn("FG");
        when(projection2.getStandardsUnitId()).thenReturn("B");

        OpeningStockingNotificationProjection projection3 = mock(OpeningStockingNotificationProjection.class);
        when(projection3.getNotificationType()).thenReturn(OpeningDetailsNotificationStatusEnum.INFO.toString());
        when(projection3.getSilvMilestoneTypeCode()).thenReturn("RG");
        when(projection3.getStandardsUnitId()).thenReturn("C");

        when(openingRepository.getOpeningStockingNotificationsByOpeningId(openingId))
                .thenReturn(List.of(projection1, projection2, projection3));

        List<OpeningDetailsNotificationDto> result = openingDetailsNotificationService.getNotifications(openingId);

        Assertions.assertEquals(3, result.size());

        Assertions.assertEquals("Free Growing milestone is overdue for standard unit \"A\"", result.get(0).title());
        Assertions.assertEquals("Please contact your ministry representative as soon as possible!", result.get(0).description());
        Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.ERROR, result.get(0).status());

        Assertions.assertEquals("Upcoming milestone detected for standard unit \"B\"", result.get(1).title());
        Assertions.assertEquals("Monitor progress closely to declare your Free Growing in time!", result.get(1).description());
        Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.WARNING, result.get(1).status());

        Assertions.assertEquals("Regeneration milestone reminder for standard unit \"C\"", result.get(2).title());
        Assertions.assertEquals("Please update your forest cover.", result.get(2).description());
        Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.INFO, result.get(2).status());
    }

    @Test
    @DisplayName("getNotifications handles multiple standard units correctly")
    void getNotifications_multipleStandardUnits_shouldReturnCombinedNotification() {
        Long openingId = 1L;

        OpeningStockingNotificationProjection warningProjection1 = mock(OpeningStockingNotificationProjection.class);
        when(warningProjection1.getNotificationType()).thenReturn("WARNING");
        when(warningProjection1.getSilvMilestoneTypeCode()).thenReturn("FG");
        when(warningProjection1.getStandardsUnitId()).thenReturn("A");

        OpeningStockingNotificationProjection warningProjection2 = mock(OpeningStockingNotificationProjection.class);
        when(warningProjection2.getNotificationType()).thenReturn("WARNING");
        when(warningProjection2.getSilvMilestoneTypeCode()).thenReturn("FG");
        when(warningProjection2.getStandardsUnitId()).thenReturn("C");

        OpeningStockingNotificationProjection errorProjection1 = mock(OpeningStockingNotificationProjection.class);
        when(errorProjection1.getNotificationType()).thenReturn("ERROR");
        when(errorProjection1.getSilvMilestoneTypeCode()).thenReturn("FG");
        when(errorProjection1.getStandardsUnitId()).thenReturn("B");

        OpeningStockingNotificationProjection errorProjection2 = mock(OpeningStockingNotificationProjection.class);
        when(errorProjection2.getNotificationType()).thenReturn("ERROR");
        when(errorProjection2.getSilvMilestoneTypeCode()).thenReturn("FG");
        when(errorProjection2.getStandardsUnitId()).thenReturn("D");

        OpeningStockingNotificationProjection infoProjection1 = mock(OpeningStockingNotificationProjection.class);
        when(infoProjection1.getNotificationType()).thenReturn("INFO");
        when(infoProjection1.getSilvMilestoneTypeCode()).thenReturn("RG");
        when(infoProjection1.getStandardsUnitId()).thenReturn("E");

        OpeningStockingNotificationProjection infoProjection2 = mock(OpeningStockingNotificationProjection.class);
        when(infoProjection2.getNotificationType()).thenReturn("INFO");
        when(infoProjection2.getSilvMilestoneTypeCode()).thenReturn("RG");
        when(infoProjection2.getStandardsUnitId()).thenReturn("F");

        when(openingRepository.getOpeningStockingNotificationsByOpeningId(openingId))
                .thenReturn(List.of(warningProjection1, warningProjection2, errorProjection1, errorProjection2, infoProjection1, infoProjection2));

        List<OpeningDetailsNotificationDto> result = openingDetailsNotificationService.getNotifications(openingId);

        Assertions.assertEquals(3, result.size());

        Assertions.assertEquals("Free Growing milestone is overdue for standard unit \"B, D\"", result.get(0).title());
        Assertions.assertEquals("Please contact your ministry representative as soon as possible!", result.get(0).description());
        Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.ERROR, result.get(0).status());

        Assertions.assertEquals("Upcoming milestone detected for standard unit \"A, C\"", result.get(1).title());
        Assertions.assertEquals("Monitor progress closely to declare your Free Growing in time!", result.get(1).description());
        Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.WARNING, result.get(1).status());

        Assertions.assertEquals("Regeneration milestone reminder for standard unit \"E, F\"", result.get(2).title());
        Assertions.assertEquals("Please update your forest cover.", result.get(2).description());
        Assertions.assertEquals(OpeningDetailsNotificationStatusEnum.INFO, result.get(2).status());
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
