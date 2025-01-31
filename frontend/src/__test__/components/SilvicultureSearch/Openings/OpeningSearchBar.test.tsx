// src/__test__/components/SilvicultureSearch/Openings/OpeningsSearchBar.test.tsx

import React from "react";
import { render, screen, act } from "@testing-library/react";
import "@testing-library/jest-dom";
import OpeningsSearchBar from "../../../../components/SilvicultureSearch/Openings/OpeningsSearchBar";
import { vi } from "vitest";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import userEvent from "@testing-library/user-event";

// Mock the useOpeningsSearch context to avoid rendering errors
vi.mock("../../../../contexts/search/OpeningsSearch", async () => {
  const actual = await vi.importActual<
    typeof import("../../../../contexts/search/OpeningsSearch")
  >("../../../../contexts/search/OpeningsSearch");
  return {
    ...actual,
    useOpeningsSearch: vi.fn().mockReturnValue({
      filters: [],
      clearFilters: vi.fn(),
      searchTerm: "",
      setSearchTerm: vi.fn(),
      setIndividualClearFieldFunctions: vi.fn(),
      setFilters: vi.fn(),
    }),
  };
});

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
      "Search by opening ID, opening number or file ID"
    );
    expect(searchInput).toBeInTheDocument();
  });

  it("should call the onSearchClick function when the search button is clicked", async () => {
    // Create a mock function to pass as a prop
    const onSearchClick = vi.fn();

    render(
      <QueryClientProvider client={queryClient}>
        <OpeningsSearchBar onSearchClick={onSearchClick} />
      </QueryClientProvider>
    );

    // Click the search button
    const searchButton = screen.getAllByRole("button", { name: "Search" })[1];
    await act(async () => searchButton.click());

    // Check if the onSearchClick function was called
    expect(onSearchClick).toHaveBeenCalled();
  });

  it("should not show AdvancedSearchDropdown if isOpen is false", () => {
    // Create a mock function to pass as a prop
    const onSearchClick = vi.fn();
    const isOpen = false;
    // Mock the useState calls
    vi.spyOn(React, "useState")
      .mockImplementationOnce(() => [false, vi.fn()]) // Mocking isOpen state as false
      .mockImplementationOnce(() => [false, vi.fn()]) // Mocking showFilters state as false
      .mockImplementationOnce(() => ["", vi.fn()]) // Mocking searchInput state
      .mockImplementationOnce(() => [0, vi.fn()]) // Mocking filtersCount state
      .mockImplementationOnce(() => [null, vi.fn()]); // Mocking filtersList state
    render(
      <QueryClientProvider client={queryClient}>
        <OpeningsSearchBar onSearchClick={onSearchClick} />
      </QueryClientProvider>
    );

    // Check if an element with the class 'd-none' exists within the structure
    const dNoneElement = screen.getAllByText("", { selector: ".d-none" })[0];
    expect(dNoneElement).toBeInTheDocument();
  });

  it("should show correct filter count, when count is greater than 0", async () => {
    vi.mock("../../../../utils/searchUtils", async () => {
      const actual = await vi.importActual<
        typeof import("../../../../utils/searchUtils")
      >("../../../../utils/searchUtils");
      return {
        ...actual,
        countActiveFilters: vi.fn().mockReturnValue(2),
      };
    });

    // Create a mock function to pass as a prop
    const onSearchClick = vi.fn();
    // Mock the useState calls
    vi.spyOn(React, "useState")
      .mockImplementationOnce(() => [false, vi.fn()]) // Mocking isOpen state as false
      .mockImplementationOnce(() => [false, vi.fn()]) // Mocking showFilters state as false
      .mockImplementationOnce(() => [2, vi.fn()]) // Mocking filtersCount state
      .mockImplementationOnce(() => [null, vi.fn()]); // Mocking filtersList state

    await act(async () =>
      render(
        <QueryClientProvider client={queryClient}>
          <OpeningsSearchBar onSearchClick={onSearchClick} />
        </QueryClientProvider>
      )
    );

    // Check if an element with the class 'd-none' exists within the structure
    const dNoneElement = screen.getByText("+2");
    expect(dNoneElement).toBeInTheDocument();
  });

  it("should show inline notification when no filter provided", async () => {
    // Create a mock function to pass as a prop
    const onSearchClick = vi.fn();

    const { container, getByText } = render(
      <QueryClientProvider client={queryClient}>
        <OpeningsSearchBar
          onSearchClick={onSearchClick}
          showNoFilterNotification={true}
        />
      </QueryClientProvider>
    );

    // Click the search button
    const searchButton = screen.getAllByRole("button", { name: "Search" })[1];
    await act(async () => searchButton.click());

    // Check if the onSearchClick function was called
    expect(onSearchClick).toHaveBeenCalled();

    expect(
      getByText("Missing at least one criteria to search")
    ).toBeInTheDocument();
  });

  //test if the handleSearch is called when the user hits enter on the serchInput
  it("should call the onSearchClick function when the user hits enter on the search input", async () => {
    // Create a mock function to pass as a prop
    const onSearchClick = vi.fn();

    render(
      <QueryClientProvider client={queryClient}>
        <OpeningsSearchBar onSearchClick={onSearchClick} />
      </QueryClientProvider>
    );

    // Check if the search input field is present with the correct placeholder text
    const searchInput = screen.getByPlaceholderText(
      "Search by opening ID, opening number or file ID"
    );
    await act(async () => await userEvent.type(searchInput, "tfl47"));
    await act(async () => await userEvent.keyboard("{enter}"));

    // Check if the onSearchClick function was called
    expect(onSearchClick).toHaveBeenCalled();
  });
});
