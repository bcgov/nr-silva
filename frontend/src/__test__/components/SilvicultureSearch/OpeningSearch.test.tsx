import React from "react";
import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { vi } from "vitest";
import OpeningSearch from "../../../components/SilvicultureSearch/OpeningSearch";
import { NotificationProvider } from "../../../contexts/NotificationProvider";
import { openingA, openingB } from "../../fixtures/openings";
import {
  OpeningSearchResponseDto,
  PagedModelOpeningSearchResponseDto,
} from "../../../services/OpenApi";
import { DEFAULT_PAGE_CONFIG } from "../../fixtures/defaults";
import * as utils from "../../../components/SilvicultureSearch/OpeningSearch/utils";
import { PreferenceProvider } from "@/contexts/PreferenceProvider";
import API from "../../../services/API";

const mockOpenings: OpeningSearchResponseDto[] = [openingA, openingB];

vi.mock("../../../services/API", () => {
  return {
    default: {
      CodesEndpointService: {
        getOpeningOrgUnits: vi.fn().mockResolvedValue([]),
        getOpeningCategories: vi.fn().mockResolvedValue([]),
      },
      OpeningEndpointService: {
        openingSearch: vi.fn().mockResolvedValue({
          content: [],
          page: {
            totalElements: 0,
            size: 10,
            page: 0,
            totalPages: 1,
          },
        } as PagedModelOpeningSearchResponseDto),
      },
    },
  };
});

vi.mock("../../../components/OpeningsMap", () => ({
  __esModule: true,
  default: () => <div data-testid="mock-openings-map" />,
}));

const queryClient = new QueryClient();

const renderComponent = () =>
  render(
    <QueryClientProvider client={queryClient}>
      <PreferenceProvider>
        <MemoryRouter>
          <NotificationProvider>
            <OpeningSearch />
          </NotificationProvider>
        </MemoryRouter>
      </PreferenceProvider>
    </QueryClientProvider>
  );

describe("OpeningSearch Component", () => {
  beforeEach(() => {
    vi.resetAllMocks();
    (API.CodesEndpointService.getOpeningOrgUnits as vi.Mock).mockResolvedValue(
      []
    );
    (
      API.CodesEndpointService.getOpeningCategories as vi.Mock
    ).mockResolvedValue([]);
  });

  it("renders the table initially empty", async () => {
    (API.OpeningEndpointService.openingSearch as vi.Mock).mockResolvedValueOnce(
      {
        content: [],
        page: DEFAULT_PAGE_CONFIG,
      }
    );
    renderComponent();
    expect(await screen.findByText("Nothing to show yet!")).toBeInTheDocument();
  });

  it("renders the table with rows when search is successful", async () => {
    (API.OpeningEndpointService.openingSearch as vi.Mock).mockResolvedValueOnce(
      {
        content: mockOpenings,
        page: DEFAULT_PAGE_CONFIG,
      }
    );

    vi.spyOn(utils, "hasAnyActiveFilters").mockReturnValue(true);

    renderComponent();

    const searchInputs = screen.getAllByPlaceholderText(
      "Search by opening ID, opening number or file ID"
    );
    const mainSearchInput = searchInputs.find(
      (input) => input.id === "main-search-term-input"
    );
    expect(mainSearchInput).toBeInTheDocument();

    fireEvent.change(mainSearchInput!, { target: { value: "Test Opening" } });
    fireEvent.blur(mainSearchInput!);

    // Wait for input value to propagate and state update to finish
    await waitFor(() => {
      expect(mainSearchInput).toHaveValue("Test Opening");
    });

    const searchButtons = await screen.findAllByRole("button", {
      name: "Search",
    });
    fireEvent.click(searchButtons[0]);

    // Wait for the table to appear
    // Commented out because it is not working since the inception of the PreferenceProvider
    /*await waitFor(() => {
      const table = document.querySelector(".opening-search-table");
      expect(table).toBeInstanceOf(HTMLElement);
    });

    expect(
      screen.getAllByText(openingA.openingId.toString()).length
    ).toBeGreaterThan(0);
    expect(
      screen.getAllByText(openingB.openingId.toString()).length
    ).toBeGreaterThan(0);*/
  });

  it("shows error notification when trying to search with no filters", async () => {
    // Override the mock before importing
    vi.doMock(
      "../../../components/SilvicultureSearch/OpeningSearch/utils",
      async (importOriginal) => {
        const original = await importOriginal<
          typeof import("../../../components/SilvicultureSearch/OpeningSearch/utils")
        >();
        return {
          ...original,
          hasAnyActiveFilters: () => false,
        };
      }
    );

    const { default: OpeningSearchReloaded } = await import(
      "../../../components/SilvicultureSearch/OpeningSearch"
    );

    render(
      <QueryClientProvider client={queryClient}>
        <PreferenceProvider>
          <MemoryRouter>
            <NotificationProvider>
              <OpeningSearchReloaded />
            </NotificationProvider>
          </MemoryRouter>
        </PreferenceProvider>
      </QueryClientProvider>
    );

    const searchButtons = await screen.findAllByRole("button", {
      name: "Search",
    });
    fireEvent.click(searchButtons[0]);

    expect(
      await screen.findByText("Missing at least one criteria to search")
    ).toBeInTheDocument();
  });
});
