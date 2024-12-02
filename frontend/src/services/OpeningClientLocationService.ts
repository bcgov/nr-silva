import axios from 'axios';
import { getAuthIdToken } from './AuthService';
import { API_ENDPOINTS, defaultHeaders } from './apiConfig';

export interface ForestClientAutocomplete {
  id: string;
  name: string;
  acronym: string;
}

export interface ForestClientLocation {
  id: string,
  name: string,
}

export const fetchClientsByNameAcronymNumber = async (query: string): Promise<ForestClientAutocomplete[]> => {
  const response = await axios.get(API_ENDPOINTS.clientsByNameAcronymNumber(query), defaultHeaders(getAuthIdToken()));
  return response.data;
}

export const fetchClientLocations = async (clientId: string): Promise<ForestClientLocation[]> => {
  const response = await axios.get(API_ENDPOINTS.clientLocations(clientId), defaultHeaders(getAuthIdToken()));
  return response.data;
}