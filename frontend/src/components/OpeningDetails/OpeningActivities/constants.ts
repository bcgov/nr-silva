import { TableHeaderType } from "@/types/TableHeader";
import { ActivityFilterType } from "./definitions";
import { OpeningDetailsActivitiesActivitiesDto, OpeningDetailsActivitiesDisturbanceDto } from "@/types/OpeningTypes";
import { DEFAULT_PAGE_NUM, OddPageSizesConfig } from "@/constants/tableConstants";

export const COMPLEX_ACTIVITY_CODE = [
  'DS', 'JS', 'PL', 'PR', 'SP', 'SU'
] as const;

export const DisturbanceTableHeaders: TableHeaderType<keyof OpeningDetailsActivitiesDisturbanceDto>[] = [
  { key: "atuId", header: "Activity treatment unit ID" },
  { key: "disturbance", header: "Disturbance code" },
  { key: "system", header: "Silviculture system" },
  { key: "variant", header: "Variant" },
  { key: "cutPhase", header: "Cut phase" },
  { key: "disturbanceArea", header: "Disturbance area (ha)" },
  { key: "lastUpdatedOn", header: "Last updated" },
  { key: "startDate", header: "Start date" },
  { key: "endDate", header: "End date" },
];

export const ActivityTableHeaders: TableHeaderType<keyof OpeningDetailsActivitiesActivitiesDto>[] = [
  { key: "atuId", header: "Activity treatment unit ID", sortable: true },
  { key: "status", header: "Status", sortable: true },
  { key: "base", header: "Base", sortable: true },
  { key: "tech", header: "Tech", sortable: true },
  { key: "method", header: "Method", sortable: true },
  { key: "objective1", header: "Objective", sortable: true },
  { key: "area", header: "Area", sortable: true },
  { key: "funding", header: "Funding Source", sortable: true },
  { key: "projectId", header: "Project ID", sortable: true },
  { key: "lastUpdate", header: "Last updated", sortable: true },
  { key: "plannedDate", header: "Planned date", sortable: true },
  { key: "endDate", header: "End date", sortable: true },
];

export const DefaultFilter: ActivityFilterType = {
  page: DEFAULT_PAGE_NUM,
  size: OddPageSizesConfig[0],
} as const;

