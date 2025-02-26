import axios from "axios";
import CodeDescriptionDto from "../types/CodeDescriptionType";
import { API_ENDPOINTS, defaultHeaders } from "./apiConfig";
import { getAuthIdToken } from "./AuthService";
import { OrgUnitEntity } from "../types/OpeningTypes";

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
export const fetchOpeningsOrgUnits = (): Promise<OrgUnitEntity[]> => {
  const authToken = getAuthIdToken();

  return axios.get(API_ENDPOINTS.orgUnits(),defaultHeaders(authToken))
    .then((res)=> res.data);
};

