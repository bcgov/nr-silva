import React from "react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { render, screen, fireEvent, waitFor, act } from "@testing-library/react";
import { vi } from "vitest";
import { MemoryRouter } from "react-router-dom";
import OpeningsMapEntryPopup from "../../components/OpeningsMapEntryPopup";
import { isOpeningFavourite, putOpeningFavourite, deleteOpeningFavorite } from "../../services/OpeningFavouriteService";
import { useNotification } from "../../contexts/NotificationProvider";

// Mock services and context
vi.mock("../../services/OpeningFavouriteService", () => ({
  isOpeningFavourite: vi.fn(),
  putOpeningFavourite: vi.fn(),
  deleteOpeningFavorite: vi.fn(),
}));

vi.mock("../../components/FavoriteButton", () => ({
  default: ({ onFavoriteChange, favorited }: { onFavoriteChange: (fav: boolean) => void; favorited: boolean }) => (
    <button
      data-testid="mock-favorite-button"
      onClick={() => onFavoriteChange(!favorited)}
      aria-pressed={favorited}
    >
      {favorited ? "Unfavorite" : "Favorite"}
    </button>
  ),
}));

vi.mock("../../contexts/NotificationProvider", () => ({
  useNotification: vi.fn(),
}));

describe("OpeningsMapEntryPopup", () => {
  const mockDisplayNotification = vi.fn();

  beforeEach(() => {
    vi.resetAllMocks();
    (useNotification as vi.Mock).mockReturnValue({ displayNotification: mockDisplayNotification });
  });

  const renderWithProviders = async (openingId: number) => {
    const queryClient = new QueryClient();

    await act(async () => {
      render(
        <QueryClientProvider client={queryClient}>
          <MemoryRouter>
            <OpeningsMapEntryPopup openingId={openingId} />
          </MemoryRouter>
        </QueryClientProvider>
      );
    });

    await waitFor(() => expect(isOpeningFavourite).toHaveBeenCalledWith(openingId));
  };

  it("renders correctly with given openingId", async () => {
    (isOpeningFavourite as vi.Mock).mockResolvedValue(false); // Ensure mock is set before rendering
    await renderWithProviders(123);
    expect(screen.getByText("Opening ID: 123")).toBeInTheDocument();
  });

  it("fetches the favorite status on mount", async () => {
    (isOpeningFavourite as vi.Mock).mockResolvedValue(true);
    await renderWithProviders(123);

    await waitFor(() => {
      expect(isOpeningFavourite).toHaveBeenCalledWith(123);
    });

    expect(screen.getByTestId("mock-favorite-button")).toHaveAttribute("aria-pressed", "true");
  });

  it("handles setting the opening as favorite", async () => {
    (isOpeningFavourite as vi.Mock).mockResolvedValue(false);
    (putOpeningFavourite as vi.Mock).mockResolvedValue();

    await renderWithProviders(123);

    const favoriteButton = screen.getByTestId("mock-favorite-button");
    expect(favoriteButton).toBeInTheDocument();

    // Ensure button is initially not favorited
    await waitFor(() => expect(favoriteButton).toHaveAttribute("aria-pressed", "false"));

    // Click to favorite
    await act(async () => fireEvent.click(favoriteButton));

    // Ensure putOpeningFavourite was called
    await waitFor(() => expect(putOpeningFavourite).toHaveBeenCalledTimes(1));
    await waitFor(() => expect(putOpeningFavourite).toHaveBeenCalledWith(123));

    // Ensure notification was displayed
    await waitFor(() => {
      expect(mockDisplayNotification).toHaveBeenCalledWith(
        expect.objectContaining({
          title: "Opening Id 123 favourited",
          type: "success",
        })
      );
    });
  });

  it("handles removing the opening from favorites", async () => {
    (isOpeningFavourite as vi.Mock).mockResolvedValue(true);
    (deleteOpeningFavorite as vi.Mock).mockResolvedValue();

    await renderWithProviders(123);

    // Get the mocked favorite button
    const favoriteButton = screen.getByTestId("mock-favorite-button");
    expect(favoriteButton).toBeInTheDocument();

    // Ensure button is in "favorited" state before clicking
    await waitFor(() => expect(favoriteButton).toHaveAttribute("aria-pressed", "true"));

    // Click to unfavorite
    await act(async () => fireEvent.click(favoriteButton));

    // Ensure deleteOpeningFavorite was called
    await waitFor(() => expect(deleteOpeningFavorite).toHaveBeenCalledTimes(1));
    await waitFor(() => expect(deleteOpeningFavorite).toHaveBeenCalledWith(123));

    // Ensure notification was displayed
    await waitFor(() => {
      expect(mockDisplayNotification).toHaveBeenCalledWith(
        expect.objectContaining({
          title: "Opening Id 123 unfavourited",
          type: "success",
        })
      );
    });
  });

  it("displays an error notification on failure", async () => {
    (isOpeningFavourite as vi.Mock).mockResolvedValue(false);
    (putOpeningFavourite as vi.Mock).mockRejectedValue(new Error("Failed to favorite"));

    await renderWithProviders(123);

    const favoriteButton = screen.getByTestId("mock-favorite-button");
    expect(favoriteButton).toBeInTheDocument();

    // Click to favorite (which will fail)
    await act(async () => fireEvent.click(favoriteButton));

    await waitFor(() => {
      expect(mockDisplayNotification).toHaveBeenCalledWith(
        expect.objectContaining({
          title: "Error",
          subTitle: "Failed to update favorite status for 123",
          type: "error",
        })
      );
    });
  });
});
