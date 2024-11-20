import { ITableHeader } from "../types/TableHeader";

export const searchScreenColumns: ITableHeader[] = [
  {
    key: 'actions',
    header: 'Actions',
    selected: true
  },
  {
    key: 'openingId',
    header: 'Opening Id',
    selected: true
  },
  {
    key: 'forestFileId',
    header: 'File Id',
    selected: true
  },
  {
    key: 'categoryDescription',
    header: 'Category',
    selected: true,
    elipsis: true
  },
  {
    key: 'orgUnitName',
    header: 'Org unit',
    selected: true
  },
  {
    key: 'statusDescription',
    header: 'Status',
    selected: true
  },
  {
    key: 'cuttingPermitId',
    header: 'Cutting permit',
    selected: true
  },
  {
    key: 'cutBlockId',
    header: 'Cut block',
    selected: true
  },
  {
    key: 'openingGrossAreaHa',
    header: 'Gross Area',
    selected: true
  },
  {
    key: 'disturbanceStartDate',
    header: 'Disturbance Date',
    selected: true
  },
  {
    key: 'openingNumber',
    header: 'Opening Number',
    selected: false
  },
  {
    key: 'timberMark',
    header: 'Timber Mark',
    selected: false
  },
  {
    key: 'clientName',
    header: 'Client',
    selected: false
  },
  {
    key: 'regenDelayDate',
    header: 'Regen Delay Due Date',
    selected: false
  },
  {
    key: 'earlyFreeGrowingDate',
    header: 'Free Growing Due Date',
    selected: false
  },
  {
    key: 'updateTimestamp',
    header: 'Update Date',
    selected: false
  }
];

// List of column definitions with key and header
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