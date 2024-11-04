import axios from 'axios';
import { getAuthIdToken } from './AuthService';
import { env } from '../env';
import { RecentAction } from '../types/RecentAction';
import { OpeningPerYearChart } from '../types/OpeningPerYearChart';
import { RecentOpening } from '../types/RecentOpening';

const backendUrl = env.VITE_BACKEND_URL;

interface statusCategory {
  code: string;
  description: string;
}

interface RecentOpeningApi {
  openingId: number;
  forestFileId: string;
  cuttingPermit: string | null;
  timberMark: string | null;
  cutBlock: string | null;
  grossAreaHa: number | null;
  status: statusCategory | null;
  category: statusCategory | null;
  disturbanceStart: string | null;
  entryTimestamp: string | null;
  updateTimestamp: string | null;
}

/**
 * Fetch recent openings data from backend.
 *
 * @returns {Promise<RecentOpening[]>} Array of objects found
 */
export async function fetchRecentOpenings(): Promise<RecentOpening[]> {
  const authToken = getAuthIdToken();
  try {
    const response = await axios.get(backendUrl.concat("/api/openings/recent-openings?page=0&perPage=100"), {
      headers: {
        Authorization: `Bearer ${authToken}`
        }
    });

    if (response.status >= 200 && response.status < 300) {
      const { data } = response;

      if (data.data) {
        // Extracting row information from the fetched data
        const rows: RecentOpening[] = data.data.map((opening: RecentOpeningApi) => ({
          id: opening.openingId.toString(),
          openingId: opening.openingId.toString(),
          forestFileId: opening.forestFileId ? opening.forestFileId : '-',
          cuttingPermit: opening.cuttingPermit ? opening.cuttingPermit : '-',
          timberMark: opening.timberMark ? opening.timberMark : '-',
          cutBlock: opening.cutBlock ? opening.cutBlock : '-',
          grossAreaHa: opening.grossAreaHa ? opening.grossAreaHa.toString() : '-',
          status: opening.status && opening.status.description? opening.status.description : '-',
          category: opening.category && opening.category.description? opening.category.description : '-',
          disturbanceStart: opening.disturbanceStart ? opening.disturbanceStart : '-',
          entryTimestamp: opening.entryTimestamp ? opening.entryTimestamp.split('T')[0] : '-',
          updateTimestamp: opening.updateTimestamp ? opening.updateTimestamp.split('T')[0] : '-'
        }));
  
        return rows;
      }
    }
    return [];
  } catch (error) {
    console.error('Error fetching recent openings:', error);
    throw error;
  }
}

interface IOpeningPerYear {
  orgUnitCode: string | null;
  statusCode: string | null;
  entryDateStart: string | null;
  entryDateEnd: string | null;
}

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

/**
 * Fetches the submission trends/favorites from the backend.
 *
 * This function sends a GET request to the backend API to retrieve the user favorite openings.
 * It includes an authorization token in the request headers.
 *
 * @returns {Promise<number[]>} A promise that resolves to an array of numbers representing the opening ids.
 * If the response data is not an array, it returns an empty array.
 */
export async function fetchSubmissionTrends(): Promise<number[]> {
  const authToken = getAuthIdToken();  
  const response = await axios.get(
    `${backendUrl}/api/openings/favorites`, {
    headers: {
      Authorization: `Bearer ${authToken}`
    }
  });

  const { data } = response;
  if (data && Array.isArray(data)) {
    return data;
  } else {
    return [];
  }  
}

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
