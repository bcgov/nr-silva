import React from "react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { vi } from "vitest";
import "@testing-library/jest-dom";
import { MemoryRouter } from "react-router-dom";
import OpeningSearchBar from "../../../components/SilvicultureSearch/OpeningSearch/OpeningSearchBar";
import { OpeningSearchFilterType } from "../../../components/SilvicultureSearch/OpeningSearch/definitions";
import { CodeDescriptionDto } from "../../../services/OpenApi";
import { defaultSearchTableHeaders } from "../../../utils/localStorageUtils";

// Mock dependencies
vi.mock("../../../hooks/UseBreakpoint", () => ({
  default: vi.fn().mockReturnValue("md"),
}));

// Mock Data
const mockCategories: CodeDescriptionDto[] = [
  { code: "CAT1", description: "Category 1" },
  { code: "CAT2", description: "Category 2" },
];

const mockOrgUnits: CodeDescriptionDto[] = [
  { code: "DAS", description: "Org Unit DAS" },
  { code: "DBS", description: "Org Unit DBS" },
];

const mockHeaders = [...defaultSearchTableHeaders];

const mockFilters: OpeningSearchFilterType = {
  mainSearchTerm: "",
  category: [],
  orgUnit: [],
  statusList: [],
  cuttingPermitId: "",
  cutBlockId: "",
  timberMark: "",
  clientLocationCode: "",
  clientNumber: "",
  myOpenings: false,
  submittedToFrpa: false,
};

const mockSetFilters = vi.fn();
const mockSetHeaders = vi.fn();
const mockHandleSearch = vi.fn();
const mockSetShowMap = vi.fn();

const renderComponent = (props = {}) => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        retry: false,
      },
    },
  });

  return render(
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>
        <OpeningSearchBar
          headers={mockHeaders}
          setHeaders={mockSetHeaders}
          filters={mockFilters}
          setFilters={mockSetFilters}
          categories={mockCategories}
          orgUnits={mockOrgUnits}
          handleSearch={mockHandleSearch}
          totalResults={10}
          showMap={false}
          setShowMap={mockSetShowMap}
          setEnableSearch={vi.fn()}
          resetPagination={vi.fn()}
        />
      </MemoryRouter>
    </QueryClientProvider>
  );
};

