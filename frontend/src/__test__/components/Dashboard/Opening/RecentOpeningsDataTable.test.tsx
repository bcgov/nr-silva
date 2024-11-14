// src/__test__/components/SilvicultureSearch/Openings/OpeningsSearchBar.test.tsx

import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { vi } from "vitest";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import PaginationProvider from "../../../../contexts/PaginationProvider";
import RecentOpeningsDataTable from "../../../../components/Dashboard/Opening/RecentOpeningsDataTable";
import { MemoryRouter } from "react-router-dom";

describe("OpeningsSearchBar", () => {
  // Create a new QueryClient instance for each test
  const queryClient = new QueryClient();
  const handleCheckboxChange = vi.fn()
  const setLoadId = vi.fn()
    const toggleSpatial = vi.fn()
    const showSpatial = false
    const data = { data: [], perPage: 0, totalPages: 0 }
    const headers = []

  it("renders the blank table when data array is empty", () => {
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
    // Check if the search input field is present with the correct placeholder text
    const searchInput = screen.getByText(/Total Search Results: 0/i);
    expect(searchInput).toBeInTheDocument();
  });

  it("renders the table with data", () => {
    const data = { data: [{
        "openingId": 114203,
        "forestFileId": "TFL47",
        "categoryCode": "FTML",
        "categoryDescription": null,
        "statusCode": "Active",
        "statusDescription": "Active",
        "cuttingPermitId": "12T",
        "cutBlockId": "12-44A",
        "orgUnitName": "DCC - Cariboo chilcotin natural resources",
        "updateTimestamp": "2021-12-08"
      }], perPage: 1, totalPages: 1 }
    const headers = [{
        key: 'openingId',
        header: 'Opening Id',
        selected: false
      },
      {
        key: 'forestFileId',
        header: 'File Id',
        selected: false
      },
      {
        key: 'cuttingPermit',
        header: 'Cutting permit',
        selected: false
      },
      {
        key: 'timberMark',
        header: 'Timber mark',
        selected: false
      },
      {
        key: 'cutBlock',
        header: 'Cut block',
        selected: false
      },]
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
    // Check if the tableHeader field is present with the correct placeholder text
    const tableHeader = screen.getByText(/Total Search Results: 1/i);
    expect(tableHeader).toBeInTheDocument();
  });

  it("should render the table with the correct headers", () => {
    const data = { data: [{
      "openingId": 114203,
      "forestFileId": "TFL47",
      "categoryCode": "FTML",
      "categoryDescription": null,
      "statusCode": "Active",
      "statusDescription": "Active",
      "cuttingPermitId": "12T",
      "cutBlockId": "12-44A",
      "orgUnitName": "DCC - Cariboo chilcotin natural resources",
      "updateTimestamp": "2021-12-08"
    }], perPage: 1, totalPages: 1 }
  const headers = [{
      key: 'openingId',
      header: 'Opening Id',
      selected: false
    },
    {
      key: 'forestFileId',
      header: 'File Id',
      selected: false
    },
    {
      key: 'cuttingPermit',
      header: 'Cutting permit',
      selected: false
    },
    {
      key: 'timberMark',
      header: 'Timber mark',
      selected: false
    },
    {
      key: 'cutBlock',
      header: 'Cut block',
      selected: false
    },]
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
    const table = screen.getByRole('table');
    expect(table).toBeInTheDocument();
  });
});