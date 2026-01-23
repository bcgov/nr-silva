package ca.bc.gov.restapi.results.postgres.service.opening.details;

import ca.bc.gov.restapi.results.common.repository.OpeningAttachmentRepository;
import ca.bc.gov.restapi.results.common.service.opening.details.AbstractOpeningDetailsAttachmentServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Opening Details Attachment Service | Postgres-only")
public class OpeningDetailsAttachmentPostgresServiceTest extends
    AbstractOpeningDetailsAttachmentServiceTest<OpeningDetailsAttachmentPostgresService> {

  @Override
  protected OpeningDetailsAttachmentPostgresService createService(
      OpeningAttachmentRepository openingAttachmentRepository) {
    return new OpeningDetailsAttachmentPostgresService(openingAttachmentRepository);
  }
}
