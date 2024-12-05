// TableCellContent.test.tsx
import React from "react";
import { MemoryRouter } from 'react-router-dom';
import { vi } from "vitest";
import { render, screen } from "@testing-library/react";
import TableCellContent from "../../components/TableCellContent";
import { OpeningsSearch } from "../../types/OpeningsSearch";
import { NotificationProvider } from "../../contexts/NotificationProvider"

// Mock components
vi.mock("../StatusTag", () => ({
  default: ({ code }: { code: string }) => <div>{`Status: ${code}`}</div>,
}));
vi.mock("../ActionButtons", () => ({
  default: ({ rowId }: { rowId: string }) => <div>{`Actions for ${rowId}`}</div>,
}));
vi.mock("../SpatialCheckbox", () => ({
  default: ({ rowId }: { rowId: string }) => <div>{`Spatial Checkbox for ${rowId}`}</div>,
}));

describe("TableCellContent", () => {
  const row = {
    openingId: 1,
    statusDescription: "Active",
    categoryCode: "A",
    categoryDescription: "Category A",
  } as OpeningsSearch;
  const selectedRows: string[] = [];
  const handleRowSelectionChanged = vi.fn();

  it("renders StatusTag when headerKey is 'statusDescription'", () => {
    render(
      <NotificationProvider>
      <TableCellContent
        headerKey="statusDescription"
        row={row}
        showSpatial={false}
        selectedRows={selectedRows}
        handleRowSelectionChanged={handleRowSelectionChanged}
      />
      </NotificationProvider>
    );

    expect(screen.getByText(/Active/i)).toBeInTheDocument();
  });

  it("renders ActionButtons and optionally SpatialCheckbox when headerKey is 'actions'", () => {
    const { rerender } = render(
      <MemoryRouter>
        <NotificationProvider>
          <TableCellContent
            headerKey="actions"
            row={row}
            showSpatial={false}
            selectedRows={selectedRows}
            handleRowSelectionChanged={handleRowSelectionChanged}
          />
        </NotificationProvider>
      </MemoryRouter>
    );
    console.log(screen.debug());
    expect(screen.queryByText(/Favorite Opening/i)).toBeInTheDocument();
  });

  it("renders category code and description when headerKey is 'Category'", () => {
    render(
      <MemoryRouter>
        <NotificationProvider>
          <TableCellContent
            headerKey="Category"
            row={row}
            showSpatial={false}
            selectedRows={selectedRows}
            handleRowSelectionChanged={handleRowSelectionChanged}
          />
        </NotificationProvider>
      </MemoryRouter>
    );
    expect(screen.getAllByText("A - Category A")[0]).toBeInTheDocument();
  });

  it("renders default content for other headerKey values", () => {
    render(
      <MemoryRouter>
        <NotificationProvider>
          <TableCellContent
            headerKey="unknownKey"
            row={{ ...row, unknownKey: "Unknown Value" } as OpeningsSearch}
            showSpatial={false}
            selectedRows={selectedRows}
            handleRowSelectionChanged={handleRowSelectionChanged}
          />
        </NotificationProvider>
      </MemoryRouter>
    );

    expect(screen.getByText(/Unknown Value/i)).toBeInTheDocument();
  });

  it("renders SpatialCheckbox when headerKey is 'actions' and showSpatial is true", () => {
    render(
      <MemoryRouter>
        <NotificationProvider>
          <TableCellContent
            headerKey="actions"
            row={row}
            showSpatial={true}  // Set showSpatial to true
            selectedRows={selectedRows}
            handleRowSelectionChanged={handleRowSelectionChanged}
          />
        </NotificationProvider>
      </MemoryRouter>
    );
    //check if the Checkbox text is present
    expect(screen.getByText(/Click to view this opening's map activity./i)).toBeInTheDocument();
  });
  
});
