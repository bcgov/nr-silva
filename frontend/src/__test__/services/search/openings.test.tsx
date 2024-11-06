// fetchOpenings.test.ts
import axios from "axios";
import "@testing-library/jest-dom";
import { fetchOpenings, OpeningFilters } from "../../../services/search/openings";
import { getAuthIdToken } from "../../../services/AuthService";
import { createDateParams } from "../../../utils/searchUtils";
import { describe, it, beforeEach, afterEach, vi, expect } from "vitest";

// Mock dependencies
vi.mock("axios");
vi.mock("../../../services/AuthService");
vi.mock("../../../utils/searchUtils");

// Define mocked functions and modules
const mockedAxios = axios as vi.Mocked<typeof axios>;
const mockedGetAuthIdToken = getAuthIdToken as vi.Mock;
const mockedCreateDateParams = createDateParams as vi.Mock;

// Sample filters
const sampleFilters: OpeningFilters = {
  searchInput: "",
  startDate: "2024-11-19",
  endDate: "2024-11-21",
  orgUnit: ["DCC", "DCK", "DCR"],
  category: ["EXCLU", "CONT"],
  status: ["DFT", "APP"],
  clientAcronym: "12",
  blockStatus: "",
  cutBlock: "L",
  cuttingPermit: "PC",
  timberMark: "123",
  dateType: "Disturbance",
  openingFilters: ["Openings created by me", "Submitted to FRPA section 108"],
  blockStatuses: [],
  page: 1,
  perPage: 5,
};

// Mock response from the backend API
const mockApiResponse = {
  data: {
    pageIndex: 0,
    perPage: 5,
    totalPages: 100,
    hasNextPage: false,
    data: [
      {
        openingId: 9100129,
        openingNumber: "98",
        cuttingPermitId: "S",
        timberMark: "W1729S",
        cutBlockId: "06-03",
        orgUnitCode: "DPG",
        orgUnitName: "Prince George Natural Resource District",
        entryUserId: "Datafix107808",
        statusCode: "APP",
        statusDescription: "Approved",
        categoryCode: "FTML",
        categoryDescription: "Forest Tenure - Major Licensee",
      },
    ],
  },
};

describe("fetchOpenings", () => {
  beforeEach(() => {
    mockedGetAuthIdToken.mockReturnValue("mocked-token");
    mockedCreateDateParams.mockReturnValue({
      dateStartKey: "disturbanceStartDate",
      dateEndKey: "disturbanceEndDate",
    });
    mockedAxios.get.mockResolvedValue(mockApiResponse); // Mock API response
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it("should fetch openings with the correct parameters and return flattened data", async () => {
    const result = await fetchOpenings(sampleFilters);
    const expectedToken = 'mocked-token';
    // Verify that axios was called with the correct URL and headers
    expect(mockedAxios.get).toHaveBeenCalledWith(
      expect.stringContaining("/api/opening-search?"),
      expect.objectContaining({
        headers: {
          Authorization: `Bearer ${expectedToken}`,
        },
      })
    );

    // Check if the result data matches the expected flattened structure
    expect(result.data[0].openingId).toEqual(9100129);
  });

  it("should handle an empty response data array gracefully", async () => {
    mockedAxios.get.mockResolvedValueOnce({ data: { data: [] } });
    const result = await fetchOpenings(sampleFilters);

    // Ensure the function returns an empty array when the response is empty
    expect(result.data).toEqual([]);
  });

  it("should throw an error when the API request fails", async () => {
    mockedAxios.get.mockRejectedValueOnce(new Error("Network error"));

    await expect(fetchOpenings(sampleFilters)).rejects.toThrow("Network error");
  });
});
