import { useMemo, useState } from "react";
import PaginationContext, { PaginationContextData } from "./PaginationContext";

const PaginationProvider: React.FC<{children: React.ReactNode}> = ({ children }) => {
  const [data, setData] = useState<any[]>([]);
  const [initialItemsPerPage, setInitialItemsPerPage] = useState<number>(20);
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [itemsPerPage, setItemsPerPage] = useState<number>(20);
  const [totalResultItems, setTotalResultItems] = useState<number>(0); // State for totalResultItems

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
    setTotalResultItems(data.length); // Update totalResultItems
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
    totalResultItems, // Expose totalResultItems to the context
    setTotalResultItems, // Expose setter for totalResultItems
  }), [
    getCurrentData,
    currentPage,
    totalPages,
    handlePageChange,
    handleItemsPerPageChange,
    itemsPerPage,
    setPageData,
    setInitialItemsPerPage,
    totalResultItems, // Include in dependencies
  ]);

  return (
    <PaginationContext.Provider value={contextValue}>
      {children}
    </PaginationContext.Provider>
  );
};

export default PaginationProvider;
