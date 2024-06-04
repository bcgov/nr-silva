import axios from 'axios';
import { getAuthIdToken } from './AuthService';
import { env } from '../env';

const backendUrl = env.VITE_BACKEND_URL;

interface IOpening {
  openingId: number;
  fileId: string;
  cuttingPermit: string | null;
  timberMark: string | null;
  cutBlock: string | null;
  grossAreaHa: number | null;
  status: {code: string, description: string} | null;
  category: {code: string, description: string} | null;
  disturbanceStart: string | null;
  entryTimestamp: string | null;
  updateTimestamp: string | null;
}

export async function fetchRecentOpenings() {
  let authToken = getAuthIdToken();
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
        const rows: any[] = data.data.map((opening: IOpening) => ({
          id: opening.openingId.toString(),
          openingId: opening.openingId.toString(),
          fileId: opening.fileId ? opening.fileId : '-',
          cuttingPermit: opening.cuttingPermit ? opening.cuttingPermit : '-',
          timberMark: opening.timberMark ? opening.timberMark : '-',
          cutBlock: opening.cutBlock ? opening.cutBlock : '-',
          grossAreaHa: opening.grossAreaHa ? opening.grossAreaHa.toString() : '-',
          status: opening.status ? opening.status.description : '-',
          category: opening.category ? opening.category.code : '-',
          disturbanceStart: opening.disturbanceStart ? opening.disturbanceStart : '-',
          createdAt: opening.entryTimestamp ? opening.entryTimestamp.split('T')[0] : '-',
          lastViewed: opening.updateTimestamp ? opening.updateTimestamp.split('T')[0] : '-'
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

export async function fetchOpeningsPerYear(props: IOpeningPerYear) {
  let authToken = getAuthIdToken();
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
      const formattedData = data.map(item => ({
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

interface IFreeGrowingProps {
  orgUnitCode: string;
  clientNumber: string;
  entryDateStart: string | null;
  entryDateEnd: string | null;
}

export async function fetchFreeGrowingMilestones(props: IFreeGrowingProps) {
  let authToken = getAuthIdToken();
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
      const transformedData = data.map(item => ({
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

export async function fetchRecentActions() {
  let authToken = getAuthIdToken();
  try {
    // Comment out the actual API call for now
    // const response = await axios.get(backendUrl.concat("/api/dashboard-metrics/my-recent-actions/requests"));
    //     headers: {
    //         Authorization: `Bearer ${authToken}`
    //     }
    // });

    // Temporarily use the sample data for testing
    // const { data } = response;
    const data = [
      {
        "activityType": "Update",
        "openingId": 1541297,
        "statusCode": "APP",
        "statusDescription": "Approved",
        "lastUpdatedLabel": "1 minute ago",
        "lastUpdated": "2024-05-16T19:59:21.635Z"
      },
      // Add more sample objects here if needed
    ];

    if (Array.isArray(data)) {
      // Transforming response data into a format consumable by the component
      const rows = data.map(action => ({
        activityType: action.activityType,
        openingID: action.openingId.toString(), // Convert openingId to string if needed
        status: action.statusDescription,
        lastUpdated: action.lastUpdatedLabel // Use lastUpdatedLabel from API
      }));
      
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
