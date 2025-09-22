package ca.bc.gov.restapi.results.postgres.dto;

import lombok.Builder;
import lombok.With;

/** This record contains all possible filters for the dashboard openings per years api. */
@Builder
@With
public record TenureDto(
    boolean isPrimary,
    String forestFileId,
    String cutBlock,
    String cuttingPermit,
    String timberMark) {}
