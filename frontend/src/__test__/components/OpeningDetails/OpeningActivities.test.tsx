import React from "react";
import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter } from "react-router-dom";
import OpeningActivities from "@/components/OpeningDetails/OpeningActivities";
import { formatActivityObjective } from "@/components/OpeningDetails/OpeningActivities/utils";
import API from "@/services/API";
import {
  OpeningDetailsActivitiesActivitiesDto,
  OpeningDetailsActivitiesDisturbanceDto,
  PagedModelOpeningDetailsActivitiesActivitiesDto,
} from "@/services/OpenApi";

vi.mock("@/services/API", () => ({
  default: {
    OpeningEndpointService: {
      getOpeningDisturbances: vi.fn(),
      getOpeningActivities: vi.fn(),
      getOpeningActivity: vi.fn(),
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
    Tooltip: ({ children, label, ...props }: any) => (
      <div data-testid="tooltip" title={typeof label === "string" ? label : undefined} {...props}>
        {children}
      </div>
    ),
    DefinitionTooltip: ({ children, definition, openOnHover, ...props }: any) => (
      <span title={typeof definition === "string" ? definition : undefined} {...props}>
        {children}
      </span>
    ),
  };
});

describe("OpeningActivities", () => {
  let queryClient: QueryClient;
  const setSelectedSilvicultureActivityIds = vi.fn();
  const setSelectedDisturbanceIds = vi.fn();

  const mockDisturbances: OpeningDetailsActivitiesDisturbanceDto[] = [
    {
      atuId: 101,
      silvBaseCode: { code: "DN", description: "Disturbance" },
      disturbance: { code: "L", description: "Clearcut" },
      system: { code: "SYS", description: "System One" },
      variant: { code: "VAR", description: "Variant One" },
      cutPhase: { code: "PHA", description: "Phase One" },
      disturbanceArea: 18.5,
      lastUpdate: "2020-05-20",
      startDate: "2020-01-10",
      endDate: "2020-05-15",
      licenseeActivityId: "LIC101",
      forestClient: { clientNumber: "0001", clientName: "Client A" } as any,
      forestClientLocation: { locationCode: "00", locationName: "Location A" } as any,
      licenceNumber: "TFL47",
      cuttingPermitId: "CP01",
      cutBlock: "CB01",
      comments: [
        {
          commentSource: { code: "ATU", description: "Activity" },
          commentType: { code: "COMMENT", description: "Comment" },
          commentText: "Disturbance completed smoothly.",
        },
      ],
    },
  ];

  const mockActivities: OpeningDetailsActivitiesActivitiesDto[] = [
    {
      atuId: 201,
      status: { code: "C", description: "Complete" },
      base: { code: "PL", description: "Planting" },
      tech: { code: "BR", description: "Brushing" },
      method: { code: "HAND", description: "Hand" },
      area: 12.3,
      funding: { code: "B", description: "Basic" },
      projectId: "P123",
      lastUpdate: "2021-06-20",
      plannedDate: "2021-05-01",
      endDate: "2021-06-15",
      objective1: { code: "OBJ1", description: "First Objective" },
      objective2: { code: "OBJ2", description: "Second Objective" },
      objective3: null as any,
    },
    {
      atuId: 202,
      status: { code: "P", description: "Planned" },
      base: { code: "DS", description: "Direct Seeding" },
      tech: { code: "AIR", description: "Aerial" },
      method: null as any,
      area: 5.5,
      funding: null as any,
      projectId: null as any,
      lastUpdate: null as any,
      plannedDate: "2021-07-01",
      endDate: "2021-07-20",
      objective1: null as any,
      objective2: null as any,
      objective3: null as any,
    },
  ];

  const mockPagedActivities: PagedModelOpeningDetailsActivitiesActivitiesDto = {
    content: mockActivities,
    page: {
      number: 0,
      size: 5,
      totalElements: 2,
      totalPages: 1,
    },
  };

  const mockActivityDetail = {
    atuId: 201,
    totalPlanting: 4500,
    species: [
      {
        species: { code: "FDC", description: "Douglas Fir" },
        plantedNumber: 2500,
        numberBeyondTransferLimit: 0,
        lot: "SL1234",
        requestId: 101,
        vegetationLotId: "VL99",
        bidPricePerTree: 0.75,
        cbst: true,
      },
    ],
    seeding: [
      {
        species: { code: "PLI", description: "Lodgepole Pine" },
        seedlotNumber: "SL5678",
        quantityKg: 3.2,
      },
    ],
    spacing: {
      cropTreesPerHa: 1200,
      interTreeDistance: 2.5,
      maxAllowableRemaining: 1500,
    },
    pruning: {
      pruningHeight: 2.8,
      prunedStemsPerHa: 450,
      minCrownPct: 35,
    },
    sitePreparation: {
      targetStemsPerHa: 1100,
    },
    survey: {
      surveyNumber: 42,
    },
    general: {
      funded: true,
    },
    comments: [
      {
        commentSource: { code: "ATU", description: "Activity" },
        commentType: { code: "GEN", description: "General" },
        commentText: "Activity comment details.",
      },
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
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  const renderComponent = (
    openingId: number = 101,
    selectedSilvicultureActivityIds: string[] = [],
    selectedDisturbanceIds: string[] = [],
    route: string = "/openings/101"
  ) => {
    return render(
      <MemoryRouter initialEntries={[route]}>
        <QueryClientProvider client={queryClient}>
          <OpeningActivities
            openingId={openingId}
            selectedSilvicultureActivityIds={selectedSilvicultureActivityIds}
            setSelectedSilvicultureActivityIds={setSelectedSilvicultureActivityIds}
            selectedDisturbanceIds={selectedDisturbanceIds}
            setSelectedDisturbanceIds={setSelectedDisturbanceIds}
          />
        </QueryClientProvider>
      </MemoryRouter>
    );
  };

  describe("utils: formatActivityObjective", () => {
    it("returns null tooltip and placeholder when data is null", () => {
      const result = formatActivityObjective(null);
      expect(result.tooltipDefinition).toBeNull();
      expect(result.displayText).toBe("--");
    });

    it("formats objectives correctly when present", () => {
      const result = formatActivityObjective(mockActivities[0]);
      expect(result.tooltipDefinition).toEqual([
        "OBJ1 - First Objective",
        "OBJ2 - Second Objective",
      ]);
      expect(result.displayText).toContain("OBJ1");
      expect(result.displayText).toContain("OBJ2");
    });

    it("returns placeholder when data has no objectives", () => {
      const result = formatActivityObjective(mockActivities[1]);
      expect(result.tooltipDefinition).toEqual([]);
      expect(result.displayText).toBe("--");
    });
  });

  describe("Component Rendering & States", () => {
    it("renders loading skeleton when queries are loading", () => {
      vi.mocked(API.OpeningEndpointService.getOpeningDisturbances).mockImplementation(
        () => new Promise(() => {})
      );
      vi.mocked(API.OpeningEndpointService.getOpeningActivities).mockImplementation(
        () => new Promise(() => {})
      );

      renderComponent(101);

      const skeleton = document.querySelector(".cds--accordion.cds--skeleton");
      expect(skeleton).toBeInTheDocument();
    });

    it("renders empty section when no disturbance and no activity records exist", async () => {
      vi.mocked(API.OpeningEndpointService.getOpeningDisturbances).mockResolvedValue({
        content: [],
      });
      vi.mocked(API.OpeningEndpointService.getOpeningActivities).mockResolvedValue({
        content: [],
        page: { totalElements: 0, number: 0, size: 5, totalPages: 0 },
      });

      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText("Nothing to show yet!")).toBeInTheDocument();
      });

      expect(
        screen.getByText("No Activities have been added to this opening yet")
      ).toBeInTheDocument();
    });

    it("renders tab title and both Disturbance and Activity accordions when data is present", async () => {
      vi.mocked(API.OpeningEndpointService.getOpeningDisturbances).mockResolvedValue({
        content: mockDisturbances,
      });
      vi.mocked(API.OpeningEndpointService.getOpeningActivities).mockResolvedValue(
        mockPagedActivities
      );

      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText(/3\s+activities in the opening area/i)).toBeInTheDocument();
      });

      expect(screen.getByText("Disturbance events")).toBeInTheDocument();
      expect(screen.getByText("Total disturbance: 1")).toBeInTheDocument();
      expect(screen.getByText("Silviculture activities")).toBeInTheDocument();
      expect(screen.getByText("Total activities: 2")).toBeInTheDocument();
    });
  });

  describe("Disturbance Accordion Interactions", () => {
    beforeEach(() => {
      vi.mocked(API.OpeningEndpointService.getOpeningDisturbances).mockResolvedValue({
        content: mockDisturbances,
      });
      vi.mocked(API.OpeningEndpointService.getOpeningActivities).mockResolvedValue({
        content: [],
        page: { totalElements: 0, number: 0, size: 5, totalPages: 0 },
      });
    });

    it("renders disturbance table headers and cell values when opened", async () => {
      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText("Disturbance events")).toBeInTheDocument();
      });

      fireEvent.click(screen.getByRole("button", { name: /Disturbance events/i }));

      await waitFor(() => {
        expect(screen.getByText("Activity treatment unit ID")).toBeInTheDocument();
      });

      expect(screen.getByText("Disturbance code")).toBeInTheDocument();
      expect(screen.getByText("Silviculture system")).toBeInTheDocument();
      expect(screen.getByText("Variant")).toBeInTheDocument();
      expect(screen.getByText("Cut phase")).toBeInTheDocument();
      expect(screen.getByText("Start date")).toBeInTheDocument();
      expect(screen.getByText("End date")).toBeInTheDocument();

      // Check row values
      expect(screen.getByText("101")).toBeInTheDocument();
      expect(screen.getByText("L - Clearcut")).toBeInTheDocument();
      expect(screen.getByText("SYS")).toBeInTheDocument();
      expect(screen.getByText("VAR")).toBeInTheDocument();
      expect(screen.getByText("PHA")).toBeInTheDocument();
    });

    it("filters disturbances by search input", async () => {
      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText("Disturbance events")).toBeInTheDocument();
      });

      fireEvent.click(screen.getByRole("button", { name: /Disturbance events/i }));

      await waitFor(() => {
        expect(screen.getByText("Activity treatment unit ID")).toBeInTheDocument();
      });

      const searchInput = screen.getByRole("searchbox", { name: /Filter disturbances/i });
      fireEvent.change(searchInput, { target: { value: "NonExistentTerm" } });

      await waitFor(() => {
        expect(screen.getByText(/No results for "NonExistentTerm"/i)).toBeInTheDocument();
      });

      fireEvent.change(searchInput, { target: { value: "" } });
      await waitFor(() => {
        expect(screen.getByText("101")).toBeInTheDocument();
      });
    });

    it("toggles map polygon selection checkbox for disturbance", async () => {
      renderComponent(101, [], []);

      await waitFor(() => {
        expect(screen.getByText("Disturbance events")).toBeInTheDocument();
      });

      fireEvent.click(screen.getByRole("button", { name: /Disturbance events/i }));

      await waitFor(() => {
        expect(screen.getByTestId("disturbance-map-checkbox-101-0")).toBeInTheDocument();
      });

      const mapCheckbox = screen.getByTestId("disturbance-map-checkbox-101-0");
      expect(mapCheckbox).not.toBeChecked();

      fireEvent.click(mapCheckbox);
      expect(setSelectedDisturbanceIds).toHaveBeenCalledWith(expect.any(Function));
    });

    it("expands disturbance row to reveal DisturbanceDetail", async () => {
      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText("Disturbance events")).toBeInTheDocument();
      });

      fireEvent.click(screen.getByRole("button", { name: /Disturbance events/i }));

      await waitFor(() => {
        expect(document.querySelector("#disturbance-row-101 .cds--table-expand__button")).toBeInTheDocument();
      });

      const expandBtn = document.querySelector("#disturbance-row-101 .cds--table-expand__button")!;
      fireEvent.click(expandBtn);

      await waitFor(() => {
        expect(screen.getByText("Disturbance overview")).toBeInTheDocument();
      });

      expect(screen.getByText(/Disturbance completed smoothly/)).toBeInTheDocument();
      expect(screen.getByText("LIC101")).toBeInTheDocument();
      expect(screen.getByText("TFL47")).toBeInTheDocument();
      expect(screen.getByText("CP01")).toBeInTheDocument();
      expect(screen.getByText("CB01")).toBeInTheDocument();
    });
  });

  describe("Silviculture Activities Accordion Interactions", () => {
    beforeEach(() => {
      vi.mocked(API.OpeningEndpointService.getOpeningDisturbances).mockResolvedValue({
        content: [],
      });
      vi.mocked(API.OpeningEndpointService.getOpeningActivities).mockResolvedValue(
        mockPagedActivities
      );
      vi.mocked(API.OpeningEndpointService.getOpeningActivity).mockResolvedValue(
        mockActivityDetail
      );
    });

    it("renders activity table headers and rows when opened", async () => {
      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText("Silviculture activities")).toBeInTheDocument();
      });

      fireEvent.click(screen.getByRole("button", { name: /Silviculture activities/i }));

      await waitFor(() => {
        expect(screen.getByRole("button", { name: "Activity treatment unit ID" })).toBeInTheDocument();
      });

      expect(screen.getByRole("button", { name: "Status" })).toBeInTheDocument();
      expect(screen.getByRole("button", { name: "Base" })).toBeInTheDocument();
      expect(screen.getByRole("button", { name: "Tech" })).toBeInTheDocument();
      expect(screen.getByRole("button", { name: "Method" })).toBeInTheDocument();
      expect(screen.getByRole("button", { name: "Objective" })).toBeInTheDocument();
      expect(screen.getByRole("button", { name: "Area" })).toBeInTheDocument();
      expect(screen.getByRole("button", { name: "Funding Source" })).toBeInTheDocument();

      // Row values
      expect(screen.getByText("201")).toBeInTheDocument();
      expect(screen.getByText("PL - Planting")).toBeInTheDocument();
      expect(screen.getByText("BR")).toBeInTheDocument();
      expect(screen.getByText("HAND")).toBeInTheDocument();

      expect(screen.getByText("202")).toBeInTheDocument();
      expect(screen.getByText("DS - Direct Seeding")).toBeInTheDocument();
    });

    it("toggles map polygon selection checkbox for silviculture activity", async () => {
      renderComponent(101, ["201-PL"]);

      await waitFor(() => {
        expect(screen.getByText("Silviculture activities")).toBeInTheDocument();
      });

      fireEvent.click(screen.getByRole("button", { name: /Silviculture activities/i }));

      await waitFor(() => {
        expect(screen.getByTestId("activities-map-checkbox-201-0")).toBeInTheDocument();
      });

      const mapCheckbox = screen.getByTestId("activities-map-checkbox-201-0");
      expect(mapCheckbox).toBeChecked();

      fireEvent.click(mapCheckbox);
      expect(setSelectedSilvicultureActivityIds).toHaveBeenCalledWith(expect.any(Function));
    });

    it("expands activity row to reveal ActivityDetail and subcomponents", async () => {
      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText("Silviculture activities")).toBeInTheDocument();
      });

      fireEvent.click(screen.getByRole("button", { name: /Silviculture activities/i }));

      await waitFor(() => {
        expect(document.querySelector("#activity-row-201 .cds--table-expand__button")).toBeInTheDocument();
      });

      const expandBtn = document.querySelector("#activity-row-201 .cds--table-expand__button")!;
      fireEvent.click(expandBtn);

      await waitFor(() => {
        expect(screen.getByText("Planting specifications")).toBeInTheDocument();
      });

      expect(screen.getByText("Total planting: 4500")).toBeInTheDocument();
      expect(screen.getByText("Total species: 1")).toBeInTheDocument();
      expect(screen.getByText("FDC - Douglas Fir")).toBeInTheDocument();
      expect(screen.getByText("SL1234")).toBeInTheDocument();

      expect(screen.getByText(/Activity comment details/)).toBeInTheDocument();
    });

    it("supports deep linking to specific activity via query parameter", async () => {
      renderComponent(101, [], [], "/openings/101?activityId=201&section=activity-comment");

      await waitFor(() => {
        expect(screen.getByText("Planting specifications")).toBeInTheDocument();
      });
    });

    it("triggers activity search and sorting", async () => {
      renderComponent(101);

      await waitFor(() => {
        expect(screen.getByText("Silviculture activities")).toBeInTheDocument();
      });

      fireEvent.click(screen.getByRole("button", { name: /Silviculture activities/i }));

      await waitFor(() => {
        expect(screen.getByRole("button", { name: "Activity treatment unit ID" })).toBeInTheDocument();
      });

      const activitySearchInput = screen.getByRole("searchbox", { name: /Filter table/i });
      fireEvent.change(activitySearchInput, { target: { value: "Planting" } });
      fireEvent.keyDown(activitySearchInput, { key: "Enter", code: "Enter" });

      await waitFor(() => {
        expect(API.OpeningEndpointService.getOpeningActivities).toHaveBeenCalledWith(
          101,
          "Planting",
          expect.any(Number),
          expect.any(Number),
          undefined
        );
      });

      const sortableHeader = screen.getByRole("button", { name: "Activity treatment unit ID" });
      fireEvent.click(sortableHeader);

      await waitFor(() => {
        expect(API.OpeningEndpointService.getOpeningActivities).toHaveBeenCalledWith(
          101,
          "Planting",
          expect.any(Number),
          expect.any(Number),
          expect.arrayContaining([expect.stringContaining("atuId")])
        );
      });
    });
  });
});
