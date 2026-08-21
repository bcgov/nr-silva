package ca.bc.gov.restapi.results.postgres.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents a tenure returned from validation, including the resolved timber mark.
 *
 * @param fileId the forest file ID (forest_file_id)
 * @param cuttingPermit the cutting permit ID (cutting_permit_id); may be null
 * @param cutBlock the cut block ID (cut_block_id)
 * @param isPrimary whether this is the primary licence holder for the opening
 * @param timberMark the timber mark resolved from silva.cut_block
 */
public record TenureDto(
    @NotBlank @Size(max = 10) String fileId,
    @Size(max = 3) String cuttingPermit,
    @NotBlank @Size(max = 10) String cutBlock,
    boolean isPrimary,
    String timberMark) {}
