package ca.bc.gov.restapi.results.oracle.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.oracle.dto.comment.CommentDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTombstoneOverviewDto;
import ca.bc.gov.restapi.results.oracle.entity.comments.CommentProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewMilestoneProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewOpeningProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneProjection;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvicultureCommentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingDetailsProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingLayerProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingSpeciesProjection;
import ca.bc.gov.restapi.results.oracle.util.OpeningTestDataFactory;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Opening Service")
class OpeningServiceTest {

    @Mock
    private OpeningRepository openingRepository;

    @Mock
    private ForestClientService forestClientService;

    @Mock
    private SilvicultureCommentRepository commentRepository;

    private OpeningService openingService;


    // Tombstone projections
    private OpeningTombstoneProjection openingTombstone;
    private OpeningTombstoneOverviewOpeningProjection openingOverview;
    private OpeningTombstoneOverviewMilestoneProjection milestoneOverview;
    private ForestClientDto forestClient;
    private CommentProjection commentProjection;
    
    // Stocking projections
    private OpeningStockingDetailsProjection stockingDetailsProjection;
    private OpeningStockingSpeciesProjection stockingAcceptableSpeciesProjection;
    private OpeningStockingSpeciesProjection stockingPreferredSpeciesProjection;
    private OpeningStockingLayerProjection stockingLayerProjection;

    @BeforeEach
    void setUp() {
        openingService = new OpeningService(openingRepository, forestClientService, commentRepository);
    }

    private void createTombstoneTestData() {
        openingTombstone = OpeningTestDataFactory.createTombstoneProjection();
        openingOverview = OpeningTestDataFactory.createTombstoneOverviewOpeningProjection();
        milestoneOverview = OpeningTestDataFactory.createTombstoneOverviewMilestoneProjection();
        forestClient = OpeningTestDataFactory.createForestClientDto();
        commentProjection = OpeningTestDataFactory.createCommentProjection();
        
    }

    private void createStockingTestData() {
        stockingDetailsProjection = OpeningTestDataFactory.createOpeningStockingDetailsProjection();
        stockingAcceptableSpeciesProjection = OpeningTestDataFactory.createAcceptableSpeciesProjection();
        stockingPreferredSpeciesProjection = OpeningTestDataFactory.createPreferredSpeciesProjection();
        stockingLayerProjection = OpeningTestDataFactory.createStockingLayerProjection();
    }

