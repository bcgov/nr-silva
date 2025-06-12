import { TableHeaderType } from "@/types/TableHeader";
import { OpeningDetailsTenureDto } from "@/services/OpenApi";
import { DEFAULT_PAGE_NUM, OddPageSizesConfig } from "@/constants/tableConstants";
import { TenureFilterType } from "./definitions";

export const TenureTableHeaders: TableHeaderType<keyof OpeningDetailsTenureDto>[] = [
  { key: "fileId", header: "File ID", sortable: true },
  { key: "cutBlock", header: "Cut block", sortable: true },
  { key: "cuttingPermit", header: "Cutting permit", sortable: true },
  { key: "timberMark", header: "Timber mark", sortable: true },
  { key: "status", header: "Status", sortable: true },
  { key: "plannedGrossArea", header: "Planned gross area (ha)", sortable: true },
  { key: "plannedNetArea", header: "Planned net area (ha)", sortable: true }
];

export const DefaultFilter: TenureFilterType = {
  page: DEFAULT_PAGE_NUM,
  size: OddPageSizesConfig[0]!,
} as const;
