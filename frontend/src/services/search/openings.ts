import axios from "axios";
import qs from "qs";
import { getAuthIdToken } from "../AuthService";
import { dateTypes, blockStatuses } from "../../mock-data/openingSearchFilters";
import { createDateParams } from "../../utils/searchUtils";
import { API_ENDPOINTS, defaultHeaders } from "../apiConfig";
import { TextValueData } from "@/types/GeneralTypes";

export interface OpeningSearchFilters {
  mainSearchTerm?: string;
  orgUnit?: string[];
  category?: string[];
  statusList?: string[];
  myOpenings?: boolean;
  submittedToFrpa?: boolean;
  disturbanceDateStart?: string;
  disturbanceDateEnd?: string;
  regenDelayDateStart?: string;
  regenDelayDateEnd?: string;
  freeGrowingDateStart?: string;
  freeGrowingDateEnd?: string;
  updateDateStart?: string;
  updateDateEnd?: string;
  cuttingPermitId?: string;
  cutBlockId?: string;
  clientLocationCode?: string;
  clientNumber?: string;
  timberMark?: string;
}

export interface OpeningFilters extends OpeningSearchFilters {
  page?: number;
  perPage?: number;
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

export interface OpeningItem {
  openingId: number;
  openingNumber: string;
  category: {
    code: string;
    description: string;
  };
  status: {
    code: string;
    description: string;
  };
  cuttingPermitId: number | null;
  timberMark: string | null;
  cutBlockId: number | null;
  openingGrossAreaHa: number | null;
  disturbanceStartDate: string | null;
  orgUnitCode: string;
  orgUnitName: string;
  clientNumber: string | null;
  clientAcronym: string | null;
  regenDelayDate: string;
  freeGrowingDate: string;
  updateTimestamp: string;
  entryUserId: string;
  submittedToFrpa: boolean;
  forestFileId: string | null;
  silvaReliefAppId: string | null;
}

export interface OrgUnit {
  orgUnitNo: number;
  orgUnitCode: string;
  orgUnitName: string;
}

export interface CodeDescription {
  code: string;
  description: string;
}

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

export interface PagedResult<T> {
  data: T[];
  hasNextPage: boolean;
  pageIndex: number;
  perPage: number;
  totalItems: number;
  totalPages: number;
}

export const fetchOpenings = async (filters: OpeningFilters): Promise<PagedResult<OpeningItem>> => {

  // Remove undefined, null, or empty string values from the params object
  const cleanedParams = Object.fromEntries(
    Object.entries(filters).filter(([_, v]) => v !== undefined && v !== null && v !== "")
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
  const flattenedData = response.data.data.map((item: OpeningItem) => ({
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
    data: flattenedData
  };
};

// Used to fetch the recent openings for a user based on a limit value
export const fetchUserRecentOpenings = async (limit: number): Promise<any> => {
  
  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  const response = await axios.get(API_ENDPOINTS.recentOpenings(),defaultHeaders(authToken));

  // Flatten the data part of the response
  const flattenedData = response.data.data.map((item: OpeningItem) => ({
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
    data: flattenedData
  };
};

export const fetchCategories = async (): Promise<CodeDescription[]> => {
  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  const response = await axios.get(API_ENDPOINTS.categories(), defaultHeaders(authToken));

  // Returning the api response data
  return response.data;
};

export const fetchOrgUnits = async (): Promise<OrgUnit[]> => {
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
