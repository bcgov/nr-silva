package ca.bc.gov.restapi.results.common.service.opening.details;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsAttachmentMetaDto;

import java.util.List;

public interface OpeningDetailsAttachmentService {
  public List<OpeningDetailsAttachmentMetaDto> getAttachmentList(Long openingId);
  public String getS3PresignedUrl(String guid);
}
