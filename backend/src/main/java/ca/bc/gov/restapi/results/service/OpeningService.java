package ca.bc.gov.restapi.results.service;

import ca.bc.gov.restapi.results.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.endpoint.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.endpoint.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.entity.OpeningEntity;
import ca.bc.gov.restapi.results.repository.OpeningRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/** This class holds methods for fetching and handling {@link OpeningEntity} in general. */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningService {

  private final OpeningRepository openingRepository;

  private final CutBlockOpenAdminService cutBlockOpenAdminService;

  /**
   * Gets all recent openings for the Home Screen.
   *
   * @return {@link List} of {@link RecentOpeningDto} containing all recent openings.
   */
  public PaginatedResult<RecentOpeningDto> getRecentOpenings(PaginationParameters pagination) {
    log.info(
        "Getting recent openings with page index {} and page size {}",
        pagination.page(),
        pagination.pageSize());

    String entryUserId = "idir-here";

    // Openings
    Pageable pageable = PageRequest.of(pagination.page(), pagination.pageSize());
    Page<OpeningEntity> openingPage = openingRepository.findAllByEntryUserId(entryUserId, pageable);

    PaginatedResult<RecentOpeningDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setCurrentPage(pagination.page());
    paginatedResult.setPageSize(pagination.pageSize());

    if (openingPage.getContent().isEmpty()) {
      log.info("No recent openings given page index and size");
      paginatedResult.setData(List.of());
      paginatedResult.setPages(0);
      paginatedResult.setHasNextPage(false);
      return paginatedResult;
    }

    // Cut Block Open Admin
    List<Long> openingIds = openingPage.getContent().stream().map(OpeningEntity::getId).toList();
    List<CutBlockOpenAdminEntity> cutBlocks =
        cutBlockOpenAdminService.findAllByOpeningIdIn(openingIds);

    List<RecentOpeningDto> list = createDtoFromEntity(openingPage.getContent(), cutBlocks);
    paginatedResult.setData(list);
    paginatedResult.setPages(openingPage.getTotalPages());
    paginatedResult.setHasNextPage(openingPage.hasNext());

    return paginatedResult;
  }

  private List<RecentOpeningDto> createDtoFromEntity(
      List<OpeningEntity> openings, List<CutBlockOpenAdminEntity> clutBloks) {
    if (openings.size() != clutBloks.size()) {
      log.warn("Different number of records for the Opening x Cut Block Open Admin relationship");
    }

    List<RecentOpeningDto> recentOpeningDtos = new ArrayList<>();

    for (int i = 0; i < openings.size(); i++) {
      OpeningEntity opening = openings.get(i);

      String forestFileId = "";
      String cuttingPermitId = "";
      String timberMark = "";
      String cuttingBlockId = "";
      BigDecimal openingGrossArea = BigDecimal.ZERO;
      LocalDate disturbanceStartDate = null;

      if (clutBloks.size() - 1 >= i) {
        CutBlockOpenAdminEntity cutBlockOpenAdmin = clutBloks.get(i);
        forestFileId = cutBlockOpenAdmin.getForestFileId();
        cuttingPermitId = cutBlockOpenAdmin.getCuttingPermitId();
        timberMark = cutBlockOpenAdmin.getTimberMark();
        cuttingBlockId = cutBlockOpenAdmin.getCutBlockId();
        openingGrossArea = cutBlockOpenAdmin.getOpeningGrossArea();
        disturbanceStartDate = cutBlockOpenAdmin.getDisturbanceEndDate();
      }

      RecentOpeningDto openingDto =
          new RecentOpeningDto(
              opening.getId(),
              forestFileId,
              cuttingPermitId,
              timberMark,
              cuttingBlockId,
              openingGrossArea,
              opening.getStatus(),
              opening.getCategory(),
              disturbanceStartDate);

      recentOpeningDtos.add(openingDto);
    }

    return recentOpeningDtos;
  }
}
