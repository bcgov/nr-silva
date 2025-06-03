import { TableHeaderType } from "@/types/TableHeader";
import { ForestCoverFilterType } from "./definitions";
import { DEFAULT_PAGE_NUM, PageSizesConfig } from "@/constants/tableConstants";

// TODO: Replace 'any' with the actual type for forest cover data
export const ForestCoverTableHeaders: TableHeaderType<keyof any>[] = [
  { key: "forestCover", header: "Forest cover", sortable: true },
  { key: "polygonArea", header: "Polygon area (ha)", sortable: true },
  { key: "stockingStatus", header: "Stocking status", sortable: true },
  { key: "inventoryLayer", header: "Inventory layer", sortable: true },
  { key: "silvicultureLayer", header: "Silviculture layer", sortable: true },
  { key: "referenceYear", header: "Reference year", sortable: true },
];

export const DefaultFilter: ForestCoverFilterType = {
  page: DEFAULT_PAGE_NUM,
  size: PageSizesConfig[0],
} as const;
