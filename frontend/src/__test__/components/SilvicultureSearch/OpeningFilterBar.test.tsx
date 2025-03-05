import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { describe, expect, vi } from "vitest";
import OpeningFilterBar from "../../../components/SilvicultureSearch/OpeningSearch/OpeningFilterBar"
import { OpeningSearchFilterType } from "../../../components/SilvicultureSearch/OpeningSearch/definitions";

describe("OpeningFilterBar", () => {
  const mockSetFilters = vi.fn();
  const mockHandleClearFilters = vi.fn();

  const defaultFilters: OpeningSearchFilterType = {
    orgUnit: [
      { code: "DAS", description: "District A" },
      { code: "DBS", description: "District B" },
    ],
    category: [{ code: "CAT1", description: "Category 1" }],
    myOpenings: true,
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("renders filters correctly", () => {
    render(
      <OpeningFilterBar
        filters={defaultFilters}
        setFilters={mockSetFilters}
        handleClearFilters={mockHandleClearFilters}
      />
    );

    // Adjusted to match the actual text inside the elements
    expect(screen.getByText("Org Unit: DAS")).toBeInTheDocument();
    expect(screen.getByText("Org Unit: DBS")).toBeInTheDocument();
    expect(screen.getByText("Category: CAT1")).toBeInTheDocument();
    expect(screen.getByText("My Openings: true")).toBeInTheDocument();
  });

  it("calls handleClose when a filter tag is dismissed", () => {
    render(
      <OpeningFilterBar
        filters={defaultFilters}
        setFilters={mockSetFilters}
        handleClearFilters={mockHandleClearFilters}
      />
    );

    const dismissButtons = screen.getAllByRole("button", { name: /Dismiss/i });
    fireEvent.click(dismissButtons[0]); // Click the first dismiss button

    expect(mockSetFilters).toHaveBeenCalled();
  });

  it("calls handleClearFilters when the clear button is clicked", () => {
    render(
      <OpeningFilterBar
        filters={defaultFilters}
        setFilters={mockSetFilters}
        handleClearFilters={mockHandleClearFilters}
      />
    );

    const clearButton = screen.getByRole("button", { name: /Clear filters/i });
    fireEvent.click(clearButton);

    expect(mockHandleClearFilters).toHaveBeenCalledTimes(1);
  });

  it("removes a tag from multi-value filters when dismissed", () => {
    render(
      <OpeningFilterBar
        filters={defaultFilters}
        setFilters={mockSetFilters}
        handleClearFilters={mockHandleClearFilters}
      />
    );

    const dismissButton = screen.getAllByRole("button", { name: /Dismiss/i })[0]; // First dismissible tag
    fireEvent.click(dismissButton);

    expect(mockSetFilters).toHaveBeenCalledWith(expect.any(Function));

    // Simulating state update to ensure correct logic
    const updatedFilters = mockSetFilters.mock.calls[0][0](defaultFilters);
    expect(updatedFilters.orgUnit).toHaveLength(1);
  });

  it("removes a single-value filter when dismissed", () => {
    const filtersWithSingleValue: OpeningSearchFilterType = {
      ...defaultFilters,
      myOpenings: true,
    };

    render(
      <OpeningFilterBar
        filters={filtersWithSingleValue}
        setFilters={mockSetFilters}
        handleClearFilters={mockHandleClearFilters}
      />
    );

    const dismissButton = screen.getAllByRole("button", { name: /Dismiss/i }).pop();
    fireEvent.click(dismissButton!);

    expect(mockSetFilters).toHaveBeenCalledWith(expect.any(Function));

    const updatedFilters = mockSetFilters.mock.calls[0][0](filtersWithSingleValue);
    expect(updatedFilters.myOpenings).toBe("");
  });
});
