package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.repository.OpeningAttachmentRepository;
import ca.bc.gov.restapi.results.common.service.opening.details.AbstractOpeningDetailsAttachmentServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Opening Details Attachment Service | Legacy(Oracle primary)")
public class OpeningDetailsAttachmentOracleServiceTest extends
    AbstractOpeningDetailsAttachmentServiceTest<OpeningDetailsAttachmentOracleService> {

  @Override
  protected OpeningDetailsAttachmentOracleService createService(
      OpeningAttachmentRepository openingAttachmentRepository) {
    return new OpeningDetailsAttachmentOracleService(openingAttachmentRepository);
  }
}
