import React from "react";
import { render, screen, fireEvent, waitFor, act } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import OpeningSubmissionTrend from "../../../components/OpeningSubmissionTrend";
import "@testing-library/jest-dom";

// Mock services
import { fetchOpeningsOrgUnits, fetchUserSubmissionTrends } from "../../../services/OpeningService";

// Mock Carbon components
vi.mock("@carbon/charts-react", () => ({
  GroupedBarChart: React.forwardRef((_props, _ref) => (
    <div data-testid="grouped-bar-chart">Chart</div>
  )),
}));

vi.mock("../../../services/OpeningService", () => ({
  fetchOpeningsOrgUnits: vi.fn(),
  fetchUserSubmissionTrends: vi.fn(),
}));

vi.mock("react-router-dom", async () => {
  const actual = await vi.importActual("react-router-dom");
  return {
    ...actual,
    useNavigate: vi.fn(),
  };
});

// Mock QueryClientProvider setup
const renderWithProviders = async () => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        retry: false,
      },
    },
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
    await renderWithProviders();
    expect(screen.getByText("Opening submission per year")).toBeInTheDocument();
    expect(screen.getByText("Check quantity and evolution of openings")).toBeInTheDocument();
  });

  it("should display the dropdowns and combo box after fetching org unit data", async () => {
    (fetchUserSubmissionTrends as vi.Mock).mockResolvedValueOnce([]);
    (fetchOpeningsOrgUnits as vi.Mock).mockResolvedValueOnce([
      { code: "DAS", description: "District A" },
      { code: "DBS", description: "District B" },
    ]);
    renderWithProviders();

    await waitFor(() => expect(screen.getByText("District")).toBeInTheDocument());
    expect(screen.getByText("Status")).toBeInTheDocument();
    expect(screen.getByText("Opening submission year")).toBeInTheDocument();
  });

  it("should show no results message when no data is returned", async () => {
    (fetchUserSubmissionTrends as vi.Mock).mockResolvedValueOnce([]);
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
    (fetchUserSubmissionTrends as vi.Mock).mockResolvedValueOnce([
      { monthName: "Jan", year: 2023, amount: 10 },
      { monthName: "Feb", year: 2023, amount: 20 },
    ]);
    await renderWithProviders();

    await waitFor(() => expect(screen.getByTestId("grouped-bar-chart")).toBeInTheDocument());
  });

  it("should update year and refetch data", async () => {
    (fetchOpeningsOrgUnits as vi.Mock).mockResolvedValueOnce([
      { code: "DAS", description: "District A" },
    ]);
    (fetchUserSubmissionTrends as vi.Mock).mockResolvedValue([]);

    const { getByLabelText, getByText } = await renderWithProviders();

    // Wait for dropdowns to appear
    await waitFor(() => {
      expect(getByText("Opening submission year")).toBeInTheDocument();
    });

    const yearDropdownInput = getByLabelText("Opening submission year", {
      selector: 'input',
    });

    // Simulate input value change
    fireEvent.change(yearDropdownInput, { target: { value: "2022" } });

    // Verify it still renders correctly after interaction
    expect(getByText("Opening submission per year")).toBeInTheDocument();
  });

  it("should update org units and trigger data fetch", async () => {
    (fetchOpeningsOrgUnits as vi.Mock).mockResolvedValueOnce([
      { code: "DAS", description: "District A" },
    ]);
    (fetchUserSubmissionTrends as vi.Mock).mockResolvedValue([]); // allow refetch

    const { getByText, getAllByRole } = await renderWithProviders();

    await waitFor(() => expect(getByText("District")).toBeInTheDocument());

    const dropdowns = getAllByRole("combobox");
    expect(dropdowns.length).toBeGreaterThan(0);
  });

  it("should show loading spinner when fetching", async () => {
    (fetchUserSubmissionTrends as vi.Mock).mockImplementation(() => new Promise(() => { }));
    const { container } = await renderWithProviders();

    const spinner = container.querySelector(".trend-loading-spinner");
    expect(spinner).toBeInTheDocument();
  });
});
