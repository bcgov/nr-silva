package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentEntity;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentMetaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OpeningAttachmentRepository extends JpaRepository<OpeningAttachmentEntity, Long> {

    /**
     * Finds all attachments for a given Opening ID.
     *
     * @param openingId The ID of the opening.
     * @return List of attachments.
     */
    @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_ATTACHMENT_LIST)
    List<OpeningAttachmentMetaProjection> findByOpeningId(Long openingId);
}