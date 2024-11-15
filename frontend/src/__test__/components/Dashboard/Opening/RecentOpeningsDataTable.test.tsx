// src/__test__/components/SilvicultureSearch/Openings/OpeningsSearchBar.test.tsx

import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { vi } from "vitest";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import PaginationProvider from "../../../../contexts/PaginationProvider";
import RecentOpeningsDataTable from "../../../../components/Dashboard/Opening/RecentOpeningsDataTable";
import { MemoryRouter } from "react-router-dom";
import exp from "constants";

describe("OpeningsSearchBar", () => {
  // Create a new QueryClient instance for each test
  const queryClient = new QueryClient();
  const handleCheckboxChange = vi.fn()
  const setLoadId = vi.fn()
    const toggleSpatial = vi.fn()
    const showSpatial = false
    const data = { data: [], perPage: 0, totalPages: 0 }
    const headers = []

  it("shows appropriate message when no data is in the table", () => {
    render(
      <MemoryRouter>
        <QueryClientProvider client={queryClient}>
            <PaginationProvider>
             <RecentOpeningsDataTable
                rows={data?.data || []}
                headers={headers}
                defaultColumns={headers}
                handleCheckboxChange={handleCheckboxChange}
                setOpeningId={setLoadId}
                toggleSpatial={toggleSpatial}
                showSpatial={showSpatial}
                totalItems={(data?.perPage ?? 0) * (data?.totalPages ?? 0)}
                />
            </PaginationProvider>
      </QueryClientProvider>
      </MemoryRouter>
    );
    expect(screen.getByText(/There are no openings to show yet/i)).toBeInTheDocument();
    expect(screen.queryByText(/Your recent openings will appear here once you generate one/i)).toBeInTheDocument();
  });

  it("renders a blank table when rows is empty array", () => {
    render(
      <MemoryRouter>
        <QueryClientProvider client={queryClient}>
            <PaginationProvider>
             <RecentOpeningsDataTable
                rows={[]}
                headers={headers}
                defaultColumns={headers}
                handleCheckboxChange={handleCheckboxChange}
                setOpeningId={setLoadId}
                toggleSpatial={toggleSpatial}
                showSpatial={showSpatial}
                totalItems={(data?.perPage ?? 0) * (data?.totalPages ?? 0)}
                />
            </PaginationProvider>
      </QueryClientProvider>
      </MemoryRouter>
    );
    // Check if the table is present
    const table = screen.getByRole('table');
    expect(table).toBeInTheDocument();
  });
});