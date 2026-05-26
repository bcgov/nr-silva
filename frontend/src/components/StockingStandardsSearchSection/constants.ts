import { StockingStandardsHeaderType } from "@/types/TableHeader";

export const defaultStockingStandardsSearchTableHeaders: StockingStandardsHeaderType[] = [
  { key: 'standardsRegimeId', header: 'Standards ID', selected: true },
  { key: 'standardsRegimeName', header: 'Standards name', selected: true },
  { key: 'standardsObjective', header: 'Objective', selected: true },
  { key: 'bgc', header: 'BGC', selected: true },
];
