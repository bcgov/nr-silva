// Centralized API configuration file
import { env } from '../env';

// Define the API base URL from the environment variables
const API_BASE_URL = env.VITE_BACKEND_URL;

// Define the API endpoints, making it easier to refactor in the future when needed
const API_ENDPOINTS = {
  openingFavourites: () => `${API_BASE_URL}/api/openings/favourites`,
  openingFavouriteWithId: (openingId: number) => `${API_BASE_URL}/api/openings/favourites/${openingId}`,
  openingSearch: (filters: string) => `${API_BASE_URL}/api/opening-search${filters}`,
  recentOpenings: () => `${API_BASE_URL}/api/openings/recent`,
  categories: () => `${API_BASE_URL}/api/opening-search/categories`,
  orgUnits: () => `${API_BASE_URL}/api/opening-search/org-units`,
  clientsByNameAcronymNumber: (query: string) => `${API_BASE_URL}/api/forest-clients/byNameAcronymNumber?value=${query}`,
  clientLocations: (clientId: string) => `${API_BASE_URL}/api/forest-clients/${clientId}/locations`,
  submissionTrends: (queryString: string | null) => `${API_BASE_URL}/api/users/submission-trends${queryString ? `?${queryString}` : ''}`
};

// Define the default headers for the API requests, including ones used by CORS
const defaultHeaders = (authToken: string|null) => ({
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': window.location.origin,
    Authorization: `Bearer ${authToken}`
  }
});

export { API_BASE_URL, API_ENDPOINTS, defaultHeaders };