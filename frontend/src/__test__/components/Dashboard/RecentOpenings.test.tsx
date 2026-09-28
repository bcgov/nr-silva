import React from "react";
import { MemoryRouter } from "react-router-dom";
import { render, screen, waitFor, fireEvent, act } from "@testing-library/react";
import { describe, it, expect, vi, beforeEach } from "vitest";
import RecentOpenings from "../../../components/RecentOpenings";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import API from "../../../services/API";
import { openingA, openingB } from "../../fixtures/openings";

vi.mock("../../../services/API", () => {
  return {
    default: {
      UserRecentOpeningEndpointService: {
        getUserRecentOpenings: vi.fn(),
      },
    },
  };
});

vi.mock("@/hooks/usePolygonAvailability", () => ({
  default: () => ({ isAvailable: true, isLoading: false }),
}));

vi.mock("../../../components/OpeningsMap", () => ({
  default: ({ openingIds, setOpeningPolygonNotFound }: any) => (
    <div data-testid="openings-map">
      <span>Map with {openingIds.length} openings</span>
      <button
        data-testid="trigger-map-error-btn"
        onClick={() => setOpeningPolygonNotFound(true, 12345)}
      >
        Trigger Error
      </button>
    </div>
  ),
}));

const renderWithProviders = (defaultMapOpen = false) => {
  const queryClient = new QueryClient();
  return render(
    <MemoryRouter>
      <QueryClientProvider client={queryClient}>
        <RecentOpenings defaultMapOpen={defaultMapOpen} />
      </QueryClientProvider>
    </MemoryRouter>
  );
};

describe("RecentOpenings Component", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("should render the section title and subtitle", () => {
    (
      API.UserRecentOpeningEndpointService.getUserRecentOpenings as vi.Mock
    ).mockResolvedValueOnce({ content: [] });
    renderWithProviders();

    expect(screen.getByText("Recent openings")).toBeInTheDocument();
    expect(
      screen.getByText(
        "Track the history of openings you have looked at and check spatial information by selecting the openings in the table below"
      )
    ).toBeInTheDocument();
  });

  it("should display a loading skeleton when fetching data", () => {
    (
      API.UserRecentOpeningEndpointService.getUserRecentOpenings as vi.Mock
    ).mockReturnValueOnce(new Promise(() => {}));

    renderWithProviders();

    expect(screen.getByLabelText("loading table data")).toBeInTheDocument();
  });

  it("should display an empty state if no recent openings are available", async () => {
    (
      API.UserRecentOpeningEndpointService.getUserRecentOpenings as vi.Mock
    ).mockResolvedValueOnce({ content: [] });

    renderWithProviders();

    await waitFor(() =>
      expect(
        screen.getByText("There are no openings to show yet")
      ).toBeInTheDocument()
    );
    expect(
      screen.getByText(
        "Your recent openings will appear here once you generate one"
      )
    ).toBeInTheDocument();
  });

  it("should render the table when recent openings data is available", async () => {
    const mockData = { content: [openingA, openingB] };

    (
      API.UserRecentOpeningEndpointService.getUserRecentOpenings as vi.Mock
    ).mockResolvedValueOnce(mockData);

    renderWithProviders();

    await waitFor(() => {
      expect(
        screen.getByRole("table", { name: "Recent openings table" })
      ).toBeInTheDocument();
    });

    expect(await screen.findByText(openingA.category.code)).toBeInTheDocument();
    expect(await screen.findByText(openingB.category.code)).toBeInTheDocument();
  });

  it("should disable the map button if no openings exist", async () => {
    (
      API.UserRecentOpeningEndpointService.getUserRecentOpenings as vi.Mock
    ).mockResolvedValueOnce({ content: [] });

    renderWithProviders();

    await waitFor(() =>
      expect(screen.getByRole("button", { name: /Show map/i })).toBeDisabled()
    );
  });

  it("toggles the map and selects/deselects rows when data exists", async () => {
    const mockData = { content: [openingA, openingB] };
    (
      API.UserRecentOpeningEndpointService.getUserRecentOpenings as vi.Mock
    ).mockResolvedValueOnce(mockData);

    renderWithProviders();

    await waitFor(() => {
      expect(
        screen.getByRole("table", { name: "Recent openings table" })
      ).toBeInTheDocument();
    });

    const toggleButton = screen.getByTestId("toggle-map-button");
    expect(toggleButton).toHaveTextContent("Show map");
    expect(toggleButton).toBeEnabled();

    // Click to open map
    act(() => {
      fireEvent.click(toggleButton);
    });
    expect(toggleButton).toHaveTextContent("Hide map");
    expect(screen.getByTestId("openings-map")).toBeInTheDocument();

    // OpeningTableRow renders spatial checkbox buttons when showMap is true
    const spatialButtons = screen.getAllByRole("button", {
      name: /view this opening on the map/i,
    });
    expect(spatialButtons.length).toBeGreaterThan(0);

    // Select row
    act(() => {
      fireEvent.click(spatialButtons[0]!);
    });
    expect(screen.getByText("Map with 1 openings")).toBeInTheDocument();

    // Deselect row
    act(() => {
      fireEvent.click(spatialButtons[0]!);
    });
    expect(screen.getByText("Map with 0 openings")).toBeInTheDocument();

    // Trigger map error
    act(() => {
      fireEvent.click(screen.getByTestId("trigger-map-error-btn"));
    });
    expect(
      screen.getByText("No map data available for this opening ID")
    ).toBeInTheDocument();

    // Hide map
    act(() => {
      fireEvent.click(toggleButton);
    });
    expect(toggleButton).toHaveTextContent("Show map");
  });
});
