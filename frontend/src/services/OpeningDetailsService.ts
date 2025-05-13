import axios from "axios";
import qs from "qs";
import { OpeningDetailsStockingDto, OpeningDetailsTombstoneOverviewDto, OpeningTenureDto, PaginatedPrimaryResponseDto } from "@/types/OpeningTypes";
import { API_ENDPOINTS, defaultHeaders } from "./apiConfig";
import { getAuthIdToken } from "./AuthService";
import { TenureFilterType } from "@/components/OpeningDetails/TenureIdentification/definitions";

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
