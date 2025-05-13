import axios from "axios";
import {
  OpeningActivityDetail,
  OpeningDetailsActivitiesActivitiesDto,
  OpeningDetailsActivitiesDisturbanceDto,
  OpeningDetailsStockingDto,
  OpeningDetailsTombstoneOverviewDto
} from "@/types/OpeningTypes";
import { API_ENDPOINTS, defaultHeaders } from "./apiConfig";
import { getAuthIdToken } from "./AuthService";
import { PaginatedResponseType } from "@/types/PaginationTypes";

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
export const fetchOpeningActivities = (openingId: number): Promise<PaginatedResponseType<OpeningDetailsActivitiesActivitiesDto>> => {
  const authToken = getAuthIdToken();

  return axios.get(API_ENDPOINTS.openingActivity(openingId).activities(), defaultHeaders(authToken))
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
};
