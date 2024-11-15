import { ITableHeader } from "../../../../types/TableHeader";

// List of column definitions with key and header
const columnDefinitions = [
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

// Assign the selected flag to each column (true/false based on your requirements)
export const columns: ITableHeader[] = columnDefinitions.map((col) => ({
  ...col,
  selected: col.key !== 'disturbanceStartDate',  // Assuming 'Disturbance Date' is not selected
}));
