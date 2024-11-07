// src/__test__/components/SilvicultureSearch/Openings/OpeningsSearchBar.test.tsx

import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import OpeningsSearchBar from "../../../../components/SilvicultureSearch/Openings/OpeningsSearchBar";
import { vi } from "vitest";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { OpeningsSearchProvider, useOpeningsSearch } from "../../../../contexts/search/OpeningsSearch";

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

  it("should show AdvancedSearchDropdown if isOpen is true", () => {
    // Create a mock function to pass as a prop
    const onSearchClick = vi.fn();
    const isOpen = false;
     // Mock the useState calls
    vi.spyOn(React, 'useState')
      .mockImplementationOnce(() => [true, vi.fn()]) // Mocking isOpen state as false
      .mockImplementationOnce(() => [false, vi.fn()]) // Mocking showFilters state as false
      .mockImplementationOnce(() => ["", vi.fn()])  // Mocking searchInput state
      .mockImplementationOnce(() => [0, vi.fn()])    // Mocking filtersCount state
      .mockImplementationOnce(() => [null, vi.fn()]); // Mocking filtersList state
    render(
      <QueryClientProvider client={queryClient}>
        <OpeningsSearchBar onSearchClick={onSearchClick} />
      </QueryClientProvider>
    );

    // Check if an element with the class 'd-none' exists within the structure
    const dNoneElement = screen.getAllByText("", {selector: ".d-block"})[0];
    expect(dNoneElement).toBeInTheDocument();
  });
  
  it("should not show AdvancedSearchDropdown if isOpen is false", () => {
    // Create a mock function to pass as a prop
    const onSearchClick = vi.fn();
    const isOpen = false;
     // Mock the useState calls
    vi.spyOn(React, 'useState')
      .mockImplementationOnce(() => [false, vi.fn()]) // Mocking isOpen state as false
      .mockImplementationOnce(() => [false, vi.fn()]) // Mocking showFilters state as false
      .mockImplementationOnce(() => ["", vi.fn()])  // Mocking searchInput state
      .mockImplementationOnce(() => [0, vi.fn()])    // Mocking filtersCount state
      .mockImplementationOnce(() => [null, vi.fn()]); // Mocking filtersList state
    render(
      <QueryClientProvider client={queryClient}>
        <OpeningsSearchBar onSearchClick={onSearchClick} />
      </QueryClientProvider>
    );

    // Check if an element with the class 'd-none' exists within the structure
    const dNoneElement = screen.getAllByText("", {selector: ".d-none"})[0];
    expect(dNoneElement).toBeInTheDocument();
  });

  it("should show correct filter count, when count is greater than 0", () => {
    // Create a mock function to pass as a prop
    const onSearchClick = vi.fn();
     // Mock the useState calls
    vi.spyOn(React, 'useState')
      .mockImplementationOnce(() => [false, vi.fn()]) // Mocking isOpen state as false
      .mockImplementationOnce(() => [false, vi.fn()]) // Mocking showFilters state as false
      .mockImplementationOnce(() => ["", vi.fn()])  // Mocking searchInput state
      .mockImplementationOnce(() => [2, vi.fn()])    // Mocking filtersCount state
      .mockImplementationOnce(() => [null, vi.fn()]); // Mocking filtersList state
    
    render(
      <QueryClientProvider client={queryClient}>
          <OpeningsSearchBar onSearchClick={onSearchClick} />
      </QueryClientProvider>
    );

    console.log(screen.debug());

    // Check if an element with the class 'd-none' exists within the structure
    const dNoneElement = screen.getByText('+2');
    expect(dNoneElement).toBeInTheDocument();
  });
});