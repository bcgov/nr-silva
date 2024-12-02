// TableRowComponent.test.tsx
import React from "react";
import { vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import TableRowComponent from "../../components/TableRowComponent";
import { OpeningsSearch } from "../../types/OpeningsSearch";
import { ITableHeader } from "../../types/TableHeader";

describe("TableRowComponent", () => {
  const row: OpeningsSearch = {
    openingId: 1,
    statusDescription: "Active",
    categoryCode: "A",
    categoryDescription: "Category A",
  };

  const headers: ITableHeader[] = [
    { key: "statusDescription", label: "Status", selected: true },
    { key: "categoryCode", label: "Category Code", selected: true },
    { key: "actions", label: "Actions", selected: false },
  ];

  const showSpatial = false;
  const selectedRows: string[] = [];
  const handleRowSelectionChanged = vi.fn();
  const setOpeningDetails = vi.fn();

  afterEach(() => {
    handleRowSelectionChanged.mockClear();
    setOpeningDetails.mockClear();
  });

  it("renders TableCellContent for each selected header", () => {
    render(
      <TableRowComponent
        row={row}
        headers={headers}
        showSpatial={showSpatial}
        selectedRows={selectedRows}
        handleRowSelectionChanged={handleRowSelectionChanged}
        setOpeningDetails={setOpeningDetails}
      />
    );

    // Check that the expected content for each selected header is rendered
    expect(screen.getByText("Active")).toBeInTheDocument();
    expect(screen.getByText("A")).toBeInTheDocument();
    expect(screen.queryByText("Category A")).not.toBeInTheDocument(); // CategoryDescription isn't selected
  });

  it("calls setOpeningDetails when the row is clicked", () => {
    render(
      <TableRowComponent
        row={row}
        headers={headers}
        showSpatial={showSpatial}
        selectedRows={selectedRows}
        handleRowSelectionChanged={handleRowSelectionChanged}
        setOpeningDetails={setOpeningDetails}
      />
    );

    // Simulate click on the row
    const tableRow = screen.getByRole("row");
    fireEvent.click(tableRow);

    // Verify that setOpeningDetails was called with true
    expect(setOpeningDetails).toHaveBeenCalledWith("1");
  });

  it("renders TableCell components only for selected headers", () => {
    render(
      <TableRowComponent
        row={row}
        headers={headers}
        showSpatial={showSpatial}
        selectedRows={selectedRows}
        handleRowSelectionChanged={handleRowSelectionChanged}
        setOpeningDetails={setOpeningDetails}
      />
    );

    // Verify content specific to selected headers
    expect(screen.getByText("Active")).toBeInTheDocument();
    expect(screen.getByText("A")).toBeInTheDocument();
    expect(screen.queryByText("Actions")).not.toBeInTheDocument();
  });
});
