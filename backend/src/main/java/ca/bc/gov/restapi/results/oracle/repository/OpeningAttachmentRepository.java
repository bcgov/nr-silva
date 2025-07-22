package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentEntity;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentMetaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OpeningAttachmentRepository extends JpaRepository<OpeningAttachmentEntity, Long> {

    /**
     * Finds all attachments for a given Opening ID.
     *
     * @param openingId The ID of the opening.
     * @return List of attachments.
     */
    @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_ATTACHMENT_LIST)
    List<OpeningAttachmentMetaProjection> findByOpeningId(Long openingId);

    /**
     * Finds the full attachment entity (including file data) by its GUID.
     *
     * @param attachmentGuid GUID of the attachment.
     * @return Optional containing the attachment if found.
     */
    Optional<OpeningAttachmentEntity> findByAttachmentGuid(UUID attachmentGuid);
}