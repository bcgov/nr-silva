import { TableHeaderType } from "@/types/TableHeader";
import { ForestCoverFilterType } from "./definitions";
import { DEFAULT_PAGE_NUM, PageSizesConfig } from "@/constants/tableConstants";

// TODO: Replace 'any' with the actual type for forest cover data
export const ForestCoverTableHeaders: TableHeaderType<keyof any>[] = [
  { key: "forestCover", header: "Forest Cover", sortable: true },
  { key: "polygonArea", header: "Polygon Area (ha)", sortable: true },
  { key: "stockingStatus", header: "Stocking Status", sortable: true },
  { key: "inventoryLayer", header: "Inventory Layer", sortable: true },
  { key: "silvicultureLayer", header: "Silviculture Layer", sortable: true },
  { key: "referenceYear", header: "Reference Year", sortable: true },
];

export const DefaultFilter: ForestCoverFilterType = {
  page: DEFAULT_PAGE_NUM,
  size: PageSizesConfig[0],
} as const;
