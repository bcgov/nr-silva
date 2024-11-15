// TableCellContent.test.tsx
import React from "react";
import { vi } from "vitest";
import { render, screen } from "@testing-library/react";
import TableCellContent from "../../components/TableCellContent";
import { OpeningsSearch } from "../../types/OpeningsSearch";

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
      <TableCellContent
        headerKey="statusDescription"
        row={row}
        showSpatial={false}
        selectedRows={selectedRows}
        handleRowSelectionChanged={handleRowSelectionChanged}
      />
    );

    expect(screen.getByText(/Active/i)).toBeInTheDocument();
  });

  it("renders ActionButtons and optionally SpatialCheckbox when headerKey is 'actions'", () => {
    const { rerender } = render(
      <TableCellContent
        headerKey="actions"
        row={row}
        showSpatial={false}
        selectedRows={selectedRows}
        handleRowSelectionChanged={handleRowSelectionChanged}
      />
    );
    console.log(screen.debug());
    expect(screen.queryByText(/View/i)).toBeInTheDocument();
  });

  it("renders category code and description when headerKey is 'Category'", () => {
    render(
      <TableCellContent
        headerKey="Category"
        row={row}
        showSpatial={false}
        selectedRows={selectedRows}
        handleRowSelectionChanged={handleRowSelectionChanged}
      />
    );
    expect(screen.getAllByText("A - Category A")[0]).toBeInTheDocument();
  });

  it("renders default content for other headerKey values", () => {
    render(
      <TableCellContent
        headerKey="unknownKey"
        row={{ ...row, unknownKey: "Unknown Value" } as OpeningsSearch}
        showSpatial={false}
        selectedRows={selectedRows}
        handleRowSelectionChanged={handleRowSelectionChanged}
      />
    );

    expect(screen.getByText(/Unknown Value/i)).toBeInTheDocument();
  });

  it("renders SpatialCheckbox when headerKey is 'actions' and showSpatial is true", () => {
    render(
      <TableCellContent
        headerKey="actions"
        row={row}
        showSpatial={true}  // Set showSpatial to true
        selectedRows={selectedRows}
        handleRowSelectionChanged={handleRowSelectionChanged}
      />
    );
    //check if the Checkbox text is present
    expect(screen.getByText(/Click to view this opening's map activity./i)).toBeInTheDocument();
  });
  
});