    @Test
    @DisplayName("Get Opening Tombstone with valid data and client number should succeed")
    void getOpeningTombstone_withClientNumber_shouldSucceed() {
        createTombstoneTestData();

        Long openingId = 123L;
        String clientNumber = "123456";

        // Use the pre-created instances
        when(openingRepository.getOpeningTombstoneByOpeningId(openingId))
                .thenReturn(Optional.of(openingTombstone));
        when(forestClientService.getClientByNumber(clientNumber))
                .thenReturn(Optional.of(forestClient));
        when(openingRepository.getOpeningTombstoneOverviewByOpeningId(openingId))
                .thenReturn(Optional.of(openingOverview));
        when(openingRepository.getOpeningTombstoneMilestoneByOpeningId(openingId))
                .thenReturn(Optional.of(milestoneOverview));
        when(commentRepository.getCommentById(openingId))
                .thenReturn(List.of(commentProjection));

        // Execute
        Optional<OpeningDetailsTombstoneOverviewDto> result = openingService.getOpeningTombstone(openingId);

        // Verify
        Assertions.assertTrue(result.isPresent());
        OpeningDetailsTombstoneOverviewDto dto = result.get();

        // Verify OpeningDetailsTombstoneOverviewDto
        Assertions.assertEquals(openingId, dto.openingId());

        // Verify OpeningDetailsTombstoneDto
        Assertions.assertEquals("OPN12345", dto.tombstone().openingNumber());
        Assertions.assertEquals(OpeningStatusEnum.APP, dto.tombstone().openingStatus());
        Assertions.assertEquals("DCC", dto.tombstone().orgUnitCode());
        Assertions.assertEquals("Cariboo-Chilcotin Natural Resource District", dto.tombstone().orgUnitName());
        Assertions.assertEquals(OpeningCategoryEnum.FTML, dto.tombstone().openCategory());
        Assertions.assertEquals("FILE987", dto.tombstone().fileId());
        Assertions.assertEquals("CB001", dto.tombstone().cutBlockID());
        Assertions.assertEquals("CP456", dto.tombstone().cuttingPermitId());
        Assertions.assertEquals("TM7890", dto.tombstone().timberMark());
        Assertions.assertEquals("PUBLIC", dto.tombstone().maxAllowedAccess());
        Assertions.assertEquals(250.5F, dto.tombstone().openingGrossArea());
        Assertions.assertEquals("TESTUSER", dto.tombstone().createdBy());
        Assertions.assertEquals(LocalDate.of(2022, 3, 15), dto.tombstone().createdOn());
        Assertions.assertEquals(LocalDate.of(2023, 5, 20), dto.tombstone().lastUpdatedOn());
        Assertions.assertEquals(LocalDate.of(2022, 6, 1), dto.tombstone().disturbanceStartDate());

        // Verify ForestClientDto within OpeningDetailsTombstoneDto
        Assertions.assertEquals("123456", dto.tombstone().client().clientNumber());
        Assertions.assertEquals("Client Name", dto.tombstone().client().clientName());
        Assertions.assertEquals(ForestClientStatusEnum.ACTIVE, dto.tombstone().client().clientStatusCode());
        Assertions.assertEquals(ForestClientTypeEnum.ASSOCIATION, dto.tombstone().client().clientTypeCode());
        Assertions.assertEquals("ABC", dto.tombstone().client().acronym());

        // Verify OpeningDetailsOverviewDto
        Assertions.assertNotNull(dto.overview());

        // Verify OpeningDetailsOverviewOpeningDto
        Assertions.assertEquals("LIC-123", dto.overview().opening().licenseeId());
        Assertions.assertEquals("FL", dto.overview().opening().tenureType().code());
        Assertions.assertEquals("Forest License", dto.overview().opening().tenureType().description());
        Assertions.assertEquals("TSA", dto.overview().opening().managementUnitType().code());
        Assertions.assertEquals("Timber Supply Area", dto.overview().opening().managementUnitType().description());
        Assertions.assertEquals("TSA01", dto.overview().opening().managementUnitId());
        Assertions.assertEquals("TSO456", dto.overview().opening().timberSaleOffice().code());
        Assertions.assertEquals("Williams Lake Timber Sales Office", dto.overview().opening().timberSaleOffice().description());

        // Verify comments within OpeningDetailsOverviewOpeningDto
        Assertions.assertNotNull(dto.overview().opening().comments());
        Assertions.assertEquals(1, dto.overview().opening().comments().size());
        CommentDto comment = dto.overview().opening().comments().get(0);
        Assertions.assertEquals("SRC001", comment.commentSource().code());
        Assertions.assertEquals("Source Name", comment.commentSource().description());
        Assertions.assertEquals("TYPE001", comment.commentType().code());
        Assertions.assertEquals("Type Name", comment.commentType().description());
        Assertions.assertEquals("This is a test comment.", comment.commentText());

        // Verify OpeningDetailsOverviewMilestoneDto
        Assertions.assertEquals("SU123", dto.overview().milestones().standardsUnitId());
        Assertions.assertEquals(LocalDate.of(2022, 10, 15), dto.overview().milestones().postHarvestDeclaredDate());
        Assertions.assertEquals(LocalDate.of(2023, 11, 30), dto.overview().milestones().regenDeclaredDate());
        Assertions.assertEquals(7, dto.overview().milestones().regenOffsetYears());
        Assertions.assertEquals(LocalDate.of(2029, 6, 1), dto.overview().milestones().regenDueDate());
        Assertions.assertEquals(LocalDate.of(2032, 8, 15), dto.overview().milestones().freeGrowingDeclaredDate());
        Assertions.assertEquals(15, dto.overview().milestones().freeGrowingOffsetYears());
        Assertions.assertEquals(LocalDate.of(2037, 6, 1), dto.overview().milestones().freeGrowingDueDate());
    }

