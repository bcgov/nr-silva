// SpatialCheckbox.test.tsx
import React from "react";
import { vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import SpatialCheckbox from "../../components/SpatialCheckbox";

describe("SpatialCheckbox", () => {
  const rowId = "test-row-id";
  const handleRowSelectionChanged = vi.fn();

  afterEach(() => {
    handleRowSelectionChanged.mockClear();
  });

  it("renders the checkbox with the tooltip", () => {
    render(
      <SpatialCheckbox
        rowId={rowId}
        selectedRows={[]}
        handleRowSelectionChanged={handleRowSelectionChanged}
      />
    );

    // Check if the checkbox is in the document
    const checkbox = screen.getByRole("checkbox", {
      name: /click to view this opening's map activity/i,
    });
    expect(checkbox).toBeInTheDocument();
  });

  it("sets the checkbox to 'checked' if rowId is in selectedRows", () => {
    render(
      <SpatialCheckbox
        rowId={rowId}
        selectedRows={[rowId]}
        handleRowSelectionChanged={handleRowSelectionChanged}
      />
    );

    const checkbox = screen.getByRole("checkbox");
    expect(checkbox).toBeChecked();
  });

  it("calls handleRowSelectionChanged with rowId when checkbox is toggled", () => {
    render(
      <SpatialCheckbox
        rowId={rowId}
        selectedRows={[]}
        handleRowSelectionChanged={handleRowSelectionChanged}
      />
    );

    const checkbox = screen.getByRole("checkbox");

    // Simulate a click on the checkbox
    fireEvent.click(checkbox);

    // Check if handleRowSelectionChanged was called with the correct rowId
    expect(handleRowSelectionChanged).toHaveBeenCalledWith(rowId);
  });
});
