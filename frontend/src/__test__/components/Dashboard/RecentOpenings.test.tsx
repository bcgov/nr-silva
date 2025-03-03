import React from "react";
import { MemoryRouter } from "react-router-dom";
import { render, screen, waitFor } from "@testing-library/react";
import { describe, it, expect, vi, beforeEach } from "vitest";
import RecentOpenings from "../../../components/RecentOpenings";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { fetchUserRecentOpenings } from "../../../services/OpeningService";
import { NotificationProvider } from "../../../contexts/NotificationProvider";
import { OpeningSearchResponseDto } from "../../../types/OpeningTypes";

vi.mock("../../../services/OpeningService", () => ({
  fetchUserRecentOpenings: vi.fn(),
}));

const openingA: OpeningSearchResponseDto = {
  openingId: 101,
  openingNumber: null,
  category: {
    code: "NREQ",
    description: "Areas where SP/SMP's are not required by law",
  },
  status: {
    code: "FG",
    description: "Free Growing",
  },
  cuttingPermitId: null,
  timberMark: null,
  cutBlockId: null,
  openingGrossAreaHa: 27.5,
  disturbanceStartDate: "2011-10-21T00:00:00",
  orgUnitCode: "DAS",
  orgUnitName: "Org one",
  clientNumber: null,
  clientAcronym: "",
  regenDelayDate: "2011-10-21T00:00:00",
  updateTimestamp: "2020-10-08T14:01:47",
  entryUserId: "IDIR\\MYDUDE",
  submittedToFrpa: false,
  forestFileId: null,
  silvaReliefAppId: "0",
  favourite: false,
};

const openingB = {
  openingId: 102,
  openingNumber: null,
  category: {
    code: "FTML",
    description: "Forest Tenure - Major Licensee",
  },
  status: {
    code: "APP",
    description: "Approved",
  },
  cuttingPermitId: "12T",
  timberMark: "47/12S",
  cutBlockId: "12-69",
  openingGrossAreaHa: 12.9,
  disturbanceStartDate: "2024-01-20T00:00:00",
  orgUnitCode: "DAS",
  orgUnitName: "Org one",
  clientNumber: null,
  clientLocation: null,
  clientAcronym: "",
  clientName: "",
  regenDelayDate: "2024-01-20T00:00:00",
  earlyFreeGrowingDate: "2036-01-20T00:00:00",
  lateFreeGrowingDate: "2039-01-20T00:00:00",
  updateTimestamp: "2024-01-01T00:00:00",
  entryUserId: "idir-here",
  submittedToFrpa: true,
  forestFileId: "TFL47",
  silvaReliefAppId: 101,
  lastViewDate: "2025-02-05T10:57:59.115832",
  favourite: false,
};

const renderWithProviders = () => {
  const queryClient = new QueryClient();
  return render(
    <MemoryRouter>
      <QueryClientProvider client={queryClient}>
        <NotificationProvider>
          <RecentOpenings />
        </NotificationProvider>
      </QueryClientProvider>
    </MemoryRouter>
  );
};

describe("RecentOpenings Component", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("should render the section title and subtitle", () => {
    renderWithProviders();

    expect(screen.getByText("Recent openings")).toBeInTheDocument();
    expect(
      screen.getByText(
        "Track the history of openings you have looked at and check spatial information by selecting the openings in the table below"
      )
    ).toBeInTheDocument();
  });

  it("should display a loading skeleton when fetching data", () => {
    (fetchUserRecentOpenings as vi.Mock).mockReturnValueOnce(
      new Promise(() => {})
    );

    renderWithProviders();

    expect(screen.getByLabelText("loading table data")).toBeInTheDocument();
  });

  it("should display an empty state if no recent openings are available", async () => {
    (fetchUserRecentOpenings as vi.Mock).mockResolvedValueOnce({ content: [] });

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

    (fetchUserRecentOpenings as vi.Mock).mockResolvedValueOnce(mockData);

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
    (fetchUserRecentOpenings as vi.Mock).mockResolvedValueOnce({ content: [] });

    renderWithProviders();

    await waitFor(() =>
      expect(screen.getByRole("button", { name: /Show map/i })).toBeDisabled()
    );
  });
});
