import { OpendingHeaderKeyType, TableHeaderType } from "@/types/TableHeader";

export const myOpeningsHeaders: TableHeaderType<OpendingHeaderKeyType>[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'openingId', header: 'Opening ID', selected: true },
  { key: 'forestFileId', header: 'File ID', selected: true },
  { key: 'cutBlockId', header: 'Cut block', selected: true },
  { key: 'cuttingPermitId', header: 'Cutting permit', selected: true },
  { key: 'openingGrossAreaHa', header: 'Opening gross area (ha)', selected: true },
  { key: 'disturbanceGrossArea', header: 'Disturbance gross area (ha)', selected: true },
  { key: 'category', header: 'Category', selected: true },
  { key: 'clientNumber', header: 'Client', selected: true },
  { key: 'status', header: 'Status', selected: true }
];
