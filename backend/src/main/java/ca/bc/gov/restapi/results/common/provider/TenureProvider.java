package ca.bc.gov.restapi.results.common.provider;

import ca.bc.gov.restapi.results.common.dto.ExternalCutBlockDetails;
import ca.bc.gov.restapi.results.common.dto.ExternalTenureSummary;
import java.util.Optional;

/**
 * Functional port for retrieving and validating external tenure and cut block data.
 *
 * <p>Implementations translate external data sources (Oracle DB, REST API, or Mock fixtures) into
 * clean Silva domain records.
 */
public interface TenureProvider {

  /**
   * Validates cut block existence and licensee authorization against the external authority.
   *
   * @param forestFileId tenure file ID (e.g. TFL001)
   * @param cuttingPermitId cutting permit (may be null)
   * @param cutBlockId cut block ID
   * @param clientNumber client number claiming licensee rights
   * @return external cut block verification details
   */
  ExternalCutBlockDetails verifyCutBlock(
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      String clientNumber);

  /**
   * Retrieves summary tenure details for a cut block.
   *
   * @param forestFileId tenure file ID
   * @param cuttingPermitId cutting permit (may be null)
   * @param cutBlockId cut block ID
   * @return optional containing the tenure summary if found, or empty if not found
   */
  Optional<ExternalTenureSummary> getTenureSummary(
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId);
}

