import axios from 'axios';
import { getAuthIdToken } from './AuthService';
import { API_ENDPOINTS, defaultHeaders } from './apiConfig';
import { CodeDescriptionDto } from '@/types/OpenApiTypes';

export interface ForestClientAutocomplete {
  id: string;
  name: string;
  acronym: string;
}

export const fetchClientsByNameAcronymNumber = (query: string): Promise<ForestClientAutocomplete[]> => {
  return axios.get(API_ENDPOINTS.clientsByNameAcronymNumber(query), defaultHeaders(getAuthIdToken()))
    .then((res) => res.data);
};

export const fetchClientLocations = async (clientId: string): Promise<CodeDescriptionDto[]> => {
  return axios.get(API_ENDPOINTS.clientLocations(clientId), defaultHeaders(getAuthIdToken()))
    .then((res) => res.data);
}
