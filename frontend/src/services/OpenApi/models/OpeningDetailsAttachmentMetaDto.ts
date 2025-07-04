/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
/**
 * Metadata about an attachment associated with an Opening.
 */
export type OpeningDetailsAttachmentMetaDto = {
    /**
     * ID of the associated opening.
     */
    openingId: number;
    /**
     * Name of the attachment file.
     */
    attachmentName: string;
    /**
     * Description of the attachment file (optional).
     */
    attachmentDescription?: string | null;
    /**
     * MIME type code referencing THE.MIME_TYPE_CODE (optional).
     */
    mimeTypeCode?: string | null;
    /**
     * User ID of who created the attachment.
     */
    entryUserId: string;
    /**
     * Timestamp when the attachment was first added.
     */
    entryTimestamp: string;
    /**
     * User ID of who last updated the attachment.
     */
    updateUserId: string;
    /**
     * Timestamp of the most recent update to the attachment.
     */
    updateTimestamp: string;
    /**
     * Revision count for concurrency control.
     */
    revisionCount: number;
    /**
     * Globally unique identifier (GUID) used to securely reference the attachment.
     */
    attachmentGuid: string;
    /**
     * Size of the attachment file in bytes.
     */
    attachmentSize?: number;
};

