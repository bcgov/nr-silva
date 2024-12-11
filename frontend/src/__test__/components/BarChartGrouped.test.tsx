// React and test imports
import React from "react";
import { render, screen, fireEvent, act, waitFor  } from "@testing-library/react";
import { vi } from "vitest";

// Third-party library imports
import { BrowserRouter } from "react-router-dom";

// Utility functions
import {fetchOpeningsPerYear} from "../../services/OpeningService";

// Local components
import BarChartGrouped from "../../components/BarChartGrouped";

// Mock services
vi.mock("../../services/OpeningService",async () => {
  const actual = await vi.importActual("../../services/OpeningService");
  return {
    ...actual,
    fetchOpeningsPerYear: vi.fn(),
  };
});

vi.mock("../../services/search/openings", () => ({
  fetchOrgUnits: vi.fn(() => Promise.resolve([{ orgUnitCode: "001", orgUnitName: "Unit 1" }])),
  status: [
    { value: "open", text: "Open" },
    { value: "closed", text: "Closed" },
  ],
}));

// Mock navigator
vi.mock("react-router-dom", async () => {
  const actual = await vi.importActual("react-router-dom");
  return {
    ...actual,
    useNavigate: vi.fn(),
  };
});

// Sample data
const sampleResultsApi = [
  {
      "month": 12,
      "year": 2023,
      "monthName": "Dec",
      "amount": 0,
      "statusCounts": {}
  },
  {
      "month": 1,
      "year": 2024,
      "monthName": "Jan",
      "amount": 0,
      "statusCounts": {}
  },
  {
      "month": 2,
      "year": 2024,
      "monthName": "Feb",
      "amount": 0,
      "statusCounts": {}
  },
  {
      "month": 3,
      "year": 2024,
      "monthName": "Mar",
      "amount": 0,
      "statusCounts": {}
  },
  {
      "month": 4,
      "year": 2024,
      "monthName": "Apr",
      "amount": 0,
      "statusCounts": {}
  },
  {
      "month": 5,
      "year": 2024,
      "monthName": "May",
      "amount": 0,
      "statusCounts": {}
  },
  {
      "month": 6,
      "year": 2024,
      "monthName": "Jun",
      "amount": 0,
      "statusCounts": {}
  },
  {
      "month": 7,
      "year": 2024,
      "monthName": "Jul",
      "amount": 3,
      "statusCounts": {
          "APP": 3
      }
  },
  {
      "month": 8,
      "year": 2024,
      "monthName": "Aug",
      "amount": 0,
      "statusCounts": {}
  },
  {
      "month": 9,
      "year": 2024,
      "monthName": "Sep",
      "amount": 0,
      "statusCounts": {}
  },
  {
      "month": 10,
      "year": 2024,
      "monthName": "Oct",
      "amount": 0,
      "statusCounts": {}
  },
  {
      "month": 11,
      "year": 2024,
      "monthName": "Nov",
      "amount": 0,
      "statusCounts": {}
  }
];

const sampleResults = sampleResultsApi.map(item => ({
  group: "Openings",
  key: `${item.monthName} ${item.year}`,
  year: item.year,
  month: item.month,
  value: item.amount,
  statusCount: item.statusCounts
}));

describe("BarChartGrouped Component", () => {
  it("renders without crashing", () => {
    (fetchOpeningsPerYear as vi.Mock).mockResolvedValue([]);
    render(
      <BrowserRouter>
        <BarChartGrouped />
      </BrowserRouter>
    );
    expect(screen.getByText(/Loading/i)).toBeInTheDocument();
  });

  it("displays filter dropdowns and date pickers", async () => {
    (fetchOpeningsPerYear as vi.Mock).mockResolvedValue([]);
    render(
      <BrowserRouter>
        <BarChartGrouped />
      </BrowserRouter>
    );

    // Wait for async fetch calls
    await waitFor(() => expect(screen.getByText("District")).toBeInTheDocument());
    expect(screen.getByText("Status")).toBeInTheDocument();
    expect(screen.getByLabelText("Start Date")).toBeInTheDocument();
    expect(screen.getByLabelText("End Date")).toBeInTheDocument();
  });

  it("calls the fetchOrgUnits function on mount", async () => {
    const { fetchOrgUnits } = await import("../../services/search/openings");
    (fetchOpeningsPerYear as vi.Mock).mockResolvedValue([]);
    render(
      <BrowserRouter>
        <BarChartGrouped />
      </BrowserRouter>
    );

    await waitFor(() => expect(fetchOrgUnits).toHaveBeenCalled());
  });

  it("updates selected filters when a filterable multi-select item is chosen", async () => {
    (fetchOpeningsPerYear as vi.Mock).mockResolvedValue([]);
    await act(async () =>render(
      <BrowserRouter>
        <BarChartGrouped />
      </BrowserRouter>
    ));

    await waitFor(() => screen.getByText("District"));

    const dropdown = screen.getByPlaceholderText("Filter by district");
    fireEvent.change(dropdown, { target: { value: "Unit 1" } });

    expect(screen.getByDisplayValue("Unit 1")).toBeInTheDocument();
  });

  it("renders empty state when no data is available", async () => {
    (fetchOpeningsPerYear as vi.Mock).mockResolvedValue([]);
    render(
      <BrowserRouter>
        <BarChartGrouped />
      </BrowserRouter>
    );

    await waitFor(() => screen.getByText("You don't have any openings to show yet"));

    expect(screen.getByText("Select a filter to bring up the openings")).toBeInTheDocument();
  });

});