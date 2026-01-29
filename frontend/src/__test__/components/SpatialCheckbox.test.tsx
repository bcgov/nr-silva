// SpatialCheckbox.test.tsx
import React from "react";
import { vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import SpatialCheckbox from "../../components/SpatialCheckbox";

describe("SpatialCheckbox", () => {
  const rowId = 101;
  const handleRowSelectionChanged = vi.fn();

  afterEach(() => {
    handleRowSelectionChanged.mockClear();
  });

  it("renders the checkbox with the tooltip", () => {
    render(
      <SpatialCheckbox
        rowId={rowId}
        selectedRows={[]}
        handleRowSelection={handleRowSelectionChanged}
      />
    );

    // Check if the checkbox is in the document
    const button = screen.getByRole("button", {
      name: /Click to view this opening on the map/i,
    });
    expect(button).toBeInTheDocument();
  });

  it("sets the checkbox to 'checked' if rowId is in selectedRows", () => {
    render(
      <SpatialCheckbox
        rowId={rowId}
        selectedRows={[rowId]}
        handleRowSelection={handleRowSelectionChanged}
      />
    );

    const button = screen.getByRole("button", {
      name: /Click to view this opening on the map/i,
    });
    expect(button).toHaveClass("spatial-checkbox-checked");
  });

  it("calls handleRowSelectionChanged with rowId when checkbox is toggled", () => {
    render(
      <SpatialCheckbox
        rowId={rowId}
        selectedRows={[]}
        handleRowSelection={handleRowSelectionChanged}
      />
    );

    const button = screen.getByRole("button", {
      name: /Click to view this opening on the map/i,
    });

    // Simulate a click on the button
    fireEvent.click(button);

    // Check if handleRowSelection was called with the correct rowId
    expect(handleRowSelectionChanged).toHaveBeenCalledWith(rowId);
  });
});
