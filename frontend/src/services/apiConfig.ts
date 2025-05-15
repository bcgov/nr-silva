// Centralized API configuration file
import { env } from '../env';
import { MapKindType } from "@/types/MapLayer";

const resolveQueryString = (queryString: string | null) => {
  return queryString ? `?${queryString}` : '';
}

// Define the API base URL from the environment variables
const API_BASE_URL = `${env.VITE_BACKEND_URL}/api`;

// Define the API endpoints, making it easier to refactor in the future when needed
const API_ENDPOINTS = {
  openingFavourites: () => `${API_BASE_URL}/openings/favourites`,
  openingFavouriteWithId: (openingId: number) => `${API_BASE_URL}/openings/favourites/${openingId}`,
  openingSearch: (filters: string) => `${API_BASE_URL}/openings/search${filters}`,
  recentOpenings: () => `${API_BASE_URL}/openings/recent?size=10`,
  postRecentOpening: (openingId: number) => `${API_BASE_URL}/openings/recent/${openingId}`,
  categories: () => `${API_BASE_URL}/codes/categories`,
  orgUnits: () => `${API_BASE_URL}/codes/org-units`,
  clientsByNameAcronymNumber: (query: string) => `${API_BASE_URL}/forest-clients/byNameAcronymNumber?value=${query}`,
  clientLocations: (clientId: string) => `${API_BASE_URL}/forest-clients/${clientId}/locations`,
  submissionTrends: (queryString: string | null) => `${API_BASE_URL}/users/submission-trends${resolveQueryString(queryString)}`,
  openingMap: (openingId: number, kind: string) => `${API_BASE_URL}/openings/map/${openingId}${resolveQueryString(kind)}`,
  openingTombstone: (openingId: number) => `${API_BASE_URL}/openings/${openingId}/tombstone`,
  openingSsu: (openingId: number) => `${API_BASE_URL}/openings/${openingId}/ssu`,
  openingDisturbances: (openingId: number) => `${API_BASE_URL}/openings/${openingId}/disturbances`,
  openingActivity: (openingId: number) => ({
    activities: (filters: string) => `${API_BASE_URL}/openings/${openingId}/activities${filters}`,
    activityDetail: (atuId: number) => `${API_BASE_URL}/openings/${openingId}/activities/${atuId}`,
  }),
  openingTenureIdentification: (openingId: number, filters: string) => `${API_BASE_URL}/openings/${openingId}/tenures${filters}`,
};

// Define the default headers for the API requests, including ones used by CORS
const defaultHeaders = (authToken: string | null) => ({
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': window.location.origin,
    Authorization: `Bearer ${authToken}`
  }
});

export { API_BASE_URL, API_ENDPOINTS, defaultHeaders };
