import { TableHeaderType } from "@/types/TableHeader";
import { OpeningDetailsAttachmentMetaDto } from "@/services/OpenApi";

export const AttachmentTableHeaders: TableHeaderType<keyof OpeningDetailsAttachmentMetaDto>[] = [
  { key: "attachmentName", header: "File name" },
  { key: "attachmentDescription", header: "Description" },
  { key: "attachmentSize", header: "File size" },
  { key: "updateTimestamp", header: "Updated at" },
  { key: "updateUserId", header: "Updated by" },
  { key: "attachmentGuid", header: "Action" }
];
