package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.oracle.entity.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningTrendsService {

  private final OpeningRepository openingRepository;

  public List<OpeningsPerYearDto> getOpeningSubmissionTrends(
      LocalDate startDate,
      LocalDate endDate,
      List<String> orgUnits,
      List<String> statusCodes
  ) {

    // if the difference between the start date and the end date is bigger than 12, thrown an exception
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

    // Group by month and status
    Map<Integer, Map<String, Long>> monthToStatusCountMap = entities.stream()
        .filter(entity -> entity.getEntryTimestamp() != null) // Ensure timestamp is not null
        .collect(Collectors.groupingBy(
            entity -> entity.getEntryTimestamp().getMonthValue(), // Extract month value
            Collectors.groupingBy(
                OpeningTrendsProjection::getStatus, // Group by status
                Collectors.counting() // Count occurrences
            )
        ));

    // Map to count total entries grouped by month
    Map<Integer, Long> monthToCountMap = monthToStatusCountMap.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey, // Month
            entry -> entry.getValue().values().stream().mapToLong(Long::longValue).sum() // Sum counts per status
        ));

    // Generate a 12-month sequence starting from the start date
    List<YearMonth> yearMonths = IntStream.range(0, 12) // Always 12 months
        .mapToObj(offset -> YearMonth.from(startDate).plusMonths(offset))
        .toList();

    // Generate the DTOs in the custom order
    return yearMonths.stream()
        .map(yearMonth -> new OpeningsPerYearDto(
            yearMonth.getMonthValue(),
            yearMonth.getYear(),
            getMonthName(yearMonth.getMonthValue()),
            monthToCountMap.getOrDefault(yearMonth.getMonthValue(), 0L), // Total count for the month
            monthToStatusCountMap.getOrDefault(yearMonth.getMonthValue(), Collections.emptyMap()) // Status counts map
        ))
        .toList();
  }


  private String getMonthName(int month) {
    return Month.of(month).getDisplayName(TextStyle.SHORT, Locale.CANADA);
  }

}
