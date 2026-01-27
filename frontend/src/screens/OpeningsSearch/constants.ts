import { OpeningHeaderType } from "../../types/TableHeader";

export const defaultSearchTableHeaders: OpeningHeaderType[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'openingId', header: 'Opening Id', selected: true },
  { key: 'forestFileId', header: 'File Id', selected: true },
  { key: 'cutBlockId', header: 'Cut block', selected: true },
  { key: 'cuttingPermitId', header: 'Cutting permit', selected: true },
  { key: 'openingNumber', header: 'Mapsheet key', selected: true },
  { key: 'category', header: 'Category', selected: true },
  { key: 'status', header: 'Status', selected: true },
  { key: 'orgUnitName', header: 'Org unit', selected: true },
  { key: 'clientName', header: 'Client', selected: false },
  { key: 'timberMark', header: 'Timber mark', selected: false },
  { key: 'openingGrossAreaHa', header: 'Gross Area (ha)', selected: true },
  { key: 'disturbanceStartDate', header: 'Disturbance date', selected: true },
  { key: 'regenDelayDate', header: 'Regen delay due date', selected: false },
  { key: 'earlyFreeGrowingDate', header: 'Free growing due date', selected: false },
  { key: 'updateTimestamp', header: 'Update date', selected: false }
];
