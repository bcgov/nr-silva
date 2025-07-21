import { TableHeaderType } from "@/types/TableHeader";
import { OpeningForestCoverDto } from "@/services/OpenApi";

export const ForestCoverTableHeaders: TableHeaderType<keyof OpeningForestCoverDto>[] = [
  { key: "coverId", header: "Forest cover" },
  { key: "isSingleLayer", header: "Quick information" },
  { key: "grossArea", header: "Polygon area (ha)" },
  { key: "status", header: "Stocking status" },
  { key: 'coverType', header: 'Stocking type' },
  { key: "inventoryLayer", header: "Inventory layer" },
  { key: "silvicultureLayer", header: "Silviculture layer" },
  { key: "referenceYear", header: "Reference year" },
];

export const EXPAND_PROMPT = "Expand row for details"
