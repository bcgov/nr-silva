import { OpeningHeaderType } from "@/types/TableHeader";

export const defaultSearchTableHeaders: OpeningHeaderType[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'openingId', header: 'Opening ID', selected: true },
  { key: 'forestFileId', header: 'File ID', selected: true },
  { key: 'cutBlockId', header: 'Cut block', selected: true },
  { key: 'cuttingPermitId', header: 'Cutting permit', selected: true },
  { key: 'mapsheetKey', header: 'Mapsheet key', selected: true },
  { key: 'disturbanceGrossArea', header: 'Disturbance Gross Area (ha)', selected: true },
  { key: 'category', header: 'Category', selected: true },
  { key: 'status', header: 'Status', selected: true },
  { key: 'licenseeOpeningId', header: 'Licensee opening ID', selected: true },
  { key: 'orgUnitName', header: 'Org unit', selected: false },
  { key: 'clientName', header: 'Client', selected: false },
  { key: 'timberMark', header: 'Timber mark', selected: false },
  { key: 'openingGrossAreaHa', header: 'Opening Gross Area (ha)', selected: false },
  { key: 'disturbanceStartDate', header: 'Disturbance date', selected: false },
  { key: 'regenDelayDate', header: 'Regen delay due date', selected: false },
  { key: 'earlyFreeGrowingDate', header: 'Free growing due date', selected: false },
  { key: 'updateTimestamp', header: 'Update date', selected: false }
];
