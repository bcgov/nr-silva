package ca.bc.gov.restapi.results.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.oracle.service.CutBlockOpenAdminService;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import java.math.BigDecimal;
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

  private OpeningService openingService;

  @BeforeEach
  void setup() {
    openingService =
        new OpeningService(openingRepository, cutBlockOpenAdminService, loggedUserService);
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
}
