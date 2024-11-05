import { render, screen, fireEvent } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";
import "@testing-library/jest-dom";
import AdvancedSearchDropdown from "../../../../components/SilvicultureSearch/Openings/AdvancedSearchDropdown";
import { useOpeningFiltersQuery } from "../../../../services/queries/search/openingQueries";
import { useOpeningsSearch } from "../../../../contexts/search/OpeningsSearch";
import React from "react";

// Mocking the toggleShowFilters function
const toggleShowFilters = vi.fn();

// Mocking useOpeningFiltersQuery to return mock data for filters
vi.mock("../../../../services/queries/search/openingQueries", () => ({
  useOpeningFiltersQuery: vi.fn(),
}));

// Mocking useOpeningsSearch to return the necessary functions and state
vi.mock("../../../../contexts/search/OpeningsSearch", () => ({
  useOpeningsSearch: vi.fn(),
}));

describe("AdvancedSearchDropdown", () => {
  beforeEach(() => {
    // Mock data to return for the filters query
    (useOpeningFiltersQuery as jest.Mock).mockReturnValue({
      data: {
        categories: ["FTML", "CONT"],
        orgUnits: ["DCK", "DCR"],
        dateTypes: ["Disturbance", "Free Growing"],
      },
      isLoading: false,
      isError: false,
    });

    // Mock implementation of useOpeningsSearch context
    (useOpeningsSearch as jest.Mock).mockReturnValue({
      filters: {
        openingFilters: [],
        orgUnit: [],
        category: [],
        clientAcronym: "",
        clientLocationCode: "",
        cutBlock: "",
        cuttingPermit: "",
        timberMark: "",
        dateType: "",
        startDate: null,
        endDate: null,
        status: [],
      },
      setFilters: vi.fn(),
      clearFilters: vi.fn(),
    });
  });

  it("displays an error message if there is an error", () => {
    (useOpeningFiltersQuery as jest.Mock).mockReturnValue({
      isLoading: false,
      isError: true,
      data: null,
    });

    render(<AdvancedSearchDropdown toggleShowFilters={toggleShowFilters} />);
    expect(
      screen.getByText("There was an error while loading the advanced filters.")
    ).toBeInTheDocument();
  });
});