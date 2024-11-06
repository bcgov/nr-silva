import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { describe, expect, it, vi } from "vitest";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import OpeningsTab from "../../../src/components/OpeningsTab";
import { Provider } from "react-redux";
import store from "../../store";
import {useUserRecentOpeningQuery} from "../../../src/services/queries/search/openingQueries";
import { MemoryRouter } from "react-router-dom";
import PaginationContext from "../../contexts/PaginationContext";

// Mocking useUserRecentOpeningQuery to return the necessary functions and state
vi.mock("../../../src/services/queries/search/openingQueries", () => ({
  useUserRecentOpeningQuery: vi.fn(),
}));


const paginationValueMock = {
    getCurrentData: () => [],
    currentPage: 0,
    totalPages: 0,
    handlePageChange: vi.fn(),
    handleItemsPerPageChange: vi.fn(),
    itemsPerPage: 5,
    totalResultItems:100,
    setTotalResultItems:vi.fn(),
    setPageData: vi.fn(),
    setInitialItemsPerPage: vi.fn(),
  };

describe("OpeningsTab", () => {
  const queryClient = new QueryClient();
  const showSpatial = false;
  const setShowSpatial = vi.fn();

  it("renders the component successfully", () => {
    (useUserRecentOpeningQuery as jest.Mock).mockReturnValue({ data: [], isFetching: false });
    render(
      <MemoryRouter>
        <QueryClientProvider client={queryClient}>
        <PaginationContext.Provider value={paginationValueMock}>
            <Provider store={store}>
            <OpeningsTab 
                showSpatial={true}
                setShowSpatial={setShowSpatial}
            />
            </Provider>
            </PaginationContext.Provider>
        </QueryClientProvider>
      </MemoryRouter>
    );
    const searchInput = screen.getByText(/Track the history of openings you have looked at and check spatial information by selecting the openings in the table below/i);
    expect(searchInput).toBeInTheDocument();
  });

  it("shows the spatial area with Hide Spatial Button", () => {
    (useUserRecentOpeningQuery as jest.Mock).mockReturnValue({ data: [], isFetching: false });
    render(
      <MemoryRouter>
        <QueryClientProvider client={queryClient}>
        <PaginationContext.Provider value={paginationValueMock}>
            <Provider store={store}>
            <OpeningsTab 
                showSpatial={true}
                setShowSpatial={setShowSpatial}
            />
            </Provider>
            </PaginationContext.Provider>
        </QueryClientProvider>
      </MemoryRouter>
    );
    const hideSpatialButton = screen.getByRole('button', { name: /Hide Spatial/i });
    expect(hideSpatialButton).toBeInTheDocument();
  });
  
  it("shows table skeleton when the data is loading", () => {
    (useUserRecentOpeningQuery as jest.Mock).mockReturnValue({ data: [], isFetching: true });

    render(
      <MemoryRouter>
        <QueryClientProvider client={queryClient}>
        <PaginationContext.Provider value={paginationValueMock}>
            <Provider store={store}>
            <OpeningsTab 
                showSpatial={showSpatial}
                setShowSpatial={setShowSpatial}
            />
            </Provider>
            </PaginationContext.Provider>
        </QueryClientProvider>
      </MemoryRouter>
    );
    console.log(screen.debug());

    expect.poll(() => document.querySelector('--cds-skeleton')).toBeTruthy();
  });
  
});
