import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import axios from 'axios';
import { getAuthIdToken } from '../../services/AuthService';
import { fetchClientsByNameAcronymNumber, fetchClientLocations } from '../../services/OpeningClientLocationService';
import { API_ENDPOINTS, defaultHeaders } from '../../services/apiConfig';

vi.mock('axios');
vi.mock('../../services/AuthService');
vi.mock('../../services/apiConfig');

describe('OpeningClientLocationService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('fetchClientsByNameAcronymNumber', () => {
    it('should fetch clients by name, acronym, or number', async () => {
      const mockData = [{ id: '1', name: 'Client A', acronym: 'CA' }];
      axios.get.mockResolvedValueOnce({ data: mockData });
      getAuthIdToken.mockReturnValue('mock-token');
      API_ENDPOINTS.clientsByNameAcronymNumber.mockReturnValue('/mock-endpoint');

      const result = await fetchClientsByNameAcronymNumber('Client A');

      expect(axios.get).toHaveBeenCalledWith('/mock-endpoint', defaultHeaders('mock-token'));
      expect(result).toEqual(mockData);
    });

    it('should handle errors', async () => {
      axios.get.mockRejectedValueOnce(new Error('Network Error'));
      getAuthIdToken.mockReturnValue('mock-token');
      API_ENDPOINTS.clientsByNameAcronymNumber.mockReturnValue('/mock-endpoint');

      await expect(fetchClientsByNameAcronymNumber('Client A')).rejects.toThrow('Network Error');
    });
  });

  describe('fetchClientLocations', () => {
    it('should fetch client locations by client ID', async () => {
      const mockData = [{ id: '1', name: 'Location A' }];
      axios.get.mockResolvedValueOnce({ data: mockData });
      getAuthIdToken.mockReturnValue('mock-token');
      API_ENDPOINTS.clientLocations.mockReturnValue('/mock-endpoint');

      const result = await fetchClientLocations('1');

      expect(axios.get).toHaveBeenCalledWith('/mock-endpoint', defaultHeaders('mock-token'));
      expect(result).toEqual(mockData);
    });

    it('should handle errors', async () => {
      axios.get.mockRejectedValueOnce(new Error('Network Error'));
      getAuthIdToken.mockReturnValue('mock-token');
      API_ENDPOINTS.clientLocations.mockReturnValue('/mock-endpoint');

      await expect(fetchClientLocations('1')).rejects.toThrow('Network Error');
    });
  });
});