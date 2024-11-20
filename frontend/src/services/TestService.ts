import axios from 'axios';
import { ForestClientType } from '../types/ForestClientTypes/ForestClientType';
import { env } from '../env';
import { getAuthIdToken } from './AuthService';

const backendUrl = env.VITE_BACKEND_URL;

export const getForestClientByNumberOrAcronym = async (numberOrAcronym: string): Promise<ForestClientType> => {
  const url = `${backendUrl}/api/forest-clients/${numberOrAcronym}`;
  const authToken = getAuthIdToken();

  try {
    const response = await axios.get(url, {
      headers: {
        'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': window.location.origin,
      Authorization: `Bearer ${authToken}`
      }
    });

    return response.data as ForestClientType;
  } catch (error) {
    console.error(`Failed to fetch forest client with ID or Acronym ${numberOrAcronym}:`, error);
    throw error;
  }
};
