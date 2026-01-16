package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.repository.OpeningAttachmentRepository;
import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class OpeningDetailsAttachmentService extends AbstractOpeningDetailsAttachmentService {

  public OpeningDetailsAttachmentService(OpeningAttachmentRepository openingAttachmentRepository) {
    super(openingAttachmentRepository);
  }

}
