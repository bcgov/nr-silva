package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.dto.opening.history.*;
import ca.bc.gov.restapi.results.common.projection.opening.history.*;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.oracle.service.opening.history.OpeningStandardUnitHistoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Opening Standard Unit History Service")
public class OpeningStandardUnitHistoryServiceTest {

    @Mock
    OpeningRepository openingRepository;

    @Mock
    SilvicultureCommentRepository commentRepository;

    private OpeningStandardUnitHistoryService openingStandardUnitHistoryService;

    @BeforeEach
    void setUp() {
        openingStandardUnitHistoryService = new OpeningStandardUnitHistoryService(openingRepository, commentRepository);
    }

    @Test
    @DisplayName("getStandardUnitHistoryDetails returns empty list when no details are found")
    void getStandardUnitHistoryDetails_noDetails_WithComparison_shouldReturnEmptyList() {
        // Given
        Long openingId = 1L;
        Long stockingEventHistoryId = 101L;

        when(openingRepository.getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId)).thenReturn(List.of());

        // When
        List<OpeningStockingHistoryWithComparisonDto> result =
                openingStandardUnitHistoryService.getStandardUnitHistoryDetailsWithComparison(openingId, stockingEventHistoryId);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());

        verify(openingRepository, times(1))
                .getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(openingId, stockingEventHistoryId);
        verify(openingRepository, never())
                .getOpeningStandardUnitHistoryLayerDetailsWithComparisonByOpeningIdAndHistoryId(any(), any());
        verify(openingRepository, never())
                .getOpeningStandardUnitHistoryLayerSpeciesDetailsWithComparisonByOpeningIdAndHistoryId(any(), any());
    }

    @Test
    @DisplayName("getStandardUnitOverviewHistoryList returns correct overview list")
    void getStandardUnitOverviewHistoryList_returnsCorrectOverviewList() {
        // Given
        Long openingId = 1L;

        // Create mock projections
        OpeningStockingHistoryProjection amendedProjection = mock(OpeningStockingHistoryProjection.class);
        when(amendedProjection.getStockingEventHistoryId()).thenReturn(2L);
        when(amendedProjection.getAmendmentNumber()).thenReturn(1);
        when(amendedProjection.getEventTimestamp()).thenReturn(LocalDateTime.of(2025, 7, 3, 11, 50));
        when(amendedProjection.getSuCount()).thenReturn(2);
        when(amendedProjection.getTotalNar()).thenReturn(20.0);
        when(amendedProjection.getAuditActionCode()).thenReturn("AMD");
        when(amendedProjection.getAuditActionDescription()).thenReturn("Amended");
        when(amendedProjection.getEsfSubmissionId()).thenReturn(null);
        when(amendedProjection.getSubmittedByUserId()).thenReturn("IDIR\\TEST");
        when(amendedProjection.getApprovedByUserId()).thenReturn("IDIR\\TEST");

        OpeningStockingHistoryProjection
            correctionProjection = mock(OpeningStockingHistoryProjection.class);
        when(correctionProjection.getStockingEventHistoryId()).thenReturn(1L);
        when(correctionProjection.getAmendmentNumber()).thenReturn(null);
        when(correctionProjection.getEventTimestamp()).thenReturn(LocalDateTime.of(2025, 7, 3, 11, 25));
        when(correctionProjection.getSuCount()).thenReturn(1);
        when(correctionProjection.getTotalNar()).thenReturn(10.0);
        when(correctionProjection.getAuditActionCode()).thenReturn("COR");
        when(correctionProjection.getAuditActionDescription()).thenReturn("Correction");
        when(correctionProjection.getEsfSubmissionId()).thenReturn(null);
        when(correctionProjection.getSubmittedByUserId()).thenReturn("IDIR\\TEST");
        when(correctionProjection.getApprovedByUserId()).thenReturn(null);

        when(openingRepository.getOpeningStandardUnitHistoryByOpeningId(openingId))
                .thenReturn(List.of(amendedProjection, correctionProjection));

        // When
        List<OpeningStockingHistoryOverviewDto> result =
                openingStandardUnitHistoryService.getStandardUnitOverviewHistoryList(openingId);

        // Then
        Assertions.assertEquals(2, result.size());

        // Verify Amended projection (first in result)
        OpeningStockingHistoryOverviewDto amendedDto = result.get(0);
        Assertions.assertEquals(2L, amendedDto.stockingEventHistoryId());
        Assertions.assertEquals(1, amendedDto.amendmentNumber());
        Assertions.assertTrue(amendedDto.eventTimestamp().isAfter(LocalDateTime.of(2025, 7, 3, 11, 45)));
        Assertions.assertEquals(2, amendedDto.suCount());
        Assertions.assertEquals(20.0, amendedDto.totalNar());
        Assertions.assertEquals("AMD", amendedDto.auditAction().code());
        Assertions.assertEquals("Amended", amendedDto.auditAction().description());
        Assertions.assertNull(amendedDto.esfSubmissionId());
        Assertions.assertEquals("IDIR\\TEST", amendedDto.submittedByUserId());
        Assertions.assertEquals("IDIR\\TEST", amendedDto.approvedByUserId());
        Assertions.assertTrue(amendedDto.isLatest());
        Assertions.assertFalse(amendedDto.isOldest());

        // Verify Correction projection (second in result)
        OpeningStockingHistoryOverviewDto correctionDto = result.get(1);
        Assertions.assertEquals(1L, correctionDto.stockingEventHistoryId());
        Assertions.assertNull(correctionDto.amendmentNumber());
        Assertions.assertTrue(correctionDto.eventTimestamp().isBefore(LocalDateTime.of(2025, 7, 3, 11, 30)));
        Assertions.assertEquals(1, correctionDto.suCount());
        Assertions.assertEquals(10.0, correctionDto.totalNar());
        Assertions.assertEquals("COR", correctionDto.auditAction().code());
        Assertions.assertEquals("Correction", correctionDto.auditAction().description());
        Assertions.assertNull(correctionDto.esfSubmissionId());
        Assertions.assertEquals("IDIR\\TEST", correctionDto.submittedByUserId());
        Assertions.assertNull(correctionDto.approvedByUserId());
        Assertions.assertFalse(correctionDto.isLatest());
        Assertions.assertTrue(correctionDto.isOldest());

        verify(openingRepository).getOpeningStandardUnitHistoryByOpeningId(openingId);
    }

    @Test
    @DisplayName("getStandardUnitHistoryDetails returns details for creating new stocking standard unit layers")
    void getStandardUnitHistoryDetails_creatingLayers_shouldReturnCorrectDetailsWithComparison() {
        // Given
        Long openingId = 123L;
        Long stockingEventHistoryId = 1L;
        Long stockingStandardUnitId = 2L;
        Long stockingLayerId = 2193916L;

        // Create standard unit projection
        OpeningStockingHistoryDetailsWithComparisonProjection
            suDetailsProjection = mock(OpeningStockingHistoryDetailsWithComparisonProjection.class);
        when(suDetailsProjection.getStockingStandardUnitId()).thenReturn(stockingStandardUnitId);
        when(suDetailsProjection.getStandardsUnitId()).thenReturn("1");
        when(suDetailsProjection.getOldNetArea()).thenReturn(null);
        when(suDetailsProjection.getNewNetArea()).thenReturn(10.0);
        when(suDetailsProjection.getOldMaxSoilDisturbance()).thenReturn(null);
        when(suDetailsProjection.getNewMaxSoilDisturbance()).thenReturn(5.0);
        when(suDetailsProjection.getOldVarianceIndicator()).thenReturn("false");
        when(suDetailsProjection.getNewVarianceIndicator()).thenReturn("false");
        when(suDetailsProjection.getOldRegenObligationIndicator()).thenReturn("false");
        when(suDetailsProjection.getNewRegenObligationIndicator()).thenReturn("true");
        when(suDetailsProjection.getOldNoRegenEarlyOffsetYears()).thenReturn(null);
        when(suDetailsProjection.getNewNoRegenEarlyOffsetYears()).thenReturn(null);
        when(suDetailsProjection.getOldNoRegenLateOffsetYears()).thenReturn(null);
        when(suDetailsProjection.getNewNoRegenLateOffsetYears()).thenReturn(null);
        when(suDetailsProjection.getOldRegenOffsetYears()).thenReturn(null);
        when(suDetailsProjection.getNewRegenOffsetYears()).thenReturn(6);
        when(suDetailsProjection.getOldFreeGrowingEarlyOffsetYears()).thenReturn(null);
        when(suDetailsProjection.getNewFreeGrowingEarlyOffsetYears()).thenReturn(null);
        when(suDetailsProjection.getOldFreeGrowingLateOffsetYears()).thenReturn(null);
        when(suDetailsProjection.getNewFreeGrowingLateOffsetYears()).thenReturn(20);
        when(suDetailsProjection.getOldBgcZone()).thenReturn(null);
        when(suDetailsProjection.getNewBgcZone()).thenReturn("CWH");
        when(suDetailsProjection.getOldBgcSubzone()).thenReturn(null);
        when(suDetailsProjection.getNewBgcSubzone()).thenReturn("vm");
        when(suDetailsProjection.getOldBgcVariant()).thenReturn(null);
        when(suDetailsProjection.getNewBgcVariant()).thenReturn("1");
        when(suDetailsProjection.getOldBecSiteSeries()).thenReturn(null);
        when(suDetailsProjection.getNewBecSiteSeries()).thenReturn("06");

        // Create layer projection
        OpeningStockingHistoryLayerWithComparisonProjection
            layerProjection = mock(OpeningStockingHistoryLayerWithComparisonProjection.class);
        when(layerProjection.getSsuId()).thenReturn(stockingStandardUnitId);
        when(layerProjection.getOldLayerId()).thenReturn(null);
        when(layerProjection.getNewLayerId()).thenReturn(stockingLayerId);
        when(layerProjection.getOldStockingLayerCode()).thenReturn(null);
        when(layerProjection.getNewStockingLayerCode()).thenReturn("I");
        when(layerProjection.getOldStockingLayerDescription()).thenReturn(null);
        when(layerProjection.getNewStockingLayerDescription()).thenReturn("Inventory Layer");
        when(layerProjection.getOldMinHorizontalDistance()).thenReturn(null);
        when(layerProjection.getNewMinHorizontalDistance()).thenReturn(1.0);
        when(layerProjection.getOldMinPerfStockingStandard()).thenReturn(null);
        when(layerProjection.getNewMinPerfStockingStandard()).thenReturn(700);
        when(layerProjection.getOldMinStockingStandard()).thenReturn(null);
        when(layerProjection.getNewMinStockingStandard()).thenReturn(400);
        when(layerProjection.getOldTargetWellSpacedTrees()).thenReturn(null);
        when(layerProjection.getNewTargetWellSpacedTrees()).thenReturn(1200);
        when(layerProjection.getOldHeightRelativeToComp()).thenReturn(null);
        when(layerProjection.getNewHeightRelativeToComp()).thenReturn(30);

        // Create species projection
        OpeningStockingHistoryLayerSpeciesWithComparisonProjection
            speciesProjection = mock(OpeningStockingHistoryLayerSpeciesWithComparisonProjection.class);
        when(speciesProjection.getNewStockingLayerId()).thenReturn(stockingLayerId);
        when(speciesProjection.getOldLayerCode()).thenReturn(null);
        when(speciesProjection.getNewLayerCode()).thenReturn("I");
        when(speciesProjection.getOldSpeciesCode()).thenReturn(null);
        when(speciesProjection.getNewSpeciesCode()).thenReturn("BA");
        when(speciesProjection.getOldSpeciesDescription()).thenReturn(null);
        when(speciesProjection.getNewSpeciesDescription()).thenReturn("amabilis fir");
        when(speciesProjection.getNewPreferredInd()).thenReturn(true);
        when(speciesProjection.getOldMinHeight()).thenReturn(null);
        when(speciesProjection.getNewMinHeight()).thenReturn(1.5);

        // Setup repository mocks
        when(openingRepository.getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId)).thenReturn(List.of(suDetailsProjection));
        when(openingRepository.getOpeningStandardUnitHistoryLayerDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId)).thenReturn(List.of(layerProjection));
        when(openingRepository.getOpeningStandardUnitHistoryLayerSpeciesDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId)).thenReturn(List.of(speciesProjection));

        // When
        List<OpeningStockingHistoryWithComparisonDto> result =
                openingStandardUnitHistoryService.getStandardUnitHistoryDetailsWithComparison(openingId, stockingEventHistoryId);

        // Then
        Assertions.assertEquals(1, result.size());

        // Verify standard unit details - both old and new values
        var standardUnit = result.get(0).standardUnit();
        // Old values (should be null for new stocking standards)
        Assertions.assertNull(standardUnit.oldNetArea());
        Assertions.assertNull(standardUnit.oldMaxSoilDisturbance());
        Assertions.assertEquals(false, Boolean.valueOf(standardUnit.oldVarianceIndicator()));
        Assertions.assertEquals(false, Boolean.valueOf(standardUnit.oldRegenObligationIndicator()));
        Assertions.assertNull(standardUnit.oldBgcZone());
        // New values
        Assertions.assertEquals(stockingStandardUnitId, standardUnit.stockingStandardUnitId());
        Assertions.assertEquals("1", standardUnit.standardsUnitId());
        Assertions.assertEquals(10.0, standardUnit.newNetArea());
        Assertions.assertEquals(5, standardUnit.newMaxSoilDisturbance());
        Assertions.assertEquals(false, Boolean.valueOf(standardUnit.newVarianceIndicator()));
        Assertions.assertEquals(true, Boolean.valueOf(standardUnit.newRegenObligationIndicator()));
        Assertions.assertEquals(Integer.valueOf(20), standardUnit.newFreeGrowingLateOffsetYears());
        Assertions.assertEquals("CWH", standardUnit.newBgcZone());
        Assertions.assertEquals("vm", standardUnit.newBgcSubzone());
        Assertions.assertEquals("1", standardUnit.newBgcVariant());
        Assertions.assertEquals("06", standardUnit.newBecSiteSeries());

        // Verify layer details - both old and new values
        Assertions.assertEquals(1, result.get(0).layers().size());
        var layer = result.get(0).layers().get(0);
        // Old values (should be null for new layers)
        Assertions.assertNull(layer.oldLayerId());
        Assertions.assertNull(layer.oldStockingLayer().code());
        Assertions.assertNull(layer.oldStockingLayer().description());
        Assertions.assertNull(layer.oldMinHorizontalDistance());
        Assertions.assertNull(layer.oldMinPerfStockingStandard());
        Assertions.assertNull(layer.oldMinStockingStandard());
        Assertions.assertNull(layer.oldTargetWellSpacedTrees());
        Assertions.assertNull(layer.oldHeightRelativeToComp());
        // New values
        Assertions.assertEquals(stockingLayerId, layer.newLayerId());
        Assertions.assertEquals("I", layer.newStockingLayer().code());
        Assertions.assertEquals("Inventory Layer", layer.newStockingLayer().description());
        Assertions.assertEquals(Double.valueOf(1.0), layer.newMinHorizontalDistance());
        Assertions.assertEquals(Integer.valueOf(700), layer.newMinPerfStockingStandard());
        Assertions.assertEquals(Integer.valueOf(400), layer.newMinStockingStandard());
        Assertions.assertEquals(Integer.valueOf(1200), layer.newTargetWellSpacedTrees());
        Assertions.assertEquals(Integer.valueOf(30), layer.newHeightRelativeToComp());

        // Verify species details - both old and new values
        Assertions.assertEquals(1, layer.preferredSpecies().size());
        Assertions.assertEquals(0, layer.acceptableSpecies().size());

        var species = layer.preferredSpecies().get(0);
        // Old values (should be null for new species)
        Assertions.assertNull(species.oldLayerCode());
        Assertions.assertNull(species.oldSpecies().code());
        Assertions.assertNull(species.oldSpecies().description());
        Assertions.assertNull(species.oldMinHeight());
        // New values
        Assertions.assertEquals("I", species.newLayerCode());
        Assertions.assertEquals("BA", species.newSpecies().code());
        Assertions.assertEquals("amabilis fir", species.newSpecies().description());
        Assertions.assertEquals(1.5, species.newMinHeight());

        // Verify repository calls
        verify(openingRepository).getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId);
        verify(openingRepository).getOpeningStandardUnitHistoryLayerDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId);
        verify(openingRepository).getOpeningStandardUnitHistoryLayerSpeciesDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId);
    }

    @Test
    @DisplayName("getStandardUnitHistoryDetails returns details for multi-layer conversion scenario")
    void getStandardUnitHistoryDetails_multiLayerConversion_shouldReturnCorrectDetailsWithComparison() {
        // Given
        Long openingId = 123L;
        Long stockingEventHistoryId = 1L;
        Long standardUnitId1 = 1L;
        Long standardUnitId2 = 2L;
        Long originalLayerId1 = 1L;
        Long originalLayerId2 = 2L;
        Long newLayerId3 = 3L;
        Long newLayerId4 = 4L;
        Long newLayerId5 = 5L;

        // Create standard unit projections
        OpeningStockingHistoryDetailsWithComparisonProjection
            suDetailsProjection1 = mock(OpeningStockingHistoryDetailsWithComparisonProjection.class);
        //when(suDetailsProjection1.getStockingEventHistoryId()).thenReturn(stockingEventHistoryId);
        when(suDetailsProjection1.getStockingStandardUnitId()).thenReturn(standardUnitId1);
        when(suDetailsProjection1.getStandardsUnitId()).thenReturn("1");
        when(suDetailsProjection1.getOldNetArea()).thenReturn(12.0);
        when(suDetailsProjection1.getNewNetArea()).thenReturn(12.0);
        when(suDetailsProjection1.getOldMaxSoilDisturbance()).thenReturn(5.0);
        when(suDetailsProjection1.getNewMaxSoilDisturbance()).thenReturn(5.0);
        when(suDetailsProjection1.getOldVarianceIndicator()).thenReturn("true");
        when(suDetailsProjection1.getNewVarianceIndicator()).thenReturn("true");
        when(suDetailsProjection1.getOldRegenObligationIndicator()).thenReturn("false");
        when(suDetailsProjection1.getNewRegenObligationIndicator()).thenReturn("false");
        when(suDetailsProjection1.getOldNoRegenEarlyOffsetYears()).thenReturn(1);
        when(suDetailsProjection1.getNewNoRegenEarlyOffsetYears()).thenReturn(1);
        when(suDetailsProjection1.getOldNoRegenLateOffsetYears()).thenReturn(2);
        when(suDetailsProjection1.getNewNoRegenLateOffsetYears()).thenReturn(2);
        when(suDetailsProjection1.getOldBgcZone()).thenReturn("CWH");
        when(suDetailsProjection1.getNewBgcZone()).thenReturn("CWH");
        when(suDetailsProjection1.getOldBgcSubzone()).thenReturn("xm");
        when(suDetailsProjection1.getNewBgcSubzone()).thenReturn("xm");
        when(suDetailsProjection1.getOldBgcVariant()).thenReturn("1");
        when(suDetailsProjection1.getNewBgcVariant()).thenReturn("1");
        when(suDetailsProjection1.getOldBecSiteSeries()).thenReturn("03");
        when(suDetailsProjection1.getNewBecSiteSeries()).thenReturn("03");

        OpeningStockingHistoryDetailsWithComparisonProjection
            suDetailsProjection2 = mock(OpeningStockingHistoryDetailsWithComparisonProjection.class);
        //when(suDetailsProjection2.getStockingEventHistoryId()).thenReturn(stockingEventHistoryId);
        when(suDetailsProjection2.getStockingStandardUnitId()).thenReturn(standardUnitId2);
        when(suDetailsProjection2.getStandardsUnitId()).thenReturn("2");
        when(suDetailsProjection2.getOldRegimeId()).thenReturn(3L);
        when(suDetailsProjection2.getNewRegimeId()).thenReturn(3L);
        when(suDetailsProjection2.getOldNetArea()).thenReturn(10.0);
        when(suDetailsProjection2.getNewNetArea()).thenReturn(10.0);
        when(suDetailsProjection2.getOldMaxSoilDisturbance()).thenReturn(5.0);
        when(suDetailsProjection2.getNewMaxSoilDisturbance()).thenReturn(5.0);
        when(suDetailsProjection2.getOldVarianceIndicator()).thenReturn("true");
        when(suDetailsProjection2.getNewVarianceIndicator()).thenReturn("true");
        when(suDetailsProjection2.getOldRegenObligationIndicator()).thenReturn("true");
        when(suDetailsProjection2.getNewRegenObligationIndicator()).thenReturn("true");
        when(suDetailsProjection2.getOldRegenOffsetYears()).thenReturn(6);
        when(suDetailsProjection2.getNewRegenOffsetYears()).thenReturn(6);
        when(suDetailsProjection2.getOldFreeGrowingEarlyOffsetYears()).thenReturn(11);
        when(suDetailsProjection2.getNewFreeGrowingEarlyOffsetYears()).thenReturn(11);
        when(suDetailsProjection2.getOldFreeGrowingLateOffsetYears()).thenReturn(14);
        when(suDetailsProjection2.getNewFreeGrowingLateOffsetYears()).thenReturn(14);
        when(suDetailsProjection2.getOldBgcZone()).thenReturn("CWH");
        when(suDetailsProjection2.getNewBgcZone()).thenReturn("CWH");
        when(suDetailsProjection2.getOldBgcSubzone()).thenReturn("vm");
        when(suDetailsProjection2.getNewBgcSubzone()).thenReturn("vm");
        when(suDetailsProjection2.getOldBgcVariant()).thenReturn("1");
        when(suDetailsProjection2.getNewBgcVariant()).thenReturn("1");
        when(suDetailsProjection2.getOldBecSiteSeries()).thenReturn("01");
        when(suDetailsProjection2.getNewBecSiteSeries()).thenReturn("01");

        // Create layer projections
        // SU1: Original layer converted to new layer code
        OpeningStockingHistoryLayerWithComparisonProjection
            layerProjection1 = mock(OpeningStockingHistoryLayerWithComparisonProjection.class);
        //when(layerProjection1.getStockingEventHistoryId()).thenReturn(stockingEventHistoryId);
        when(layerProjection1.getSsuId()).thenReturn(standardUnitId1);
        when(layerProjection1.getOldLayerId()).thenReturn(originalLayerId1);
        when(layerProjection1.getNewLayerId()).thenReturn(originalLayerId1);
        when(layerProjection1.getOldStockingLayerCode()).thenReturn("I");
        when(layerProjection1.getNewStockingLayerCode()).thenReturn("4");
        when(layerProjection1.getOldStockingLayerDescription()).thenReturn("Inventory Layer");
        when(layerProjection1.getNewStockingLayerDescription()).thenReturn("Regen");
        when(layerProjection1.getOldMinHorizontalDistance()).thenReturn(1.5);
        when(layerProjection1.getNewMinHorizontalDistance()).thenReturn(1.5);
        when(layerProjection1.getOldMinPerfStockingStandard()).thenReturn(700);
        when(layerProjection1.getNewMinPerfStockingStandard()).thenReturn(700);
        when(layerProjection1.getOldMinStockingStandard()).thenReturn(400);
        when(layerProjection1.getNewMinStockingStandard()).thenReturn(400);
        when(layerProjection1.getOldTargetWellSpacedTrees()).thenReturn(900);
        when(layerProjection1.getNewTargetWellSpacedTrees()).thenReturn(900);
        when(layerProjection1.getOldHeightRelativeToComp()).thenReturn(30);
        when(layerProjection1.getNewHeightRelativeToComp()).thenReturn(30);

        // SU2: Unchanged layer
        OpeningStockingHistoryLayerWithComparisonProjection
            layerProjection2 = mock(OpeningStockingHistoryLayerWithComparisonProjection.class);
        //when(layerProjection2.getStockingEventHistoryId()).thenReturn(stockingEventHistoryId);
        when(layerProjection2.getSsuId()).thenReturn(standardUnitId2);
        when(layerProjection2.getOldLayerId()).thenReturn(originalLayerId2);
        when(layerProjection2.getNewLayerId()).thenReturn(originalLayerId2);
        when(layerProjection2.getOldStockingLayerCode()).thenReturn("I");
        when(layerProjection2.getNewStockingLayerCode()).thenReturn("I");
        when(layerProjection2.getOldStockingLayerDescription()).thenReturn("Inventory Layer");
        when(layerProjection2.getNewStockingLayerDescription()).thenReturn("Inventory Layer");
        when(layerProjection2.getOldMinHorizontalDistance()).thenReturn(2.0);
        when(layerProjection2.getNewMinHorizontalDistance()).thenReturn(2.0);
        when(layerProjection2.getOldMinPerfStockingStandard()).thenReturn(400);
        when(layerProjection2.getNewMinPerfStockingStandard()).thenReturn(400);
        when(layerProjection2.getOldMinStockingStandard()).thenReturn(500);
        when(layerProjection2.getNewMinStockingStandard()).thenReturn(500);
        when(layerProjection2.getOldTargetWellSpacedTrees()).thenReturn(900);
        when(layerProjection2.getNewTargetWellSpacedTrees()).thenReturn(900);
        when(layerProjection2.getOldHeightRelativeToComp()).thenReturn(150);
        when(layerProjection2.getNewHeightRelativeToComp()).thenReturn(150);

        // SU1: New layer 1 - Mature
        OpeningStockingHistoryLayerWithComparisonProjection
            layerProjection3 = mock(OpeningStockingHistoryLayerWithComparisonProjection.class);
        //when(layerProjection3.getStockingEventHistoryId()).thenReturn(stockingEventHistoryId);
        when(layerProjection3.getSsuId()).thenReturn(standardUnitId1);
        when(layerProjection3.getOldLayerId()).thenReturn(null);
        when(layerProjection3.getNewLayerId()).thenReturn(newLayerId3);
        when(layerProjection3.getOldStockingLayerCode()).thenReturn(null);
        when(layerProjection3.getNewStockingLayerCode()).thenReturn("1");
        when(layerProjection3.getOldStockingLayerDescription()).thenReturn(null);
        when(layerProjection3.getNewStockingLayerDescription()).thenReturn("Mature");
        when(layerProjection3.getNewResidualBasalArea()).thenReturn(20.0);

        // SU1: New layer 2 - Pole
        OpeningStockingHistoryLayerWithComparisonProjection
            layerProjection4 = mock(OpeningStockingHistoryLayerWithComparisonProjection.class);
        //when(layerProjection4.getStockingEventHistoryId()).thenReturn(stockingEventHistoryId);
        when(layerProjection4.getSsuId()).thenReturn(standardUnitId1);
        when(layerProjection4.getOldLayerId()).thenReturn(null);
        when(layerProjection4.getNewLayerId()).thenReturn(newLayerId4);
        when(layerProjection4.getOldStockingLayerCode()).thenReturn(null);
        when(layerProjection4.getNewStockingLayerCode()).thenReturn("2");
        when(layerProjection4.getOldStockingLayerDescription()).thenReturn(null);
        when(layerProjection4.getNewStockingLayerDescription()).thenReturn("Pole");
        when(layerProjection4.getNewResidualBasalArea()).thenReturn(15.0);

        // SU1: New layer 3 - Sapling
        OpeningStockingHistoryLayerWithComparisonProjection
            layerProjection5 = mock(OpeningStockingHistoryLayerWithComparisonProjection.class);
        //when(layerProjection5.getStockingEventHistoryId()).thenReturn(stockingEventHistoryId);
        when(layerProjection5.getSsuId()).thenReturn(standardUnitId1);
        when(layerProjection5.getOldLayerId()).thenReturn(null);
        when(layerProjection5.getNewLayerId()).thenReturn(newLayerId5);
        when(layerProjection5.getOldStockingLayerCode()).thenReturn(null);
        when(layerProjection5.getNewStockingLayerCode()).thenReturn("3");
        when(layerProjection5.getOldStockingLayerDescription()).thenReturn(null);
        when(layerProjection5.getNewStockingLayerDescription()).thenReturn("Sapling");
        when(layerProjection5.getNewMinPerfStockingStandard()).thenReturn(600);
        when(layerProjection5.getNewMinStockingStandard()).thenReturn(400);
        when(layerProjection5.getNewTargetWellSpacedTrees()).thenReturn(900);

        // Create species projections
        // SU1: Layer 1 - Updated species
        OpeningStockingHistoryLayerSpeciesWithComparisonProjection speciesProjection1 = mock(
            OpeningStockingHistoryLayerSpeciesWithComparisonProjection.class);
        //when(speciesProjection1.getSsuId()).thenReturn(standardUnitId1);
        //when(speciesProjection1.getOldStockingLayerId()).thenReturn(originalLayerId1);
        when(speciesProjection1.getNewStockingLayerId()).thenReturn(originalLayerId1);
        when(speciesProjection1.getOldLayerCode()).thenReturn("I");
        when(speciesProjection1.getNewLayerCode()).thenReturn("4");
        when(speciesProjection1.getOldSpeciesCode()).thenReturn("AT");
        when(speciesProjection1.getNewSpeciesCode()).thenReturn("AT");
        when(speciesProjection1.getOldSpeciesDescription()).thenReturn("trembling aspen");
        when(speciesProjection1.getNewSpeciesDescription()).thenReturn("trembling aspen");
        //when(speciesProjection1.getOldPreferredInd()).thenReturn(true);
        when(speciesProjection1.getNewPreferredInd()).thenReturn(true);
        when(speciesProjection1.getOldMinHeight()).thenReturn(2.0);
        when(speciesProjection1.getNewMinHeight()).thenReturn(2.0);

        OpeningStockingHistoryLayerSpeciesWithComparisonProjection speciesProjection2 = mock(
            OpeningStockingHistoryLayerSpeciesWithComparisonProjection.class);
        //when(speciesProjection2.getSsuId()).thenReturn(standardUnitId1);
        //when(speciesProjection2.getOldStockingLayerId()).thenReturn(originalLayerId1);
        when(speciesProjection2.getNewStockingLayerId()).thenReturn(originalLayerId1);
        when(speciesProjection2.getOldLayerCode()).thenReturn("I");
        when(speciesProjection2.getNewLayerCode()).thenReturn("4");
        when(speciesProjection2.getOldSpeciesCode()).thenReturn("BA");
        when(speciesProjection2.getNewSpeciesCode()).thenReturn("BA");
        when(speciesProjection2.getOldSpeciesDescription()).thenReturn("amabilis fir");
        when(speciesProjection2.getNewSpeciesDescription()).thenReturn("amabilis fir");
        //when(speciesProjection2.getOldPreferredInd()).thenReturn(true);
        when(speciesProjection2.getNewPreferredInd()).thenReturn(true);
        when(speciesProjection2.getOldMinHeight()).thenReturn(1.5);
        when(speciesProjection2.getNewMinHeight()).thenReturn(1.5);

        OpeningStockingHistoryLayerSpeciesWithComparisonProjection speciesProjection3 = mock(
            OpeningStockingHistoryLayerSpeciesWithComparisonProjection.class);
        //when(speciesProjection3.getSsuId()).thenReturn(standardUnitId1);
        //when(speciesProjection3.getOldStockingLayerId()).thenReturn(originalLayerId1);
        when(speciesProjection3.getNewStockingLayerId()).thenReturn(originalLayerId1);
        when(speciesProjection3.getOldLayerCode()).thenReturn("I");
        when(speciesProjection3.getNewLayerCode()).thenReturn("4");
        when(speciesProjection3.getOldSpeciesCode()).thenReturn("BG");
        when(speciesProjection3.getNewSpeciesCode()).thenReturn("BG");
        when(speciesProjection3.getOldSpeciesDescription()).thenReturn("grand fir");
        when(speciesProjection3.getNewSpeciesDescription()).thenReturn("grand fir");
        //when(speciesProjection3.getOldPreferredInd()).thenReturn(false);
        when(speciesProjection3.getNewPreferredInd()).thenReturn(false);
        when(speciesProjection3.getOldMinHeight()).thenReturn(1.8);
        when(speciesProjection3.getNewMinHeight()).thenReturn(1.8);

        // SU1: New Layer 5 - New species
        OpeningStockingHistoryLayerSpeciesWithComparisonProjection speciesProjection4 = mock(
            OpeningStockingHistoryLayerSpeciesWithComparisonProjection.class);
        //when(speciesProjection4.getSsuId()).thenReturn(standardUnitId1);
        //when(speciesProjection4.getOldStockingLayerId()).thenReturn(null);
        when(speciesProjection4.getNewStockingLayerId()).thenReturn(newLayerId5);
        when(speciesProjection4.getOldLayerCode()).thenReturn(null);
        when(speciesProjection4.getNewLayerCode()).thenReturn("3");
        when(speciesProjection4.getOldSpeciesCode()).thenReturn(null);
        when(speciesProjection4.getNewSpeciesCode()).thenReturn("AC");
        when(speciesProjection4.getOldSpeciesDescription()).thenReturn(null);
        when(speciesProjection4.getNewSpeciesDescription()).thenReturn("poplar");
        //when(speciesProjection4.getOldPreferredInd()).thenReturn(false);
        when(speciesProjection4.getNewPreferredInd()).thenReturn(true);
        when(speciesProjection4.getOldMinHeight()).thenReturn(null);
        when(speciesProjection4.getNewMinHeight()).thenReturn(null);

        // SU1: New Layer 4 - New species
        OpeningStockingHistoryLayerSpeciesWithComparisonProjection speciesProjection5 = mock(
            OpeningStockingHistoryLayerSpeciesWithComparisonProjection.class);
        //when(speciesProjection5.getSsuId()).thenReturn(standardUnitId1);
        //when(speciesProjection5.getOldStockingLayerId()).thenReturn(null);
        when(speciesProjection5.getNewStockingLayerId()).thenReturn(newLayerId4);
        when(speciesProjection5.getOldLayerCode()).thenReturn(null);
        when(speciesProjection5.getNewLayerCode()).thenReturn("2");
        when(speciesProjection5.getOldSpeciesCode()).thenReturn(null);
        when(speciesProjection5.getNewSpeciesCode()).thenReturn("DR");
        when(speciesProjection5.getOldSpeciesDescription()).thenReturn(null);
        when(speciesProjection5.getNewSpeciesDescription()).thenReturn("red alder");
        //when(speciesProjection5.getOldPreferredInd()).thenReturn(false);
        when(speciesProjection5.getNewPreferredInd()).thenReturn(true);
        when(speciesProjection5.getOldMinHeight()).thenReturn(null);
        when(speciesProjection5.getNewMinHeight()).thenReturn(null);

        // SU1: New Layer 3 - New species
        OpeningStockingHistoryLayerSpeciesWithComparisonProjection speciesProjection6 = mock(
            OpeningStockingHistoryLayerSpeciesWithComparisonProjection.class);
        //when(speciesProjection6.getSsuId()).thenReturn(standardUnitId1);
        //when(speciesProjection6.getOldStockingLayerId()).thenReturn(null);
        when(speciesProjection6.getNewStockingLayerId()).thenReturn(newLayerId3);
        when(speciesProjection6.getOldLayerCode()).thenReturn(null);
        when(speciesProjection6.getNewLayerCode()).thenReturn("1");
        when(speciesProjection6.getOldSpeciesCode()).thenReturn(null);
        when(speciesProjection6.getNewSpeciesCode()).thenReturn("FDI");
        when(speciesProjection6.getOldSpeciesDescription()).thenReturn(null);
        when(speciesProjection6.getNewSpeciesDescription()).thenReturn("interior Douglas-fir");
        //when(speciesProjection6.getOldPreferredInd()).thenReturn(false);
        when(speciesProjection6.getNewPreferredInd()).thenReturn(true);
        when(speciesProjection6.getOldMinHeight()).thenReturn(null);
        when(speciesProjection6.getNewMinHeight()).thenReturn(null);

        // SU2: Unchanged species
        OpeningStockingHistoryLayerSpeciesWithComparisonProjection speciesProjection7 = mock(
            OpeningStockingHistoryLayerSpeciesWithComparisonProjection.class);
        //when(speciesProjection7.getSsuId()).thenReturn(standardUnitId2);
        //when(speciesProjection7.getOldStockingLayerId()).thenReturn(originalLayerId2);
        when(speciesProjection7.getNewStockingLayerId()).thenReturn(originalLayerId2);
        when(speciesProjection7.getOldLayerCode()).thenReturn("I");
        when(speciesProjection7.getNewLayerCode()).thenReturn("I");
        when(speciesProjection7.getOldSpeciesCode()).thenReturn("BA");
        when(speciesProjection7.getNewSpeciesCode()).thenReturn("BA");
        when(speciesProjection7.getOldSpeciesDescription()).thenReturn("amabilis fir");
        when(speciesProjection7.getNewSpeciesDescription()).thenReturn("amabilis fir");
        // when(speciesProjection7.getOldPreferredInd()).thenReturn(true);
        when(speciesProjection7.getNewPreferredInd()).thenReturn(true);
        when(speciesProjection7.getOldMinHeight()).thenReturn(1.4);
        when(speciesProjection7.getNewMinHeight()).thenReturn(1.4);

        OpeningStockingHistoryLayerSpeciesWithComparisonProjection speciesProjection8 = mock(
            OpeningStockingHistoryLayerSpeciesWithComparisonProjection.class);
        // when(speciesProjection8.getSsuId()).thenReturn(standardUnitId2);
        // when(speciesProjection8.getOldStockingLayerId()).thenReturn(originalLayerId2);
        when(speciesProjection8.getNewStockingLayerId()).thenReturn(originalLayerId2);
        when(speciesProjection8.getOldLayerCode()).thenReturn("I");
        when(speciesProjection8.getNewLayerCode()).thenReturn("I");
        when(speciesProjection8.getOldSpeciesCode()).thenReturn("CW");
        when(speciesProjection8.getNewSpeciesCode()).thenReturn("CW");
        when(speciesProjection8.getOldSpeciesDescription()).thenReturn("western redcedar");
        when(speciesProjection8.getNewSpeciesDescription()).thenReturn("western redcedar");
        // when(speciesProjection8.getOldPreferredInd()).thenReturn(true);
        when(speciesProjection8.getNewPreferredInd()).thenReturn(true);
        when(speciesProjection8.getOldMinHeight()).thenReturn(1.5);
        when(speciesProjection8.getNewMinHeight()).thenReturn(1.5);

        OpeningStockingHistoryLayerSpeciesWithComparisonProjection speciesProjection9 = mock(
            OpeningStockingHistoryLayerSpeciesWithComparisonProjection.class);
        // when(speciesProjection9.getSsuId()).thenReturn(standardUnitId2);
        // when(speciesProjection9.getOldStockingLayerId()).thenReturn(originalLayerId2);
        when(speciesProjection9.getNewStockingLayerId()).thenReturn(originalLayerId2);
        when(speciesProjection9.getOldLayerCode()).thenReturn("I");
        when(speciesProjection9.getNewLayerCode()).thenReturn("I");
        when(speciesProjection9.getOldSpeciesCode()).thenReturn("HW");
        when(speciesProjection9.getNewSpeciesCode()).thenReturn("HW");
        when(speciesProjection9.getOldSpeciesDescription()).thenReturn("western hemlock");
        when(speciesProjection9.getNewSpeciesDescription()).thenReturn("western hemlock");
        // when(speciesProjection9.getOldPreferredInd()).thenReturn(true);
        when(speciesProjection9.getNewPreferredInd()).thenReturn(true);
        when(speciesProjection9.getOldMinHeight()).thenReturn(2.0);
        when(speciesProjection9.getNewMinHeight()).thenReturn(2.0);

        OpeningStockingHistoryLayerSpeciesWithComparisonProjection speciesProjection10 = mock(
            OpeningStockingHistoryLayerSpeciesWithComparisonProjection.class);
        // when(speciesProjection10.getSsuId()).thenReturn(standardUnitId2);
        // when(speciesProjection10.getOldStockingLayerId()).thenReturn(originalLayerId2);
        when(speciesProjection10.getNewStockingLayerId()).thenReturn(originalLayerId2);
        when(speciesProjection10.getOldLayerCode()).thenReturn("I");
        when(speciesProjection10.getNewLayerCode()).thenReturn("I");
        when(speciesProjection10.getOldSpeciesCode()).thenReturn("SS");
        when(speciesProjection10.getNewSpeciesCode()).thenReturn("SS");
        when(speciesProjection10.getOldSpeciesDescription()).thenReturn("Sitka spruce");
        when(speciesProjection10.getNewSpeciesDescription()).thenReturn("Sitka spruce");
        // when(speciesProjection10.getOldPreferredInd()).thenReturn(false);
        when(speciesProjection10.getNewPreferredInd()).thenReturn(false);
        when(speciesProjection10.getOldMinHeight()).thenReturn(2.0);
        when(speciesProjection10.getNewMinHeight()).thenReturn(2.0);

        // Setup repository mocks
        when(openingRepository.getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId)).thenReturn(List.of(suDetailsProjection1, suDetailsProjection2));
        when(openingRepository.getOpeningStandardUnitHistoryLayerDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId)).thenReturn(List.of(
                layerProjection1, layerProjection2, layerProjection3, layerProjection4, layerProjection5));
        when(openingRepository.getOpeningStandardUnitHistoryLayerSpeciesDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId)).thenReturn(List.of(
                speciesProjection1, speciesProjection2, speciesProjection3, speciesProjection4,
                speciesProjection5, speciesProjection6, speciesProjection7, speciesProjection8,
                speciesProjection9, speciesProjection10));

        // When
        List<OpeningStockingHistoryWithComparisonDto> result =
                openingStandardUnitHistoryService.getStandardUnitHistoryDetailsWithComparison(openingId, stockingEventHistoryId);

        // Then
        Assertions.assertEquals(2, result.size());

        // Verify first standard unit - converted from single to multi-layer
        var standardUnit1 = result.stream()
                .filter(su -> su.standardUnit().stockingStandardUnitId().equals(standardUnitId1))
                .findFirst()
                .orElseThrow();

        // Check standard unit details
        Assertions.assertEquals("1", standardUnit1.standardUnit().standardsUnitId());
        Assertions.assertEquals(12.0, standardUnit1.standardUnit().oldNetArea());
        Assertions.assertEquals(12.0, standardUnit1.standardUnit().newNetArea());
        Assertions.assertEquals(5.0, standardUnit1.standardUnit().oldMaxSoilDisturbance());
        Assertions.assertEquals(5.0, standardUnit1.standardUnit().newMaxSoilDisturbance());
        Assertions.assertTrue(Boolean.parseBoolean(standardUnit1.standardUnit().oldVarianceIndicator()));
        Assertions.assertTrue(Boolean.parseBoolean(standardUnit1.standardUnit().newVarianceIndicator()));
        Assertions.assertFalse(Boolean.parseBoolean(standardUnit1.standardUnit().oldRegenObligationIndicator()));
        Assertions.assertFalse(Boolean.parseBoolean(standardUnit1.standardUnit().newRegenObligationIndicator()));
        Assertions.assertEquals(Integer.valueOf(1), standardUnit1.standardUnit().oldNoRegenEarlyOffsetYears());
        Assertions.assertEquals(Integer.valueOf(1), standardUnit1.standardUnit().newNoRegenEarlyOffsetYears());
        Assertions.assertEquals(Integer.valueOf(2), standardUnit1.standardUnit().oldNoRegenLateOffsetYears());
        Assertions.assertEquals(Integer.valueOf(2), standardUnit1.standardUnit().newNoRegenLateOffsetYears());
        Assertions.assertEquals("CWH", standardUnit1.standardUnit().oldBgcZone());
        Assertions.assertEquals("CWH", standardUnit1.standardUnit().newBgcZone());
        Assertions.assertEquals("xm", standardUnit1.standardUnit().oldBgcSubzone());
        Assertions.assertEquals("xm", standardUnit1.standardUnit().newBgcSubzone());
        Assertions.assertEquals("03", standardUnit1.standardUnit().oldBecSiteSeries());
        Assertions.assertEquals("03", standardUnit1.standardUnit().newBecSiteSeries());

        // Check layers
        Assertions.assertEquals(4, standardUnit1.layers().size());

        // Find and check original layer (now with code '4')
        var originalLayer = standardUnit1.layers().stream()
                .filter(l -> l.oldLayerId() != null && l.oldLayerId().equals(originalLayerId1))
                .findFirst()
                .orElseThrow();

        Assertions.assertEquals(originalLayerId1, originalLayer.oldLayerId());
        Assertions.assertEquals(originalLayerId1, originalLayer.newLayerId());
        Assertions.assertEquals("I", originalLayer.oldStockingLayer().code());
        Assertions.assertEquals("4", originalLayer.newStockingLayer().code());
        Assertions.assertEquals("Inventory Layer", originalLayer.oldStockingLayer().description());
        Assertions.assertEquals("Regen", originalLayer.newStockingLayer().description());
        Assertions.assertEquals(Double.valueOf(1.5), originalLayer.oldMinHorizontalDistance());
        Assertions.assertEquals(Double.valueOf(1.5), originalLayer.newMinHorizontalDistance());
        Assertions.assertEquals(Integer.valueOf(700), originalLayer.oldMinPerfStockingStandard());
        Assertions.assertEquals(Integer.valueOf(700), originalLayer.newMinPerfStockingStandard());
        Assertions.assertEquals(Integer.valueOf(400), originalLayer.oldMinStockingStandard());
        Assertions.assertEquals(Integer.valueOf(400), originalLayer.newMinStockingStandard());
        Assertions.assertEquals(Integer.valueOf(900), originalLayer.oldTargetWellSpacedTrees());
        Assertions.assertEquals(Integer.valueOf(900), originalLayer.newTargetWellSpacedTrees());

        // Check original layer species
        Assertions.assertEquals(2, originalLayer.preferredSpecies().size());
        Assertions.assertEquals(1, originalLayer.acceptableSpecies().size());

        var tremAsp = originalLayer.preferredSpecies().stream()
                .filter(s -> s.newSpecies().code().equals("AT"))
                .findFirst().orElseThrow();
        Assertions.assertEquals("I", tremAsp.oldLayerCode());
        Assertions.assertEquals("4", tremAsp.newLayerCode());
        Assertions.assertEquals("AT", tremAsp.oldSpecies().code());
        Assertions.assertEquals("AT", tremAsp.newSpecies().code());
        Assertions.assertEquals(2.0, tremAsp.oldMinHeight());
        Assertions.assertEquals(2.0, tremAsp.newMinHeight());

        var amaFir = originalLayer.preferredSpecies().stream()
                .filter(s -> s.newSpecies().code().equals("BA"))
                .findFirst().orElseThrow();
        Assertions.assertEquals("I", amaFir.oldLayerCode());
        Assertions.assertEquals("4", amaFir.newLayerCode());
        Assertions.assertEquals("BA", amaFir.oldSpecies().code());
        Assertions.assertEquals("BA", amaFir.newSpecies().code());
        Assertions.assertEquals(1.5, amaFir.oldMinHeight());
        Assertions.assertEquals(1.5, amaFir.newMinHeight());

        var grandFir = originalLayer.acceptableSpecies().stream()
                .filter(s -> s.newSpecies().code().equals("BG"))
                .findFirst().orElseThrow();
        Assertions.assertEquals("I", grandFir.oldLayerCode());
        Assertions.assertEquals("4", grandFir.newLayerCode());
        Assertions.assertEquals("BG", grandFir.oldSpecies().code());
        Assertions.assertEquals("BG", grandFir.newSpecies().code());
        Assertions.assertEquals(1.8, grandFir.oldMinHeight());
        Assertions.assertEquals(1.8, grandFir.newMinHeight());

        // Find and check new layers
        var matureLayer = standardUnit1.layers().stream()
                .filter(l -> l.newLayerId() != null && l.newLayerId().equals(newLayerId3))
                .findFirst()
                .orElseThrow();
        Assertions.assertNull(matureLayer.oldLayerId());
        Assertions.assertEquals(newLayerId3, matureLayer.newLayerId());
        Assertions.assertNull(matureLayer.oldStockingLayer().code());
        Assertions.assertEquals("1", matureLayer.newStockingLayer().code());
        Assertions.assertNull(matureLayer.oldStockingLayer().description());
        Assertions.assertEquals("Mature", matureLayer.newStockingLayer().description());
        Assertions.assertEquals(20.0, matureLayer.newResidualBasalArea());

        // Check species in mature layer
        Assertions.assertEquals(1, matureLayer.preferredSpecies().size());
        var fdSpecies = matureLayer.preferredSpecies().get(0);
        Assertions.assertNull(fdSpecies.oldLayerCode());
        Assertions.assertEquals("1", fdSpecies.newLayerCode());
        Assertions.assertNull(fdSpecies.oldSpecies().code());
        Assertions.assertEquals("FDI", fdSpecies.newSpecies().code());
        Assertions.assertNull(fdSpecies.oldSpecies().description());
        Assertions.assertEquals("interior Douglas-fir", fdSpecies.newSpecies().description());

        var poleLayer = standardUnit1.layers().stream()
                .filter(l -> l.newLayerId() != null && l.newLayerId().equals(newLayerId4))
                .findFirst()
                .orElseThrow();
        Assertions.assertNull(poleLayer.oldLayerId());
        Assertions.assertEquals(newLayerId4, poleLayer.newLayerId());
        Assertions.assertNull(poleLayer.oldStockingLayer().code());
        Assertions.assertEquals("2", poleLayer.newStockingLayer().code());
        Assertions.assertNull(poleLayer.oldStockingLayer().description());
        Assertions.assertEquals("Pole", poleLayer.newStockingLayer().description());
        Assertions.assertEquals(15.0, poleLayer.newResidualBasalArea());

        // Check species in pole layer
        Assertions.assertEquals(1, poleLayer.preferredSpecies().size());
        var drSpecies = poleLayer.preferredSpecies().get(0);
        Assertions.assertNull(drSpecies.oldLayerCode());
        Assertions.assertEquals("2", drSpecies.newLayerCode());
        Assertions.assertNull(drSpecies.oldSpecies().code());
        Assertions.assertEquals("DR", drSpecies.newSpecies().code());
        Assertions.assertNull(drSpecies.oldSpecies().description());
        Assertions.assertEquals("red alder", drSpecies.newSpecies().description());

        var saplingLayer = standardUnit1.layers().stream()
                .filter(l -> l.newLayerId() != null && l.newLayerId().equals(newLayerId5))
                .findFirst()
                .orElseThrow();
        Assertions.assertNull(saplingLayer.oldLayerId());
        Assertions.assertEquals(newLayerId5, saplingLayer.newLayerId());
        Assertions.assertNull(saplingLayer.oldStockingLayer().code());
        Assertions.assertEquals("3", saplingLayer.newStockingLayer().code());
        Assertions.assertNull(saplingLayer.oldStockingLayer().description());
        Assertions.assertEquals("Sapling", saplingLayer.newStockingLayer().description());
        Assertions.assertEquals(Integer.valueOf(600), saplingLayer.newMinPerfStockingStandard());
        Assertions.assertEquals(Integer.valueOf(400), saplingLayer.newMinStockingStandard());
        Assertions.assertEquals(Integer.valueOf(900), saplingLayer.newTargetWellSpacedTrees());

        // Check species in sapling layer
        Assertions.assertEquals(1, saplingLayer.preferredSpecies().size());
        var acSpecies = saplingLayer.preferredSpecies().get(0);
        Assertions.assertNull(acSpecies.oldLayerCode());
        Assertions.assertEquals("3", acSpecies.newLayerCode());
        Assertions.assertNull(acSpecies.oldSpecies().code());
        Assertions.assertEquals("AC", acSpecies.newSpecies().code());
        Assertions.assertNull(acSpecies.oldSpecies().description());
        Assertions.assertEquals("poplar", acSpecies.newSpecies().description());

        // Verify second standard unit - unchanged
        var standardUnit2 = result.stream()
                .filter(su -> su.standardUnit().stockingStandardUnitId().equals(standardUnitId2))
                .findFirst()
                .orElseThrow();

        // Check standard unit details
        Assertions.assertEquals("2", standardUnit2.standardUnit().standardsUnitId());
        Assertions.assertEquals(3L, standardUnit2.standardUnit().oldRegimeId());
        Assertions.assertEquals(3L, standardUnit2.standardUnit().newRegimeId());
        Assertions.assertEquals(10.0, standardUnit2.standardUnit().oldNetArea());
        Assertions.assertEquals(10.0, standardUnit2.standardUnit().newNetArea());
        Assertions.assertEquals(5.0, standardUnit2.standardUnit().oldMaxSoilDisturbance());
        Assertions.assertEquals(5.0, standardUnit2.standardUnit().newMaxSoilDisturbance());
        Assertions.assertTrue(Boolean.parseBoolean(standardUnit2.standardUnit().oldVarianceIndicator()));
        Assertions.assertTrue(Boolean.parseBoolean(standardUnit2.standardUnit().newVarianceIndicator()));
        Assertions.assertTrue(Boolean.parseBoolean(standardUnit2.standardUnit().oldRegenObligationIndicator()));
        Assertions.assertTrue(Boolean.parseBoolean(standardUnit2.standardUnit().newRegenObligationIndicator()));
        Assertions.assertEquals(Integer.valueOf(6), standardUnit2.standardUnit().oldRegenOffsetYears());
        Assertions.assertEquals(Integer.valueOf(6), standardUnit2.standardUnit().newRegenOffsetYears());
        Assertions.assertEquals(Integer.valueOf(11), standardUnit2.standardUnit().oldFreeGrowingEarlyOffsetYears());
        Assertions.assertEquals(Integer.valueOf(11), standardUnit2.standardUnit().newFreeGrowingEarlyOffsetYears());
        Assertions.assertEquals(Integer.valueOf(14), standardUnit2.standardUnit().oldFreeGrowingLateOffsetYears());
        Assertions.assertEquals(Integer.valueOf(14), standardUnit2.standardUnit().newFreeGrowingLateOffsetYears());
        Assertions.assertEquals("CWH", standardUnit2.standardUnit().oldBgcZone());
        Assertions.assertEquals("CWH", standardUnit2.standardUnit().newBgcZone());
        Assertions.assertEquals("vm", standardUnit2.standardUnit().oldBgcSubzone());
        Assertions.assertEquals("vm", standardUnit2.standardUnit().newBgcSubzone());
        Assertions.assertEquals("01", standardUnit2.standardUnit().oldBecSiteSeries());
        Assertions.assertEquals("01", standardUnit2.standardUnit().newBecSiteSeries());

        // Check layer
        Assertions.assertEquals(1, standardUnit2.layers().size());
        var layer2 = standardUnit2.layers().get(0);
        Assertions.assertEquals(originalLayerId2, layer2.oldLayerId());
        Assertions.assertEquals(originalLayerId2, layer2.newLayerId());
        Assertions.assertEquals("I", layer2.oldStockingLayer().code());
        Assertions.assertEquals("I", layer2.newStockingLayer().code());
        Assertions.assertEquals("Inventory Layer", layer2.oldStockingLayer().description());
        Assertions.assertEquals("Inventory Layer", layer2.newStockingLayer().description());
        Assertions.assertEquals(Double.valueOf(2.0), layer2.oldMinHorizontalDistance());
        Assertions.assertEquals(Double.valueOf(2.0), layer2.newMinHorizontalDistance());
        Assertions.assertEquals(Integer.valueOf(400), layer2.oldMinPerfStockingStandard());
        Assertions.assertEquals(Integer.valueOf(400), layer2.newMinPerfStockingStandard());
        Assertions.assertEquals(Integer.valueOf(500), layer2.oldMinStockingStandard());
        Assertions.assertEquals(Integer.valueOf(500), layer2.newMinStockingStandard());
        Assertions.assertEquals(Integer.valueOf(900), layer2.oldTargetWellSpacedTrees());
        Assertions.assertEquals(Integer.valueOf(900), layer2.newTargetWellSpacedTrees());
        Assertions.assertEquals(Integer.valueOf(150), layer2.oldHeightRelativeToComp());
        Assertions.assertEquals(Integer.valueOf(150), layer2.newHeightRelativeToComp());

        // Check species
        Assertions.assertEquals(3, layer2.preferredSpecies().size());
        Assertions.assertEquals(1, layer2.acceptableSpecies().size());

        // Verify repository calls
        verify(openingRepository).getOpeningStandardUnitHistoryDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId);
        verify(openingRepository).getOpeningStandardUnitHistoryLayerDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId);
        verify(openingRepository).getOpeningStandardUnitHistoryLayerSpeciesDetailsWithComparisonByOpeningIdAndHistoryId(
                openingId, stockingEventHistoryId);
    }

    @Test
    @DisplayName("getOpeningStockingHistoryList returns correct stocking history list")
    void getOpeningStockingHistoryList_returnsCorrectStockingHistoryList() {
        // Given
        Long openingId = 10L;
        Long eventHistoryId = 100L;
        Long ssuId = 200L;

        OpeningStockingHistoryDetailsProjection detailsProjection = mock(OpeningStockingHistoryDetailsProjection.class);
        when(detailsProjection.getStockingStandardUnit()).thenReturn("SU1");
        when(detailsProjection.getSsuid()).thenReturn(ssuId);
        when(detailsProjection.getSrid()).thenReturn(300L);
        when(detailsProjection.getDefaultMof()).thenReturn(true);
        when(detailsProjection.getManualEntry()).thenReturn(false);
        when(detailsProjection.getFspId()).thenReturn(400L);
        when(detailsProjection.getNetArea()).thenReturn(50.0f);
        when(detailsProjection.getSoilDisturbancePercent()).thenReturn(5.0f);
        when(detailsProjection.getBecZoneCode()).thenReturn("CWH");
        when(detailsProjection.getBecSubzoneCode()).thenReturn("vm");
        when(detailsProjection.getBecVariant()).thenReturn("1");
        when(detailsProjection.getBecPhase()).thenReturn("a");
        when(detailsProjection.getBecSiteSeries()).thenReturn("06");
        when(detailsProjection.getBecSiteType()).thenReturn("typeA");
        when(detailsProjection.getBecSeral()).thenReturn("seralA");
        when(detailsProjection.getRegenDelay()).thenReturn(10L);
        when(detailsProjection.getFreeGrowingLate()).thenReturn(20L);
        when(detailsProjection.getFreeGrowingEarly()).thenReturn(5L);
        when(detailsProjection.getAdditionalStandards()).thenReturn("Additional");
        when(detailsProjection.getAmendmentComment()).thenReturn("Comment");

        OpeningStockingLayerHistoryProjection layerProjection = mock(OpeningStockingLayerHistoryProjection.class);
        when(layerProjection.getLayerCode()).thenReturn("I");
        when(layerProjection.getLayerName()).thenReturn("Inventory Layer");
        when(layerProjection.getMinWellspacedTrees()).thenReturn(100L);
        when(layerProjection.getMinPreferredWellspacedTrees()).thenReturn(80L);
        when(layerProjection.getMinHorizontalDistanceWellspacedTrees()).thenReturn(1L);
        when(layerProjection.getTargetWellspacedTrees()).thenReturn(120L);
        when(layerProjection.getMinResidualBasalArea()).thenReturn(2L);
        when(layerProjection.getMinPostspacingDensity()).thenReturn(400L);
        when(layerProjection.getMaxPostspacingDensity()).thenReturn(800L);
        when(layerProjection.getMaxConiferous()).thenReturn(90L);
        when(layerProjection.getHeightRelativeToComp()).thenReturn(30L);

        OpeningStockingSpeciesHistoryProjection preferredSpeciesProjection = mock(OpeningStockingSpeciesHistoryProjection.class);
        when(preferredSpeciesProjection.getLayerCode()).thenReturn("I");
        when(preferredSpeciesProjection.getSpeciesCode()).thenReturn("BA");
        when(preferredSpeciesProjection.getSpeciesName()).thenReturn("amabilis fir");
        when(preferredSpeciesProjection.getMinHeight()).thenReturn(1.5f);

        OpeningStockingSpeciesHistoryProjection acceptableSpeciesProjection = mock(OpeningStockingSpeciesHistoryProjection.class);
        when(acceptableSpeciesProjection.getLayerCode()).thenReturn("I");
        when(acceptableSpeciesProjection.getSpeciesCode()).thenReturn("BG");
        when(acceptableSpeciesProjection.getSpeciesName()).thenReturn("grand fir");
        when(acceptableSpeciesProjection.getMinHeight()).thenReturn(1.2f);

        when(openingRepository.getOpeningStockingHistoryDetailsByOpeningIdAndEventHistoryId(openingId, eventHistoryId))
            .thenReturn(List.of(detailsProjection));

        when(openingRepository.getOpeningStockingLayerHistoryByOpeningIdAndEventHistoryId(openingId, eventHistoryId, ssuId))
            .thenReturn(List.of(layerProjection));

        when(openingRepository.getOpeningStockingSpeciesHistoryByOpeningIdAndEventHistoryId(openingId, eventHistoryId, "Y", ssuId))
            .thenReturn(List.of(preferredSpeciesProjection));
        when(openingRepository.getOpeningStockingSpeciesHistoryByOpeningIdAndEventHistoryId(openingId, eventHistoryId, "N", ssuId))
            .thenReturn(List.of(acceptableSpeciesProjection));

        when(commentRepository.getCommentById(null, null, ssuId, null, null)).thenReturn(List.of());

        // When
        List<OpeningStockingHistoryDto> result =
            openingStandardUnitHistoryService.getOpeningStockingHistoryList(openingId, eventHistoryId);

        // Then
        Assertions.assertEquals(1, result.size());
        OpeningStockingHistoryDto dto = result.get(0);

        OpeningStockingHistoryDetailsDto details = dto.stocking();
        Assertions.assertEquals("SU1", details.stockingStandardUnit());
        Assertions.assertEquals(ssuId, details.ssuId());
        Assertions.assertEquals(300L, details.srid());
        Assertions.assertTrue(details.defaultMof());
        Assertions.assertFalse(details.manualEntry());
        Assertions.assertEquals(400L, details.fspId());
        Assertions.assertEquals(50.0f, details.netArea());
        Assertions.assertEquals(5.0f, details.soilDisturbancePercent());
        Assertions.assertEquals("CWH", details.bec().becZoneCode());
        Assertions.assertEquals("vm", details.bec().becSubzoneCode());
        Assertions.assertEquals("1", details.bec().becVariant());
        Assertions.assertEquals("a", details.bec().becPhase());
        Assertions.assertEquals("06", details.bec().becSiteSeries());
        Assertions.assertEquals("typeA", details.bec().becSiteType());
        Assertions.assertEquals("seralA", details.bec().becSeral());
        Assertions.assertEquals(10, details.regenDelay());
        Assertions.assertEquals(20, details.freeGrowingLate());
        Assertions.assertEquals(5, details.freeGrowingEarly());
        Assertions.assertEquals("Additional", details.additionalStandards());
        Assertions.assertEquals("Comment", details.amendmentComment());

        Assertions.assertEquals(1, dto.layers().size());
        OpeningStockingHistoryLayerDto layer = dto.layers().get(0);
        Assertions.assertEquals("I", layer.layer().code());
        Assertions.assertEquals("Inventory Layer", layer.layer().description());
        Assertions.assertEquals(100, layer.minWellspacedTrees());
        Assertions.assertEquals(80, layer.minPreferredWellspacedTrees());
        Assertions.assertEquals(1L, layer.minHorizontalDistanceWellspacedTrees());
        Assertions.assertEquals(120, layer.targetWellspacedTrees());
        Assertions.assertEquals(2L, layer.minResidualBasalArea());
        Assertions.assertEquals(400, layer.minPostspacingDensity());
        Assertions.assertEquals(800, layer.maxPostspacingDensity());
        Assertions.assertEquals(90, layer.maxConiferous());
        Assertions.assertEquals(30, layer.heightRelativeToComp());

        Assertions.assertEquals(1, dto.preferredSpecies().size());
        OpeningStockingHistorySpeciesDto preferredSpecies = dto.preferredSpecies().get(0);
        Assertions.assertEquals("I", preferredSpecies.layer());
        Assertions.assertEquals("BA", preferredSpecies.species().code());
        Assertions.assertEquals("amabilis fir", preferredSpecies.species().description());
        Assertions.assertEquals(1.5f, preferredSpecies.minHeight());

        Assertions.assertEquals(1, dto.acceptableSpecies().size());
        OpeningStockingHistorySpeciesDto acceptableSpecies = dto.acceptableSpecies().get(0);
        Assertions.assertEquals("I", acceptableSpecies.layer());
        Assertions.assertEquals("BG", acceptableSpecies.species().code());
        Assertions.assertEquals("grand fir", acceptableSpecies.species().description());
        Assertions.assertEquals(1.2f, acceptableSpecies.minHeight());

        Assertions.assertTrue(dto.comments().isEmpty());

        verify(openingRepository).getOpeningStockingHistoryDetailsByOpeningIdAndEventHistoryId(openingId, eventHistoryId);
        verify(openingRepository).getOpeningStockingLayerHistoryByOpeningIdAndEventHistoryId(openingId, eventHistoryId, ssuId);
        verify(openingRepository).getOpeningStockingSpeciesHistoryByOpeningIdAndEventHistoryId(openingId, eventHistoryId, "Y", ssuId);
        verify(openingRepository).getOpeningStockingSpeciesHistoryByOpeningIdAndEventHistoryId(openingId, eventHistoryId, "N", ssuId);
        verify(commentRepository).getCommentById(null, null, ssuId, null, null);
    }
}
