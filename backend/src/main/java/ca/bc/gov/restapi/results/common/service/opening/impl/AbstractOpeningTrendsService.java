package ca.bc.gov.restapi.results.common.service.opening.impl;

import ca.bc.gov.restapi.results.common.projection.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.service.opening.OpeningTrendsService;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOpeningTrendsService implements OpeningTrendsService {

  protected final OpeningRepository openingRepository;

  @Override
  public List<OpeningsPerYearDto> getOpeningSubmissionTrends(LocalDate startDate, LocalDate endDate,
      List<String> orgUnits, List<String> statusCodes) {
    // if the difference between the start and end date is bigger than 12, thrown an exception
    if (ChronoUnit.MONTHS.between(startDate, endDate) > 12) {
      throw new IllegalArgumentException("The date range must be within 12 months");
    }

    List<OpeningTrendsProjection> entities =
        openingRepository.getOpeningTrends(
            startDate.format(DateTimeFormatter.ISO_DATE),
            endDate.format(DateTimeFormatter.ISO_DATE),
            statusCodes == null ? List.of("NOVALUE") : statusCodes,
            orgUnits == null ? List.of("NOVALUE") : orgUnits
        );

    if (entities.isEmpty()) {
      log.info("No Opening Submission Trends data found!");
      return List.of();
    }

    // Generate the expected months
    List<YearMonth> yearMonths = IntStream.rangeClosed(0, 12)
        .mapToObj(offset -> YearMonth.from(startDate).plusMonths(offset))
        .filter(yearMonth -> !yearMonth.isAfter(YearMonth.from(endDate)))
        .toList();

    // Transform data into a map for quick lookup
    Map<YearMonth, Map<String, Long>> trendMap = new HashMap<>();

    for (OpeningTrendsProjection aggregate : entities) {
      YearMonth yearMonth = YearMonth.of(aggregate.getYear(), aggregate.getMonth());
      trendMap.computeIfAbsent(yearMonth, k -> new HashMap<>())
          .merge(aggregate.getStatus(), aggregate.getCount(), Long::sum);
    }

    return yearMonths.stream()
        .map(yearMonth -> new OpeningsPerYearDto(
            yearMonth.getMonthValue(),
            yearMonth.getYear(),
            trendMap.getOrDefault(yearMonth, Collections.emptyMap())
                .values().stream().mapToLong(Long::longValue).sum(),
            trendMap.getOrDefault(yearMonth, Collections.emptyMap())
        ))
        .toList();
  }
}
