package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverDamageDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverDetailedSpeciesDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverDetailsDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverLayerDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverLayerListDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverPolygonDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverUnmappedDto;
import ca.bc.gov.restapi.results.oracle.repository.ForestCoverEntityRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningDetailsForestCoverService {

  private final ForestCoverEntityRepository coverRepository;

  public List<OpeningForestCoverDto> getOpeningForestCoverList(Long openingId,
      String mainSearchTerm) {

    return coverRepository
        .findByOpeningDetails(openingId, mainSearchTerm)
        .stream()
        .map(projection -> new OpeningForestCoverDto(
                projection.getCoverId(),
                projection.getPolygonId(),
                projection.getStandardUnitId(),
                new CodeDescriptionDto(
                    projection.getUnmappedCode(),
                    projection.getUnmappedName()
                ),
                projection.getGrossArea(),
                projection.getNetArea(),
                new CodeDescriptionDto(
                    projection.getStatusCode(),
                    projection.getStatusName()
                ),
                new CodeDescriptionDto(
                    projection.getTypeCode(),
                    projection.getTypeName()
                ),
                new OpeningForestCoverLayerListDescriptionDto(
                    List.of(),
                    projection.getTotal(),
                    projection.getInventoryTotalWellSpaced(),
                    projection.getInventoryWellSpaced(),
                    projection.getInventoryFreeGrowing()
                ),
                new OpeningForestCoverLayerListDescriptionDto(
                    List.of(),
                    null,
                    projection.getSilvicultureTotalWellSpaced(),
                    projection.getSilvicultureWellSpaced(),
                    projection.getSilvicultureFreeGrowing()
                ),
                projection.getReferenceYear()
            )
        )
        .map(dto ->
            dto.withSilvicultureLayer(
                dto
                    .silvicultureLayer()
                    .withSpecies(
                        getLayerSpecies(dto.coverId(), "S")
                    )
            )
        )
        .map(dto ->
            dto.withInventoryLayer(
                dto
                    .inventoryLayer()
                    .withSpecies(
                        getLayerSpecies(dto.coverId(), "I")
                    )
            )
        )
        .toList();
  }

  public Optional<OpeningForestCoverDetailsDto> getDetails(Long coverId) {

    return coverRepository
        .findByOpeningDetailsPolygon(coverId)
        .map(projection ->
            new OpeningForestCoverDetailsDto(
                new OpeningForestCoverPolygonDto(
                    projection.getForestCoverId(),
                    new CodeDescriptionDto(
                        projection.getReserveCode(),
                        projection.getReserveName()
                    ),
                    new CodeDescriptionDto(
                        projection.getObjectiveCode(),
                        projection.getObjectiveName()
                    ),
                    new CodeDescriptionDto(
                        projection.getSiteClassCode(),
                        projection.getSiteClassName()
                    ),
                    projection.getSiteIndex(),
                    new CodeDescriptionDto(
                        projection.getSiteIndexSourceCode(),
                        projection.getSiteIndexSourceName()
                    ),
                    new CodeDescriptionDto(
                        projection.getTreeCoverPatternCode(),
                        projection.getTreeCoverPatternName()
                    ),
                    projection.getReentryYear()
                ),
                null,
                List.of()
            )
        )
        .map(dto ->
            coverRepository
                .findByOpeningDetailsUnmapped(coverId)
                .map(projection ->
                    new OpeningForestCoverUnmappedDto(
                        projection.getUnmappedAreaId(),
                        projection.getArea(),
                        new CodeDescriptionDto(
                            projection.getStockingStatusCode(),
                            projection.getStockingStatusName()
                        ),
                        new CodeDescriptionDto(
                            projection.getStockingTypeCode(),
                            projection.getStockingTypeName()
                        )
                    )
                )
                .map(dto::withUnmapped)
                .orElse(dto)
        )
        .map(dto ->
            dto.withLayers(getDetailsLayer(coverId))
        );
  }

  private List<OpeningForestCoverLayerDto> getDetailsLayer(Long coverId) {
    return coverRepository
        .findByOpeningDetailsLayer(coverId)
        .stream()
        .map(projection ->
            new OpeningForestCoverLayerDto(
                projection.getLayerId(),
                new CodeDescriptionDto(
                    projection.getLayerCode(),
                    projection.getLayerName()
                ),
                projection.getCrownClosure(),
                projection.getBasalAreaSt(),
                projection.getTotalStems(),
                projection.getTotalWellSpaced(),
                projection.getWellSpaced(),
                projection.getFreeGrowing(),
                getDetailedSpecies(projection.getLayerId()),
                getDamage(projection.getLayerId())
            )
        )
        .toList();
  }

  private List<OpeningForestCoverDetailedSpeciesDto> getDetailedSpecies(Long coverLayerId) {
    return coverRepository
        .findByOpeningDetailsDetailedSpecies(coverLayerId)
        .stream()
        .map(projection -> new OpeningForestCoverDetailedSpeciesDto(
                new CodeDescriptionDto(
                    projection.getSpeciesCode(),
                    projection.getSpeciesName()
                ),
                projection.getSpeciesPercent(),
                projection.getAverageAge(),
                projection.getAverageHeight()
            )
        )
        .toList();
  }

  private OpeningForestCoverDamageDto getDamage(Long coverLayerId) {
    return coverRepository
        .findByOpeningDetailsDamage(coverLayerId)
        .map(projection -> new OpeningForestCoverDamageDto(
                new CodeDescriptionDto(
                    projection.getDamageAgentCode(),
                    projection.getDamageAgentName()
                ),
                projection.getForestHealthIncidence(),
                projection.getIncidenceArea()
            )
        )
        .orElse(null);
  }

  private List<CodeDescriptionDto> getLayerSpecies(Long coverId, String I) {
    return coverRepository
        .findByOpeningDetailsSpecies(coverId, I)
        .stream()
        .map(projection -> new CodeDescriptionDto(
                projection.getSpeciesCode(),
                projection.getSpeciesName()
            )
        )
        .toList();
  }

}
