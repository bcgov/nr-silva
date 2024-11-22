import axios from 'axios';
import { getAuthIdToken } from './AuthService';
import { API_ENDPOINTS, defaultHeaders } from './apiConfig';


/**
 * Fetches the submission trends/favourites from the backend.
 *
 * This function sends a GET request to the backend API to retrieve the user favorite openings.
 * It includes an authorization token in the request headers.
 *
 * @returns {Promise<number[]>} A promise that resolves to an array of numbers representing the opening ids.
 * If the response data is not an array, it returns an empty array.
 */
export const fetchOpeningFavourites = async (): Promise<number[]> =>{
  const response = await axios.get(API_ENDPOINTS.openingFavourites(), defaultHeaders(getAuthIdToken()));

  const { data } = response;
  if (data && Array.isArray(data)) {
    return data;
  } else {
    return [];
  }  
}

export const isOpeningFavourite = async (openingId: number): Promise<boolean> =>{
  const response = await axios.get(API_ENDPOINTS.openingFavouriteWithId(openingId), defaultHeaders(getAuthIdToken()));
  return response.data;
}

/**
 * Sets an opening as a favorite for the authenticated user.
 *
 * @param {number} openingId - The ID of the opening to be set as favorite.
 * @returns {Promise<void>} A promise that resolves when the operation is complete.
 * @throws {Error} Throws an error if the request fails with a status code other than 202.
 */
export const setOpeningFavorite = async (openingId: number): Promise<void> => {
  const response = await axios.put(API_ENDPOINTS.openingFavouriteWithId(openingId), null, defaultHeaders(getAuthIdToken()));

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
  const response = await axios.delete(API_ENDPOINTS.openingFavouriteWithId(openingId), defaultHeaders(getAuthIdToken()));

  if (response.status !== 204) {
    throw new Error(`Failed to remove favorite opening. Status code: ${response.status}`);
  }
}