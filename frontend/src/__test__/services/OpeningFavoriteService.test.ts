import { describe, it, expect, vi } from 'vitest';
import axios from 'axios';
import { getAuthIdToken } from '../../services/AuthService';
import { env } from '../../env';
import { fetchOpeningFavourites, putOpeningFavourite, deleteOpeningFavorite } from '../../services/OpeningFavouriteService';

vi.mock('axios');
vi.mock('../../services/AuthService');

describe('OpeningFavouriteService', () => {
  const backendUrl = env.VITE_BACKEND_URL;
  const authToken = 'test-token';

  beforeEach(() => {
    vi.clearAllMocks();
    (getAuthIdToken as vi.Mock).mockReturnValue(authToken);
  });

  it('should fetch submission trends successfully', async () => {
    const mockData = [1, 2, 3];
    (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

    const result = await fetchOpeningFavourites();

    expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/openings/favourites`, {
      headers: { Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json" }
    });
    expect(result).toEqual(mockData);
  });

  it('should fetch submission trends with empty results', async () => {
    const mockData = [];
    (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

    const result = await fetchOpeningFavourites();

    expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/openings/favourites`, {
      headers: { Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json" }
    });
    expect(result).toEqual(mockData);
  });

  it('should handle error while fetching submission trends', async () => {
    (axios.get as vi.Mock).mockRejectedValue(new Error('Network Error'));

    await expect(fetchOpeningFavourites()).rejects.toThrow('Network Error');
  });

    it('should fetch submission trends successfully', async () => {
      const mockData = [1, 2, 3];
      (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

      const result = await fetchOpeningFavourites();

      expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/openings/favourites`, {
        headers: { Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json" }
      });
      expect(result).toEqual(mockData);
    });

    it('should fetch submission trends with empty results', async () => {
      const mockData = [];
      (axios.get as vi.Mock).mockResolvedValue({ data: mockData });

      const result = await fetchOpeningFavourites();

      expect(axios.get).toHaveBeenCalledWith(`${backendUrl}/api/openings/favourites`, {
        headers: { Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json" }
      });
      expect(result).toEqual(mockData);
    });

    it('should handle error while fetching submission trends', async () => {
      (axios.get as vi.Mock).mockRejectedValue(new Error('Network Error'));

      await expect(fetchOpeningFavourites()).rejects.toThrow('Network Error');
    });

    it('should set an opening as favorite successfully', async () => {
      const openingId = 1;
      (axios.put as vi.Mock).mockResolvedValue({ status: 202 });

      await putOpeningFavourite(openingId);

      expect(axios.put).toHaveBeenCalledWith(`${backendUrl}/api/openings/favourites/${openingId}`, null, {
        headers: { Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json" }
      });
    });

    it('should throw an error if setting an opening as favorite fails', async () => {
      const openingId = 1;
      (axios.put as vi.Mock).mockResolvedValue({ status: 500 });

      await expect(putOpeningFavourite(openingId)).rejects.toThrow('Failed to set favorite opening. Status code: 500');
    });

    it('should delete a favorite opening successfully', async () => {
      const openingId = 1;
      (axios.delete as vi.Mock).mockResolvedValue({ status: 204 });

      await deleteOpeningFavorite(openingId);

      expect(axios.delete).toHaveBeenCalledWith(`${backendUrl}/api/openings/favourites/${openingId}`, {
        headers: { Authorization: `Bearer ${authToken}`,
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json" }
      });
    });

    it('should throw an error if deleting a favorite opening fails', async () => {
      const openingId = 1;
      (axios.delete as vi.Mock).mockResolvedValue({ status: 500 });

      await expect(deleteOpeningFavorite(openingId)).rejects.toThrow('Failed to remove favorite opening. Status code: 500');
    });

});
