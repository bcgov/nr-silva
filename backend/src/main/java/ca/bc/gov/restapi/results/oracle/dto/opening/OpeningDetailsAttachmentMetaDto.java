package ca.bc.gov.restapi.results.oracle.dto.opening;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Represents the metadata of an attachment associated with an Opening record, excluding the actual
 * binary attachment data.
 *
 * <p>Maps to the {@code THE.OPENING_ATTACHMENT} table, excluding the BLOB column.
 *
 * <p>The {@code OPENING_ATTACHMENT_GUID} is the globally unique identifier (GUID) used for
 * referencing and downloading the attachment instead of the primary key ID.
 */
@Schema(description = "Metadata about an attachment associated with an Opening.")
public record OpeningDetailsAttachmentMetaDto(
    @Schema(
            description = "ID of the associated opening.",
            example = "5678",
            requiredMode = Schema.RequiredMode.REQUIRED)
        Long openingId,
    @Schema(
            description = "Name of the attachment file.",
            example = "Permit_Document.pdf",
            requiredMode = Schema.RequiredMode.REQUIRED)
        String attachmentName,
    @Schema(
            types = {"string", "null"},
            description = "Description of the attachment file (optional).",
            example = "Approved forestry permit")
        String attachmentDescription,
    @Schema(
            types = {"string", "null"},
            description = "MIME type code referencing THE.MIME_TYPE_CODE (optional).",
            example = "PDF")
        String mimeTypeCode,
    @Schema(
            description = "User ID of who created the attachment.",
            example = "IDIR/JDOE",
            requiredMode = Schema.RequiredMode.REQUIRED)
        String entryUserId,
    @Schema(
            description = "Timestamp when the attachment was first added.",
            requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime entryTimestamp,
    @Schema(
            description = "User ID of who last updated the attachment.",
            example = "IDIR/ASNOW",
            requiredMode = Schema.RequiredMode.REQUIRED)
        String updateUserId,
    @Schema(
            description = "Timestamp of the most recent update to the attachment.",
            requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime updateTimestamp,
    @Schema(
            description = "Revision count for concurrency control.",
            example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED)
        int revisionCount,
    @Schema(
            description =
                "Globally unique identifier (GUID) used to securely reference the attachment.",
            example = "0A6147F904F56F9EE0633A54228E33C0",
            requiredMode = Schema.RequiredMode.REQUIRED)
        String attachmentGuid) {}
