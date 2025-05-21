import axios from "axios";
import qs from "qs";
import {
  OpeningActivityDetail,
  OpeningDetailsActivitiesActivitiesDto,
  OpeningDetailsActivitiesDisturbanceDto,
  OpeningDetailsStockingDto,
  OpeningDetailsTombstoneOverviewDto,
  OpeningTenureDto,
  PaginatedPrimaryResponseDto
} from "@/types/OpeningTypes";
import { API_ENDPOINTS, defaultHeaders } from "./apiConfig";
import { getAuthIdToken } from "./AuthService";
import { PaginatedResponseType } from "@/types/PaginationTypes";
import { TenureFilterType } from "@/components/OpeningDetails/TenureIdentification/definitions";
import { ActivityFilterType } from "../components/OpeningDetails/OpeningActivities/definitions";

/**
 * Fetch the tombstone and overview information for the Opening details page.
 */
export const fetchOpeningTombstone = (openingId: number): Promise<OpeningDetailsTombstoneOverviewDto> => {
  const authToken = getAuthIdToken();

  return axios.get(API_ENDPOINTS.openingTombstone(openingId), defaultHeaders(authToken))
    .then((res) => res.data);
}

/**
 * Fetch the opening stocking details (SSU) for the Opening details page.
 */
export const fetchOpeningSsu = (openingId: number): Promise<OpeningDetailsStockingDto[]> => {
  const authToken = getAuthIdToken();

  return axios.get(API_ENDPOINTS.openingSsu(openingId), defaultHeaders(authToken))
    .then((res) => res.data);
}

/**
 * Fetches the disturbances associated with a specific opening.
 * 
 * @param openingId - The ID of the opening.
 * @returns A promise that resolves to a paginated response containing disturbance data.
 */
export const fetchOpeningDisturbances = (openingId: number): Promise<PaginatedResponseType<OpeningDetailsActivitiesDisturbanceDto>> => {
  const authToken = getAuthIdToken();

  return axios.get(API_ENDPOINTS.openingDisturbances(openingId), defaultHeaders(authToken))
    .then((res) => res.data as PaginatedResponseType<OpeningDetailsActivitiesDisturbanceDto>);
};

/**
 * Fetches the activities associated with a specific opening.
 * 
 * @param openingId - The ID of the opening.
 * @returns A promise that resolves to a paginated response containing activity data.
 */
export const fetchOpeningActivities = (openingId: number, filters?: ActivityFilterType): Promise<PaginatedResponseType<OpeningDetailsActivitiesActivitiesDto>> => {
  const authToken = getAuthIdToken();

  const query: Record<string, string> = { ...(filters ?? {}) } as any;

  if (filters?.sortField && filters?.sortDirection) {
    query.sort = `${filters.sortField},${filters.sortDirection}`;
  }

  // Remove original sortField and sortDirection
  delete query.sortField;
  delete query.sortDirection;

  const queryString = qs.stringify(query, {
    skipNulls: true,
    addQueryPrefix: true,
  });

  return axios.get(API_ENDPOINTS.openingActivity(openingId).activities(queryString), defaultHeaders(authToken))
    .then((res) => res.data as PaginatedResponseType<OpeningDetailsActivitiesActivitiesDto>);
};

/**
 * Fetches the details of a specific activity within an opening.
 * 
 * @param openingId - The ID of the opening.
 * @param atuId - The ID of the activity unit.
 * @returns A promise that resolves to the details of the specified activity.
 */
export const fetchOpeningActivityDetail = (openingId: number, atuId: number): Promise<OpeningActivityDetail> => {
  const authToken = getAuthIdToken();

  return axios.get(API_ENDPOINTS.openingActivity(openingId).activityDetail(atuId), defaultHeaders(authToken))
    .then((res) => res.data as OpeningActivityDetail);
}

/**
 * Fetches tenure identification data for a given opening ID with optional filters.
 *
 * @param openingId - The ID of the opening
 * @param filters - Optional filtering and sorting parameters
 * @returns A promise resolving to paginated tenure identification data
 */
export const fetchOpeningTenure = (
  openingId: number,
  filters?: TenureFilterType
): Promise<PaginatedPrimaryResponseDto<OpeningTenureDto>> => {
  const authToken = getAuthIdToken();

  const query: Record<string, string> = { ...(filters ?? {}) } as any;

  if (filters?.sortField && filters?.sortDirection) {
    query.sort = `${filters.sortField},${filters.sortDirection}`;
  }

  // Remove original sortField and sortDirection
  delete query.sortField;
  delete query.sortDirection;

  const queryString = qs.stringify(query, {
    skipNulls: true,
    addQueryPrefix: true,
  });

  const url = `${API_ENDPOINTS.openingTenureIdentification(openingId, queryString)}`;

  return axios.get(url, defaultHeaders(authToken)).then((res) => res.data);
};
