import axios from 'axios';
import { getAuthIdToken } from './AuthService';
import { env } from '../env';
import { RecentAction } from '../types/RecentAction';
import { OpeningPerYearChart } from '../types/OpeningPerYearChart';
import { RecentOpening } from '../types/RecentOpening';
import { IOpeningPerYear } from '../types/IOpeningPerYear';

const backendUrl = env.VITE_BACKEND_URL;

/**
 * Fetch openings per year data from backend.
 *
 * @returns {Promise<OpeningPerYearChart[]>} Array of objects found
 */
export async function fetchOpeningsPerYear(props: IOpeningPerYear): Promise<OpeningPerYearChart[]> {
  const authToken = getAuthIdToken();
  try {
    // Construct URL with optional parameters
    let url = backendUrl.concat("/api/dashboard-metrics/submission-trends");
    if (props.orgUnitCode || props.statusCode || props.entryDateStart || props.entryDateEnd) {
      url += '?';
      if (props.orgUnitCode) url += `orgUnitCode=${props.orgUnitCode}&`;
      if (props.statusCode) url += `statusCode=${props.statusCode}&`;
      if (props.entryDateStart) url += `entryDateStart=${props.entryDateStart}&`;
      if (props.entryDateEnd) url += `entryDateEnd=${props.entryDateEnd}&`;
      // Remove trailing '&' if present
      url = url.replace(/&$/, '');
    }

    const response = await axios.get(url, {
      headers: {
        Authorization: `Bearer ${authToken}`
      }
    });

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

export const fetchOpeningsPerYearAPI = async (props: IOpeningPerYear): Promise<OpeningPerYearChart[]> => {
  const authToken = getAuthIdToken();
  
  try {
    let url = `${backendUrl}/api/dashboard-metrics/submission-trends`;
    if (props.orgUnitCode || props.statusCode || props.entryDateStart || props.entryDateEnd) {
      url += "?";
      if (props.orgUnitCode) url += `orgUnitCode=${props.orgUnitCode}&`;
      if (props.statusCode) url += `statusCode=${props.statusCode}&`;
      if (props.entryDateStart) url += `entryDateStart=${props.entryDateStart}&`;
      if (props.entryDateEnd) url += `entryDateEnd=${props.entryDateEnd}&`;
      url = url.replace(/&$/, "");
    }

    const response = await axios.get(url, {
      headers: { Authorization: `Bearer ${authToken}` }
    });

    if (response.data && Array.isArray(response.data)) {
      return response.data.map(item => ({
        group: "Openings",
        key: item.monthName,
        value: item.amount
      }));
    }
    
    return [];
  } catch (error) {
    console.error("Error fetching openings per year:", error);
    throw error;
  }
};

interface IFreeGrowingProps {
  orgUnitCode: string;
  clientNumber: string;
  entryDateStart: string | null;
  entryDateEnd: string | null;
}

export interface IFreeGrowingChartData {
  group: string;
  value: number;
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
export function fetchRecentActions(): RecentAction[] {
  // const authToken = getAuthIdToken();
  try {
    // Comment out the actual API call for now
    // const response = await axios.get(backendUrl.concat("/api/dashboard-metrics/my-recent-actions/requests"));
    //     headers: {
    //         Authorization: `Bearer ${authToken}`
    //     }
    // });

    // Temporarily use the sample data for testing
    // const { data } = response;
    const data: RecentAction[] = [
      {
        "activityType": "Update",
        "openingId": "1541297",
        "statusCode": "APP",
        "statusDescription": "Approved",
        "lastUpdatedLabel": "1 minute ago",
        "lastUpdated": "2024-05-16T19:59:21.635Z"
      }
      // Add more sample objects here if needed
    ];

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
