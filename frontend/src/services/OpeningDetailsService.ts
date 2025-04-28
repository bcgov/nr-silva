import axios from "axios";
import { OpeningDetailsStockingDto, OpeningDetailsTombstoneOverviewDto } from "../types/OpeningTypes";
import { API_ENDPOINTS, defaultHeaders } from "./apiConfig";
import { getAuthIdToken } from "./AuthService";

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
  