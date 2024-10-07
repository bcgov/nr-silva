import axios from 'axios';
import { getAuthIdToken } from './AuthService';
import { env } from '../env';

const backendUrl = env.VITE_BACKEND_URL || '';

export interface WmsLayersWhitelistUser {
  userName: string
}

/**
 * Get the list of users that can see and download WMS layers information.
 *
 * @returns {Promise<WmsLayersWhitelistUser[]>} Array of objects found
 */
export async function getWmsLayersWhitelistUsers(): Promise<WmsLayersWhitelistUser[]> {
  const authToken = getAuthIdToken();
  try {
    const response = await axios.get(backendUrl.concat("/api/secrets/wms-layers-whitelist"), {
      headers: {
        Authorization: `Bearer ${authToken}`
        }
    });

    if (response.status >= 200 && response.status < 300) {
      if (response.data) {
        // Extracting row information from the fetched data
        const rows: WmsLayersWhitelistUser[] = response.data.map((user: WmsLayersWhitelistUser) => ({
          userName: user.userName
        }));
  
        return rows;
      }
    }
    return [];
  } catch (error) {
    console.error('Error fetching wms whitelist users:', error);
    throw error;
  }
}
