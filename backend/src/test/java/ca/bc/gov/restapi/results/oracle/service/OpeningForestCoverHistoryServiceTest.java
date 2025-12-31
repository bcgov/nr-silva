package ca.bc.gov.restapi.results.oracle.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ca.bc.gov.restapi.results.oracle.dto.cover.history.*;
import ca.bc.gov.restapi.results.common.projection.cover.history.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.bc.gov.restapi.results.oracle.repository.ForestCoverEntityRepository;
import ca.bc.gov.restapi.results.oracle.service.opening.history.OpeningForestCoverHistoryService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Opening Forest Cover History Service")
public class OpeningForestCoverHistoryServiceTest {

    @Mock
    ForestCoverEntityRepository forestCoverEntityRepository;

    private OpeningForestCoverHistoryService openingForestCoverHistoryService;

    @BeforeEach
    void setUp() {
        openingForestCoverHistoryService = new OpeningForestCoverHistoryService(forestCoverEntityRepository);
    }

    @Test
    @DisplayName("getOpeningForestCoverHistoryOverviewList returns correct overview list")
    void getOpeningForestCoverHistoryOverviewList_returnsCorrectOverviewList() {
        // Given
        Long openingId = 1L;

        ForestCoverHistoryOverviewProjection projection1 = mock(ForestCoverHistoryOverviewProjection.class);
        when(projection1.getFcDate()).thenReturn(LocalDateTime.of(2012, 5, 2, 0, 0));
        when(projection1.getNp()).thenReturn(1.0);
        when(projection1.getNsr()).thenReturn(0.0);
        when(projection1.getImm()).thenReturn(15.7);
        when(projection1.getOther()).thenReturn(0.0);
        when(projection1.getTotal()).thenReturn(16.7);
        when(projection1.getHasDetails()).thenReturn(true);
        when(projection1.getIsCurrent()).thenReturn(true);
        when(projection1.getIsOldest()).thenReturn(false);

        ForestCoverHistoryOverviewProjection projection2 = mock(ForestCoverHistoryOverviewProjection.class);
        when(projection2.getFcDate()).thenReturn(LocalDateTime.of(2004, 11, 29, 10, 8));
        when(projection2.getNp()).thenReturn(1.0);
        when(projection2.getNsr()).thenReturn(0.0);
        when(projection2.getImm()).thenReturn(15.6);
        when(projection2.getOther()).thenReturn(0.0);
        when(projection2.getTotal()).thenReturn(16.6);
        when(projection2.getHasDetails()).thenReturn(true);
        when(projection2.getIsCurrent()).thenReturn(false);
        when(projection2.getIsOldest()).thenReturn(true);

        when(forestCoverEntityRepository.findHistoryOverviewByOpeningId(openingId))
                .thenReturn(List.of(projection1, projection2));

        // When
        List<OpeningForestCoverHistoryOverviewDto> result =
                openingForestCoverHistoryService.getOpeningForestCoverHistoryOverviewList(openingId);

        // Then
        assertEquals(2, result.size());

        OpeningForestCoverHistoryOverviewDto dto1 = result.get(0);
        assertEquals(LocalDateTime.of(2012, 5, 2, 0, 0), dto1.updateTimestamp());
        assertEquals(1.0, dto1.np());
        assertEquals(0.0, dto1.nsr());
        assertEquals(15.7, dto1.imm());
        assertEquals(0.0, dto1.other());
        assertEquals(16.7, dto1.total());
        assertTrue(dto1.hasDetails());
        assertTrue(dto1.isCurrent());
        assertFalse(dto1.isOldest());

        OpeningForestCoverHistoryOverviewDto dto2 = result.get(1);
        assertEquals(LocalDateTime.of(2004, 11, 29, 10, 8), dto2.updateTimestamp());
        assertEquals(1.0, dto2.np());
        assertEquals(0.0, dto2.nsr());
        assertEquals(15.6, dto2.imm());
        assertEquals(0.0, dto2.other());
        assertEquals(16.6, dto2.total());
        assertTrue(dto2.hasDetails());
        assertFalse(dto2.isCurrent());
        assertTrue(dto2.isOldest());

        verify(forestCoverEntityRepository).findHistoryOverviewByOpeningId(openingId);
    }

