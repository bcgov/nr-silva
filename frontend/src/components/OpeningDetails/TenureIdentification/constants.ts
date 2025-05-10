import { TableHeaderType } from "@/types/TableHeader";
import { OpeningTenureDto } from "../../../types/OpeningTypes";

export const TenureTableHeaders: TableHeaderType<keyof OpeningTenureDto>[] = [
  { key: "fileId", header: "File ID" },
  { key: "cutBlock", header: "Cut block" },
  { key: "cuttingPermit", header: "Cutting permit" },
  { key: "timberMark", header: "Timber mark" },
  { key: "status", header: "Status" },
  { key: "plannedGrossArea", header: "Planned gross area (ha)" },
  { key: "plannedNetArea", header: "Planned net area (ha)" }
];
