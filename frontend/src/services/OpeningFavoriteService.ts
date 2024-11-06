import axios from 'axios';
import { getAuthIdToken } from './AuthService';
import { env } from '../env';

const backendUrl = env.VITE_BACKEND_URL;

/**
 * Fetches the submission trends/favorites from the backend.
 *
 * This function sends a GET request to the backend API to retrieve the user favorite openings.
 * It includes an authorization token in the request headers.
 *
 * @returns {Promise<number[]>} A promise that resolves to an array of numbers representing the opening ids.
 * If the response data is not an array, it returns an empty array.
 */
export const fetchOpeningTrends = async (): Promise<number[]> =>{
  const authToken = getAuthIdToken();  
  const response = await axios.get(
    `${backendUrl}/api/openings/favorites`, {
    headers: {
      Authorization: `Bearer ${authToken}`
    }
  });

  const { data } = response;
  if (data && Array.isArray(data)) {
    return data;
  } else {
    return [];
  }  
}

/**
 * Sets an opening as a favorite for the authenticated user.
 *
 * @param {number} openingId - The ID of the opening to be set as favorite.
 * @returns {Promise<void>} A promise that resolves when the operation is complete.
 * @throws {Error} Throws an error if the request fails with a status code other than 202.
 */
export const setOpeningFavorite = async (openingId: number): Promise<void> => {
  const authToken = getAuthIdToken();
  const response = await axios.put(
    `${backendUrl}/api/openings/favorites/${openingId}`, null, {
    headers: {
      Authorization: `Bearer ${authToken}`
    }
  });

  if (response.status !== 202) {
    throw new Error(`Failed to set favorite opening. Status code: ${response.status}`);
  }
}

/**
 * Deletes a favorite opening by its ID.
 *
 * @param {number} openingId - The ID of the opening to be removed from favorites.
 * @returns {Promise<void>} A promise that resolves when the favorite opening is successfully deleted.
 * @throws {Error} Throws an error if the deletion fails or the response status is not 204.
 */
export const deleteOpeningFavorite = async (openingId: number): Promise<void> => {
  const authToken = getAuthIdToken();
  const response = await axios.delete(
    `${backendUrl}/api/openings/favorites/${openingId}`, {
    headers: {
      Authorization: `Bearer ${authToken}`
    }
  });

  if (response.status !== 204) {
    throw new Error(`Failed to remove favorite opening. Status code: ${response.status}`);
  }
}