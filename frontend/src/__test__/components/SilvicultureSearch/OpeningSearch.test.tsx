import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import OpeningSearch from "../../../components/SilvicultureSearch/OpeningSearch";
import { vi } from "vitest";

// Mock API calls using vi.mock
vi.mock("../../../services/OpeningSearchService", () => ({
  fetchCategories: vi.fn().mockResolvedValue([]),
  fetchOpeningsOrgUnits: vi.fn().mockResolvedValue([]),
  searchOpenings: vi.fn().mockResolvedValue({
    content: [],
    page: { totalElements: 0 },
  }),
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
        <OpeningSearch />
      </MemoryRouter>
    </QueryClientProvider>
  );

describe("OpeningSearch Component", () => {
  it("renders the table initially empty", async () => {
    renderComponent();
    expect(await screen.findByText("Nothing to show yet!")).toBeInTheDocument();
  });
});
