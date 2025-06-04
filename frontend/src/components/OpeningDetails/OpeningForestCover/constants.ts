import { TableHeaderType } from "@/types/TableHeader";
import { ForestCoverFilterType } from "./definitions";
import { DEFAULT_PAGE_NUM, PageSizesConfig } from "@/constants/tableConstants";

// TODO: Replace 'any' with the actual type for forest cover data
export const ForestCoverTableHeaders: TableHeaderType<string>[] = [
  { key: "forestCover", header: "Forest cover" },
  { key: "polygonArea", header: "Polygon area (ha)" },
  { key: "stockingStatus", header: "Stocking status" },
  { key: "inventoryLayer", header: "Inventory layer" },
  { key: "silvicultureLayer", header: "Silviculture layer" },
  { key: "referenceYear", header: "Reference year" },
];

export const DefaultFilter: ForestCoverFilterType = {
  page: DEFAULT_PAGE_NUM,
  size: PageSizesConfig[0] as number,
} as const;