    @Test
    @DisplayName("getOpeningForestCoverList returns correct DTOs")
    void getOpeningForestCoverList_returnsCorrectDtos() {
        Long openingId = 1L;
        String updateDate = "2020-01-01";
        String archiveDate = "2020-01-02";

        ForestCoverHistoryProjection projection = mock(ForestCoverHistoryProjection.class);
        when(projection.getCoverId()).thenReturn(10L);
        when(projection.getArchiveDate()).thenReturn(LocalDate.of(2020, 1, 2));
        when(projection.getPolygonId()).thenReturn("100");
        when(projection.getStandardUnitId()).thenReturn("200");
        when(projection.getUnmappedCode()).thenReturn("U");
        when(projection.getUnmappedName()).thenReturn("Unmapped");
        when(projection.getGrossArea()).thenReturn(50.0f);
        when(projection.getNetArea()).thenReturn(45.0f);
        when(projection.getStatusCode()).thenReturn("A");
        when(projection.getStatusName()).thenReturn("Active");
        when(projection.getTypeCode()).thenReturn("T");
        when(projection.getTypeName()).thenReturn("Type");
        when(projection.getTotal()).thenReturn(16.7f);
        when(projection.getInventoryTotalWellSpaced()).thenReturn(10.0f);
        when(projection.getInventoryWellSpaced()).thenReturn(8.0f);
        when(projection.getInventoryFreeGrowing()).thenReturn(5.0f);
        when(projection.getSilvicultureTotalWellSpaced()).thenReturn(12.0f);
        when(projection.getSilvicultureWellSpaced()).thenReturn(9.0f);
        when(projection.getSilvicultureFreeGrowing()).thenReturn(6.0f);
        when(projection.getReferenceYear()).thenReturn(2020);
        when(projection.getIsSingleLayer()).thenReturn("Y");
        when(projection.getReserveCode()).thenReturn("R");

        when(forestCoverEntityRepository.findHistoryByOpeningDetails(openingId, updateDate, null))
                .thenReturn(List.of(projection));
        when(forestCoverEntityRepository.findHistoryByOpeningDetailsSpecies(10L, "S", archiveDate))
                .thenReturn(List.of());
        when(forestCoverEntityRepository.findHistoryByOpeningDetailsSpecies(10L, "I", archiveDate))
                .thenReturn(List.of());

        List<OpeningForestCoverHistoryDto> result =
                openingForestCoverHistoryService.getOpeningForestCoverList(openingId, updateDate, null);

        assertEquals(1, result.size());
        OpeningForestCoverHistoryDto dto = result.get(0);
        assertEquals(10L, dto.coverId());
        assertEquals(LocalDate.of(2020, 1, 2), dto.archiveDate());
        assertEquals("100", dto.polygonId());
        assertEquals("200", dto.standardUnitId());
        assertEquals("U", dto.unmappedArea().code());
        assertEquals("Unmapped", dto.unmappedArea().description());
        assertEquals(50.0f, dto.grossArea());
        assertEquals(45.0f, dto.netArea());
        assertEquals("A", dto.status().code());
        assertEquals("Active", dto.status().description());
        assertEquals("T", dto.coverType().code());
        assertEquals("Type", dto.coverType().description());
        assertEquals(16.7f, dto.inventoryLayer().total());
        assertEquals(2020, dto.referenceYear());
        assertTrue(dto.isSingleLayer());
        assertTrue(dto.hasReserve());

        verify(forestCoverEntityRepository).findHistoryByOpeningDetails(openingId, updateDate, null);
        verify(forestCoverEntityRepository).findHistoryByOpeningDetailsSpecies(10L, "S", archiveDate);
        verify(forestCoverEntityRepository).findHistoryByOpeningDetailsSpecies(10L, "I", archiveDate);
    }