describe("OpeningSearchBar", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("renders search input and buttons", () => {
    renderComponent();

    // Ensure the search input exists
    const searchInputs = screen.getAllByPlaceholderText(
      "Search by opening ID, opening number or file ID"
    );
    expect(searchInputs.length).toBeGreaterThan(0);
    expect(searchInputs[0]).toBeInTheDocument();

    // Ensure the correct "Advanced Search" button is selected
    const advancedSearchButtons = screen.getAllByRole("button", {
      name: "Advanced Search",
    });
    expect(advancedSearchButtons.length).toBeGreaterThan(0);

    const textOnlyAdvancedSearchButton = advancedSearchButtons.find((button) =>
      button.textContent?.includes("Advanced Search")
    );
    expect(textOnlyAdvancedSearchButton).toBeInTheDocument();

    // Get all "Search" buttons and select the correct one
    const searchButtons = screen.getAllByRole("button", { name: "Search" });

    // Ensure we have at least one Search button
    expect(searchButtons.length).toBeGreaterThan(0);

    const textOnlySearchButton = searchButtons.find(
      (button) => button.textContent?.trim() === "Search"
    );

    expect(textOnlySearchButton).toBeInTheDocument();
  });

  it("updates search input value when user types", () => {
    renderComponent();

    // Find all inputs with the placeholder text
    const searchInputs = screen.getAllByPlaceholderText(
      "Search by opening ID, opening number or file ID"
    );

    // Find the correct input (ensure it's not from advanced search)
    const mainSearchInput = searchInputs.find(
      (input) => input.id === "main-search-term-input"
    );
    expect(mainSearchInput).toBeInTheDocument();

    // Simulate user typing "Test Opening"
    fireEvent.change(mainSearchInput!, { target: { value: "Test Opening" } });

    // Check if the input value updated correctly
    expect(mainSearchInput).toHaveValue("Test Opening");
  });

  it("opens the advanced search modal when the button is clicked", () => {
    renderComponent();

    // Get all matching "Advanced Search" buttons
    const advancedSearchButtons = screen.getAllByRole("button", {
      name: /Advanced Search/i,
    });

    // Select the first visible button (ensuring it's interactable)
    const advancedSearchButton = advancedSearchButtons[0];

    // Click the button
    fireEvent.click(advancedSearchButton);

    // Ensure the modal opens
    expect(screen.getByText("Advanced search")).toBeInTheDocument();
  });

  it("selects a category from the multiselect", async () => {
    renderComponent();

    // Find and open the category dropdown
    const categoryDropdown = screen.getAllByPlaceholderText("Category")[0];
    fireEvent.click(categoryDropdown);

    // Wait for the category option to appear
    const categoryOption = await screen.findByRole("option", {
      name: /Category 1/i,
    });

    // Select the category option
    fireEvent.click(categoryOption);

    // Ensure `mockSetFilters` was called
    await waitFor(() => {
      expect(mockSetFilters).toHaveBeenCalled();
    });

    // Extract the actual argument from the mock calls
    const lastCallArg = mockSetFilters.mock.calls.at(-1)?.[0];

    // If `mockSetFilters` received a function, call it with a fake previous state
    if (typeof lastCallArg === "function") {
      const newState = lastCallArg({}); // Simulate React state update

      expect(newState).toEqual(
        expect.objectContaining({
          category: expect.arrayContaining([
            expect.objectContaining({
              code: "CAT1",
              description: "Category 1",
            }),
          ]),
        })
      );
    } else {
      // Fallback in case it wasn't a function
      expect(lastCallArg).toEqual(
        expect.objectContaining({
          category: expect.arrayContaining([
            expect.objectContaining({
              code: "CAT1",
              description: "Category 1",
            }),
          ]),
        })
      );
    }
  });

  it("selects an org unit from the multiselect", async () => {
    renderComponent();

    // Get all elements with the placeholder "Org unit" and select the first one
    const orgUnitDropdown = screen.getAllByPlaceholderText("Org unit")[0];
    fireEvent.click(orgUnitDropdown);

    // Wait for the org unit option to appear
    const orgUnitOption = await screen.findByRole("option", {
      name: /Org Unit DAS/i,
    });

    // Click the option
    fireEvent.click(orgUnitOption);

    // Ensure `mockSetFilters` was called
    await waitFor(() => {
      expect(mockSetFilters).toHaveBeenCalled();
    });

    // Extract the actual argument from the mock calls
    const lastCallArg = mockSetFilters.mock.calls.at(-1)?.[0];

    // If `mockSetFilters` received a function, call it with a fake previous state
    if (typeof lastCallArg === "function") {
      const newState = lastCallArg({}); // Simulate React state update

      expect(newState).toEqual(
        expect.objectContaining({
          orgUnit: expect.arrayContaining([
            expect.objectContaining({
              code: "DAS",
              description: "Org Unit DAS",
            }),
          ]),
        })
      );
    } else {
      // Fallback in case it wasn't a function
      expect(lastCallArg).toEqual(
        expect.objectContaining({
          orgUnit: expect.arrayContaining([
            expect.objectContaining({
              code: "DAS",
              description: "Org Unit DAS",
            }),
          ]),
        })
      );
    }
  });

  it("updates filters when checkboxes are selected", async () => {
    renderComponent();

    // Find and click the "Openings created by me" checkbox
    const myOpeningsCheckbox = screen.getByLabelText("Openings created by me");
    fireEvent.click(myOpeningsCheckbox);

    // Ensure `mockSetFilters` was called
    await waitFor(() => {
      expect(mockSetFilters).toHaveBeenCalled();
    });

    // Extract the actual argument from the mock calls
    const lastCallArg = mockSetFilters.mock.calls.at(-1)?.[0];

    // If `mockSetFilters` received a function, call it with a fake previous state
    let newState;
    if (typeof lastCallArg === "function") {
      newState = lastCallArg({}); // Simulate React state update
    } else {
      newState = lastCallArg;
    }

    expect(newState).toEqual(expect.objectContaining({ myOpenings: true }));

    // Find and click the "FRPA section 108" checkbox
    const frpaCheckbox = screen.getByLabelText("FRPA section 108");
    fireEvent.click(frpaCheckbox);

    // Ensure `mockSetFilters` was called again
    await waitFor(() => {
      expect(mockSetFilters).toHaveBeenCalled();
    });

    // Extract and validate the latest call
    const frpaCallArg = mockSetFilters.mock.calls.at(-1)?.[0];

    let frpaState;
    if (typeof frpaCallArg === "function") {
      frpaState = frpaCallArg({}); // Simulate state update
    } else {
      frpaState = frpaCallArg;
    }

    expect(frpaState).toEqual(
      expect.objectContaining({ submittedToFrpa: true })
    );
  });

  it("calls handleSearch when the search button is clicked", async () => {
    renderComponent({ handleSearch: mockHandleSearch });

    // Get all buttons and filter by class name
    const searchButtons = screen
      .getAllByRole("button")
      .filter((btn) => btn.classList.contains("search-button"));

    const searchButton = searchButtons[0]; // Select the first one

    fireEvent.click(searchButton);

    await waitFor(() => {
      expect(mockHandleSearch).toHaveBeenCalled();
    });
  });

  it("toggles the map display when the map button is clicked", () => {
    renderComponent();

    const mapButtons = screen
      .getAllByRole("button")
      .filter((btn) => btn.classList.contains("map-button"));

    const mapButton = mapButtons[0];

    fireEvent.click(mapButton);

    expect(mockSetShowMap).toHaveBeenCalledWith(true);
  });
});
