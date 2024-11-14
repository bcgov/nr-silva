import { ITableHeader } from "../../../../types/TableHeader";

export const columns: ITableHeader[] = [
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
    key: 'cuttingPermitId',
    header: 'Cutting permit',
    selected: true
  },
  {
    key: 'timberMark',
    header: 'Timber mark',
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
    key: 'statusDescription',
    header: 'Status',
    selected: true
  },
  {
    key: 'categoryDescription',
    header: 'Category',
    selected: true
  },
  {
    key: 'disturbanceStartDate',
    header: 'Disturbance Date',
    selected: false
  },
  {
    key: 'actions',
    header: 'Actions',
    selected: true
  }
];