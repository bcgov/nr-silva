import axios from "axios";
import {
  OpeningActivityBaseDto,
  OpeningActivityJuvelineDto,
  OpeningActivityPruningDto,
  OpeningActivitySitePrepDto,
  OpeningActivitySpeciesDto,
  OpeningActivitySurveyDto,
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

export const fetchOpeningDisturbances = (openingId: number): Promise<PaginatedResponseType<OpeningDetailsActivitiesDisturbanceDto>> => {
  const authToken = getAuthIdToken();

  return axios.get(API_ENDPOINTS.openingDisturbances(openingId), defaultHeaders(authToken))
    .then((res) => res.data as PaginatedResponseType<OpeningDetailsActivitiesDisturbanceDto>);
};

export const fetchOpeningActivities = (openingId: number): Promise<PaginatedResponseType<OpeningDetailsActivitiesActivitiesDto>> => {
  const authToken = getAuthIdToken();

  return axios.get(API_ENDPOINTS.openingActivity(openingId).activities(), defaultHeaders(authToken))
    .then((res) => res.data as PaginatedResponseType<OpeningDetailsActivitiesActivitiesDto>);
};

export const fetchOpeningActivityDetail = (baseCode: string, openingId: number, atuId: number)
  : Promise<OpeningActivityBaseDto
    | OpeningActivityJuvelineDto
    | OpeningActivityPruningDto
    | OpeningActivitySitePrepDto
    | OpeningActivitySpeciesDto
    | OpeningActivitySurveyDto> => {

  const authToken = getAuthIdToken();

  return axios.get(API_ENDPOINTS.openingActivity(openingId).activityDetail(atuId), defaultHeaders(authToken))
    .then((res) => {
      switch (baseCode) {
        case "JS":
          return res.data as OpeningActivityJuvelineDto;
        case "PR":
          return res.data as OpeningActivityPruningDto;
        case "SP":
          return res.data as OpeningActivitySitePrepDto;
        case "SU":
          return res.data as OpeningActivitySurveyDto;
        case "DS":
        case "PL":
          return res.data as OpeningActivitySpeciesDto;
        default:
          return res.data as OpeningActivityBaseDto;
      }
    });
};
