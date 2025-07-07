import { JWT } from '@/types/amplify';
import { FamLoginUser, IdpProviderType, validIdpProviders } from '@/types/AuthTypes';
import { extractGroups, parsePrivileges } from '@/utils/famUtils';

// Define a global variable to store the ID token
let authIdToken: string | null = null;

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

  /**
   * Normalizes and validates IDP provider from token.
   * Returns valid provider or undefined.
   */
  const idpProvider = validIdpProviders.includes(
    (decodedIdToken?.['custom:idp_name'] as string)?.toUpperCase() as IdpProviderType
  )
    ? ((decodedIdToken?.['custom:idp_name'] as string).toUpperCase() as IdpProviderType)
    : undefined;

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
