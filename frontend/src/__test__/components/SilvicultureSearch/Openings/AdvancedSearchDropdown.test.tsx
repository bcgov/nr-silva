import { render, screen } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";
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
        categories: [{
            code: "FTML",
            description: "Forest Tenure - Major Licensee"
          }, {
            code: "CONT",
            description: "SP as a part of contractual agreement"
          }
        ],
        orgUnits: [{
          orgUnitCode:'DCK', 
          orgUnitName: 'Chilliwack Natural Resource District'
          }, {
            orgUnitCode:'DCR', 
            orgUnitName: 'Campbell River Natural Resource District'
          }
        ],
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
    (useOpeningFiltersQuery as vi.Mock).mockReturnValue({
      isLoading: false,
      isError: true,
      data: null,
    });

    render(<AdvancedSearchDropdown toggleShowFilters={toggleShowFilters} />);
    expect(
      screen.getByText("There was an error while loading the advanced filters.")
    ).toBeInTheDocument();
  });

  it("displays the advanced search dropdown", () => {
    render(<AdvancedSearchDropdown toggleShowFilters={toggleShowFilters} />);
    expect(screen.getByText("Opening Filters")).toBeInTheDocument();
  });

  it("displays the advanced search dropdown with filters", () => {
    render(<AdvancedSearchDropdown toggleShowFilters={toggleShowFilters} />);
    expect(screen.getByText("Opening Filters")).toBeInTheDocument();
    expect(screen.getByText("Org Unit")).toBeInTheDocument();
    expect(screen.getByText("Category")).toBeInTheDocument();
    expect(screen.getByText("Client acronym")).toBeInTheDocument();
    expect(screen.getByText("Client location code")).toBeInTheDocument();
    expect(screen.getByText("Cut block")).toBeInTheDocument();
    expect(screen.getByText("Cutting permit")).toBeInTheDocument();
    expect(screen.getByText("Timber mark")).toBeInTheDocument();
    expect(screen.getByLabelText("Start Date")).toBeInTheDocument();
    expect(screen.getByLabelText("End Date")).toBeInTheDocument();    
    expect(screen.getByText("Status (Select One)")).toBeInTheDocument();
  });
});