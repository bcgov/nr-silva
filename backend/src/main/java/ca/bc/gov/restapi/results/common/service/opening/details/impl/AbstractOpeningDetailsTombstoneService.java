package ca.bc.gov.restapi.results.common.service.opening.details.impl;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsTombstoneOverviewDto;
import ca.bc.gov.restapi.results.common.entity.BaseOpeningEntity;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.opening.conversion.OpeningDetailsCommentConverter;
import ca.bc.gov.restapi.results.common.service.opening.conversion.OpeningDetailsTombstoneConverter;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsNotificationService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsTombstoneService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractOpeningDetailsTombstoneService implements OpeningDetailsTombstoneService {

  protected final OpeningRepository<? extends BaseOpeningEntity> openingRepository;
  protected final ForestClientService forestClientService;
  protected final SilvicultureCommentRepository commentRepository;
  protected final OpeningDetailsNotificationService openingDetailsNotificationService;

  @Override
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
            .map(getComments(openingId))
            .map(getNotifications(openingId));
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
                                    .getCommentById(openingId, null, null, null, null)
                                    .stream()
                                    .map(OpeningDetailsCommentConverter.mapComments())
                                    .toList()
                            )
                    )
            );
  }

  private Function<OpeningDetailsTombstoneOverviewDto, OpeningDetailsTombstoneOverviewDto> getNotifications(
      Long openingId
  ) {
    return tombstone ->
        tombstone.withNotifications(
            openingDetailsNotificationService.getNotifications(openingId)
        );
  }
}
