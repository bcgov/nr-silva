import axios from "axios";
import qs from "qs";
import { getAuthIdToken } from "../AuthService";
import { dateTypes, blockStatuses } from "../../mock-data/openingSearchFilters";
import { createDateParams } from "../../utils/searchUtils";
import { API_ENDPOINTS, defaultHeaders } from "../apiConfig";
import { TextValueData } from "../../utils/multiSelectSortUtils";
import CodeDescriptionDto from "../../types/CodeDescriptionType";
import { OpeningSearchResponseDto } from "../../types/OpeningTypes";
import type { PaginatedResponseType } from "@/types/PaginationTypes";

export interface OpeningFilters {
  searchInput?: string;
  startDate?: string;
  endDate?: string;
  orgUnit?: string[];
  category?: string[];
  clientAcronym?: string;
  blockStatus?: string;
  dateType?: string;
  cutBlock?: string;
  cuttingPermit?: string;
  grossArea?: string;
  timberMark?: string;
  status?: string[];
  openingFilters?: string[];
  blockStatuses?: string[];
  page?: number;
  perPage?: number;
  clientLocationCode?: string;
  clientNumber?: string;
}

export const openingFiltersKeys = [
  "searchInput",
  "startDate",
  "endDate",
  "orgUnit",
  "category",
  "clientAcronym",
  "blockStatus",
  "dateType",
  "cutBlock",
  "cuttingPermit",
  "grossArea",
  "timberMark",
  "status",
  "openingFilters",
  "blockStatuses",
  "page",
  "perPage",
  "clientLocationCode",
  "clientNumber"
] as const;

export const status: TextValueData[] = [
  {value:'AMG', text: 'Amalgamate'},
  {value:'AMD', text: 'Amended'},
  {value:'APP', text: 'Approved'},
  {value:'DFT', text: 'Draft'},
  {value:'FG', text: 'Free Growing'},
  {value:'RMD', text: 'Removed'},
  {value:'RET', text: 'Retired'},
  {value:'SUB', text: 'Submitted'}
];

export const fetchOpenings = async (filters: OpeningFilters): Promise<PaginatedResponseType<OpeningSearchResponseDto>> => {
  // Get the date params based on dateType
  // Get the date params based on dateType
  const { dateStartKey, dateEndKey } = createDateParams(filters);

  const params = {
    mainSearchTerm: filters.searchInput,
    orgUnit: filters.orgUnit, //Keep it as an array
    category: filters.category, // Keep it as an array
    statusList: filters.status, // Keep it as an array
    myOpenings: filters.openingFilters?.includes("Openings created by me") || undefined,
    submittedToFrpa: filters.openingFilters?.includes("FRPA section 108") || undefined,
    [dateStartKey]: filters.startDate,  // Use dynamic key for start date
    [dateEndKey]: filters.endDate,      // Use dynamic key for end date
    cuttingPermitId:filters.cuttingPermit,
    cutBlockId: filters.cutBlock,
    clientLocationCode: filters.clientLocationCode,
    clientNumber: filters.clientNumber,
    timberMark:filters.timberMark,
    page: filters.page && filters.page - 1, // Adjust page index (-1)
    size: filters.perPage
  };

  // Remove undefined, null, or empty string values from the params object
  const cleanedParams = Object.fromEntries(
    Object.entries(params).filter(([_, v]) => v !== undefined && v !== null && v !== "")
  );

  // Stringify the cleanedParams using qs with arrayFormat: 'repeat'
  const queryString = qs.stringify(cleanedParams, {
    addQueryPrefix: true,
    arrayFormat: 'repeat'  // This will format arrays like statusList=DUB&statusList=APP
  });

  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  const response = await axios.get(API_ENDPOINTS.openingSearch(queryString), defaultHeaders(authToken));

  // Flatten the data part of the response
  const flattenedData = response.data.content.map((item: OpeningSearchResponseDto) => ({
    ...item,
    statusCode: item.status?.code,
    statusDescription: item.status?.description,
    categoryCode: item.category?.code,
    categoryDescription: item.category?.description,
    status: undefined, // Remove the old nested status object
    category: undefined // Remove the old nested category object
  }));

  // Returning the modified response data with the flattened structure
  return {
    ...response.data,
    content: flattenedData
  };
};

export const fetchCategories = async (): Promise<CodeDescriptionDto[]> => {
  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  const response = await axios.get(API_ENDPOINTS.categories(), defaultHeaders(authToken));

  // Returning the api response data
  return response.data;
};

export const fetchOrgUnits = async (): Promise<CodeDescriptionDto[]> => {
  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  const response = await axios.get(API_ENDPOINTS.orgUnits(),defaultHeaders(authToken));

  // Returning the api response data
  return response.data;
};

export const fetchDateTypes = async (): Promise<any> => {
  return dateTypes;
};

export const fetchBlockStatuses = async (): Promise<any> => {
  return blockStatuses;
};

export const fetchOpeningFilters = async (): Promise<any> => {
  const [categories, orgUnits, dateTypes, blockStatuses] = await Promise.all([
    fetchCategories(),
    fetchOrgUnits(),
    fetchDateTypes(),
    fetchBlockStatuses()
  ]);
  return { categories, orgUnits, dateTypes, blockStatuses };
};
