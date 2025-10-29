import React from "react";
import { MemoryRouter } from "react-router-dom";
import {
  render,
  act,
  fireEvent,
  waitFor,
  screen,
} from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import FavouriteOpenings from "../../components/FavouriteOpenings";
import { NotificationProvider } from "../../contexts/NotificationProvider";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import API from "../../services/API";

vi.mock("../../services/API", () => {
  return {
    default: {
      OpeningEndpointService: {
        getFavorites: vi.fn(),
        removeFromFavorites: vi.fn(),
      },
    },
  };
});

const renderWithProviders = async () => {
  const queryClient = new QueryClient();
  let rendered;
  await act(async () => {
    rendered = render(
      <QueryClientProvider client={queryClient}>
        <NotificationProvider>
          <MemoryRouter>
            <FavouriteOpenings />
          </MemoryRouter>
        </NotificationProvider>
      </QueryClientProvider>
    );
  });
  return rendered;
};

describe("FavouriteOpenings Component", () => {
  it("renders correctly with given histories", async () => {
    (
      API.OpeningEndpointService.getFavorites as vi.Mock
    ).mockResolvedValueOnce([1, 2]);

    const { container } = await renderWithProviders();

    await waitFor(() => {
      expect(
        container.querySelector("#favourite-opening-tile-1")
      ).toBeInTheDocument();
      expect(
        container.querySelector("#favourite-opening-tile-2")
      ).toBeInTheDocument();
    });
  });

  it("renders correctly with empty histories", async () => {
    (
      API.OpeningEndpointService.getFavorites as vi.Mock
    ).mockResolvedValueOnce([]); // Simulate empty data

    await renderWithProviders();

    await waitFor(() => {
      expect(
        screen.getByText("You don't have any favourites to show yet!")
      ).toBeInTheDocument();
    });

    await waitFor(() => {
      expect(
        screen.getByText(
          "You can favourite your openings by clicking on the heart icon inside opening details page"
        )
      ).toBeInTheDocument();
    });
  });

  it("should call deleteOpeningFavorite when FavoriteButton is clicked", async () => {
    (
      API.OpeningEndpointService.getFavorites as vi.Mock
    ).mockResolvedValueOnce([1, 2]);
    (
      API.OpeningEndpointService.removeFromFavorites as vi.Mock
    ).mockRejectedValueOnce();
    const { container } = await renderWithProviders();

    const favoriteButton = container.querySelector(".favorite-icon button");
    if (favoriteButton) {
      await act(async () => fireEvent.click(favoriteButton));
      expect(
        API.OpeningEndpointService.removeFromFavorites
      ).toHaveBeenCalled();
    }
  });

  it("should call deleteOpeningFavorite and handle error when FavoriteButton is clicked", async () => {
    (
      API.OpeningEndpointService.getFavorites as vi.Mock
    ).mockResolvedValueOnce([1, 2]);
    (
      API.OpeningEndpointService.removeFromFavorites as vi.Mock
    ).mockRejectedValueOnce(new Error("Failed to delete favorite"));
    const { container } = await renderWithProviders();

    const favoriteButton = container.querySelector(".favorite-icon button");
    if (favoriteButton) {
      await act(async () => fireEvent.click(favoriteButton));
      expect(
        API.OpeningEndpointService.removeFromFavorites
      ).toHaveBeenCalled();
    }
  });
});
