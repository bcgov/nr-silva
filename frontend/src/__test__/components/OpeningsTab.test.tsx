// src/__test__/components/SilvicultureSearch/Openings/OpeningsSearchBar.test.tsx

import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { vi } from "vitest";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import OpeningsTab from "../../../src/components/OpeningsTab";
import { Provider } from "react-redux";
import store from "../../store";

describe("OpeningsTab", () => {
  // Create a new QueryClient instance for each test
  const queryClient = new QueryClient();
    const showSpatial = false
    const setShowSpatial = vi.fn()

  it("renders the component successfully", () => {
    render(
        <QueryClientProvider client={queryClient}>
            <Provider store={store}>
                <OpeningsTab 
                showSpatial={showSpatial}
                setShowSpatial={setShowSpatial}
                />
            </Provider>
      </QueryClientProvider>
    );
    // Check if the component is present with the correct text
    const searchInput = screen.getByText(/Track the history of openings you have looked at and check spatial information by selecting the openings in the table below/i);
    expect(searchInput).toBeInTheDocument();
  });

  it("shows the spatial area with Hide Spatial Button", () => {
    render(
        <QueryClientProvider client={queryClient}>
            <Provider store={store}>
                <OpeningsTab 
                showSpatial={true}
                setShowSpatial={setShowSpatial}
                />
            </Provider>
      </QueryClientProvider>
    );
    console.log(screen.debug())
    // Check if the component is present with the correct text
    const searchInput = screen.getByRole('button', { name: /Hide Spatial/i });
    expect(searchInput).toBeInTheDocument();
  });
});