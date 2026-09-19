import React from "react";
import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter } from "react-router-dom";
import OpeningForestCover from "@/components/OpeningDetails/OpeningForestCover";
import { formatForestCoverSpeciesArray } from "@/components/OpeningDetails/OpeningForestCover/utils";
import API from "@/services/API";
import {
  OpeningForestCoverDto,
  OpeningForestCoverDetailsDto,
  OpeningForestCoverHistoryOverviewDto,
} from "@/services/OpenApi";

vi.mock("@/services/API", () => ({
  default: {
    OpeningEndpointService: {
      getCover: vi.fn(),
      getCoverHistoryOverview: vi.fn(),
      getCoverHistory: vi.fn(),
      getCoverDetails: vi.fn(),
      getForestCoverHistoryDetails: vi.fn(),
    },
  },
}));

vi.mock("@/hooks/useDeepLinkScroll", () => ({
  default: vi.fn(),
}));

const mockUsePolygonAvailability = vi.fn();
vi.mock("@/hooks/usePolygonAvailability", () => ({
  default: (...args: any[]) => mockUsePolygonAvailability(...args),
}));

vi.mock("@carbon/react", async (importOriginal) => {
  const actual = await importOriginal<typeof import("@carbon/react")>();
  return {
    ...actual,
    DefinitionTooltip: ({ children, definition }: any) => (
      <span title={typeof definition === "string" ? definition : undefined}>
        {children}
      </span>
    ),
  };
});

