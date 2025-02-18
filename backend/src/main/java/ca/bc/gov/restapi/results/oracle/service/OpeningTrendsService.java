package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.oracle.entity.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    Map<String, Map<String, Long>> dateToStatusCountMap = entities.stream()
        .filter(entity -> entity.getEntryTimestamp() != null) // Ensure timestamp is not null
        .collect(Collectors.groupingBy(
            entity -> getDateKey(entity.getEntryTimestamp()), // Extract month value
            Collectors.groupingBy(
                OpeningTrendsProjection::getStatus, // Group by status
                Collectors.counting() // Count occurrences
            )
        ));

    // Map to count total entries grouped by month
    Map<String, Long> monthToCountMap = dateToStatusCountMap.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey, // Month
            entry -> entry.getValue().values().stream().mapToLong(Long::longValue).sum() // Sum counts per status
        ));

    // Generate a 12-month sequence starting from the start date
    List<YearMonth> yearMonths = IntStream.range(0, 13) // Always 12 months
        .mapToObj(offset -> YearMonth.from(endDate).minusMonths(offset)) // Generate the sequence
        // Filter out dates before the start date
        // We use this combination as we want to include the start date in the sequence
        .filter(yearMonth -> !yearMonth.isBefore(YearMonth.from(startDate)))
        .sorted() // Sort in ascending order
        .toList();

    // Generate the DTOs in the custom order
    return yearMonths.stream()
        .map(yearMonth -> new OpeningsPerYearDto(
            yearMonth.getMonthValue(),
            yearMonth.getYear(),
            getMonthName(yearMonth.getMonthValue()),
            monthToCountMap.getOrDefault(getDateKey(yearMonth), 0L), // Total count for the month
            dateToStatusCountMap.getOrDefault(getDateKey(yearMonth), Collections.emptyMap()) // Status counts map
        ))
        .toList();
  }


  private String getMonthName(int month) {
    return Month.of(month).getDisplayName(TextStyle.SHORT, Locale.CANADA);
  }

  private String getDateKey(LocalDateTime entryTimestamp) {
    return String.format("%04d-%02d", entryTimestamp.getYear(), entryTimestamp.getMonthValue());
  }

  private String getDateKey(YearMonth entryTimestamp) {
    return String.format("%04d-%02d", entryTimestamp.getYear(), entryTimestamp.getMonthValue());
  }

}
