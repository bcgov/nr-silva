package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTombstoneOverviewDto;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.oracle.service.conversion.opening.OpeningDetailsCommentConverter;
import ca.bc.gov.restapi.results.oracle.service.conversion.opening.OpeningDetailsTombstoneConverter;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningDetailsTombstoneService {

  private final OpeningRepository openingRepository;
  private final ForestClientService forestClientService;
  private final SilvicultureCommentRepository commentRepository;

  public Optional<OpeningDetailsTombstoneOverviewDto> getOpeningTombstone(Long openingId) {

    return
        //Everything starts with the base tombstone search/load because if no data is found
        // then we will not load anything else
        openingRepository
            .getOpeningTombstoneByOpeningId(openingId)
            //The map starts to convert to the final object
            .map(OpeningDetailsTombstoneConverter.mapTombstoneOverview())
            //We then follow up with client information load, if any exists, or we move ahead
            .map(getForestClientInformation())
            //Then we move to the overview. We start with the opening part of it
            .map(getOverview(openingId))
            //Then we move to the milestone part of it. This is split to avoid multiple returns when
            // it's not required
            .map(getMilestones(openingId))
            //Finally we load the comments. This is the last step, and it loads comments associated to
            // the opening. This is done in a separate step to avoid loading comments if no tombstone
            // is found. This is a performance optimization.
            .map(getComments(openingId));
  }

  private Function<OpeningDetailsTombstoneOverviewDto, OpeningDetailsTombstoneOverviewDto> getForestClientInformation() {
    return tombstone ->
        Optional
            .ofNullable(tombstone.tombstone().client().clientNumber())
            .flatMap(forestClientService::getClientByNumber)
            .map(
                client -> tombstone
                    .withTombstone(
                        tombstone
                            .tombstone()
                            .withClient(client)
                    )
            )
            .orElse(tombstone);
  }

  private Function<OpeningDetailsTombstoneOverviewDto, OpeningDetailsTombstoneOverviewDto> getOverview(
      Long openingId) {
    return tombstone ->
        openingRepository
            .getOpeningTombstoneOverviewByOpeningId(openingId)
            .map(OpeningDetailsTombstoneConverter.mapTombstoneOpeningOverview(tombstone))
            .orElse(tombstone);
  }

  private Function<OpeningDetailsTombstoneOverviewDto, OpeningDetailsTombstoneOverviewDto> getMilestones(
      Long openingId) {
    return tombstone ->
        openingRepository
            .getOpeningTombstoneMilestoneByOpeningId(openingId)
            .map(OpeningDetailsTombstoneConverter.mapTombstoneMilestoneOverview(tombstone))
            .orElse(tombstone);
  }

  private Function<OpeningDetailsTombstoneOverviewDto, OpeningDetailsTombstoneOverviewDto> getComments(
      Long openingId) {
    return tombstone ->
        tombstone
            .withOverview(
                tombstone
                    .overview()
                    .withOpening(
                        tombstone
                            .overview()
                            .opening()
                            .withComments(
                                commentRepository
                                    .getCommentById(openingId)
                                    .stream()
                                    .map(OpeningDetailsCommentConverter.mapComments())
                                    .toList()
                            )
                    )
            );
  }

}
