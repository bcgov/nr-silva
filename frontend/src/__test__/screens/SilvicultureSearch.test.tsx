import React from "react";
import { render, screen } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import SilvicultureSearch from "../../screens/SilvicultureSearch";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { PreferenceProvider } from "@/contexts/PreferenceProvider";

vi.mock("@/services/OpeningSearchService", async (original) => {
  const actual = await original();
  return {
    ...actual,
    fetchOpeningsOrgUnits: vi
      .fn(() => Promise.resolve([]))
      .mockResolvedValue([]),
    fetchCategories: vi.fn(() => Promise.resolve([])).mockResolvedValue([]),
    searchOpenings: vi.fn().mockResolvedValue({
      content: [],
      page: {
        totalElements: 0,
        size: 10,
        page: 0,
        totalPages: 1,
      },
    } as PaginatedRecentOpeningsDto),
  };
});

describe("SilvicultureSearch Component", () => {
  const queryClient = new QueryClient();

  const renderComponent = () =>
    render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
          <PreferenceProvider>
            <SilvicultureSearch />
          </PreferenceProvider>
        </QueryClientProvider>
      </BrowserRouter>
    );

  it("should render the page title correctly", () => {
    renderComponent();

    const titleElement = screen.getByText("Silviculture Search");
    const subtitleElement = screen.getByText(
      "Search for opening types, activities, stocking standards or standards units"
    );

    expect(titleElement).toBeInTheDocument();
    expect(subtitleElement).toBeInTheDocument();
  });
});
