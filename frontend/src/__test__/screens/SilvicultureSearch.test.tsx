import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import SilvicultureSearch from "../../screens/SilvicultureSearch";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { OpeningsSearchProvider } from "../../contexts/search/OpeningsSearch";

describe("SilvicultureSearch Component", () => {
  const queryClient = new QueryClient();

  const renderComponent = () => 
    render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
        <OpeningsSearchProvider>
            <SilvicultureSearch />
          </OpeningsSearchProvider>
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

  it("should switch to the correct tab when a tab is clicked", async () => {
    renderComponent();

    const tabs = screen.getAllByRole("tab");

    // Simulate clicking on the first tab (default behavior)
    fireEvent.click(tabs[0]);
    expect(tabs[0]).toHaveAttribute("aria-selected", "true");

    // Clicking on other tabs (even if disabled, ensure no interaction)
    fireEvent.click(tabs[1]);
    expect(tabs[1]).not.toHaveAttribute("aria-selected", "true");
  });

  it("should show content for the first tab by default", () => {
    renderComponent();

    const openingTabContent = screen.getByText(/Nothing to show yet/i);
    expect(openingTabContent).toBeInTheDocument();
  });

  it("should set the active tab based on URL query parameters", async () => {
    const originalLocation = window.location;
    delete window.location;
    window.location = {
      search: "?tab=openings",
      ...originalLocation,
    };

    renderComponent();

    const tabs = screen.getAllByRole("tab");
    await waitFor(() => {
      expect(tabs[0]).toHaveAttribute("aria-selected", "true");
    });

    window.location = originalLocation; // Reset location after the test
  });
});