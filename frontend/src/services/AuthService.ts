import { JWT } from '../types/amplify';
import { formatRolesArray } from '../utils/famUtils';
import { UserClientRolesType } from '../types/UserRoleType';


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
  clientRoles?: UserClientRolesType[];    
  firstName?: string;
  lastName?: string;
}

// Function to set the authIdToken variable
const setAuthIdToken = (token: string | null) => {
  authIdToken = token;
};

// Function to get the authIdToken variable
export const getAuthIdToken = () => {
  return authIdToken;
};

/**
 * See OIDC Attribute Mapping mapping reference:
 *      https://github.com/bcgov/nr-forests-access-management/wiki/OIDC-Attribute-Mapping
 * Note, current user data return for 'userData.username' is matched to "cognito:username" on Cognito.
 * Which isn't what we really want to display. The display username is "custom:idp_username" from token.
 */

/**
 * Function to parse roles and extract client IDs
 */
export const parseToken = (idToken: JWT | undefined): FamLoginUser | undefined => {
  if(!idToken)
    return undefined;
  setAuthIdToken(idToken?.toString() || null);
  const decodedIdToken = idToken?.payload;

  // Extract the first name and last name from the displayName and remove unwanted part
  let displayName: string = '';
  if (decodedIdToken && 'custom:idp_display_name' in decodedIdToken) {
    displayName = decodedIdToken['custom:idp_display_name'] as string;
  }
  const hasComma = displayName.includes(',');

  let [lastName, firstName] = hasComma
    ? displayName.split(', ')
    : displayName.split(' ');

  if (!hasComma) {
    // In case of "First Last" format, swap first and last names
    [lastName, firstName] = [firstName, lastName];
  }

  const sanitizedFirstName = hasComma ? firstName.split(' ')[0].trim() : firstName;

  let userName: string = '';
  if (decodedIdToken && 'custom:idp_username' in decodedIdToken) {
    userName = decodedIdToken['custom:idp_username'] as string;
  }

  let email: string = '';
  if (decodedIdToken && 'custom:idp_username' in decodedIdToken) {
    email = decodedIdToken['email'] as string;
  }

  let idpProvider: string = '';
  if (decodedIdToken && 'identities' in decodedIdToken) {
    const identities = decodedIdToken['identities'] as object;
    if (identities && 'providerName' in identities) {
      idpProvider = identities['providerName'] as string;
    }
  }

  // Get user roles from FAM token
  const rolesArray: UserClientRolesType[] = formatRolesArray(decodedIdToken);

  const famLoginUser = {
    userName,
    displayName,
    email,
    idpProvider,
    clientRoles: rolesArray,
    exp: idToken?.payload.exp,
    firstName: sanitizedFirstName,
    lastName,
    providerUsername: `${idpProvider}\\${userName}`
  };
  

  return famLoginUser;
}
