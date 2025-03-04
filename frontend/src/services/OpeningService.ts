import axios from 'axios';
import qs from "qs";
import { getAuthIdToken } from './AuthService';
import {
  IOpeningPerYear,
  PaginatedRecentOpeningsDto,
  OpeningsPerYearDto
} from '../types/OpeningTypes';
import { API_ENDPOINTS, defaultHeaders } from '@/services/apiConfig';
import CodeDescriptionDto from '@/types/CodeDescriptionType';


/**
 * Fetch users submission trends.
 *
 * @returns {Promise<OpeningsPerYearDto[]>} Array of objects found
 */
export const fetchUserSubmissionTrends = (
  props: IOpeningPerYear
): Promise<OpeningsPerYearDto[]> => {
  const authToken = getAuthIdToken();
  const args: string[] = [];

  if (props.orgUnitCode) {
    props.orgUnitCode.forEach((orgUnit) => {
      args.push(`orgUnitCode=${orgUnit}`);
    });
  }

  if (props.statusCode) {
    props.statusCode.forEach((status) => {
      args.push(`statusCode=${status}`);
    });
  }

  if (props.entryDateStart) {
    args.push(`entryDateStart=${props.entryDateStart}`);
  }

  if (props.entryDateEnd) {
    args.push(`entryDateEnd=${props.entryDateEnd}`);
  }

  const urlParams = args.join('&');

  return axios.get(API_ENDPOINTS.submissionTrends(urlParams), defaultHeaders(authToken))
    .then((res) => res.data);
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
export const fetchOpeningsOrgUnits = (): Promise<CodeDescriptionDto[]> => {
  const authToken = getAuthIdToken();

  return axios.get(API_ENDPOINTS.orgUnits(), defaultHeaders(authToken))
    .then((res) => res.data);
};

export const putUserRecentOpening = (openingId: number) => {
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  return axios.put(API_ENDPOINTS.postRecentOpening(openingId), null, defaultHeaders(authToken))
    .then((res) => res.data);
};
