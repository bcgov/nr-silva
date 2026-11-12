package ca.bc.gov.restapi.results.common.dto;

/**
 * Domain record encapsulating cut block details verified against the external authority (e.g. FTA).
 *
 * <p>Note: Opening associations and availability are owned by Silva's persistence store
 * ({@code silva.cut_block_open_admin}) and are evaluated by Silva domain services, not the
 * external provider.
 *
 * @param exists indicates whether the cut block was found in the external system of record
 * @param cbSkey the master surrogate key of the cut block in FTA (nullable if not found)
 * @param clientIsLicensee flag indicating if the requesting client is an authorized licensee
 * @param timberMark the authoritative timber mark for the cut block
 * @param status the operational cut block status code (e.g. "ACT", "S")
 */
public record ExternalCutBlockDetails(
    boolean exists,
    Long cbSkey,
    boolean clientIsLicensee,
    String timberMark,
    String status) {

  /**
   * Factory method representing a cut block that does not exist in the external system.
   *
   * @return cut block details with exists=false and null attributes
   */
  public static ExternalCutBlockDetails notFound() {
    return new ExternalCutBlockDetails(false, null, false, null, null);
  }

  /**
   * Factory method for creating verified cut block details.
   *
   * @param cbSkey surrogate key
   * @param clientIsLicensee whether client is licensee
   * @param timberMark timber mark
   * @param status operational status
   * @return verified cut block details
   */
  public static ExternalCutBlockDetails of(
      Long cbSkey, boolean clientIsLicensee, String timberMark, String status) {
    return new ExternalCutBlockDetails(true, cbSkey, clientIsLicensee, timberMark, status);
  }
}

