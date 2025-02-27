import React from "react";
import "@testing-library/jest-dom";
import { MemoryRouter } from "react-router-dom";
import { render, screen } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { NotificationProvider } from "../../contexts/NotificationProvider";
import Dashboard from "../../screens/Dashboard";

// Mock components
vi.mock("../../components/PageTitle", () => ({
  default: () => <h1 data-testid="page-title">Dashboard</h1>,
}));

vi.mock("../../components/FavouriteCard", () => ({
  default: ({ title }: { title: string }) => <div data-testid="favourite-card">{title}</div>,
}));

vi.mock("../../components/RecentOpenings", () => ({
  default: () => <div data-testid="recent-openings">Recent Openings</div>,
}));

vi.mock("../../components/OpeningSubmissionTrend", () => ({
  default: () => <div data-testid="opening-submission-trend">Opening Submission Trend</div>,
}));

vi.mock("../../components/FavouriteOpenings", () => ({
  default: () => <div data-testid="favourite-openings">Favourite Openings</div>,
}));

// Mock FavouriteCardsConfig
vi.mock("../../screens/Dashboard/constants", () => ({
  FavouriteCardsConfig: [
    { index: 1, title: "Card 1", link: "/card1", icon: "icon1" },
    { index: 2, title: "Card 2", link: "/card2", icon: "icon2" },
  ],
}));

// Helper function to render the component with QueryClientProvider
const renderWithProviders = () => {
  const queryClient = new QueryClient();
  return render(
    <MemoryRouter>
      <NotificationProvider>
        <QueryClientProvider client={queryClient}>
          <Dashboard />
        </QueryClientProvider>
      </NotificationProvider>
    </MemoryRouter>
  );
};

describe("Dashboard Component", () => {
  it("should render the page title", () => {
    renderWithProviders();
    expect(screen.getByTestId("page-title")).toHaveTextContent("Dashboard");
  });

  it("should render favourite cards", () => {
    renderWithProviders();
    const favCards = screen.getAllByTestId("favourite-card");
    expect(favCards).toHaveLength(2);
    expect(favCards[0]).toHaveTextContent("Card 1");
    expect(favCards[1]).toHaveTextContent("Card 2");
  });

  it("should render the Recent Openings section", () => {
    renderWithProviders();
    expect(screen.getByTestId("recent-openings")).toBeInTheDocument();
  });

  it("should render the Opening Submission Trend section", () => {
    renderWithProviders();
    expect(screen.getByTestId("opening-submission-trend")).toBeInTheDocument();
  });

  it("should render the Favourite Openings section", () => {
    renderWithProviders();
    expect(screen.getByTestId("favourite-openings")).toBeInTheDocument();
  });
});
