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
            orgUnits == null ? List.of("NOVALUE") : orgUnits,
            statusCodes == null ? List.of("NOVALUE") : statusCodes
        );

    if (entities.isEmpty()) {
      log.info("No Opening Submission Trends data found!");
      return List.of();
    }

    // Map to count entries grouped by month
    Map<Integer, Long> monthToCountMap = entities.stream()
        .filter(entity -> entity.getEntryTimestamp() != null) // Ensure timestamp is not null
        .collect(Collectors.groupingBy(
            entity -> entity.getEntryTimestamp().getMonthValue(), // Extract month value
            Collectors.counting() // Count occurrences
        ));

    // Generate a 12-month sequence starting from the start date
    List<Integer> monthSequence = IntStream.range(0, 12) // Always 12 months
        .mapToObj(offset -> YearMonth.from(startDate).plusMonths(offset).getMonthValue())
        .toList();

    // Generate the DTOs in the custom order
    return monthSequence.stream()
        .map(month -> new OpeningsPerYearDto(
            month,
            getMonthName(month),
            monthToCountMap.getOrDefault(month, 0L) // Use 0 if no entries for this month
        ))
        .toList();
  }


  private String getMonthName(int month) {
    return Month.of(month).getDisplayName(TextStyle.SHORT, Locale.CANADA);
  }

}
