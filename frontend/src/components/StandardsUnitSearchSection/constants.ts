import { StandardsUnitHeaderType } from "@/types/TableHeader";

export const PREFERRED_SPECIES_LIMIT = 3 as const;

export const defaultStandardsUnitSearchTableHeaders: StandardsUnitHeaderType[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'openingId', header: 'Opening ID', selected: true },
  { key: 'fileId', header: 'File ID', selected: true },
  { key: 'cutBlock', header: 'Cut block', selected: true },
  { key: 'cuttingPermit', header: 'Cutting permit', selected: false },
  { key: 'standardsUnitId', header: 'SU', selected: true },
  { key: 'standardsRegimeId', header: 'SSID', selected: true },
  { key: 'netArea', header: 'NAR (ha)', selected: true },
  { key: 'dueDates', header: 'Due dates', selected: true },
  { key: 'totalLayer', header: 'Total layer', selected: true },
  { key: 'preferredSpecies', header: 'Preferred species', selected: true },
  { key: 'bgc', header: 'BGC', selected: false },
  { key: 'orgUnit', header: 'Org unit', selected: false },
  { key: 'openingClient', header: 'Client', selected: false },
  { key: 'updateTimestamp', header: 'Update date', selected: false },
];
