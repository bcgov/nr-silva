package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.common.dto.ExternalCutBlockDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;

/**
 * Response for {@code POST /api/tenures/validate}.
 *
 * @param validationResults per-tenure validation status and first error (if any)
 * @param duplicateConflicts list of duplicate tenure groups detected
 * @param isValid true only if all tenures are valid AND no duplicates exist
 * @param tenures the validated tenures with resolved timber mark; only populated when all valid
 * @param resolvedBlocks tenure index → resolved ExternalCutBlockDetails; not serialised to JSON
 */
public record TenureValidationResponseDto(
    List<TenureValidationResultDto> validationResults,
    List<DuplicateConflictDto> duplicateConflicts,
    boolean isValid,
    List<TenureDto> tenures,
    @JsonIgnore Map<Integer, ExternalCutBlockDetails> resolvedBlocks) {}