describe("OpeningForestCover", () => {
  let queryClient: QueryClient;
  const setSelectedForestCoverIds = vi.fn();

  const mockForestCoverList: OpeningForestCoverDto[] = [
    {
      coverId: 101,
      polygonId: "1",
      standardUnitId: "SU1",
      unmappedArea: { code: "NAT", description: "Natural" },
      grossArea: 15.5,
      netArea: 12.3,
      status: { code: "IMM", description: "Immature" },
      coverType: { code: "ART", description: "Artificial" },
      inventoryLayer: {
        species: [
          { code: "FDC", description: "Douglas Fir" },
          { code: "PLI", description: "Lodgepole Pine" },
        ],
        total: 1000,
        totalWellSpaced: 800,
        wellSpaced: 700,
        freeGrowing: 600,
      },
      silvicultureLayer: {
        species: [{ code: "FDC", description: "Douglas Fir" }],
        total: 1200,
        totalWellSpaced: 900,
        wellSpaced: 850,
        freeGrowing: 750,
      },
      referenceYear: 2022,
      isSingleLayer: true,
      hasReserve: true,
    },
  ];

  const mockHistoryOverviewList: OpeningForestCoverHistoryOverviewDto[] = [
    {
      updateTimestamp: "2023-05-15T00:00:00",
      np: 2.5,
      nsr: 1.0,
      imm: 12.0,
      other: 0.0,
      total: 15.5,
      hasDetails: true,
      isCurrent: true,
      isOldest: false,
    },
    {
      updateTimestamp: "2021-04-10T00:00:00",
      np: 3.0,
      nsr: 2.5,
      imm: 10.0,
      other: 0.0,
      total: 15.5,
      hasDetails: true,
      isCurrent: false,
      isOldest: true,
    },
  ];

  const mockCoverDetails: OpeningForestCoverDetailsDto = {
    polygon: {
      forestCoverId: 101,
      reserve: { code: "G", description: "Group" },
      objective: { code: "BIO", description: "Biodiversity" },
      siteClass: { code: "G", description: "Good" },
      siteIndex: 24,
      siteIndexSource: { code: "S", description: "Survey" },
      treeCoverPattern: { code: "1", description: "Uniform" },
      reentryYear: 2030,
    } as any,
    isSingleLayer: true,
    unmapped: [
      {
        unmappedAreaId: "1",
        unmappedArea: { code: "R", description: "Rock" },
        stockingStatus: { code: "IMM", description: "Immature" },
        stockingType: { code: "ART", description: "Artificial" },
        area: 1.2,
      } as any,
    ],
    layers: [
      {
        layerId: 1,
        layer: { code: "I", description: "Inventory" },
        crownClosure: 45,
        basalAreaSt: 25,
        totalStems: 1000,
        totalWellSpaced: 800,
        wellSpaced: 700,
        freeGrowing: 600,
        species: [
          {
            species: { code: "FDC", description: "Douglas Fir" },
            percentage: 60,
            averageAge: 25,
            averageHeight: 12,
          },
        ],
        damage: [],
      } as any,
    ],
  };

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: { retry: false },
      },
    });
    vi.clearAllMocks();
    mockUsePolygonAvailability.mockReturnValue({
      isAvailable: true,
      isLoading: false,
    });
    vi.mocked(API.OpeningEndpointService.getCover).mockResolvedValue(mockForestCoverList);
    vi.mocked(API.OpeningEndpointService.getCoverHistoryOverview).mockResolvedValue(
      mockHistoryOverviewList
    );
    vi.mocked(API.OpeningEndpointService.getCoverDetails).mockResolvedValue(mockCoverDetails);
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  const renderComponent = (
    openingId: number = 101,
    selectedForestCoverIds: string[] = [],
    route: string = "/openings/101"
  ) => {
    return render(
      <MemoryRouter initialEntries={[route]}>
        <QueryClientProvider client={queryClient}>
          <OpeningForestCover
            openingId={openingId}
            selectedForestCoverIds={selectedForestCoverIds}
            setSelectedForestCoverIds={setSelectedForestCoverIds}
          />
        </QueryClientProvider>
      </MemoryRouter>
    );
  };

  describe("utils: formatForestCoverSpeciesArray", () => {
    it("returns empty array and placeholder for null or undefined input", () => {
      expect(formatForestCoverSpeciesArray(null)).toEqual({
        tooltipDefinition: [],
        displayText: "--",
      });
      expect(formatForestCoverSpeciesArray(undefined)).toEqual({
        tooltipDefinition: [],
        displayText: "--",
      });
    });

    it("formats species array correctly with tooltip definitions and bullet display", () => {
      const species = [
        { code: "FDC", description: "Douglas Fir" },
        { code: "PLI", description: "Lodgepole Pine" },
      ];
      const result = formatForestCoverSpeciesArray(species);
      expect(result.tooltipDefinition).toEqual([
        "FDC - Douglas Fir",
        "PLI - Lodgepole Pine",
      ]);
      expect(result.displayText).toContain("FDC");
      expect(result.displayText).toContain("PLI");
    });
  });

  describe("Loading & Empty States", () => {
    it("renders loading skeletons when queries are loading", () => {
      vi.mocked(API.OpeningEndpointService.getCover).mockImplementation(
        () => new Promise(() => {})
      );
      vi.mocked(API.OpeningEndpointService.getCoverHistoryOverview).mockImplementation(
        () => new Promise(() => {})
      );

      renderComponent(101);

      expect(screen.getByText("... forest cover polygons in this opening")).toBeInTheDocument();
      // Dropdown skeleton has cds--skeleton cds--dropdown_skeleton
      expect(document.querySelector(".cds--skeleton")).toBeInTheDocument();
    });

    it("renders empty state when forest cover data is empty", async () => {
      vi.mocked(API.OpeningEndpointService.getCover).mockResolvedValue([]);

      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText("Nothing to show yet!")).toBeInTheDocument();
      });

      expect(
        screen.getByText("No forest cover have been added to this opening yet")
      ).toBeInTheDocument();
    });
  });

  describe("Populated Table Rendering & Interactions", () => {
    it("renders table headers, row cells, and total count", async () => {
      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText(/1 forest cover polygons in this opening/i)).toBeInTheDocument();
      });

      // Headers
      expect(screen.getByText("Forest cover")).toBeInTheDocument();
      expect(screen.getByText("Quick information")).toBeInTheDocument();
      expect(screen.getByText("Polygon area (ha)")).toBeInTheDocument();
      expect(screen.getByText("Stocking status")).toBeInTheDocument();
      expect(screen.getByText("Stocking type")).toBeInTheDocument();
      expect(screen.getByText("Inventory layer")).toBeInTheDocument();
      expect(screen.getByText("Silviculture layer")).toBeInTheDocument();
      expect(screen.getByText("Reference year")).toBeInTheDocument();

      // Row cells
      expect(screen.getByText("Polygon ID: 1")).toBeInTheDocument();
      expect(screen.getByText("Standards unit: SU1")).toBeInTheDocument();
      expect(screen.getByText("Gross: 15.5 ha")).toBeInTheDocument();
      expect(screen.getByText("Net: 12.3 ha")).toBeInTheDocument();
      expect(screen.getByText("Single layer")).toBeInTheDocument();
      expect(screen.getByText("Reserve")).toBeInTheDocument();
      expect(screen.getByText("ART - Artificial")).toBeInTheDocument();
    });

    it("toggles map polygon checkbox selection", async () => {
      renderComponent(101, ["101-1"]);

      await waitFor(() => {
        expect(screen.getByText("Polygon ID: 1")).toBeInTheDocument();
      });

      const checkbox = screen.getByRole("checkbox", {
        name: "Select polygon on map",
      });
      expect(checkbox).toBeChecked();

      fireEvent.click(checkbox);
      expect(setSelectedForestCoverIds).toHaveBeenCalledWith(expect.any(Function));
    });

    it("filters forest cover table by search term and clears search", async () => {
      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText("Polygon ID: 1")).toBeInTheDocument();
      });

      const searchInput = screen.getByRole("searchbox", { name: /Filter table/i });
      fireEvent.change(searchInput, { target: { value: "123" } });
      fireEvent.keyDown(searchInput, { key: "Enter", code: "Enter" });

      await waitFor(() => {
        expect(API.OpeningEndpointService.getCover).toHaveBeenCalledWith(101, "123");
      });

      const clearBtn = screen.getByRole("button", { name: /Clear search input/i });
      fireEvent.click(clearBtn);

      await waitFor(() => {
        expect(searchInput).toHaveValue("");
      });
    });

    it("expands row to show ForestCoverExpandedRow and details", async () => {
      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText("Polygon ID: 1")).toBeInTheDocument();
      });

      const expandBtn = document.querySelector("#fc-row-101-1 .cds--table-expand__button")!;
      fireEvent.click(expandBtn);

      await waitFor(() => {
        expect(screen.getByText("Polygon details")).toBeInTheDocument();
      });

      expect(screen.getByText("Reserve type")).toBeInTheDocument();
      expect(screen.getByText("G - Group")).toBeInTheDocument();
      expect(screen.getByText("Forest management")).toBeInTheDocument();
      expect(screen.getByText("Forest cover ID")).toBeInTheDocument();
    });
  });

  describe("History Overview Modal & Dropdown", () => {
    it("opens history overview modal on link click and displays historical summary table", async () => {
      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByTestId("view-history-overview-link")).toBeInTheDocument();
      });

      const historyLink = screen.getByTestId("view-history-overview-link");
      fireEvent.click(historyLink);

      await waitFor(() => {
        expect(screen.getByText("Forest cover history overview")).toBeInTheDocument();
      });

      // Check modal table headers
      expect(screen.getByText("Date")).toBeInTheDocument();
      expect(screen.getByText("NP (ha)")).toBeInTheDocument();
      expect(screen.getByText("NSR (ha)")).toBeInTheDocument();
      expect(screen.getByText("Total IMM (ha)")).toBeInTheDocument();
      expect(screen.getByText("Other (ha)")).toBeInTheDocument();
      expect(screen.getByText("Total (ha)")).toBeInTheDocument();

      // Check modal table values
      expect(screen.getByTestId("fc-history-np-2023-05-15T00:00:00")).toHaveTextContent("2.5");
      expect(screen.getByTestId("fc-history-total-2023-05-15T00:00:00")).toHaveTextContent("15.5");

      // Close modal
      const closeBtn = screen.getByRole("button", { name: /Close/i });
      fireEvent.click(closeBtn);
    });

    it("switches to historical data version when selected from dropdown", async () => {
      vi.mocked(API.OpeningEndpointService.getCoverHistory).mockResolvedValue([
        {
          ...mockForestCoverList[0],
          coverId: 88,
          polygonId: "2",
          grossArea: 10.0,
          archiveDate: "2021-04-10T00:00:00",
        } as any,
      ]);

      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByTestId("forest-cover-action-dropdown")).toBeInTheDocument();
      });

      // Click dropdown to open menu
      const dropdownButton = screen.getByRole("combobox", { name: "Action date" });
      fireEvent.click(dropdownButton);

      // Select the historical item (10/04/2021 - Oldest)
      await waitFor(() => {
        expect(screen.getByText(/10\/04\/2021\s+-\s+Oldest/i)).toBeInTheDocument();
      });

      fireEvent.click(screen.getByText(/10\/04\/2021\s+-\s+Oldest/i));

      await waitFor(() => {
        expect(API.OpeningEndpointService.getCoverHistory).toHaveBeenCalledWith(
          101,
          "2021-04-10"
        );
      });

      // In historical view, map polygon checkbox is disabled
      const checkbox = screen.getByRole("checkbox", { name: "Select polygon on map" });
      expect(checkbox).toBeDisabled();
    });
  });
});
