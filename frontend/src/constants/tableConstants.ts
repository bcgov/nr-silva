import { ITableHeader } from "../types/TableHeader";

const searchScreenColumnDefinitions = [
  { key: 'actions', header: 'Actions' },
  { key: 'openingId', header: 'Opening Id' },
  { key: 'forestFileId', header: 'File Id' },
  { key: 'categoryDescription', header: 'Category', elipsis: true },
  { key: 'orgUnitName', header: 'Org unit' },
  { key: 'statusDescription', header: 'Status' },
  { key: 'cuttingPermitId', header: 'Cutting permit' },
  { key: 'cutBlockId', header: 'Cut block' },
  { key: 'openingGrossAreaHa', header: 'Gross Area' },
  { key: 'disturbanceStartDate', header: 'Disturbance Date' },
  { key: 'openingNumber', header: 'Opening Number' },
  { key: 'timberMark', header: 'Timber Mark' },
  { key: 'clientName', header: 'Client' },
  { key: 'regenDelayDate', header: 'Regen Delay Due Date' },
  { key: 'earlyFreeGrowingDate', header: 'Free Growing Due Date' },
  { key: 'updateTimestamp', header: 'Update Date' },
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
  { key: 'openingId', header: 'Opening Id' },
  { key: 'forestFileId', header: 'File Id' },
  { key: 'cuttingPermitId', header: 'Cutting permit' },
  { key: 'timberMark', header: 'Timber mark' },
  { key: 'cutBlockId', header: 'Cut block' },
  { key: 'openingGrossAreaHa', header: 'Gross Area' },
  { key: 'statusDescription', header: 'Status' },
  { key: 'categoryDescription', header: 'Category' },
  { key: 'disturbanceStartDate', header: 'Disturbance Date' },
  { key: 'actions', header: 'Actions' },
];

export const recentOpeningsColumns: ITableHeader[] = recentOpeningsColumnDefinitions.map((col) => ({
  ...col,
  selected: col.key !== 'disturbanceStartDate',
}));