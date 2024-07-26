package ca.bc.gov.restapi.results.oracle.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.repository.OpeningSearchRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class OpeningServiceTest {

  @Mock OpeningRepository openingRepository;

  @Mock CutBlockOpenAdminService cutBlockOpenAdminService;

  @Mock LoggedUserService loggedUserService;

  @Mock OpeningSearchRepository openingSearchRepository;

  private OpeningService openingService;

  private OpeningSearchFiltersDto mockFilter(
      String orgUnit,
      String category,
      String status,
      String entryUserId,
      Boolean submittedToFrpa,
      String disturbanceDateStart,
      String disturbanceDateEnd,
      String regenDelayDateStart,
      String regenDelayDateEnd,
      String freeGrowingDateStart,
      String freeGrowingDateEnd,
      String updateDateStart,
      String updateDateEnd,
      String mainSearchTerm) {
    return new OpeningSearchFiltersDto(
        orgUnit,
        category,
        status,
        entryUserId,
        submittedToFrpa,
        disturbanceDateStart,
        disturbanceDateEnd,
        regenDelayDateStart,
        regenDelayDateEnd,
        freeGrowingDateStart,
        freeGrowingDateEnd,
        updateDateStart,
        updateDateEnd,
        mainSearchTerm);
  }

  private OpeningSearchFiltersDto mockOrgUnit(String orgUnit) {
    return mockFilter(
        orgUnit, null, null, null, null, null, null, null, null, null, null, null, null, null);
  }

  private OpeningSearchFiltersDto mockMainFilter(String mainSearchTerm) {
    return mockFilter(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        mainSearchTerm);
  }

  @BeforeEach
  void setup() {
    openingService =
        new OpeningService(
            openingRepository,
            cutBlockOpenAdminService,
            loggedUserService,
            openingSearchRepository);
  }

  @Test
  @DisplayName("Get a list of recent openings without Cut Block for the home screen")
  void getRecentOpenings_openingWithoutCutBlockAdmin_shouldSucceed() {
    OpeningEntity openingEntity = new OpeningEntity();
    openingEntity.setId(123L);

    List<OpeningEntity> openingList = new ArrayList<>(1);
    openingList.add(openingEntity);

    int pages = 3;
    int pageSize = 5;
    int totalResults = pageSize * pages - 1;
    Pageable openingsPageable = Pageable.ofSize(pageSize);

    Page<OpeningEntity> openingsPage = new PageImpl<>(openingList, openingsPageable, totalResults);
    when(openingRepository.findAllByEntryUserId(any(), any())).thenReturn(openingsPage);

    when(cutBlockOpenAdminService.findAllByOpeningIdIn(any())).thenReturn(List.of());

    int currentPage = 1;

    PaginationParameters pagination = new PaginationParameters(currentPage, pages);
    PaginatedResult<RecentOpeningDto> paginatedResult =
        openingService.getRecentOpeningsCurrentUser(pagination);

    Assertions.assertNotNull(paginatedResult);
    Assertions.assertEquals(currentPage, paginatedResult.getPageIndex());
    Assertions.assertEquals(pages, paginatedResult.getTotalPages());
    Assertions.assertFalse(paginatedResult.getData().isEmpty());
    Assertions.assertEquals(1, paginatedResult.getData().size());
  }

  @Test
  @DisplayName("Get a list of recent openings for logged user")
  void getRecentOpenings_fetchPaginated_shouldSucceed() {
    OpeningEntity openingEntity = new OpeningEntity();
    openingEntity.setId(123L);

    List<OpeningEntity> openingList = new ArrayList<>(1);
    openingList.add(openingEntity);

    int pages = 3;
    int pageSize = 5;
    int totalResults = pageSize * pages - 1;
    Pageable openingsPageable = Pageable.ofSize(pageSize);

    Page<OpeningEntity> openingsPage = new PageImpl<>(openingList, openingsPageable, totalResults);
    when(openingRepository.findAllByEntryUserId(any(), any())).thenReturn(openingsPage);

    CutBlockOpenAdminEntity cutBlock = new CutBlockOpenAdminEntity();
    cutBlock.setId(222L);
    cutBlock.setForestFileId("TFL47");
    cutBlock.setCuttingPermitId("12T");
    cutBlock.setTimberMark("47/12S");
    cutBlock.setCutBlockId("12-69");
    cutBlock.setOpeningGrossArea(new BigDecimal("12.9"));
    cutBlock.setOpeningId(openingEntity.getId());
    when(cutBlockOpenAdminService.findAllByOpeningIdIn(any())).thenReturn(List.of(cutBlock));

    int currentPage = 1;

    PaginationParameters pagination = new PaginationParameters(currentPage, pages);
    PaginatedResult<RecentOpeningDto> paginatedResult =
        openingService.getRecentOpeningsCurrentUser(pagination);

    Assertions.assertNotNull(paginatedResult);
    Assertions.assertEquals(currentPage, paginatedResult.getPageIndex());
    Assertions.assertEquals(pages, paginatedResult.getTotalPages());
    Assertions.assertFalse(paginatedResult.getData().isEmpty());
    Assertions.assertEquals(1, paginatedResult.getData().size());
  }

  @Test
  @DisplayName("Get an empty list of recent openings for the home screen")
  void getRecentOpenings_emptyPages_shouldSucceed() {
    Page<OpeningEntity> openingsPage = new PageImpl<>(new ArrayList<>());
    when(openingRepository.findAllByEntryUserId(any(), any())).thenReturn(openingsPage);

    when(cutBlockOpenAdminService.findAllByOpeningIdIn(any())).thenReturn(List.of());

    int currentPage = 0;
    int pages = 1;
    PaginationParameters pagination = new PaginationParameters(currentPage, pages);
    PaginatedResult<RecentOpeningDto> paginatedResult =
        openingService.getRecentOpeningsCurrentUser(pagination);

    Assertions.assertNotNull(paginatedResult);
    Assertions.assertEquals(currentPage, paginatedResult.getPageIndex());
    Assertions.assertEquals(0, paginatedResult.getTotalPages());
    Assertions.assertTrue(paginatedResult.getData().isEmpty());
  }

  @Test
  @DisplayName("Get a list of recent openings without user")
  void getRecentOpenings_fetchNoUserPaginated_shouldSucceed() {
    OpeningEntity openingEntity = new OpeningEntity();
    openingEntity.setId(123L);

    List<OpeningEntity> openingList = new ArrayList<>(1);
    openingList.add(openingEntity);

    int pages = 3;
    int pageSize = 5;
    int totalResults = pageSize * pages - 1;
    Pageable openingsPageable = Pageable.ofSize(pageSize);

    Page<OpeningEntity> openingsPage = new PageImpl<>(openingList, openingsPageable, totalResults);
    when(openingRepository.findAll(any(Pageable.class))).thenReturn(openingsPage);

    CutBlockOpenAdminEntity cutBlock = new CutBlockOpenAdminEntity();
    cutBlock.setId(222L);
    cutBlock.setForestFileId("TFL47");
    cutBlock.setCuttingPermitId("12T");
    cutBlock.setTimberMark("47/12S");
    cutBlock.setCutBlockId("12-69");
    cutBlock.setOpeningGrossArea(new BigDecimal("12.9"));
    cutBlock.setOpeningId(openingEntity.getId());
    when(cutBlockOpenAdminService.findAllByOpeningIdIn(any())).thenReturn(List.of(cutBlock));

    int currentPage = 1;

    PaginationParameters pagination = new PaginationParameters(currentPage, pages);
    PaginatedResult<RecentOpeningDto> paginatedResult =
        openingService.getRecentOpenings(pagination);

    Assertions.assertNotNull(paginatedResult);
    Assertions.assertEquals(currentPage, paginatedResult.getPageIndex());
    Assertions.assertEquals(pages, paginatedResult.getTotalPages());
    Assertions.assertFalse(paginatedResult.getData().isEmpty());
    Assertions.assertEquals(1, paginatedResult.getData().size());
  }

  @Test
  @DisplayName("Opening search file id happy path should succeed")
  void openingSearch_fileId_shouldSucceed() {
    PaginationParameters pagination = new PaginationParameters(0, 10);
    PaginatedResult<OpeningSearchResponseDto> paginated = new PaginatedResult<>();
    paginated.setPageIndex(pagination.page());
    paginated.setPerPage(pagination.perPage());
    paginated.setTotalPages(1);
    paginated.setHasNextPage(false);

    OpeningSearchResponseDto dto = new OpeningSearchResponseDto();
    dto.setOpeningId(123456);
    dto.setOpeningNumber("123");
    dto.setCategory(OpeningCategoryEnum.FTML);
    dto.setStatus(OpeningStatusEnum.RET);
    dto.setCuttingPermitId("");
    dto.setTimberMark("");
    dto.setCutBlockId("");
    dto.setOpeningGrossAreaHa(BigDecimal.ZERO);
    dto.setDisturbanceStartDate(LocalDateTime.now());
    dto.setOrgUnitCode("");
    dto.setOrgUnitName("");
    dto.setClientNumber("");
    dto.setClientAcronym("");
    dto.setRegenDelayDate("");
    dto.setFreeGrowingDate("");
    dto.setUpdateTimestamp(LocalDateTime.now());
    dto.setEntryUserId("");
    dto.setSubmittedToFrpa(false);
    dto.setFileId(407);
    paginated.setData(List.of(dto));

    OpeningSearchFiltersDto filters = mockMainFilter("407");
    when(openingSearchRepository.searchOpeningQuery(filters, pagination)).thenReturn(paginated);

    PaginatedResult<OpeningSearchResponseDto> result =
        openingService.openingSearch(filters, pagination);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(10, result.getPerPage());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(1, result.getData().size());
    Assertions.assertEquals(dto.getOpeningId(), result.getData().get(0).getOpeningId());
    Assertions.assertEquals(dto.getOpeningNumber(), result.getData().get(0).getOpeningNumber());
    Assertions.assertEquals(dto.getCategory(), result.getData().get(0).getCategory());
    Assertions.assertEquals(dto.getStatus(), result.getData().get(0).getStatus());
    Assertions.assertEquals(dto.getCuttingPermitId(), result.getData().get(0).getCuttingPermitId());
    Assertions.assertEquals(dto.getTimberMark(), result.getData().get(0).getTimberMark());
    Assertions.assertEquals(dto.getCutBlockId(), result.getData().get(0).getCutBlockId());
    Assertions.assertEquals(
        dto.getOpeningGrossAreaHa(), result.getData().get(0).getOpeningGrossAreaHa());
    Assertions.assertEquals(
        dto.getDisturbanceStartDate(), result.getData().get(0).getDisturbanceStartDate());
    Assertions.assertEquals(dto.getFileId(), result.getData().get(0).getFileId());
    Assertions.assertEquals(dto.getOrgUnitCode(), result.getData().get(0).getOrgUnitCode());
    Assertions.assertEquals(dto.getOrgUnitName(), result.getData().get(0).getOrgUnitName());
    Assertions.assertEquals(dto.getClientNumber(), result.getData().get(0).getClientNumber());
    Assertions.assertEquals(dto.getRegenDelayDate(), result.getData().get(0).getRegenDelayDate());
    Assertions.assertEquals(dto.getFreeGrowingDate(), result.getData().get(0).getFreeGrowingDate());
    Assertions.assertEquals(dto.getEntryUserId(), result.getData().get(0).getEntryUserId());
    Assertions.assertEquals(dto.getSubmittedToFrpa(), result.getData().get(0).getSubmittedToFrpa());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Opening search org unit happy path should succeed")
  void openingSearch_orgUnit_shouldSucceed() {
    PaginationParameters pagination = new PaginationParameters(0, 10);
    PaginatedResult<OpeningSearchResponseDto> paginated = new PaginatedResult<>();
    paginated.setPageIndex(pagination.page());
    paginated.setPerPage(pagination.perPage());
    paginated.setTotalPages(1);
    paginated.setHasNextPage(false);

    OpeningSearchResponseDto dto = new OpeningSearchResponseDto();
    dto.setOpeningId(123456);
    dto.setOpeningNumber("123");
    dto.setCategory(OpeningCategoryEnum.FTML);
    dto.setStatus(OpeningStatusEnum.RET);
    dto.setCuttingPermitId("");
    dto.setTimberMark("");
    dto.setCutBlockId("");
    dto.setOpeningGrossAreaHa(BigDecimal.ZERO);
    dto.setDisturbanceStartDate(LocalDateTime.now());
    dto.setOrgUnitCode("");
    dto.setOrgUnitName("");
    dto.setClientNumber("");
    dto.setClientAcronym("");
    dto.setRegenDelayDate("");
    dto.setFreeGrowingDate("");
    dto.setUpdateTimestamp(LocalDateTime.now());
    dto.setEntryUserId("");
    dto.setSubmittedToFrpa(false);
    dto.setFileId(407);
    paginated.setData(List.of(dto));

    OpeningSearchFiltersDto filters = mockOrgUnit("DCR");
    when(openingSearchRepository.searchOpeningQuery(filters, pagination)).thenReturn(paginated);

    PaginatedResult<OpeningSearchResponseDto> result =
        openingService.openingSearch(filters, pagination);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(10, result.getPerPage());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(1, result.getData().size());
    Assertions.assertEquals(dto.getOpeningId(), result.getData().get(0).getOpeningId());
    Assertions.assertEquals(dto.getOpeningNumber(), result.getData().get(0).getOpeningNumber());
    Assertions.assertEquals(dto.getCategory(), result.getData().get(0).getCategory());
    Assertions.assertEquals(dto.getStatus(), result.getData().get(0).getStatus());
    Assertions.assertEquals(dto.getCuttingPermitId(), result.getData().get(0).getCuttingPermitId());
    Assertions.assertEquals(dto.getTimberMark(), result.getData().get(0).getTimberMark());
    Assertions.assertEquals(dto.getCutBlockId(), result.getData().get(0).getCutBlockId());
    Assertions.assertEquals(
        dto.getOpeningGrossAreaHa(), result.getData().get(0).getOpeningGrossAreaHa());
    Assertions.assertEquals(
        dto.getDisturbanceStartDate(), result.getData().get(0).getDisturbanceStartDate());
    Assertions.assertEquals(dto.getFileId(), result.getData().get(0).getFileId());
    Assertions.assertEquals(dto.getOrgUnitCode(), result.getData().get(0).getOrgUnitCode());
    Assertions.assertEquals(dto.getOrgUnitName(), result.getData().get(0).getOrgUnitName());
    Assertions.assertEquals(dto.getClientNumber(), result.getData().get(0).getClientNumber());
    Assertions.assertEquals(dto.getRegenDelayDate(), result.getData().get(0).getRegenDelayDate());
    Assertions.assertEquals(dto.getFreeGrowingDate(), result.getData().get(0).getFreeGrowingDate());
    Assertions.assertEquals(dto.getEntryUserId(), result.getData().get(0).getEntryUserId());
    Assertions.assertEquals(dto.getSubmittedToFrpa(), result.getData().get(0).getSubmittedToFrpa());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Opening search no records found should succeed")
  void openingSearch_noRecordsFound_shouldSucceed() {
    PaginationParameters pagination = new PaginationParameters(0, 10);
    PaginatedResult<OpeningSearchResponseDto> paginated = new PaginatedResult<>();
    paginated.setPageIndex(pagination.page());
    paginated.setPerPage(pagination.perPage());
    paginated.setTotalPages(1);
    paginated.setHasNextPage(false);
    paginated.setData(List.of());

    OpeningSearchFiltersDto filters = mockOrgUnit("AAA");

    when(openingSearchRepository.searchOpeningQuery(filters, pagination)).thenReturn(paginated);

    PaginatedResult<OpeningSearchResponseDto> result =
        openingService.openingSearch(filters, pagination);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(10, result.getPerPage());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertTrue(result.getData().isEmpty());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Opening search max page exception should fail")
  void openingSearch_maxPageException_shouldFail() {
    OpeningSearchFiltersDto filters = mockMainFilter("407");
    PaginationParameters pagination = new PaginationParameters(0, 999);

    Assertions.assertThrows(
        MaxPageSizeException.class,
        () -> {
          openingService.openingSearch(filters, pagination);
        });
  }
}
