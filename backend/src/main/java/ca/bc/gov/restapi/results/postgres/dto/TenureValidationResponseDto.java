package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.entity.CutBlockEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;

/**
 * Response for {@code POST /api/tenures/validate}.
 *
 * @param validationResults per-tenure validation status and first error (if any)
 * @param duplicateConflicts list of duplicate tenure groups detected
 * @param isValid true only if all tenures are valid AND no duplicates exist
 * @param resolvedBlocks tenure index → resolved CutBlockEntity; not serialised to JSON
 */
public record TenureValidationResponseDto(
    List<TenureValidationResultDto> validationResults,
    List<DuplicateConflictDto> duplicateConflicts,
    boolean isValid,
    @JsonIgnore Map<Integer, CutBlockEntity> resolvedBlocks) {}
