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
export const fetchOpeningFavourites = (): Promise<number[]> => {
  return axios.get(API_ENDPOINTS.openingFavourites(), defaultHeaders(getAuthIdToken()))
    .then((res) => res.data ?? [])
}

export const isOpeningFavourite = (openingId: number): Promise<boolean> => {
  return axios.get(API_ENDPOINTS.openingFavouriteWithId(openingId), defaultHeaders(getAuthIdToken()))
    .then((res) => res.data);
}

/**
 * Sets an opening as a favorite for the authenticated user.
 *
 * @param {number} openingId - The ID of the opening to be set as favorite.
 * @returns {Promise<void>} A promise that resolves when the operation is complete.
 * @throws {Error} Throws an error if the request fails with a status code other than 202.
 */
export const putOpeningFavourite = (openingId: number) => (
  axios.put(API_ENDPOINTS.openingFavouriteWithId(openingId), null, defaultHeaders(getAuthIdToken()))
)

/**
 * Deletes a favorite opening by its ID.
 *
 * @param {number} openingId - The ID of the opening to be removed from favorites.
 * @returns {Promise<AxiosResponse<any, any>>} A promise that resolves when the favorite opening is successfully deleted.
 * @throws {Error} Throws an error if the deletion fails or the response status is not 204.
 */
export const deleteOpeningFavorite = (openingId: number) => (
  axios.delete(API_ENDPOINTS.openingFavouriteWithId(openingId), defaultHeaders(getAuthIdToken()))
)
