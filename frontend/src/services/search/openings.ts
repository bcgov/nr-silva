import axios from "axios";
import qs from "qs";
import { getAuthIdToken } from "../AuthService";
import { env } from "../../env";
import { dateTypes, blockStatuses } from "../../mock-data/openingSearchFilters";
import { createDateParams } from "../../utils/searchUtils";

export interface OpeningFilters {
  searchInput?: string;
  startDate?: string;
  endDate?: string;
  orgUnit?: string;
  category?: string;
  clientAcronym?: string;
  blockStatus?: string;
  cutBlock?: string;
  cuttingPermit?: string;
  grossArea?: string;
  timberMark?: string;
  status?: string[];
  openingFilters?: string[];
  blockStatuses?: string[];
  page?: number;
  perPage?: number;
}

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

const backendUrl = env.VITE_BACKEND_URL;

export const fetchOpenings = async (filters: OpeningFilters): Promise<any> => {
  // Get the date params based on dateType
  // Get the date params based on dateType
  const { dateStartKey, dateEndKey } = createDateParams(filters);

  const params = {
    mainSearchTerm: filters.searchInput,
    orgUnit: filters.orgUnit,
    category: filters.category,
    statusList: filters.status, // Keep it as an array
    entryUserId: filters.clientAcronym,
    cutBlockId: filters.cutBlock,
    cuttingPermitId:filters.cuttingPermit,
    timbermark:filters.timberMark,
    myOpenings:
      filters.openingFilters?.includes("Openings created by me") || undefined,
    submittedToFrpa:
      filters.openingFilters?.includes("Submitted to FRPA section 108") || undefined,
    [dateStartKey]: filters.startDate,  // Use dynamic key for start date
    [dateEndKey]: filters.endDate,      // Use dynamic key for end date
    page: filters.page && filters.page - 1, // Adjust page index (-1)
    perPage: filters.perPage
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
  const response = await axios.get(`${backendUrl}/api/opening-search${queryString}`, {
    headers: {
      Authorization: `Bearer ${authToken}`
    }
  });

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
  const response = await axios.get(`${backendUrl}/api/users/recents`, {
    headers: {
      Authorization: `Bearer ${authToken}`
    }
  });

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

export const fetchCategories = async (): Promise<any> => {
  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  const response = await axios.get(backendUrl + "/api/opening-search/categories", {
    headers: {
      Authorization: `Bearer ${authToken}`
    }
  });

  // Returning the api response data
  return response.data;
};

export const fetchOrgUnits = async (): Promise<any> => {
  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  const response = await axios.get(backendUrl + "/api/opening-search/org-units", {
    headers: {
      Authorization: `Bearer ${authToken}`
    }
  });

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
