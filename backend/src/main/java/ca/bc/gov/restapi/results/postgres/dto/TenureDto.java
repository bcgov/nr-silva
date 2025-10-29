package ca.bc.gov.restapi.results.postgres.dto;

import lombok.Builder;
import lombok.With;

/**
 * This record represents tenure information, including primary status, forest file ID, cut block,
 * cutting permit, and timber mark.
 */
@Builder
@With
public record TenureDto(
    boolean isPrimary,
    String forestFileId,
    String cutBlock,
    String cuttingPermit,
    String timberMark) {}
