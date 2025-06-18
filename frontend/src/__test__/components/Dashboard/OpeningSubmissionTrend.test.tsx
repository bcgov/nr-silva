import React from "react";
import {
  render,
  screen,
  fireEvent,
  waitFor,
  act,
  within,
} from "@testing-library/react";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import OpeningSubmissionTrend from "../../../components/OpeningSubmissionTrend";
import API from "../../../services/API";

vi.mock("@carbon/charts-react", () => ({
  GroupedBarChart: React.forwardRef((_props, _ref) => (
    <div data-testid="grouped-bar-chart">Chart</div>
  )),
}));

vi.mock("../../../services/API", () => {
  return {
    default: {
      CodesEndpointService: {
        getOpeningOrgUnits: vi.fn(),
      },
      UserActionsEndpointService: {
        getOpeningsSubmissionTrends: vi.fn(),
      },
    },
  };
});

vi.mock("react-router-dom", async () => {
  const actual = await vi.importActual("react-router-dom");
  return {
    ...actual,
    useNavigate: vi.fn(),
  };
});

const renderWithProviders = async () => {
  const queryClient = new QueryClient({
    defaultOptions: { queries: { retry: false } },
  });
  let rendered;
  await act(async () => {
    rendered = render(
      <QueryClientProvider client={queryClient}>
        <OpeningSubmissionTrend />
      </QueryClientProvider>
    );
  });
  return rendered;
};

describe("OpeningSubmissionTrend Component", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("should render the component correctly", async () => {
    (
      API.UserActionsEndpointService.getOpeningsSubmissionTrends as vi.Mock
    ).mockResolvedValueOnce([]);
    (
      API.CodesEndpointService.getOpeningOrgUnits as vi.Mock
    ).mockResolvedValueOnce([
      { code: "DAS", description: "District A" },
      { code: "DBS", description: "District B" },
    ]);
    await renderWithProviders();
    expect(screen.getByText("Opening submission per year")).toBeInTheDocument();
    expect(
      screen.getByText("Check quantity and evolution of openings")
    ).toBeInTheDocument();
  });

  it("should display the dropdowns and combo box after fetching org unit data", async () => {
    (
      API.UserActionsEndpointService.getOpeningsSubmissionTrends as vi.Mock
    ).mockResolvedValueOnce([]);
    (
      API.CodesEndpointService.getOpeningOrgUnits as vi.Mock
    ).mockResolvedValueOnce([
      { code: "DAS", description: "District A" },
      { code: "DBS", description: "District B" },
    ]);

    await renderWithProviders();

    await waitFor(() => {
      const container = document.querySelector(
        ".submission-trend-input-container"
      );
      expect(container).toBeTruthy();
      const scoped = within(container!);
      expect(scoped.getByText("District")).toBeInTheDocument();
      expect(scoped.getByText("Status")).toBeInTheDocument();
      expect(scoped.getByText("Opening submission year")).toBeInTheDocument();
    });
  });

  it("should show no results message when no data is returned", async () => {
    (
      API.UserActionsEndpointService.getOpeningsSubmissionTrends as vi.Mock
    ).mockResolvedValueOnce([]);
    (
      API.CodesEndpointService.getOpeningOrgUnits as vi.Mock
    ).mockResolvedValueOnce([]);

    await renderWithProviders();

    await waitFor(() => {
      expect(screen.getByText("No results found")).toBeInTheDocument();
      expect(
        screen.getByText(
          "No results found with the current filters. Try adjusting them to refine your search."
        )
      ).toBeInTheDocument();
    });
  });

  it("should render the chart when data is available", async () => {
    (
      API.UserActionsEndpointService.getOpeningsSubmissionTrends as vi.Mock
    ).mockResolvedValueOnce([
      { monthName: "Jan", year: 2023, amount: 10 },
      { monthName: "Feb", year: 2023, amount: 20 },
    ]);
    (
      API.CodesEndpointService.getOpeningOrgUnits as vi.Mock
    ).mockResolvedValueOnce([]);

    await renderWithProviders();

    await waitFor(() => {
      expect(screen.getByTestId("grouped-bar-chart")).toBeInTheDocument();
    });
  });

  it("should update year and refetch data", async () => {
    (
      API.UserActionsEndpointService.getOpeningsSubmissionTrends as vi.Mock
    ).mockResolvedValueOnce([]);
    (
      API.CodesEndpointService.getOpeningOrgUnits as vi.Mock
    ).mockResolvedValueOnce([{ code: "DAS", description: "District A" }]);

    const { getByText } = await renderWithProviders();

    await waitFor(() => {
      const container = document.querySelector(
        ".submission-trend-input-container"
      );
      expect(container).toBeTruthy();
      expect(
        within(container!).getByText("Opening submission year")
      ).toBeInTheDocument();
    });

    const yearInput = document.getElementById(
      "trend-year-selection"
    ) as HTMLInputElement;
    fireEvent.change(yearInput, { target: { value: "2022" } });

    expect(yearInput.value).toBe("2022");
  });

  it("should update org units and trigger data fetch", async () => {
    (
      API.UserActionsEndpointService.getOpeningsSubmissionTrends as vi.Mock
    ).mockResolvedValueOnce([]);
    (
      API.CodesEndpointService.getOpeningOrgUnits as vi.Mock
    ).mockResolvedValueOnce([{ code: "DAS", description: "District A" }]);

    await renderWithProviders();

    await waitFor(() => {
      const container = document.querySelector(
        ".submission-trend-input-container"
      );
      expect(container).toBeTruthy();
      expect(within(container!).getByText("District")).toBeInTheDocument();
    });

    const districtInput = document.getElementById(
      "district-dropdown-input"
    ) as HTMLInputElement;
    expect(districtInput).toBeInTheDocument();

    fireEvent.change(districtInput, { target: { value: "District A" } });

    expect(
      screen.getByRole("option", { name: "DAS - District A" })
    ).toBeInTheDocument();
  });

  it("should show loading spinner when fetching", async () => {
    (
      API.UserActionsEndpointService.getOpeningsSubmissionTrends as vi.Mock
    ).mockImplementation(() => new Promise(() => {}));
    (
      API.CodesEndpointService.getOpeningOrgUnits as vi.Mock
    ).mockResolvedValueOnce([]);

    const { container } = await renderWithProviders();
    const spinner = container.querySelector(".trend-loading-spinner");
    expect(spinner).toBeInTheDocument();
  });
});
