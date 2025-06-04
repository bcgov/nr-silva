package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverDto;
import ca.bc.gov.restapi.results.oracle.dto.cover.OpeningForestCoverLayerListDescriptionDto;
import ca.bc.gov.restapi.results.oracle.repository.ForestCoverEntityRepository;
import java.util.List;
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
