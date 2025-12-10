import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import OpeningTableRow from "../../components/OpeningTableRow";
import { recentOpeningsHeaders } from "../../components/RecentOpenings/constants";
import { OpeningSearchResponseDto } from "../../services/OpenApi";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter } from "react-router-dom";
import "@testing-library/jest-dom";

// Mock components
vi.mock("../../components/StatusTag", () => ({
  default: ({ description }: { description: string }) => (
    <span data-testid="status-tag">{description}</span>
  ),
}));

vi.mock("../../components/SpatialCheckbox", () => ({
  default: ({ rowId, selectedRows, handleRowSelection }: any) => (
    <input
      type="checkbox"
      data-testid={`spatial-checkbox-${rowId}`}
      checked={selectedRows.includes(rowId)}
      onChange={() => handleRowSelection(rowId)}
    />
  ),
}));

vi.mock("../../components/OpeningBookmarkBtn", () => ({
  default: ({ openingId, favorited }: { openingId?: number; favorited: boolean }) => (
    <button data-testid={`opening-bookmark-btn-${openingId}`}>
      {favorited ? "Bookmarked" : "Bookmark"}
    </button>
  ),
}));

// Mock data
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
  silvaReliefAppId: 0,
  favourite: false,
  clientName: "",
  earlyFreeGrowingDate: null,
  clientLocation: null,
  lateFreeGrowingDate: null,
  lastViewDate: null
};

// Test setup
const renderWithProviders = (props = {}) => {
  return render(
    <MemoryRouter>
      <QueryClientProvider client={new QueryClient()}>
        <table>
          <tbody>
            <OpeningTableRow
              headers={recentOpeningsHeaders}
              rowData={openingA}
              showMap={false}
              selectedRows={[]}
              handleRowSelection={vi.fn()}
              {...props}
            />
          </tbody>
        </table>
      </QueryClientProvider>
    </MemoryRouter>
  );
};

describe("OpeningRow component", () => {
  it("should render table row with correct data", () => {
    renderWithProviders();
    expect(screen.getByText("Free Growing")).toBeInTheDocument();
    expect(screen.getByText("NREQ")).toBeInTheDocument();
  });

  it("should render a status tag with correct text", () => {
    renderWithProviders();
    expect(
      screen.getByTestId(`tag__status_colored_tag_${"teal"}`)
    ).toHaveTextContent("Free Growing");
  });

  it("should display tooltip with category description", async () => {
    renderWithProviders();
    const categoryElement = screen.getByText("NREQ");
    fireEvent.mouseOver(categoryElement);

    expect(
      await screen.findByText("Areas where SP/SMP's are not required by law")
    ).toBeInTheDocument();
  });

  it("should format and display the disturbance start date", () => {
    renderWithProviders();
    expect(screen.getByText("Oct 21, 2011")).toBeInTheDocument();
  });

  it("should display the action buttons", () => {
    renderWithProviders();
    expect(screen.getByTestId("opening-bookmark-btn-101")).toHaveTextContent(
      "Bookmark"
    );
  });
});
