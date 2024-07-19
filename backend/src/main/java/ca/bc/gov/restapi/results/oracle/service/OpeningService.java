package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.config.ConstantsConfig;
import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.SearchOpeningFiltersDto;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningAttachmentEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
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
  public PaginatedResult<SearchOpeningDto> searchOpening(
      SearchOpeningFiltersDto filtersDto, PaginationParameters pagination) {
    log.info(
        "Search Openings with page index {} and page size {}",
        pagination.page(),
        pagination.perPage());

    if (pagination.perPage() > ConstantsConfig.MAX_PAGE_SIZE) {
      throw new MaxPageSizeException();
    }

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<OpeningEntity> query = cb.createQuery(OpeningEntity.class);
    // Main table, Opening
    Root<OpeningEntity> openingRoot = query.from(OpeningEntity.class);
    // Join Opening Attachments
    Join<OpeningEntity, OpeningAttachmentEntity> openingAttachmentJoin =
        openingRoot.join("attachments");
    openingRoot.fetch("attachments", JoinType.LEFT);
    // Join Cub Block Open Admin
    Join<OpeningEntity, CutBlockOpenAdminEntity> cboaJoin = openingRoot.join("cutBlocksOpenAdmins");
    openingRoot.fetch("cutBlocksOpenAdmins", JoinType.LEFT);
    // Join Org Unit (admin district)
    Join<OpeningEntity, OrgUnitEntity> ouAdminJoin = openingRoot.join("adminDistrict");
    openingRoot.fetch("adminDistrict", JoinType.INNER);

    List<Predicate> predicates = new ArrayList<>();

    // Join with:
    // OPENING_ATTACHMENT (Relationship: OPENING:1 <-> N:OPENING_ATTACHMENT)
    // CUT_BLOCK_OPEN_ADMIN (Relationship: OPENING:1 <-> N:CUT_BLOCK_OPEN_ADMIN)
    // ORG_UNIT (Relationship: OPENING:1 <-> 1:ORG_UNIT)
    // RESULTS_ELECTRONIC_SUBMISSION
    // CLIENT_ACRONYM

    /* Filters */
    // 0. Main number filter [opening_id, opening_number, timber_mark, file_id]
    // if it's a number, filter by openingId or fileId, otherwise filter by timber mark and opening
    // number
    if (filtersDto.hasValue(filtersDto._NUMBER)) {
      log.debug("number filter detected! number=[{}]", filtersDto.getNumber());
      boolean itsNumeric = filtersDto.getNumber().replaceAll("[0-9]", "").isEmpty();
      if (itsNumeric) {
        log.debug("number filter it's numeric!");
        // Opening id
        Predicate openingIdPred = cb.equal(openingRoot.get("id"), filtersDto.getNumber());

        // File id
        Predicate fileIdPred = cb.equal(openingAttachmentJoin.get("id"), filtersDto.getNumber());

        // Combine them, for the 'or clause'
        Predicate openingIdOrFileId = cb.or(openingIdPred, fileIdPred);

        predicates.add(openingIdOrFileId);
      } else {
        log.debug("number filter it's NOT numeric!");
        // Opening number
        Predicate openingNumberPred =
            cb.equal(openingRoot.get("openingNumber"), filtersDto.getNumber());

        // Timber Mark
        Predicate timberMarkPred =
            cb.equal(cboaJoin.get("cuttingPermitId"), filtersDto.getNumber());

        // Combine them, for the 'or clause'
        Predicate timberMark = cb.or(openingNumberPred, timberMarkPred);
        predicates.add(timberMark);
      }
    }

    // 1. Org Unit code
    if (filtersDto.hasValue(filtersDto._ORG_UNIT)) {
      log.debug("org unit filter detected! org unit=[{}]", filtersDto.getOrgUnit());
      Predicate orgUnitPred = cb.equal(ouAdminJoin.get("orgUnitCode"), filtersDto.getOrgUnit());
      predicates.add(orgUnitPred);
    }
    // 2. Category code
    if (filtersDto.hasValue(filtersDto._CATEGORY)) {
      log.debug("category filter detected! category=[{}]", filtersDto.getCategory());
      Predicate categoryPred =
          cb.equal(openingRoot.get("category"), filtersDto.getCategory().toUpperCase());
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
    log.info("{} predicates!", predicates.size());
    for (int i = 0, len = predicates.size(); i < len; i++) {
      wherePredicates[i] = predicates.get(i);
    }
    query.select(openingRoot).where(wherePredicates);

    // Runs the query
    TypedQuery<OpeningEntity> typedQuery =
        entityManager
            .createQuery(query)
            .setFirstResult(pagination.page())
            .setMaxResults(pagination.perPage());

    List<OpeningEntity> resultList = typedQuery.getResultList();

    final int totalPages = (typedQuery.getMaxResults() / pagination.perPage()) + 1;
    final boolean hasNextPage = typedQuery.getMaxResults() > pagination.perPage();

    log.info("resultList size {}", resultList.size());

    PaginatedResult<SearchOpeningDto> paginatedResult = new PaginatedResult<>();
    paginatedResult.setPageIndex(pagination.page());
    paginatedResult.setPerPage(pagination.perPage());

    if (resultList.isEmpty()) {
      log.info("No openings result for the search given page index and size!");
      paginatedResult.setData(List.of());
      paginatedResult.setTotalPages(0);
      paginatedResult.setHasNextPage(false);
      return paginatedResult;
    }

    List<SearchOpeningDto> searchResultList = new ArrayList<>();

    for (OpeningEntity openingEntity : resultList) {
      SearchOpeningDto searchOpeningDto = new SearchOpeningDto();

      // From opening table
      searchOpeningDto.setOpeningId(openingEntity.getId());
      searchOpeningDto.setOpeningNumber(openingEntity.getOpeningNumber());
      searchOpeningDto.setCategory(OpeningCategoryEnum.of(openingEntity.getCategory()));
      searchOpeningDto.setStatus(OpeningStatusEnum.of(openingEntity.getStatus()));
      searchOpeningDto.setUpdateTimestamp(openingEntity.getUpdateTimestamp());
      searchOpeningDto.setEntryUserId(openingEntity.getEntryUserId());

      // From Cut Block Opening Admin table
      List<CutBlockOpenAdminEntity> cboas = openingEntity.getCutBlocksOpenAdmins();
      if (cboas.isEmpty()) {
        log.info("No cub block opening admin for opening id {}", openingEntity.getId());
      } else {
        log.info(
            "{} cut block opening admin for opening id {}", cboas.size(), openingEntity.getId());
        searchOpeningDto.setCuttingPermitId(cboas.get(0).getCuttingPermitId());
        searchOpeningDto.setCutBlockId(cboas.get(0).getCutBlockId());
        searchOpeningDto.setTimberMark(cboas.get(0).getTimberMark());
        searchOpeningDto.setGrossAreaHa(cboas.get(0).getOpeningGrossArea());
        searchOpeningDto.setDisturbanceDate(cboas.get(0).getDisturbanceEndDate());
      }

      // From Opening Attachment table
      List<OpeningAttachmentEntity> attachments = openingEntity.getAttachments();
      if (attachments.isEmpty()) {
        log.info("No attachments for opening id {}", openingEntity.getId());
      } else {
        log.info("{} attachment(s) for opening id {}", attachments.size(), openingEntity.getId());
        searchOpeningDto.setFileId(attachments.get(0).getId());
      }

      // From Org Unit (Admin District)
      OrgUnitEntity adminOrgUnit = openingEntity.getAdminDistrict();
      if (Objects.isNull(adminOrgUnit)) {
        log.info("No admin district for id {}", openingEntity.getId());
      } else {
        log.info("Admin district found for id {}", openingEntity.getId());
        searchOpeningDto.setOrgUnitNo(adminOrgUnit.getOrgUnitNo());
        searchOpeningDto.setOrgUnitCode(adminOrgUnit.getOrgUnitCode());
        searchOpeningDto.setOrgUnitName(adminOrgUnit.getOrgUnitName());
      }

      searchOpeningDto.setClientNumber(null);
      searchOpeningDto.setClientAcronym(null);
      searchOpeningDto.setRegenDelayDate(null);
      searchOpeningDto.setFreeGrowingDate(null);
      searchOpeningDto.setSubmittedToFrpa(false);

      searchResultList.add(searchOpeningDto);
    }

    paginatedResult.setData(searchResultList);
    paginatedResult.setTotalPages(totalPages);
    paginatedResult.setHasNextPage(hasNextPage);

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
