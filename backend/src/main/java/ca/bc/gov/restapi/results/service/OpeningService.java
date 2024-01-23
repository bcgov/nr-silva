package ca.bc.gov.restapi.results.service;

import ca.bc.gov.restapi.results.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.endpoint.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.entity.OpeningEntity;
import ca.bc.gov.restapi.results.repository.OpeningRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningService {

  private final OpeningRepository openingRepository;

  /**
   * Gets all recent openings for the Home Screen.
   *
   * @return {@link List} of {@link RecentOpeningDto} containing all recent openings.
   */
  public List<RecentOpeningDto> getRecentOpenings(PaginationParameters pagination) {
    log.info(
        "Getting recent openings with page index {} and page size {}",
        pagination.page(),
        pagination.pageSize());

    Pageable pageable = PageRequest.of(pagination.page(), pagination.pageSize());
    Page<OpeningEntity> openingPage = openingRepository.findAll(pageable);

    if (openingPage.getContent().isEmpty()) {
      log.info("No recent openings given page index and size");
      return List.of();
    }

    List<RecentOpeningDto> recentOpeningDtos = new ArrayList<>();
    openingPage.getContent().forEach(e -> recentOpeningDtos.add(createDtoFromEntity(e)));
    return recentOpeningDtos;
  }

  private RecentOpeningDto createDtoFromEntity(OpeningEntity entity) {
    return new RecentOpeningDto(
        entity.getId(), "", "", "", "", "", entity.getStatus(), entity.getCategory(), null);
  }
}
