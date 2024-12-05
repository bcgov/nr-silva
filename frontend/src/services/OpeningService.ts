import axios from 'axios';
import { getAuthIdToken } from './AuthService';
import { env } from '../env';
import { RecentAction } from '../types/RecentAction';
import { OpeningPerYearChart } from '../types/OpeningPerYearChart';
import { 
  IOpeningPerYear,
  IFreeGrowingProps,
  IFreeGrowingChartData
} from '../types/OpeningTypes';
import { API_ENDPOINTS, defaultHeaders } from './apiConfig';

const backendUrl = env.VITE_BACKEND_URL;

/**
 * Fetch openings per year data from backend.
 *
 * @returns {Promise<OpeningPerYearChart[]>} Array of objects found
 */
export async function fetchOpeningsPerYear(props: IOpeningPerYear): Promise<OpeningPerYearChart[]> {
  const authToken = getAuthIdToken();
  try {
    const args: string[] = [];

    if(props.orgUnitCode) {
      props.orgUnitCode.forEach((orgUnit) => {
        args.push(`orgUnitCode=${orgUnit}`);
      });
    }

    if(props.statusCode) {
      props.statusCode.forEach((status) => {
        args.push(`statusCode=${status}`);
      });
    }

    if(props.entryDateStart) {
      args.push(`entryDateStart=${props.entryDateStart}`);
    }

    if(props.entryDateEnd) {
      args.push(`entryDateEnd=${props.entryDateEnd}`);
    }

    const urlParams = args.join('&');

    const response = await axios.get(API_ENDPOINTS.submissionTrends(urlParams), defaultHeaders(authToken));

    const { data } = response;
    if (data && Array.isArray(data)) {
      // Format data for BarChartGrouped component
      const formattedData: OpeningPerYearChart[] = data.map(item => ({
        group: "Openings",
        key: item.monthName,
        value: item.amount
      }));

      return formattedData;
    } else {
      return [];
    }
  } catch (error) {
    console.error('Error fetching openings per year:', error);
    throw error;
  }
}

/**
 * Fetch free growing milestones data from backend.
 *
 * @returns {IFreeGrowingChartData[]} Array with recent action objects.
 */
export async function fetchFreeGrowingMilestones(props: IFreeGrowingProps): Promise<IFreeGrowingChartData[]> {
  const authToken = getAuthIdToken();
  let url = backendUrl.concat("/api/dashboard-metrics/free-growing-milestones");

  // Construct URL with optional parameters
  if (props.orgUnitCode || props.clientNumber || props.entryDateStart || props.entryDateEnd) {
    url += '?';
    if (props.orgUnitCode) url += `orgUnitCode=${props.orgUnitCode}&`;
    if (props.clientNumber) url += `clientNumber=${props.clientNumber}&`;
    if (props.entryDateStart) url += `entryDateStart=${props.entryDateStart}&`;
    if (props.entryDateEnd) url += `entryDateEnd=${props.entryDateEnd}&`;
    // Remove trailing '&' if present
    url = url.replace(/&$/, '');
  }

  try {
    const response = await axios.get(url, {
      headers: {
        'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': window.location.origin,
      Authorization: `Bearer ${authToken}`
      }
    });

    const { data } = response;
    if (data && Array.isArray(data)) {
      // Transform data for DonutChartView component
      const transformedData: IFreeGrowingChartData[] = data.map(item => ({
        group: item.label,
        value: item.amount
      }));

      return transformedData;
    } else {
      return [];
    }
  } catch (error) {
    console.error('Error fetching free growing milestones:', error);
    throw error;
  }
}

/**
 * Fetch recent actions data from backend.
 *
 * @returns {RecentAction[]} Array with recent action objects.
 */
export async function fetchRecentActions(): Promise<RecentAction[]> {
  const authToken = getAuthIdToken();
  try {
    const response = await axios.get(backendUrl.concat("/api/users/recent-actions"),{
      headers: {
        'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': window.location.origin,
      Authorization: `Bearer ${authToken}`
      }
    });
    
    const { data } = response;

    if (Array.isArray(data)) {
      // Transforming response data into a format consumable by the component
      const rows: RecentAction[] = data.map(action => {
        return {
          activityType: action.activityType,
          openingId: action.openingId.toString(),
          statusCode: action.statusCode,
          statusDescription: action.statusDescription,
          lastUpdated: action.lastUpdated,
          lastUpdatedLabel: action.lastUpdatedLabel
        }
      });
      
      // Returning the transformed data
      return rows;
    } else {
      return [];
    }
  } catch (error) {
    console.error('Error fetching recent actions:', error);
    throw error;
  }
}
