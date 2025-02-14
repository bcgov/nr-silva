import axios from 'axios';
import { getAuthIdToken } from './AuthService';
import { env } from '../env';
import {
  IOpeningPerYear,
  IFreeGrowingProps,
  IFreeGrowingChartData,
  PaginatedRecentOpeningsDto,
  OrgUnitEntity,
  OpeningsPerYearDto
} from '../types/OpeningTypes';
import { API_ENDPOINTS, defaultHeaders } from './apiConfig';

const backendUrl = env.VITE_BACKEND_URL;

/**
 * Fetch users submission trends.
 *
 * @returns {Promise<OpeningPerYearChart[]>} Array of objects found
 */
export async function fetchUserSubmissionTrends(props: IOpeningPerYear): Promise<OpeningsPerYearDto[]> {
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

    return axios.get(API_ENDPOINTS.submissionTrends(urlParams), defaultHeaders(authToken))
      .then((res) => res.data);
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


// Used to fetch the recent openings for a user
export const fetchUserRecentOpenings = (): Promise<PaginatedRecentOpeningsDto> => {

  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  return axios.get(API_ENDPOINTS.recentOpenings(), defaultHeaders(authToken))
    .then((res) => res.data);
};

/**
 * Fetch a list of org unit used for opening search
 */
export const fetchOpeningsOrgUnits = (): Promise<OrgUnitEntity[]> => {
  const authToken = getAuthIdToken();

  return axios.get(API_ENDPOINTS.orgUnits(),defaultHeaders(authToken))
    .then((res)=> res.data);
};
