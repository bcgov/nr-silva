package ca.bc.gov.restapi.results.postgres.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

/** This record contains all possible filters for the dashboard openings per years api. */
@RegisterReflectionForBinding
@Builder
@With
public record DashboardFiltersDto(
    String orgUnit,
    String status,
    LocalDateTime entryDateStart,
    LocalDateTime entryDateEnd,
    String clientNumber) {}
