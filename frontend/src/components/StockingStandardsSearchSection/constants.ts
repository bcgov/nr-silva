import { StockingStandardsHeaderType } from "@/types/TableHeader";

export const defaultStockingStandardsSearchTableHeaders: StockingStandardsHeaderType[] = [
  { key: 'standardsRegimeId', header: 'Standards ID', selected: true },
  { key: 'standardsRegimeName', header: 'Standards name', selected: true },
  { key: 'standardsObjective', header: 'Objective', selected: true },
  { key: 'bgc', header: 'BGC', selected: true },
  { key: 'clients', header: 'Client', selected: true },
  { key: 'fspIds', header: 'FSP ID', selected: true },
  { key: 'orgUnits', header: 'Org unit', selected: false },
  { key: 'preferredSpecies', header: 'Preferred species', selected: false },
  { key: 'updateTimestamp', header: 'Last updated', selected: false },

];
