import { ITableHeader } from "../../types/TableHeader";

export const headers: ITableHeader[] = [
  {
    key: 'openingId',
    header: 'Opening Id',
    selected: true,
  },
  {
    key: 'forestFileId',
    header: 'File Id',
    selected: true,
  },
  {
    key: 'cuttingPermit',
    header: 'Cutting permit',
    selected: true,
  },
  {
    key: 'timberMark',
    header: 'Timber mark',
    selected: true,
  },
  {
    key: 'cutBlock',
    header: 'Cut block',
    selected: true,
  },
  {
    key: 'grossAreaHa',
    header: 'Gross area (ha)',
    selected: true,
  },
  {
    key: 'status',
    header: 'Status',
    selected: false,
  },
  {
    key: 'category',
    header: 'Category',
    selected: false,
  },
  {
    key: 'disturbanceStart',
    header: 'Disturbance start',
    selected: true,
  },
  {
    key: 'actions',
    header: 'Actions',
    selected: true,
  }
];

