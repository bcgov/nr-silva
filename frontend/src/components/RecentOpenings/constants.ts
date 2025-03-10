import { OpendingHeaderKeyType, TableHeaderType } from "@/types/TableHeader";

export const recentOpeningsHeaders: TableHeaderType<OpendingHeaderKeyType>[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'openingId', header: 'Opening ID', selected: true },
  { key: 'forestFileId', header: 'File ID', selected: true },
  { key: 'category', header: 'Category', selected: true },
  { key: 'status', header: 'Status', selected: true },
  { key: 'cuttingPermitId', header: 'Cutting permit', selected: true },
  { key: 'timberMark', header: 'Timber mark', selected: true },
  { key: 'cutBlockId', header: 'Cut block', selected: true },
  { key: 'openingGrossAreaHa', header: 'Gross Area (ha)', selected: true },
  { key: 'disturbanceStartDate', header: 'Disturbance start', selected: true }
];
