package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentEntity;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentMetaProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
   * Finds the attachment metadata by its GUID as hex string.
   *
   * @param attachmentGuidHex the lowercase hex string version of the GUID (RAWTOHEX)
   * @return Optional metadata projection
   */
  @Query(
      value =
          """
          SELECT
            OPENING_ATTACHMENT_FILE_ID AS openingAttachmentFileId,
            OPENING_ID AS openingId,
            ATTACHMENT_NAME AS attachmentName,
            ATTACHMENT_DESCRIPTION AS attachmentDescription,
            MIME_TYPE_CODE AS mimeTypeCode,
            ENTRY_USERID AS entryUserId,
            ENTRY_TIMESTAMP AS entryTimestamp,
            UPDATE_USERID AS updateUserId,
            UPDATE_TIMESTAMP AS updateTimestamp,
            REVISION_COUNT AS revisionCount,
            LOWER(RAWTOHEX(OPENING_ATTACHMENT_GUID)) AS attachmentGuid
          FROM THE.OPENING_ATTACHMENT
          WHERE LOWER(RAWTOHEX(OPENING_ATTACHMENT_GUID)) = :attachmentGuidHex
          """,
      nativeQuery = true)
  Optional<OpeningAttachmentMetaProjection> findByAttachmentGuid(
      @Param("attachmentGuidHex") String attachmentGuidHex);
}
