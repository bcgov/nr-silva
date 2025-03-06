import { vi } from 'vitest';
import axios from 'axios';
import {
  fetchOpeningsOrgUnits,
  fetchUserRecentOpenings,
  fetchUserSubmissionTrends,
  putUserRecentOpening,
} from '../../services/OpeningService';
import { getAuthIdToken } from '../../services/AuthService';
import { env } from '../../env';
import { OpeningsPerYearDto, PaginatedRecentOpeningsDto } from '../../types/OpeningTypes';
import CodeDescriptionDto from '../../types/CodeDescriptionType';
import "@testing-library/jest-dom";

vi.mock('axios');
vi.mock('../../services/AuthService');

describe('OpeningService', () => {
  const backendUrl = env.VITE_BACKEND_URL;
  const authToken = 'test-token';

  beforeEach(() => {
    vi.clearAllMocks();
    (getAuthIdToken as vi.Mock).mockReturnValue(authToken);
  });

  describe('fetchUserSubmissionTrends', () => {
    it('should fetch openings per year successfully', async () => {
      const mockData: OpeningsPerYearDto[] = [
        { amount: 10, statusCounts: { APP: 5, FG: 2 }, month: 1, year: 2023 },
        { amount: 20, statusCounts: { APP: 5, FG: 2 }, month: 2, year: 2023 }
      ];
      (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

      const props = { orgUnitCode: ['001'], statusCode: ['APP'], entryDateStart: '2023-01-01', entryDateEnd: '2023-12-31' };
      const result = await fetchUserSubmissionTrends(props);

      expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/users/submission-trends?orgUnitCode=001&statusCode=APP&entryDateStart=2023-01-01&entryDateEnd=2023-12-31`, {
        headers: {
          Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json"
        }
      });
      expect(result).toEqual(mockData);
    });

    it('should handle error while fetching openings per year', async () => {
      (axios.get as vi.Mock).mockRejectedValue(new Error('Network Error'));

      await expect(fetchUserSubmissionTrends({
        orgUnitCode: null,
        statusCode: null,
        entryDateStart: null,
        entryDateEnd: null
      })).rejects.toThrow('Network Error');
    });
  });

  describe("fetchUserRecentOpenings", () => {
    it("should fetch recent openings successfully", async () => {
      const mockData: PaginatedRecentOpeningsDto = {
        content: [
          {
            openingId: 101,
            openingNumber: "ABC123",
            category: { code: "CAT1", description: "Category 1" },
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
            clientName: "",
            earlyFreeGrowingDate: null
          },
        ],
        page: { size: 1, number: 0, totalElements: 1, totalPages: 1 },
      };
      (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

      const result = await fetchUserRecentOpenings();

      expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/openings/recent?size=10`, {
        headers: {
          Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
        },
      });
      expect(result).toEqual(mockData);
    });

    it("should handle error while fetching recent openings", async () => {
      (axios.get as vi.Mock).mockRejectedValue(new Error("Request failed"));

      await expect(fetchUserRecentOpenings()).rejects.toThrow("Request failed");
    });
  });

  describe("fetchOpeningsOrgUnits", () => {
    it("should fetch organization units successfully", async () => {
      const mockData: CodeDescriptionDto[] = [
        { code: "001", description: "Org Unit A" },
        { code: "002", description: "Org Unit B" },
      ];
      (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

      const result = await fetchOpeningsOrgUnits();

      expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/codes/org-units`, {
        headers: {
          Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
        },
      });
      expect(result).toEqual(mockData);
    });

    it("should handle error while fetching organization units", async () => {
      (axios.get as vi.Mock).mockRejectedValue(new Error("Request failed"));

      await expect(fetchOpeningsOrgUnits()).rejects.toThrow("Request failed");
    });
  });

  describe("putUserRecentOpening", () => {
    it("should successfully update recent opening", async () => {
      (axios.put as vi.Mock).mockResolvedValue({ data: "Success" });

      const openingId = 123;
      const result = await putUserRecentOpening(openingId);

      expect(axios.put).toHaveBeenCalledWith(
        `${backendUrl}/api/openings/recent/${openingId}`,
        null,
        {
          headers: {
            Authorization: `Bearer ${authToken}`,
            "Access-Control-Allow-Origin": "http://localhost:3000",
            "Content-Type": "application/json",
          },
        }
      );
      expect(result).toEqual("Success");
    });

    it("should handle error while updating recent opening", async () => {
      (axios.put as vi.Mock).mockRejectedValue(new Error("Update failed"));

      await expect(putUserRecentOpening(123)).rejects.toThrow("Update failed");
    });
  });

});
