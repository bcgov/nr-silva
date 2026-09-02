package ca.bc.gov.restapi.results.postgres.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Tenure item supplied when replacing an opening's complete tenure list. */
public record TenureUpdateItemDto(
    Long cboaId,
    Integer revisionCount,
    @NotBlank @Size(max = 10) String fileId,
    @Size(max = 3) String cuttingPermit,
    @NotBlank @Size(max = 10) String cutBlock,
    boolean isPrimary) {

  /**
   * Drops edit-only identity metadata for shared tenure validation.
   *
   * @return request DTO containing only tenure business fields
   */
  public TenureRequestDto toTenureRequest() {
    return new TenureRequestDto(fileId, cuttingPermit, cutBlock, isPrimary);
  }
}
