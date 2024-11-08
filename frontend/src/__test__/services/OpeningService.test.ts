import { describe, it, expect, vi } from 'vitest';
import axios from 'axios';
import { 
  fetchRecentOpenings, 
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

  describe('fetchRecentOpenings', () => {
    it('should fetch recent openings successfully', async () => {
      const mockData = {
        data: [
          {
            openingId: 1,
            forestFileId: '123',
            cuttingPermit: '456',
            timberMark: '789',
            cutBlock: 'A',
            grossAreaHa: 10,
            status: { description: 'Active' },
            category: { description: 'Category1' },
            disturbanceStart: '2023-01-01',
            entryTimestamp: '2023-01-01T00:00:00Z',
            updateTimestamp: '2023-01-02T00:00:00Z'
          }
        ]
      };
      (axios.get as vi.Mock).mockResolvedValue({ status: 200, data: mockData });

      const result = await fetchRecentOpenings();

      expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/openings/recent-openings?page=0&perPage=100`, {
        headers: { Authorization: `Bearer ${authToken}` }
      });
      expect(result).toEqual([
        {
          id: '1',
          openingId: '1',
          forestFileId: '123',
          cuttingPermit: '456',
          timberMark: '789',
          cutBlock: 'A',
          grossAreaHa: '10',
          status: 'Active',
          category: 'Category1',
          disturbanceStart: '2023-01-01',
          entryTimestamp: '2023-01-01',
          updateTimestamp: '2023-01-02'
        }
      ]);
    });

    it('should handle error while fetching recent openings', async () => {
      (axios.get as vi.Mock).mockRejectedValue(new Error('Network Error'));

      await expect(fetchRecentOpenings()).rejects.toThrow('Network Error');
    });
  });

  describe('fetchOpeningsPerYear', () => {
    it('should fetch openings per year successfully', async () => {
      const mockData = [
        { monthName: 'January', amount: 10 },
        { monthName: 'February', amount: 20 }
      ];
      (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

      const props = { orgUnitCode: '001', statusCode: 'APP', entryDateStart: '2023-01-01', entryDateEnd: '2023-12-31' };
      const result = await fetchOpeningsPerYear(props);

      expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/dashboard-metrics/submission-trends?orgUnitCode=001&statusCode=APP&entryDateStart=2023-01-01&entryDateEnd=2023-12-31`, {
        headers: { Authorization: `Bearer ${authToken}` }
      });
      expect(result).toEqual([
        { group: 'Openings', key: 'January', value: 10 },
        { group: 'Openings', key: 'February', value: 20 }
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
        headers: { Authorization: `Bearer ${authToken}` }
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
    it('should fetch recent actions successfully', () => {
      const result = fetchRecentActions();

      expect(result).toEqual([]);
    });

  });
});