package ca.bc.gov.restapi.results.common.dto;

/**
 * Domain record capturing summary information for an external tenure cut block.
 *
 * @param forestFileId tenure file ID (e.g. TFL001)
 * @param cuttingPermitId cutting permit identifier (may be null)
 * @param cutBlockId cut block identifier within the tenure
 * @param timberMark authoritative timber mark for transportation and stumpage
 * @param licenseeClientNumber client number of the primary licensee
 * @param status operational status code (e.g. "ACT")
 * @param cbSkey master surrogate key for the cut block in FTA (nullable)
 */
public record ExternalTenureSummary(
    String forestFileId,
    String cuttingPermitId,
    String cutBlockId,
    String timberMark,
    String licenseeClientNumber,
    String status,
    Long cbSkey) {

  /**
   * Convenience constructor without cbSkey for standard external summaries.
   *
   * @param forestFileId tenure file ID
   * @param cuttingPermitId cutting permit identifier (may be null)
   * @param cutBlockId cut block identifier
   * @param timberMark timber mark
   * @param licenseeClientNumber licensee client number
   * @param status operational status code
   */
  public ExternalTenureSummary(
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      String timberMark,
      String licenseeClientNumber,
      String status) {
    this(forestFileId, cuttingPermitId, cutBlockId, timberMark, licenseeClientNumber, status, null);
  }
}

