package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.projection.opening.OpeningAttachmentMetaProjection;
import ca.bc.gov.restapi.results.common.repository.OpeningAttachmentRepository;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresQueryConstants;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningAttachmentEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CRUD operations and custom queries against the
 * `silva.opening_attachment` table in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface OpeningAttachmentPostgresRepository
    extends JpaRepository<OpeningAttachmentEntity, Long>, OpeningAttachmentRepository {

  /**
   * Finds all attachments for a given Opening ID.
   *
   * @param openingId The ID of the opening.
   * @return List of attachments.
   */
  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_ATTACHMENT_LIST)
  List<OpeningAttachmentMetaProjection> findByOpeningId(Long openingId);

  /**
   * Finds the attachment metadata by its GUID as hex string.
   *
   * @param attachmentGuidHex the lowercase hex string version of the GUID (RAWTOHEX)
   * @return Optional metadata projection
   */
  @Override
  @Query(
      value =
          """
          SELECT
            opening_attachment_file_id AS openingAttachmentFileId,
            opening_id AS openingId,
            attachment_name AS attachmentName,
            attachment_description AS attachmentDescription,
            mime_type_code AS mimeTypeCode,
            entry_userid AS entryUserId,
            entry_timestamp AS entryTimestamp,
            update_userid AS updateUserId,
            update_timestamp AS updateTimestamp,
            revision_count AS revisionCount,
            opening_attachment_guid::text, '-', '' AS attachmentGuid
          FROM silva.opening_attachment
          WHERE opening_attachment_guid::text, '-', '' = :attachmentGuidHex
          """,
      nativeQuery = true)
  Optional<OpeningAttachmentMetaProjection> findByAttachmentGuid(
      @Param("attachmentGuidHex") String attachmentGuidHex);

}
