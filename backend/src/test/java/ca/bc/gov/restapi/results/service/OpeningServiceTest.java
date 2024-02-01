package ca.bc.gov.restapi.results.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.endpoint.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.endpoint.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.entity.OpeningEntity;
import ca.bc.gov.restapi.results.repository.OpeningRepository;
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

  private OpeningService openingService;

  @BeforeEach
  void setup() {
    openingService = new OpeningService(openingRepository, cutBlockOpenAdminService);
  }

  @Test
  @DisplayName("Get a list of recent openings for the home screen")
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

    when(cutBlockOpenAdminService.findAllByOpeningIdIn(any())).thenReturn(List.of());

    int currentPage = 1;

    PaginationParameters pagination = new PaginationParameters(currentPage, pages);
    PaginatedResult<RecentOpeningDto> paginatedResult =
        openingService.getRecentOpenings(pagination);

    Assertions.assertNotNull(paginatedResult);
    Assertions.assertEquals(currentPage, paginatedResult.getCurrentPage());
    Assertions.assertEquals(pages, paginatedResult.getPages());
    Assertions.assertFalse(paginatedResult.getData().isEmpty());
    Assertions.assertEquals(1, paginatedResult.getData().size());
  }
}
