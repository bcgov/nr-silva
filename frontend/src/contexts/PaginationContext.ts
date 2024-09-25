import { createContext } from "react";

export interface PaginationContextData {
  getCurrentData(): void;
  currentPage: number;
  totalPages: number;
  handlePageChange(page: number): void;
  handleItemsPerPageChange(page: number, pageSize: number): void;
  itemsPerPage: number;
  setPageData(data: object[]): void;
  setInitialItemsPerPage(items: number): void;
}

const PaginationContext = createContext<PaginationContextData>({} as PaginationContextData);

export default PaginationContext;
