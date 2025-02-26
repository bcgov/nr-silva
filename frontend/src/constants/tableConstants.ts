import { ITableHeader } from "../types/TableHeader";

export const openingSearchTableHeaders: ITableHeader[] = [
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

export const defaultsearchScreenColumnsSelection = [
  'actions',
  'openingId',
  'forestFileId',
  'categoryDescription',
  'orgUnitName',
  'statusDescription',
  'cuttingPermitId',
  'cutBlockId',
  'openingGrossAreaHa',
  'disturbanceStartDate'
];

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
  { key: 'updateTimestamp', header: 'Update date' }
];

export const filterKeyValueMap = {
  mainSearchTerm: 'Search Term',
  orgUnit: 'Org Unit',
  category: 'Category',
  statusList: 'Status',
  myOpenings: 'My Openings',
  submittedToFrpa: 'FRPA Section 108',
  disturbanceDateStart: 'Disturbance date start',
  disturbanceDateEnd: 'Disturbance date end',
  regenDelayDateStart: 'Regen delay date start',
  regenDelayDateEnd: 'Regen delay date end',
  freeGrowingDateStart: 'Free growing date start',
  freeGrowingDateEnd: 'Free growing date end',
  updateDateStart: 'Update date start',
  updateDateEnd: 'Update date end',
  cuttingPermitId: 'Cutting permit id',
  cutBlockId: 'Cut block id',
  clientLocationCode: 'Client location code',
  clientNumber: 'Client number',
  timberMark: 'Timber mark'
};



export const searchScreenColumns: ITableHeader[] = searchScreenColumnDefinitions
  .map((col) => ({...col, selected: defaultsearchScreenColumnsSelection.includes(col.key)}));
