import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import SilvicultureSearch from "../../screens/SilvicultureSearch";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

describe("SilvicultureSearch Component", () => {
  const queryClient = new QueryClient();

  const renderComponent = () =>
    render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
          <SilvicultureSearch />
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
