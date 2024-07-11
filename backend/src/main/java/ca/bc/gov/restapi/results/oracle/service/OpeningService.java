package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.config.ConstantsConfig;
import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningFiltersDto;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/** This class holds methods for fetching and handling {@link OpeningEntity} in general. */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningService {

  private final OpeningRepository openingRepository;

  private final CutBlockOpenAdminService cutBlockOpenAdminService;

  private final LoggedUserService loggedUserService;

  private final EntityManager entityManager;

  /**
   * Gets all recent openings for the Home Screen.
   *
   * @param pagination A {@link PaginationParameters} with pagination settings.
   * @return {@link List} of {@link RecentOpeningDto} containing all recent openings for that user.
   */
  public PaginatedResult<RecentOpeningDto> getRecentOpeningsCurrentUser(
      PaginationParameters pagination) {
    log.info(
        "Getting recent openings to logged user with page index {} and page size {}",
        pagination.page(),
        pagination.perPage());

    if (pagination.perPage() > ConstantsConfig.MAX_PAGE_SIZE) {
      throw new MaxPageSizeException();
    }

    String entryUserId = loggedUserService.getLoggedUserId();

    // Openings
    Pageable pageable =
        PageRequest.of(
            pagination.page(), pagination.perPage(), Sort.by("updateTimestamp").descending());
    Page<OpeningEntity> openingPage = openingRepository.findAllByEntryUserId(entryUserId, pageable);

    PaginatedResult<RecentOpeningDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(pagination.page());
    paginatedResult.setPerPage(pagination.perPage());

    if (openingPage.getContent().isEmpty()) {
      log.info("No recent openings for this user given page index and size!");
      paginatedResult.setData(List.of());
      paginatedResult.setTotalPages(0);
      paginatedResult.setHasNextPage(false);
      return paginatedResult;
    }

    // Cut Block Open Admin
    List<Long> openingIds = openingPage.getContent().stream().map(OpeningEntity::getId).toList();
    List<CutBlockOpenAdminEntity> cutBlocks =
        cutBlockOpenAdminService.findAllByOpeningIdIn(openingIds);

    List<RecentOpeningDto> list = createDtoFromEntity(openingPage.getContent(), cutBlocks);
    paginatedResult.setData(list);
    paginatedResult.setTotalPages(openingPage.getTotalPages());
    paginatedResult.setHasNextPage(openingPage.hasNext());

    return paginatedResult;
  }

  /**
   * Get recent openings given the opening creation date.
   *
   * @param pagination A {@link PaginationParameters} with pagination settings.
   * @return {@link List} of {@link RecentOpeningDto} containing all recent openings.
   */
  public PaginatedResult<RecentOpeningDto> getRecentOpenings(PaginationParameters pagination) {
    log.info(
        "Getting recent openings, user independent, with page index {} and page size {}",
        pagination.page(),
        pagination.perPage());

    if (pagination.perPage() > ConstantsConfig.MAX_PAGE_SIZE) {
      throw new MaxPageSizeException();
    }

    // Openings
    Pageable pageable =
        PageRequest.of(
            pagination.page(), pagination.perPage(), Sort.by("updateTimestamp").descending());
    Page<OpeningEntity> openingPage = openingRepository.findAll(pageable);

    PaginatedResult<RecentOpeningDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(pagination.page());
    paginatedResult.setPerPage(pagination.perPage());

    if (openingPage.getContent().isEmpty()) {
      log.info("No recent openings given page index and size!");
      paginatedResult.setData(List.of());
      paginatedResult.setTotalPages(0);
      paginatedResult.setHasNextPage(false);
      return paginatedResult;
    }

    // Cut Block Open Admin
    List<Long> openingIds = openingPage.getContent().stream().map(OpeningEntity::getId).toList();
    List<CutBlockOpenAdminEntity> cutBlocks =
        cutBlockOpenAdminService.findAllByOpeningIdIn(openingIds);

    List<RecentOpeningDto> list = createDtoFromEntity(openingPage.getContent(), cutBlocks);
    paginatedResult.setData(list);
    paginatedResult.setTotalPages(openingPage.getTotalPages());
    paginatedResult.setHasNextPage(openingPage.hasNext());

    return paginatedResult;
  }

  /**
   * Search Opening API.
   *
   * @param filtersDto An instance of {@link SearchOpeningFiltersDto} with all possible filters.
   * @param pagination An instance of {@link PaginationParameters} with pagination settings.
   * @param number An identification number, one of Opening ID, Opening Number, Timber Mark ID, or
   *     File id.
   * @return Paginated result with found content.
   */
  public PaginatedResult<OpeningSearchResponseDto> searchOpening(
      SearchOpeningFiltersDto filtersDto, PaginationParameters pagination, String number) {
    log.info(
        "Search Openings with page index {} and page size {}",
        pagination.page(),
        pagination.perPage());

    if (pagination.perPage() > ConstantsConfig.MAX_PAGE_SIZE) {
      throw new MaxPageSizeException();
    }

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<OpeningEntity> query = cb.createQuery(OpeningEntity.class);
    Root<OpeningEntity> root = query.from(OpeningEntity.class);
    root.fetch("attachments", JoinType.LEFT);

    List<Predicate> predicates = new ArrayList<>();

    // Join with:
    // OPENING_ATTACHMENT -- entity ok (Relationship: OPENING:1 <-> N:OPENING_ATTACHMENT) opening id
    // example: 58933
    // CUT_BLOCK_OPEN_ADMIN -- entity ok
    // ORG_UNIT -- entity ok
    // RESULTS_ELECTRONIC_SUBMISSION -- entity ok
    // CLIENT_ACRONYM -- entity ok

    /* Filters */
    // 0. Main number filter [opening_id, opening_number, timber_mark, file_id]
    // if it's a number, filter by openingId or fileId, otherwise filter by timber mark and opening
    // number
    boolean itsNumeric = number.replace("[0-9]", "").isBlank();
    if (itsNumeric) {
      // Opening id
      Predicate openingIdPred = cb.equal(root.get("id"), number.trim());

      // File id
      Predicate fileIdPred = cb.equal(root.get("attachments.id"), number.trim());

      // Combine them, for the 'or clause'
      Predicate openingIdOrFileId = cb.or(openingIdPred, fileIdPred);

      predicates.add(openingIdOrFileId);
    } else {
      //
    }

    // 1. Org Unit code
    // 2. Category code
    if (!Objects.isNull(filtersDto.category())) {
      Predicate categoryPred = cb.equal(root.get("category"), filtersDto.category());
      predicates.add(categoryPred);
    }

    // 3. Status code
    // 4. User entry id
    // 5. Submitted to FRPA
    // 6. Disturbance start date
    // 7. Disturbance end date
    // 8. Regen delay start date
    // 9. Regen delay end date
    // 10. Free growing start date
    // 11. Free growing end date
    // 12. Update date start
    // 13. Update date end
    // 14. File id

    // Adds all the filters
    Predicate[] wherePredicates = new Predicate[predicates.size()];
    for (int i = 0, len = predicates.size(); i < len; i++) {
      wherePredicates[i++] = predicates.get(i);
    }
    query.where(wherePredicates);

    // Paging
    Pageable pageable =
        PageRequest.of(pagination.page(), pagination.perPage(), Sort.by("id").descending());

    // Runs the query
    List<OpeningEntity> resultList =
        entityManager
            .createQuery(query)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

    log.info("resultList size {}", resultList.size());
    for (OpeningEntity e : resultList) {
      log.info("- ID={}, number={} category={}", e.getId(), e.getOpeningNumber(), e.getCategory());
    }

    return null;

    /*
    // Openings
    Pageable pageable =
        PageRequest.of(pagination.page(), pagination.perPage(), Sort.by("id").descending());
    Page<OpeningEntity> openingPage = openingRepository.findAll(pageable);

    PaginatedResult<SearchOpeningDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(pagination.page());
    paginatedResult.setPerPage(pagination.perPage());

    if (openingPage.getContent().isEmpty()) {
      log.info("No openings result for the search given page index and size!");
      paginatedResult.setData(List.of());
      paginatedResult.setTotalPages(0);
      paginatedResult.setHasNextPage(false);
      return paginatedResult;
    }

    List<SearchOpeningDto> searchResultList = new ArrayList<>();

    // populate openings
    openingPage
        .getContent()
        .forEach(
            (opening) -> {
              SearchOpeningDto searchDto = new SearchOpeningDto();
              searchDto.setOpeningId(opening.getId());
              searchDto.setOpeningNumber(opening.getOpeningNumber());
              searchDto.setCategory(OpeningCategoryEnum.of(opening.getCategory()));
              searchDto.setStatus(OpeningStatusEnum.of(opening.getStatus()));
              // cuttingPermitId
              // timberMarkId
              // cutBlockId
              // grossAreaHa
              // disturbanceDate
              // orgUnitNo
              // orgUnitCode
              // orgUnitName
              // clientNumber
              // clientAcronym
              // regenDelayDate
              // freeGrowingDate
              // updateTimestamp
              searchDto.setEntryUserId(opening.getEntryUserId());
              // submittedToFrpa

              searchResultList.add(searchDto);
            });

    paginatedResult.setData(searchResultList);
    paginatedResult.setTotalPages(openingPage.getTotalPages());
    paginatedResult.setHasNextPage(openingPage.hasNext());

    return paginatedResult;
    */
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
              OpeningStatusEnum.of(opening.getStatus()),
              OpeningCategoryEnum.of(opening.getCategory()),
              disturbanceStartDate,
              opening.getEntryTimestamp(),
              opening.getUpdateTimestamp());

      recentOpeningDtos.add(openingDto);
    }

    return recentOpeningDtos;
  }
}
