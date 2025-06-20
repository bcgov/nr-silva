import { JWT } from '../types/amplify';
import { extractGroups, parsePrivileges } from '../utils/famUtils';
import { USER_PRIVILEGE_TYPE } from '../types/AuthTypes';


// Define a global variable to store the ID token
let authIdToken: string | null = null;

export interface FamLoginUser {
  providerUsername?: string;
  userName?: string;
  displayName?: string;
  email?: string;
  idpProvider?: string;
  roles?: string[];
  authToken?: string;
  exp?: number;
  privileges: USER_PRIVILEGE_TYPE;
  firstName?: string;
  lastName?: string;
}

// Function to set the authIdToken variable
export const setAuthIdToken = (token: string | null) => {
  authIdToken = token;
};

// Function to get the authIdToken variable
export const getAuthIdToken = () => {
  return authIdToken;
};


/**
 * Function to parse roles and extract client IDs
 */
export const parseToken = (idToken: JWT | undefined): FamLoginUser | undefined => {
  if (!idToken) return undefined;
  setAuthIdToken(idToken?.toString() || null);
  const decodedIdToken = idToken?.payload;

  const displayName = decodedIdToken?.['custom:idp_display_name'] as string || '';
  const idpProvider = (decodedIdToken?.['custom:idp_name'] as string || '').toUpperCase();
  const hasComma = displayName.includes(',');

  let [lastName, firstName] = hasComma
    ? displayName.split(', ')
    : displayName.split(' ');

  if (!hasComma) {
    // In case of "First Last" format, swap first and last names
    [lastName, firstName] = [firstName, lastName];
  }

  const sanitizedFirstName = hasComma ? firstName?.split(' ')[0]?.trim() : firstName || '';

  const userName = decodedIdToken?.['custom:idp_username'] as string || '';
  const email = decodedIdToken?.['email'] as string || '';
  const cognitoGroups = extractGroups(decodedIdToken);


  return {
    userName,
    displayName,
    email,
    idpProvider,
    privileges: parsePrivileges(cognitoGroups),
    firstName: sanitizedFirstName,
    lastName,
    providerUsername: `${idpProvider}\\${userName}`
  };
};
