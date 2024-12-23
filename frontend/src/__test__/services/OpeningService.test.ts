import { describe, it, expect, vi } from 'vitest';
import axios from 'axios';
import { 
  fetchOpeningsPerYear, 
  fetchFreeGrowingMilestones, 
  fetchRecentActions 
} from '../../services/OpeningService';
import { getAuthIdToken } from '../../services/AuthService';
import { env } from '../../env';

vi.mock('axios');
vi.mock('../../services/AuthService');

describe('OpeningService', () => {
  const backendUrl = env.VITE_BACKEND_URL;
  const authToken = 'test-token';

  beforeEach(() => {
    vi.clearAllMocks();
    (getAuthIdToken as vi.Mock).mockReturnValue(authToken);
  });

  describe('fetchOpeningsPerYear', () => {
    it('should fetch openings per year successfully', async () => {
      const mockData = [
        { monthName: 'Jan', amount: 10, statusCounts: { APP: 5, FG: 2 }, month: 1, year: 2023 },
        { monthName: 'Feb', amount: 20, statusCounts: { APP: 5, FG: 2 }, month: 2, year: 2023 }
      ];
      (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

      const props = { orgUnitCode: ['001'], statusCode: ['APP'], entryDateStart: '2023-01-01', entryDateEnd: '2023-12-31' };
      const result = await fetchOpeningsPerYear(props);

      expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/users/submission-trends?orgUnitCode=001&statusCode=APP&entryDateStart=2023-01-01&entryDateEnd=2023-12-31`, {
        headers: { Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",          
          "Content-Type": "application/json" }
      });
      expect(result).toEqual([
        { group: 'Openings', key: 'Jan 2023', value: 10, statusCount: { APP: 5, FG: 2 }, month: 1, year: 2023 },
        { group: 'Openings', key: 'Feb 2023', value: 20, statusCount: { APP: 5, FG: 2 }, month: 2, year: 2023 }
      ]);
    });

    it('should handle error while fetching openings per year', async () => {
      (axios.get as vi.Mock).mockRejectedValue(new Error('Network Error'));

      await expect(fetchOpeningsPerYear({})).rejects.toThrow('Network Error');
    });
  });

  describe('fetchFreeGrowingMilestones', () => {
    it('should fetch free growing milestones successfully', async () => {
      const mockData = [
        { label: 'Milestone1', amount: 10 },
        { label: 'Milestone2', amount: 20 }
      ];
      (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

      const props = { orgUnitCode: '001', clientNumber: '123', entryDateStart: '2023-01-01', entryDateEnd: '2023-12-31' };
      const result = await fetchFreeGrowingMilestones(props);

      expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/dashboard-metrics/free-growing-milestones?orgUnitCode=001&clientNumber=123&entryDateStart=2023-01-01&entryDateEnd=2023-12-31`, {
        headers: { Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",          
          "Content-Type": "application/json" }
      });
      expect(result).toEqual([
        { group: 'Milestone1', value: 10 },
        { group: 'Milestone2', value: 20 }
      ]);
    });

    it('should handle error while fetching free growing milestones', async () => {
      (axios.get as vi.Mock).mockRejectedValue(new Error('Network Error'));

      await expect(fetchFreeGrowingMilestones({})).rejects.toThrow('Network Error');
    });
  });

  describe('fetchRecentActions', () => {
    it('should fetch recent actions successfully', async () => {
      const mockData = {
        "activityType": "Update",
        "openingId": "1541297",
        "statusCode": "APP",
        "statusDescription": "Approved",
        "lastUpdatedLabel": "1 minute ago",
        "lastUpdated": "2024-05-16T19:59:21.635Z"
      };
      (axios.get as vi.Mock).mockResolvedValue({ data: [mockData] });

      const result = await fetchRecentActions();

      expect(result).toEqual([
        {
          activityType: mockData.activityType,
          openingId: mockData.openingId.toString(),
          statusCode: mockData.statusCode,
          statusDescription: mockData.statusDescription,
          lastUpdated: mockData.lastUpdated,
          lastUpdatedLabel: mockData.lastUpdatedLabel
        }
      ]);
    });

    it('should handle error while fetching recent actions', async () => {
      (axios.get as vi.Mock).mockRejectedValue(new Error('Network Error'));

      await expect(fetchRecentActions()).rejects.toThrow('Network Error');
    });

  });
});