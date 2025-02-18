import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import OpeningRow from "../../components/RecentOpenings/OpeningRow";
import { OpeningSearchResponseDto } from "../../types/OpeningTypes";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import "@testing-library/jest-dom";

// Mock components
vi.mock("../../components/StatusTag", () => ({
  default: ({ code }: { code: string }) => <span data-testid="status-tag">{code}</span>,
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

vi.mock("../../components/ActionButtons", () => ({
  default: ({ rowId, favorited }: { rowId: string; favorited: boolean }) => (
    <button data-testid={`action-button-${rowId}`}>{favorited ? "Unfavorite" : "Favorite"}</button>
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
  silvaReliefAppId: "0",
  favourite: false,
};

// Test setup
const renderWithProviders = (props = {}) => {
  return render(
    <QueryClientProvider client={new QueryClient()}>
      <table>
        <tbody>
          <OpeningRow
            rowData={openingA}
            showMap={false}
            selectedRows={[]}
            handleRowSelection={vi.fn()}
            {...props}
          />
        </tbody>
      </table>
    </QueryClientProvider>
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
    expect(screen.getByTestId("status-tag")).toHaveTextContent("Free Growing");
  });

  it("should display tooltip with category description", async () => {
    renderWithProviders();

    const categoryElement = screen.getByText("NREQ");
    expect(categoryElement).toBeInTheDocument();

    fireEvent.mouseOver(categoryElement); // Simulate hover to trigger tooltip

    await waitFor(() => {
      expect(screen.getByRole("tooltip")).toBeInTheDocument();
    });

    expect(screen.getByRole("tooltip")).toHaveTextContent("Areas where SP/SMP's are not required by law");
  });

  it("should format and display the disturbance start date", () => {
    renderWithProviders();
    expect(screen.getByText("Oct 21, 2011")).toBeInTheDocument(); // Updated format
  });

  it("should display the action buttons", () => {
    renderWithProviders();
    expect(screen.getByTestId("action-button-101")).toHaveTextContent("Favorite");
  });
});
