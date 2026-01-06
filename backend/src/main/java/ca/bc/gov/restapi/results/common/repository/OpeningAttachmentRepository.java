package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.projection.opening.OpeningAttachmentMetaProjection;

import java.util.List;
import java.util.Optional;

public interface OpeningAttachmentRepository {
  List<OpeningAttachmentMetaProjection> findByOpeningId(Long openingId);

  Optional<OpeningAttachmentMetaProjection> findByAttachmentGuid(String attachmentGuidHex);
}
