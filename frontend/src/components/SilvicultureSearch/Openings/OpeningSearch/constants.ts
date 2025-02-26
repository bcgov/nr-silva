import { ITableHeader } from "../../../../types/TableHeader";

export const defaultSearchTableHeaders: ITableHeader[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'openingId', header: 'Opening Id', selected: true },
  { key: 'openingNumber', header: 'Opening number', selected: false },
  { key: 'forestFileId', header: 'File Id', selected: true },
  { key: 'categoryDescription', header: 'Category', selected: true, elipsis: true },
  { key: 'orgUnitName', header: 'Org unit', selected: true },
  { key: 'statusDescription', header: 'Status', selected: true },
  { key: 'clientName', header: 'Client', selected: false },
  { key: 'timberMark', header: 'Timber mark', selected: false },
  { key: 'cuttingPermitId', header: 'Cutting permit', selected: true },
  { key: 'cutBlockId', header: 'Cut block', selected: true },
  { key: 'openingGrossAreaHa', header: 'Gross Area', selected: true },
  { key: 'disturbanceStartDate', header: 'Disturbance date', selected: true },
  { key: 'regenDelayDate', header: 'Regen delay due date', selected: false },
  { key: 'earlyFreeGrowingDate', header: 'Free growing due date', selected: false },
  { key: 'updateTimestamp', header: 'Update date', selected: false }
]