    @Test
    @DisplayName("Get Opening Tombstone with no data should return empty")
    void getOpeningTombstone_noData_shouldReturnEmpty() {
        createTombstoneTestData();

        Long openingId = 1L;
        when(openingRepository.getOpeningTombstoneByOpeningId(anyLong()))
                .thenReturn(Optional.empty());

        Optional<OpeningDetailsTombstoneOverviewDto> result = openingService.getOpeningTombstone(openingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Get Opening Stocking Details with valid data should succeed")
    void getOpeningStockingDetails_withValidData_shouldSucceed() {
        Long openingId = 1013720L;

        // Generate test data
        createStockingTestData();

        // Mock repository calls
        when(openingRepository.getOpeningStockingDetailsByOpeningId(openingId))
                .thenReturn(List.of(stockingDetailsProjection));
        when(openingRepository.getOpeningStockingSpeciesByOpeningId(openingId, "Y", stockingDetailsProjection.getSsid()))
                .thenReturn(List.of(stockingPreferredSpeciesProjection));
        when(openingRepository.getOpeningStockingSpeciesByOpeningId(openingId, "N", stockingDetailsProjection.getSsid()))
                .thenReturn(List.of(stockingAcceptableSpeciesProjection));
        when(openingRepository.getOpeningStockingLayerByOpeningId(openingId, stockingDetailsProjection.getSsid()))
                .thenReturn(Optional.of(stockingLayerProjection));

        // Execute
        List<OpeningDetailsStockingDto> result = openingService.getOpeningStockingDetails(openingId);

        // Verify
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());

        OpeningDetailsStockingDto dto = result.get(0);

        // Verify stocking details
        Assertions.assertEquals("A", dto.stocking().stockingStandardUnit());
        Assertions.assertEquals(1013720L, dto.stocking().ssid());
        Assertions.assertEquals(25.5F, dto.stocking().netArea());
        Assertions.assertEquals(5.0F, dto.stocking().soilDisturbancePercent());

        // Verify preferred species
        Assertions.assertEquals(1, dto.preferredSpecies().size());
        Assertions.assertEquals("CW", dto.preferredSpecies().get(0).species().code());
        Assertions.assertEquals("western redcedar", dto.preferredSpecies().get(0).species().description());
        Assertions.assertEquals(1L, dto.preferredSpecies().get(0).minHeight());

        // Verify acceptable species
        Assertions.assertEquals(1, dto.acceptableSpecies().size());
        Assertions.assertEquals("BA", dto.acceptableSpecies().get(0).species().code());
        Assertions.assertEquals("amabilis fir", dto.acceptableSpecies().get(0).species().description());
        Assertions.assertEquals(1L, dto.acceptableSpecies().get(0).minHeight());

        // Verify stocking layer
        Assertions.assertNotNull(dto.layer());
        Assertions.assertEquals(500, dto.layer().minWellspacedTrees());
        Assertions.assertEquals(400, dto.layer().minPreferredWellspacedTrees());
        Assertions.assertEquals(2, dto.layer().minHorizontalDistanceWellspacedTrees());
        Assertions.assertEquals(900, dto.layer().targetWellspacedTrees());
        Assertions.assertEquals(800, dto.layer().minPostspacingDensity());
        Assertions.assertEquals(2000, dto.layer().maxPostspacingDensity());
        Assertions.assertEquals(10000, dto.layer().maxConiferous());
        Assertions.assertEquals(150, dto.layer().heightRelativeToComp());
    }

    @Test
    @DisplayName("Get Opening Stocking Details with missing species should succeed")
    void getOpeningStockingDetails_withMissingSpecies_shouldSucceed() {
        Long openingId = 1013720L;

        // Generate test data
        createStockingTestData();

        // Mock repository calls
        when(openingRepository.getOpeningStockingDetailsByOpeningId(openingId))
                .thenReturn(List.of(stockingDetailsProjection));
        when(openingRepository.getOpeningStockingSpeciesByOpeningId(openingId, "Y", stockingDetailsProjection.getSsid()))
                .thenReturn(List.of()); // No preferred species
        when(openingRepository.getOpeningStockingSpeciesByOpeningId(openingId, "N", stockingDetailsProjection.getSsid()))
                .thenReturn(List.of()); // No acceptable species
        when(openingRepository.getOpeningStockingLayerByOpeningId(openingId, stockingDetailsProjection.getSsid()))
                .thenReturn(Optional.of(stockingLayerProjection));

        // Execute
        List<OpeningDetailsStockingDto> result = openingService.getOpeningStockingDetails(openingId);

        // Verify
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());

        OpeningDetailsStockingDto dto = result.get(0);

        // Verify species lists are empty
        Assertions.assertTrue(dto.preferredSpecies().isEmpty());
        Assertions.assertTrue(dto.acceptableSpecies().isEmpty());
    }

    @Test
    @DisplayName("Get Opening Stocking Details with missing layer should succeed")
    void getOpeningStockingDetails_withMissingLayer_shouldSucceed() {
        Long openingId = 1013720L;

        // Generate test data
        createStockingTestData();

        // Mock repository calls
        when(openingRepository.getOpeningStockingDetailsByOpeningId(openingId))
                .thenReturn(List.of(stockingDetailsProjection));
        when(openingRepository.getOpeningStockingSpeciesByOpeningId(openingId, "Y", stockingDetailsProjection.getSsid()))
                .thenReturn(List.of(stockingPreferredSpeciesProjection));
        when(openingRepository.getOpeningStockingSpeciesByOpeningId(openingId, "N", stockingDetailsProjection.getSsid()))
                .thenReturn(List.of(stockingAcceptableSpeciesProjection));
        when(openingRepository.getOpeningStockingLayerByOpeningId(openingId, stockingDetailsProjection.getSsid()))
                .thenReturn(Optional.empty()); // No layer

        // Execute
        List<OpeningDetailsStockingDto> result = openingService.getOpeningStockingDetails(openingId);

        // Verify
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());

        OpeningDetailsStockingDto dto = result.get(0);

        // Verify layer is null
        Assertions.assertNull(dto.layer());
    }

    @Test
    @DisplayName("Get Opening Stocking Details with no data should return empty")
    void getOpeningStockingDetails_withNoData_shouldReturnEmpty() {
        Long openingId = 1013720L;

        // Mock repository calls
        when(openingRepository.getOpeningStockingDetailsByOpeningId(openingId))
                .thenReturn(List.of()); // No data

        // Execute
        List<OpeningDetailsStockingDto> result = openingService.getOpeningStockingDetails(openingId);

        // Verify
        Assertions.assertTrue(result.isEmpty());
    }
}
