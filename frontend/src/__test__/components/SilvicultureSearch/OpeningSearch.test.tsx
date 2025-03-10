import React from "react";
import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import OpeningSearch from "../../../components/SilvicultureSearch/OpeningSearch";
import { vi } from "vitest";
import { openingA, openingB } from '../../fixtures/openings';
import { OpeningSearchResponseDto, PaginatedRecentOpeningsDto } from "../../../types/OpeningTypes";
import { DEFAULT_PAGE_CONFIG } from '../../fixtures/defaults';
import { NotificationProvider } from "../../../contexts/NotificationProvider";

// Mock API calls using vi.mock
const mockOpenings: OpeningSearchResponseDto[] = [
  openingA, openingB
];

vi.mock("../../../services/OpeningSearchService", () => ({
  fetchCategories: vi.fn().mockResolvedValue([]),
  fetchOpeningsOrgUnits: vi.fn().mockResolvedValue([]),
  searchOpenings: vi.fn().mockResolvedValue({
    content: [],
    page: {
      totalElements: 0,
      size: 10,
      number: 0,
      totalPages: 1,
    },
  } as PaginatedRecentOpeningsDto),
}));

vi.mock("../../../components/OpeningsMap", () => ({
  __esModule: true,
  default: () => <div data-testid="mock-openings-map" />,
}));

const queryClient = new QueryClient();

const renderComponent = () =>
  render(
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>
        <NotificationProvider>
          <OpeningSearch />
        </NotificationProvider>
      </MemoryRouter>
    </QueryClientProvider>
  );

describe("OpeningSearch Component", () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it("renders the table initially empty", async () => {
    renderComponent();
    expect(await screen.findByText("Nothing to show yet!")).toBeInTheDocument();
  });

  it("renders the table with rows when search is successful", async () => {
    const { searchOpenings } = await import("../../../services/OpeningSearchService");
    (searchOpenings as vi.Mock).mockResolvedValueOnce({
      content: mockOpenings,
      page: DEFAULT_PAGE_CONFIG,
    });

    renderComponent();

    const searchButtons = await screen.findAllByRole("button", { name: "Search" });
    fireEvent.click(searchButtons[0]);

    // Wait for the table to appear in the DOM
    await waitFor(() => {
      const table = document.querySelector(".opening-search-table");
      expect(table).toBeInstanceOf(HTMLElement);
      expect(table).toBeInTheDocument();
    });

    const aMatches = screen.getAllByText((_, element) =>
      element?.textContent?.includes(openingA.openingId.toString()) ?? false
    );
    expect(aMatches.length).toBeGreaterThan(0);

    const bMatches = screen.getAllByText((_, element) =>
      element?.textContent?.includes(openingB.openingId.toString()) ?? false
    );
    expect(bMatches.length).toBeGreaterThan(0);
  });
});
