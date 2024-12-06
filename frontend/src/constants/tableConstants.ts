import { ITableHeader } from "../types/TableHeader";

const searchScreenColumnDefinitions = [
  { key: 'actions', header: 'Actions' },
  
  { key: 'openingId', header: 'Opening Id' },
  { key: 'openingNumber', header: 'Opening number' },
  { key: 'forestFileId', header: 'File Id' },
  { key: 'categoryDescription', header: 'Category', elipsis: true },
  { key: 'orgUnitName', header: 'Org unit' },
  { key: 'statusDescription', header: 'Status' },
  { key: 'clientName', header: 'Client' },
  { key: 'timberMark', header: 'Timber mark' },
  { key: 'cuttingPermitId', header: 'Cutting permit' },
  { key: 'cutBlockId', header: 'Cut block' },
  { key: 'openingGrossAreaHa', header: 'Gross Area' },
  { key: 'disturbanceStartDate', header: 'Disturbance date' },
  { key: 'regenDelayDate', header: 'Regen delay due date' },
  { key: 'earlyFreeGrowingDate', header: 'Free growing due date' },
  { key: 'updateTimestamp', header: 'Update date' },
];

export const searchScreenColumns: ITableHeader[] = searchScreenColumnDefinitions.map((col) => ({
  ...col,
  selected: [
    'actions',
    'openingId',
    'forestFileId',
    'categoryDescription',
    'orgUnitName',
    'statusDescription',
    'cuttingPermitId',
    'cutBlockId',
    'openingGrossAreaHa',
    'disturbanceStartDate',
  ].includes(col.key),
}));

const recentOpeningsColumnDefinitions = [
  { key: 'actions', header: 'Actions' },
  { key: 'openingId', header: 'Opening Id' },
  { key: 'forestFileId', header: 'File Id' },
  { key: 'cuttingPermitId', header: 'Cutting permit' },
  { key: 'timberMark', header: 'Timber mark' },
  { key: 'cutBlockId', header: 'Cut block' },
  { key: 'openingGrossAreaHa', header: 'Gross Area' },
  { key: 'statusDescription', header: 'Status' },
  { key: 'categoryDescription', header: 'Category' },
  { key: 'disturbanceStartDate', header: 'Disturbance Date' }
];

export const recentOpeningsColumns: ITableHeader[] = recentOpeningsColumnDefinitions.map((col) => ({
  ...col,
  selected: col.key !== 'disturbanceStartDate',
}));