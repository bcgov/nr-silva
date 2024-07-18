// testService.tsx
import axios from 'axios';
import { ForestClientType } from '../types/ForestClientTypes/ForestClientType';
import { env } from '../env';

const backendUrl = env.VITE_BACKEND_URL;

export const getForestClientByNumberOrAcronym = async (numberOrAcronym: string): Promise<ForestClientType> => {
  const url = `${backendUrl}/api/forest-clients/${numberOrAcronym}`;
  
  try {
    const response = await axios.get(url);
    return response.data as ForestClientType;
  } catch (error) {
    console.error(`Failed to fetch forest client with ID or Acronym ${numberOrAcronym}:`, error);
    throw error;
  }
};
