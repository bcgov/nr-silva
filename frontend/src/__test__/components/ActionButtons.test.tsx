import React from "react";
import { describe, it, expect, vi } from "vitest";
import { MemoryRouter } from "react-router-dom";
import {
  render,
  screen,
  fireEvent,
  waitFor,
  act,
} from "@testing-library/react";
import ActionButtons from "../../components/ActionButtons";
import { NotificationProvider } from "../../contexts/NotificationProvider";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import API from "../../services/API";

vi.mock("../../services/API", () => {
  return {
    default: {
      OpeningEndpointService: {
        removeFromFavorites: vi.fn(),
        addToFavorites: vi.fn(),
      },
    },
  };
});

describe("ActionButtons", () => {
  const rowId = "123456";
  const favorited = false;
  const queryClient = new QueryClient();

  const renderWithProviders = () => {
    return render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <NotificationProvider>
            <ActionButtons favorited={favorited} rowId={rowId} />
          </NotificationProvider>
        </MemoryRouter>
      </QueryClientProvider>
    );
  };

  it("renders the 'Favorite Opening' button", () => {
    (
      API.OpeningEndpointService.removeFromFavorites as vi.Mock
    ).mockResolvedValueOnce();
    (
      API.OpeningEndpointService.addToFavorites as vi.Mock
    ).mockResolvedValueOnce();
    renderWithProviders();
    expect(
      screen.getByRole("button", { name: /Favorite Opening/i })
    ).toBeInTheDocument();
  });

  it("sets the 'Favorite Opening' as favorited when button is clicked", async () => {
    (
      API.OpeningEndpointService.removeFromFavorites as vi.Mock
    ).mockResolvedValueOnce();
    (
      API.OpeningEndpointService.addToFavorites as vi.Mock
    ).mockResolvedValueOnce();
    renderWithProviders();

    // Find the button
    const favButton = screen.getByRole("button", { name: /Favorite Opening/i });

    // Ensure initial state is "unfavorited"
    expect(favButton).toHaveAttribute("aria-pressed", "false");

    // Click the button
    act(() => fireEvent.click(favButton));

    // Wait for the button to update
    await waitFor(() => {
      expect(favButton).toHaveAttribute("aria-pressed", "true");
    });
  });
});
