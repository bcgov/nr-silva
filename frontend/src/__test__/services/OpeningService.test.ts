import { describe, it, expect, vi } from 'vitest';
import axios from 'axios';
import {
  fetchUserSubmissionTrends,
} from '../../services/OpeningService';
import { getAuthIdToken } from '../../services/AuthService';
import { env } from '../../env';
import { OpeningsPerYearDto } from '../../types/OpeningTypes';

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
        { monthName: 'Jan', amount: 10, statusCounts: { APP: 5, FG: 2 }, month: 1, year: 2023 },
        { monthName: 'Feb', amount: 20, statusCounts: { APP: 5, FG: 2 }, month: 2, year: 2023 }
      ];
      (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

      const props = { orgUnitCode: ['001'], statusCode: ['APP'], entryDateStart: '2023-01-01', entryDateEnd: '2023-12-31' };
      const result = await fetchUserSubmissionTrends(props);

      expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/users/submission-trends?orgUnitCode=001&statusCode=APP&entryDateStart=2023-01-01&entryDateEnd=2023-12-31`, {
        headers: { Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json" }
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

});
