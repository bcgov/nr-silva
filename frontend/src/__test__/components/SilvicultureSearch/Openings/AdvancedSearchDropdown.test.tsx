import { getByTestId, render, screen, waitFor } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";
import AdvancedSearchDropdown from "../../../../components/SilvicultureSearch/Openings/AdvancedSearchDropdown";
import { useOpeningFiltersQuery } from "../../../../services/queries/search/openingQueries";
import { useOpeningsSearch } from "../../../../contexts/search/OpeningsSearch";
import React, { act } from "react";

// Mocking the toggleShowFilters function
const toggleShowFilters = vi.fn();

// Mocking useOpeningFiltersQuery to return mock data for filters
vi.mock("../../../../services/queries/search/openingQueries", () => ({
  useOpeningFiltersQuery: vi.fn(),
}));

// Mocking useOpeningsSearch to return the necessary functions and state
vi.mock("../../../../contexts/search/OpeningsSearch", async () => {
  const actual = await vi.importActual<typeof import("../../../../contexts/search/OpeningsSearch")>("../../../../contexts/search/OpeningsSearch");
  return {
    ...actual,
    useOpeningsSearch: vi.fn(),
  }
});

describe("AdvancedSearchDropdown", () => {
  beforeEach(() => {
    // Mock data to return for the filters query
    (useOpeningFiltersQuery as vi.Mock).mockReturnValue({
      data: {
        categories: [{ code: "FTML", description: "" }, { code: "CONT", description: "" }],
        orgUnits: [{
          "orgUnitNo": 15,
          "orgUnitCode": "DCK",
          "orgUnitName": "Chilliwack Natural Resource District"
        },
        {
          "orgUnitNo": 43,
          "orgUnitCode": "DCR",
          "orgUnitName": "Campbell River Natural Resource District"
        }],
        dateTypes: ["Disturbance", "Free Growing"],
      },
      isLoading: false,
      isError: false,
    });

    // Mock implementation of useOpeningsSearch context
    (useOpeningsSearch as vi.Mock).mockReturnValue({
      filters: {
        startDate: null as Date | null,
        endDate: null as Date | null,
        orgUnit: [] as string[],
        category: [] as string[],
        status: [] as string[],
        clientAcronym: "",
        clientLocationCode: "",
        blockStatus: "",
        cutBlock: "",
        cuttingPermit: "",
        timberMark: "",
        dateType: null as string | null,
        openingFilters: [] as string[],
        blockStatuses: [] as string[],
      },
      setFilters: vi.fn(),
      clearFilters: vi.fn(),
      setIndividualClearFieldFunctions: vi.fn(),
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

  it("displays the advanced search dropdown", async () => {
    let container;
    await act(async () => {
      ({ container } = render(<AdvancedSearchDropdown toggleShowFilters={toggleShowFilters} />));
    });
    const element = container.querySelector('.d-block');
    expect(element).toBeDefined();
  });

  it("clears the date filters when all filters are cleared", async () => {
    // Mock implementation of useOpeningsSearch context
    (useOpeningsSearch as vi.Mock).mockReturnValue({
      filters: {
        startDate: "1978-01-01",
        endDate: "1978-01-01",
        orgUnit: [] as string[],
        category: [] as string[],
        status: [] as string[],
        clientAcronym: "",
        clientLocationCode: "",
        blockStatus: "",
        cutBlock: "",
        cuttingPermit: "",
        timberMark: "",
        dateType: "Disturbance",
        openingFilters: [] as string[],
        blockStatuses: [] as string[]
      },
      setIndividualClearFieldFunctions: vi.fn(),
      setFilters: vi.fn(),
    });
    let container;
    await act(async () => {
      ({ container } = render(<AdvancedSearchDropdown toggleShowFilters={toggleShowFilters} />));
    });
    const element = container.querySelector('.d-block');
    expect(element).toBeDefined();
  });
});