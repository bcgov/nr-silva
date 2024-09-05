import axios from "axios";
import { getAuthIdToken } from "../AuthService";

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
  status?: string;
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
  fileId: string | null;
  silvaReliefAppId: string | null;
}

const API_URL = "https://nr-silva-test-backend.apps.silver.devops.gov.bc.ca";

export const fetchOpenings = async (filters: OpeningFilters): Promise<any> => {
  // Map frontend filter names to backend filter names
  const params = {
    mainSearchTerm: filters.searchInput,
    orgUnit: filters.orgUnit,
    category: filters.category,
    status: filters.status,
    entryUserId: filters.clientAcronym,
    submittedToFrpa:
      filters.openingFilters?.includes("Submitted to FRPA section 108") ||
      undefined,
    disturbanceDateStart: filters.startDate,
    disturbanceDateEnd: filters.endDate,
    // Add other mappings as necessary
    page: filters.page,
    perPage: filters.perPage,
  };

  // Remove undefined or empty values from the params object
  const cleanedParams = Object.fromEntries(
    Object.entries(params).filter(([_, v]) => v !== undefined && v !== "")
  );

  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  const response = await axios.get(API_URL + "/api/opening-search", {
    params: cleanedParams,
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
  });

  // Flatten the data part of the response
  const flattenedData = response.data.data.map((item: OpeningItem) => ({
    ...item,
    statusCode: item.status?.code,
    statusDescription: item.status?.description,
    categoryCode: item.category?.code,
    categoryDescription: item.category?.description,
    status: undefined, // Remove the old nested status object
    category: undefined, // Remove the old nested category object
  }));
  // Returning the modified response data with the flattened structure
  return {
    ...response.data,
    data: flattenedData,
  };
};

export const fetchCategories = async (): Promise<any> => {
  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  const response = await axios.get(API_URL + "/api/opening-search/categories", {
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
  });

  // Returning the api response data
  return response.data;
};

export const fetchOrgUnits = async (): Promise<any> => {
  // Retrieve the auth token
  const authToken = getAuthIdToken();

  // Make the API request with the Authorization header
  const response = await axios.get(API_URL + "/api/opening-search/org-units", {
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
  });

  // Returning the api response data
  return response.data;
};

export const fetchOpeningFilters = async (): Promise<any> => {
  const [categories, orgUnits] = await Promise.all([
    fetchCategories(),
    fetchOrgUnits(),
  ]);
  return { categories: categories, orgUnits: orgUnits };
};