    @Test
    @DisplayName("getOpeningForestCoverList returns empty list when no projections")
    void getOpeningForestCoverList_returnsEmptyList() {
        Long openingId = 2L;
        String updateDate = "2021-01-01T00:00:00";
        when(forestCoverEntityRepository.findHistoryByOpeningDetails(openingId, updateDate, null)).thenReturn(List.of());

        List<OpeningForestCoverHistoryDto> result =
                openingForestCoverHistoryService.getOpeningForestCoverList(openingId, updateDate, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(forestCoverEntityRepository).findHistoryByOpeningDetails(openingId, updateDate, null);
    }

    @Test
    @DisplayName("getDetails returns correct details DTO")
    void getDetails_returnsCorrectDetailsDto() {
        Long coverId = 10L;
        String archiveDate = "2020-01-01T00:00:00";

        ForestCoverHistoryPolygonProjection polygonProjection = mock(ForestCoverHistoryPolygonProjection.class);
        when(polygonProjection.getForestCoverId()).thenReturn(coverId);
        when(polygonProjection.getReserveCode()).thenReturn("R");
        when(polygonProjection.getReserveName()).thenReturn("Reserve");
        when(polygonProjection.getObjectiveCode()).thenReturn("O");
        when(polygonProjection.getObjectiveName()).thenReturn("Objective");
        when(polygonProjection.getSiteClassCode()).thenReturn("SC");
        when(polygonProjection.getSiteClassName()).thenReturn("SiteClass");
        when(polygonProjection.getSiteIndex()).thenReturn(12L);
        when(polygonProjection.getSiteIndexSourceCode()).thenReturn("SRC");
        when(polygonProjection.getSiteIndexSourceName()).thenReturn("Source");
        when(polygonProjection.getTreeCoverPatternCode()).thenReturn("TCP");
        when(polygonProjection.getTreeCoverPatternName()).thenReturn("Pattern");
        when(polygonProjection.getReentryYear()).thenReturn(2021L);

        when(forestCoverEntityRepository.findHistoryByOpeningDetailsPolygon(coverId, archiveDate))
                .thenReturn(Optional.of(polygonProjection));

        ForestCoverHistoryUnmappedProjection unmappedProjection = mock(ForestCoverHistoryUnmappedProjection.class);
        when(unmappedProjection.getUnmappedAreaId()).thenReturn("1");
        when(unmappedProjection.getArea()).thenReturn(5.5f);
        when(unmappedProjection.getStockingStatusCode()).thenReturn("S");
        when(unmappedProjection.getStockingStatusName()).thenReturn("Status");
        when(unmappedProjection.getStockingTypeCode()).thenReturn("T");
        when(unmappedProjection.getStockingTypeName()).thenReturn("Type");

        when(forestCoverEntityRepository.findHistoryByOpeningDetailsUnmapped(coverId, archiveDate))
                .thenReturn(List.of(unmappedProjection));

        ForestCoverHistoryDetailsLayerProjection layerProjection = mock(ForestCoverHistoryDetailsLayerProjection.class);
        when(layerProjection.getLayerId()).thenReturn(2L);
        when(layerProjection.getLayerCode()).thenReturn("S");
        when(layerProjection.getLayerName()).thenReturn("Silviculture");
        when(layerProjection.getCrownClosure()).thenReturn(80L);
        when(layerProjection.getBasalAreaSt()).thenReturn(10L);
        when(layerProjection.getTotalStems()).thenReturn(100L);
        when(layerProjection.getTotalWellSpaced()).thenReturn(90L);
        when(layerProjection.getWellSpaced()).thenReturn(80L);
        when(layerProjection.getFreeGrowing()).thenReturn(70L);

        when(forestCoverEntityRepository.findHistoryByOpeningDetailsLayer(coverId, archiveDate))
                .thenReturn(List.of(layerProjection));

        when(forestCoverEntityRepository.findHistoryByOpeningDetailsDetailedSpecies(2L, archiveDate))
                .thenReturn(List.of());
        when(forestCoverEntityRepository.findHistoryByOpeningDetailsDamage(2L, archiveDate))
                .thenReturn(List.of());

        Optional<OpeningForestCoverHistoryDetailsDto> result =
                openingForestCoverHistoryService.getDetails(coverId, archiveDate);

        assertTrue(result.isPresent());
        OpeningForestCoverHistoryDetailsDto dto = result.get();
        OpeningForestCoverHistoryPolygonDto polygonDto = dto.polygon();
        assertEquals(coverId, polygonDto.forestCoverId());
        assertEquals("R", polygonDto.reserve().code());
        assertEquals("Reserve", polygonDto.reserve().description());
        assertEquals("O", polygonDto.objective().code());
        assertEquals("Objective", polygonDto.objective().description());
        assertEquals("SC", polygonDto.siteClass().code());
        assertEquals("SiteClass", polygonDto.siteClass().description());
        assertEquals(12, polygonDto.siteIndex());
        assertEquals("SRC", polygonDto.siteIndexSource().code());
        assertEquals("Source", polygonDto.siteIndexSource().description());
        assertEquals("TCP", polygonDto.treeCoverPattern().code());
        assertEquals("Pattern", polygonDto.treeCoverPattern().description());
        assertEquals(2021, polygonDto.reentryYear());

        assertNotNull(dto.unmapped());
        assertEquals(1, dto.unmapped().size());
        OpeningForestCoverHistoryUnmappedDto unmappedDto = dto.unmapped().get(0);
        assertEquals("1", unmappedDto.unmappedAreaId());
        assertEquals(5.5f, unmappedDto.area());
        assertEquals("S", unmappedDto.stockingStatus().code());
        assertEquals("Status", unmappedDto.stockingStatus().description());
        assertEquals("T", unmappedDto.stockingType().code());
        assertEquals("Type", unmappedDto.stockingType().description());

        assertNotNull(dto.layers());
        assertEquals(1, dto.layers().size());
        OpeningForestCoverHistoryLayerDto layerDto = dto.layers().get(0);
        assertEquals(2L, layerDto.layerId());
        assertEquals("S", layerDto.layer().code());
        assertEquals("Silviculture", layerDto.layer().description());
        assertEquals(80L, layerDto.crownClosure());
        assertEquals(10L, layerDto.basalAreaSt());
        assertEquals(100L, layerDto.totalStems());
        assertEquals(90L, layerDto.totalWellSpaced());
        assertEquals(80L, layerDto.wellSpaced());
        assertEquals(70L, layerDto.freeGrowing());

        verify(forestCoverEntityRepository).findHistoryByOpeningDetailsPolygon(coverId, archiveDate);
        verify(forestCoverEntityRepository).findHistoryByOpeningDetailsUnmapped(coverId, archiveDate);
        verify(forestCoverEntityRepository).findHistoryByOpeningDetailsLayer(coverId, archiveDate);
        verify(forestCoverEntityRepository).findHistoryByOpeningDetailsDetailedSpecies(2L, archiveDate);
        verify(forestCoverEntityRepository).findHistoryByOpeningDetailsDamage(2L, archiveDate);
    }

    @Test
    @DisplayName("getDetails returns empty when no polygon projection")
    void getDetails_returnsEmptyWhenNoPolygon() {
        Long coverId = 99L;
        String archiveDate = "2022-01-01T00:00:00";
        when(forestCoverEntityRepository.findHistoryByOpeningDetailsPolygon(coverId, archiveDate))
                .thenReturn(Optional.empty());

        Optional<OpeningForestCoverHistoryDetailsDto> result =
                openingForestCoverHistoryService.getDetails(coverId, archiveDate);

        assertTrue(result.isEmpty());
        verify(forestCoverEntityRepository).findHistoryByOpeningDetailsPolygon(coverId, archiveDate);
    }
}
