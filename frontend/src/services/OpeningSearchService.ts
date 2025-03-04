import axios from "axios";
import qs from "qs";
import CodeDescriptionDto from "../types/CodeDescriptionType";
import { API_ENDPOINTS, defaultHeaders } from "./apiConfig";
import { getAuthIdToken } from "./AuthService";
import { OpeningSearchResponseDto } from "../types/OpeningTypes";
import { extractCodesFromCodeDescriptionArr } from "../utils/multiSelectUtils";
import { OpeningSearchFilterType } from "../components/SilvicultureSearch/OpeningSearch/definitions";
import { PaginatedResponseType } from "../types/PaginationTypes";

export const fetchCategories = async (): Promise<CodeDescriptionDto[]> => {
  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  return axios.get(API_ENDPOINTS.categories(), defaultHeaders(authToken))
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


/**
 * Fetch a paginated result of openings based on filters.
 *
 * @param {OpeningSearchFilterType} filters - The search filters.
 * @returns {Promise<PagedResult<OpeningSearchResponseDto>>} The paginated search result.
 */
export const searchOpenings = (filters: OpeningSearchFilterType) => {
  /**
   * Processes the `filters` object by:
   * - Removing any entries where the value is `undefined`, `null`, or an empty string (`""`).
   * - Transforming arrays of `CodeDescriptionDto` objects into arrays of their `code` values.
   * - Keeping other values (numbers, strings, booleans) unchanged.
   *
   * The resulting object maintains the same keys as `OpeningSearchFilterType`, but with:
   * - `CodeDescriptionDto[]` converted to `string[]`.
   * - Other values remaining as `number`, `string` or `boolean`.
   *
   * @constant
   * @type {Partial<Record<keyof OpeningSearchFilterType, string | boolean | string[]>>}
   */
  const cleanedParams = Object.fromEntries(
    Object.entries(filters)
      .filter(([_, v]) => v !== undefined && v !== null && v !== "")
      .map(([key, value]) => [
        key,
        Array.isArray(value) && value.every((item) => typeof item === "object" && "code" in item)
          ? extractCodesFromCodeDescriptionArr(value as CodeDescriptionDto[])
          : value
      ])
  ) as Partial<Record<keyof OpeningSearchFilterType, number | string | boolean | string[]>>;

  // Set default pagnation
  if (!cleanedParams.page) {
    cleanedParams.page = 0;
  }
  if (!cleanedParams.size) {
    cleanedParams.size = 20;
  }

  // Stringify the cleanedParams using qs with arrayFormat: 'repeat'
  const queryString = qs.stringify(cleanedParams, {
    addQueryPrefix: true,
    arrayFormat: "repeat" // This will format arrays like statusList=DUB&statusList=APP
  });

  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  return axios.get(API_ENDPOINTS.openingSearch(queryString), defaultHeaders(authToken))
    .then((res) => res.data as PaginatedResponseType<OpeningSearchResponseDto>);
};

