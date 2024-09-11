import { createContext } from "react";

export interface PaginationContextData {
  getCurrentData(): void;
  currentPage: number;
  totalPages: number;
  handlePageChange(page: number): void;
  handleItemsPerPageChange(page: number, pageSize: number): void;
  itemsPerPage: number;
  setPageData(data: any[]): void;
  setInitialItemsPerPage(items: number): void;
  totalResultItems: number; // Update to totalResultItems
  setTotalResultItems(totalResultItems: number): void; // Update to setTotalResultItems
}

const PaginationContext = createContext<PaginationContextData>({} as PaginationContextData);

export default PaginationContext;
