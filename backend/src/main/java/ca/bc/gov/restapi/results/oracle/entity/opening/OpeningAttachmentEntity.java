package ca.bc.gov.restapi.results.oracle.entity.opening;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "OPENING_ATTACHMENT", schema = "THE")
public class OpeningAttachmentEntity {

    @Id
    @Column(name = "OPENING_ATTACHMENT_FILE_ID", nullable = false)
    private Long attachmentFileId;

    @Column(name = "OPENING_ID", nullable = false)
    private Long openingId;

    @Column(name = "ATTACHMENT_NAME", nullable = false, length = 50)
    private String attachmentName;

    @Column(name = "ATTACHMENT_DESCRIPTION", length = 120)
    private String attachmentDescription;

    @Column(name = "MIME_TYPE_CODE", length = 3)
    private String mimeTypeCode;

    @Lob
    @Column(name = "ATTACHMENT_DATA", nullable = false)
    private byte[] attachmentData;

    @Column(name = "ENTRY_USERID", nullable = false, length = 30)
    private String entryUserId;

    @Column(name = "ENTRY_TIMESTAMP", nullable = false)
    private LocalDateTime entryTimestamp;

    @Column(name = "UPDATE_USERID", nullable = false, length = 30)
    private String updateUserId;

    @Column(name = "UPDATE_TIMESTAMP", nullable = false)
    private LocalDateTime updateTimestamp;

    @Column(name = "REVISION_COUNT", nullable = false)
    private Integer revisionCount;

    @Column(
            name = "OPENING_ATTACHMENT_GUID",
            nullable = false,
            columnDefinition = "RAW(16)"
    )
    @Convert(converter = ca.bc.gov.restapi.results.oracle.converter.UuidToBytesConverter.class)
    private UUID attachmentGuid;
}