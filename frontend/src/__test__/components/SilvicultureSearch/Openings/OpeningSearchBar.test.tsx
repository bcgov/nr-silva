// src/__test__/components/SilvicultureSearch/Openings/OpeningsSearchBar.test.tsx

import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import OpeningsSearchBar from "../../../../components/SilvicultureSearch/Openings/OpeningsSearchBar";
import { vi } from "vitest";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useOpeningsSearch } from "../../../../contexts/search/OpeningsSearch";

// Mock the useOpeningsSearch context to avoid rendering errors
vi.mock("../../../../contexts/search/OpeningsSearch", () => ({
  useOpeningsSearch: vi.fn().mockReturnValue({
    filters: [],
    clearFilters: vi.fn(),
    searchTerm: "",
    setSearchTerm: vi.fn(),
  }),
}));

describe("OpeningsSearchBar", () => {
  // Create a new QueryClient instance for each test
  const queryClient = new QueryClient();

  it("renders the search input with the correct placeholder", () => {
    render(
      <QueryClientProvider client={queryClient}>
        <OpeningsSearchBar onSearchClick={() => {}} />
      </QueryClientProvider>
    );

    // Check if the search input field is present with the correct placeholder text
    const searchInput = screen.getByPlaceholderText(
      "Search by opening ID, opening number, timber mark or file ID"
    );
    expect(searchInput).toBeInTheDocument();
  });

  it("should call the onSearchClick function when the search button is clicked", () => {
    // Create a mock function to pass as a prop
    const onSearchClick = vi.fn();

    render(
      <QueryClientProvider client={queryClient}>
        <OpeningsSearchBar onSearchClick={onSearchClick} />
      </QueryClientProvider>
    );

    // Click the search button
    const searchButton = screen.getAllByRole("button", { name: "Search" })[1];
    searchButton.click();

    // Check if the onSearchClick function was called
    expect(onSearchClick).toHaveBeenCalled();
  });
});
