import { useMemo, useState } from "react";
import PaginationContext, { PaginationContextData } from "./PaginationContext";

const PaginationProvider: React.FC<{children: React.ReactNode}> = ({ children }) => {
  const [data, setData] = useState<any[]>([]);
  const [initialItemsPerPage, setInitialItemsPerPage] = useState<number>(0);
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [itemsPerPage, setItemsPerPage] = useState<number>(5);

  // Update the total number of pages when itemsPerPage changes
  const totalPages = Math.ceil(data.length / itemsPerPage);

  // Get the current page data
  const getCurrentData = () => {
    const start = (currentPage - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    return data.slice(start, end);
  };

  // Update the current page when the user changes the page
  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

  // Update the items per page when the user changes the value
  const handleItemsPerPageChange = (page: number, pageSize: number) => {
    setCurrentPage(page);
    setItemsPerPage(pageSize);
  };

  const setPageData = (data: any[]): void => {
    setData(data);
  };

  const contextValue: PaginationContextData = useMemo(() => ({
    getCurrentData,
    currentPage,
    totalPages,
    handlePageChange,
    handleItemsPerPageChange,
    itemsPerPage,
    setPageData,
    setInitialItemsPerPage,
  }), [
    getCurrentData,
    currentPage,
    totalPages,
    handlePageChange,
    handleItemsPerPageChange,
    itemsPerPage,
    setPageData,
    setInitialItemsPerPage,
  ]);

  return (
    <PaginationContext.Provider value={contextValue}>
      { children }
    </PaginationContext.Provider>
  )
};

export default PaginationProvider;
