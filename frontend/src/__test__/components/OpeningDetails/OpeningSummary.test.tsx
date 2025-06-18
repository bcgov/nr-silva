import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { vi } from "vitest";
import OpeningSummary from "../../../components/OpeningDetails/OpeningSummary";
import { OpeningDetailsTombstoneDto } from "../../../services/OpenApi";
import { formatLocalDate } from "../../../utils/DateUtils";
import { getClientLabel } from "../../../utils/ForestClientUtils";

// Mock the OpeningsMap component using Vitest instead of Jest
vi.mock("../../../components/OpeningsMap", () => ({
  default: () => <div data-testid="mock-openings-map">Map Component</div>,
}));

describe("OpeningSummary", () => {
  const mockTombstone: OpeningDetailsTombstoneDto = {
    openingNumber: " 92K 014 0.0  514",
    openingStatus: { code: "FG", description: "Free Growing" },
    orgUnitCode: "DAS",
    orgUnitName: "Development Unit",
    openCategory: {
      code: "FTML",
      description: "Forest Tenure - Major Licensee",
    },
    client: {
      clientNumber: "00010003",
      clientName: "Forest Client Name",
      legalFirstName: "John",
      legalMiddleName: "A.",
      clientStatusCode: { code: "ACTIVE", description: "Active Client" },
      clientTypeCode: { code: "LICENSEE", description: "Licensee" },
      acronym: "FCN",
    },
    fileId: "TFL47",
    cutBlockID: null,
    cuttingPermitId: "12K",
    timberMark: "47/12K",
    maxAllowedAccess: "7.8",
    openingGrossArea: 16.6,
    createdBy: "BABAYAGA",
    createdOn: "2001-06-07",
    lastUpdatedOn: "2014-04-02",
    disturbanceStartDate: "2001-09-18",
  };

  test("renders all opening summary information correctly", async () => {
    render(<OpeningSummary openingId={101017} tombstoneObj={mockTombstone} />);

    // Check title
    expect(screen.getByText("Opening summary")).toBeInTheDocument();

    // Check button
    expect(screen.getByText("Hide Map")).toBeInTheDocument();

    // Check card items using data-testid
    expect(
      screen.getByTestId("card-item-content-opening-number")
    ).toHaveTextContent("92K 014 0.0 514");
    expect(
      screen.getByTestId("card-item-content-opening-status")
    ).toBeInTheDocument();
    expect(screen.getByText("Free Growing")).toBeInTheDocument();

    expect(screen.getByTestId("card-item-content-org-unit")).toHaveTextContent(
      "DAS"
    );
    expect(
      screen.getByTestId("card-item-content-opening-category")
    ).toHaveTextContent("FTML");

    // Check client formatting
    const expectedClientLabel = getClientLabel(
      {
        id: "00010003",
        name: "",
        acronym: "FCN",
      },
      true
    );
    expect(screen.getByTestId("card-item-content-client")).toHaveTextContent(
      expectedClientLabel
    );
    const trigger = screen.getByTestId("card-item-content-client");
    fireEvent.mouseOver(trigger);
    expect(await screen.findByText("Forest Client Name")).toBeInTheDocument();

    expect(screen.getByTestId("card-item-content-file-id")).toHaveTextContent(
      "TFL47"
    );
    expect(
      screen.getByTestId("card-item-content-cutting-permit")
    ).toHaveTextContent("12K");
    expect(
      screen.getByTestId("card-item-content-timber-mark")
    ).toHaveTextContent("47/12K");
    expect(
      screen.getByTestId("card-item-content-max-allowed-access")
    ).toHaveTextContent("7.8");
    expect(
      screen.getByTestId("card-item-content-opening-gross-area")
    ).toHaveTextContent("16.6 ha");
    expect(
      screen.getByTestId("card-item-content-created-by")
    ).toHaveTextContent("BABAYAGA");

    // Check formatted dates
    const formattedCreatedOn = formatLocalDate("2001-06-07", true);
    expect(
      screen.getByTestId("card-item-content-created-on")
    ).toHaveTextContent(formattedCreatedOn || "");

    const formattedLastUpdated = formatLocalDate("2014-04-02", true);
    expect(
      screen.getByTestId("card-item-content-last-updated")
    ).toHaveTextContent(formattedLastUpdated || "");

    const formattedDisturbanceStart = formatLocalDate("2001-09-18", true);
    expect(
      screen.getByTestId("card-item-content-disturbance-start")
    ).toHaveTextContent(formattedDisturbanceStart || "");

    // Check if map is shown
    expect(screen.getByTestId("mock-openings-map")).toBeInTheDocument();
  });

  test("toggles map visibility when button is clicked", () => {
    render(<OpeningSummary openingId={101017} tombstoneObj={mockTombstone} />);

    // Map should be visible initially
    expect(screen.getByTestId("mock-openings-map")).toBeInTheDocument();

    // Click the Hide Map button
    fireEvent.click(screen.getByText("Hide Map"));

    // Button text should change
    expect(screen.getByText("Show Map")).toBeInTheDocument();

    // Map should be hidden
    expect(screen.queryByTestId("mock-openings-map")).not.toBeInTheDocument();

    // Click the Show Map button
    fireEvent.click(screen.getByText("Show Map"));

    // Button text should change back
    expect(screen.getByText("Hide Map")).toBeInTheDocument();

    // Map should be visible again
    expect(screen.getByTestId("mock-openings-map")).toBeInTheDocument();
  });

  test("renders skeleton when isLoading is true", () => {
    const { container } = render(<OpeningSummary isLoading={true} />);

    // Check for skeleton classes in the component
    const skeletons = container.querySelectorAll(".cds--skeleton");
    expect(skeletons.length).toBeGreaterThan(0);
  });

  test("handles null or empty values correctly", () => {
    const emptyTombstone: OpeningDetailsTombstoneDto = {
      openingNumber: "",
      openingStatus: { code: "", description: "" },
      orgUnitCode: null,
      orgUnitName: null,
      openCategory: { code: "", description: "" },
      client: {
        clientNumber: "",
        clientName: "",
        legalFirstName: "",
        legalMiddleName: "",
        clientStatusCode: { code: "", description: "" },
        clientTypeCode: { code: "", description: "" },
        acronym: "",
      },
      fileId: null,
      cutBlockID: null,
      cuttingPermitId: null,
      timberMark: null,
      maxAllowedAccess: null,
      openingGrossArea: null,
      createdBy: null,
      createdOn: null,
      lastUpdatedOn: null,
      disturbanceStartDate: null,
    };

    render(<OpeningSummary tombstoneObj={emptyTombstone} />);

    // Component should render without crashing with empty values
    expect(screen.getByText("Opening summary")).toBeInTheDocument();

    // Map should not be rendered when opening number is empty
    expect(screen.queryByTestId("mock-openings-map")).not.toBeInTheDocument();
  });
});
