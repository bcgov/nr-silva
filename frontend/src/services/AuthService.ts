import {
  JWT,
  fetchAuthSession,
  getCurrentUser,
  signInWithRedirect,
  signOut
} from 'aws-amplify/auth';
import { env } from '../env';
import { UserClientRolesType } from '../types/UserRoleType';
import { formatRolesArray } from '../utils/famUtils';

// Define a global variable to store the ID token
let authIdToken: string | null = null;

const FAM_LOGIN_USER = 'famLoginUser';

export interface FamLoginUser {
  username?: string;
  idpProvider?: string;
  roles?: string[];
  clientIds?: string[]; // Add clientIds to FamLoginUser interface
  exp?: number;
}

// Function to set the authIdToken variable
const setAuthIdToken = (token: string | null) => {
  authIdToken = token;
};

// Function to get the authIdToken variable
export const getAuthIdToken = () => {
  return authIdToken;
};

export const signIn = async (provider: string): Promise<void> => {
  const appEnv = env.VITE_ZONE ?? 'DEV';

  try {
    if (provider.localeCompare('idir') === 0) {
      await signInWithRedirect({
        provider: { custom: `${(appEnv).toLocaleUpperCase()}-IDIR` }
      });
    } else if (provider.localeCompare('bceid') === 0) {
      await signInWithRedirect({
        provider: { custom: `${(appEnv).toLocaleUpperCase()}-BCEIDBUSINESS` }
      });
    }
  } catch (e) {
    return Promise.reject(e);
  }
};

export const isLoggedIn = () => {
  // this will convert the locally stored string to FamLoginUser interface type
  // TODO add this to state once redux store is configured
  const stateInfo = (JSON.parse(localStorage.getItem(FAM_LOGIN_USER) as string) as
    | FamLoginUser
    | undefined
    | null);
  // check if the user is logged in
  let loggedIn = false;
  if (stateInfo?.exp) {
    const expirationDate = new Date(stateInfo?.exp);
    const currentDate = new Date();
    loggedIn = expirationDate < currentDate;
  }
  return loggedIn;
};

export const handlePostLogin = async () => {
  try {
    // const userData = await Auth.currentAuthenticatedUser();
    await refreshToken();
  } catch (error) {
    console.log('Authentication Error:', error);
  }
};

/**
 * Amplify method currentSession() will automatically refresh the accessToken and idToken
 * if tokens are "expired" and a valid refreshToken presented.
 *   // console.log("currentAuthToken: ", currentAuthToken)
 *   // console.log("ID Token: ", currentAuthToken.getIdToken().getJwtToken())
 *   // console.log("Access Token: ", currentAuthToken.getAccessToken().getJwtToken())
 *
 * Automatically logout if unable to get currentSession().
 */
async function refreshToken (): Promise<FamLoginUser | undefined> {
  try {
    const { tokens } = await fetchAuthSession();
    // Set the authIdToken variable
    setAuthIdToken(tokens?.idToken?.toString() || null);

    const famLoginUser = parseToken(tokens?.idToken, tokens?.accessToken);
    await storeFamUser(famLoginUser);
    return famLoginUser;

  } catch (error) {
    console.error(
      'Problem refreshing token or token is invalidated:',
      error
    );
    // logout and redirect to login.
    logout();
  }
}

/**
 * See OIDC Attribute Mapping mapping reference:
 *      https://github.com/bcgov/nr-forests-access-management/wiki/OIDC-Attribute-Mapping
 * Note, current user data return for 'userData.username' is matched to "cognito:username" on Cognito.
 * Which isn't what we really want to display. The display username is "custom:idp_username" from token.
 */

/**
 * Function to parse roles and extract client IDs
 */
function parseToken(idToken: JWT | undefined, accessToken: JWT | undefined): FamLoginUser {
  const decodedIdToken = idToken?.payload;
  const decodedAccessToken = accessToken?.payload;
  console.log("The decoded id token:")
  console.log(decodedIdToken)
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

  let roles: string[] = [];
  if (decodedAccessToken && 'cognito:groups' in decodedAccessToken) {
    roles = decodedAccessToken['cognito:groups'] as Array<string>;
  }

  // Extract client IDs from roles
  const clientIds = parseClientIdsFromRoles(roles);

  //get the user roles from the FAM token
  const rolesArray = formatRolesArray(decodedIdToken);

  const famLoginUser = {
    userName,
    displayName,
    email,
    idpProvider,
    clientRoles: rolesArray,
    clientIds,
    exp: idToken?.payload.exp,
    firstName: sanitizedFirstName,
    lastName
  };
  

  return famLoginUser;
}

/**
 * Function to parse client IDs from roles
 */
function parseClientIdsFromRoles(roles: string[]): string[] {
  // Implement logic to extract client IDs from roles here
  // Placeholder implementation
  return roles.map(role => {
    const parts = role.split(':');
    return parts[parts.length - 1];
  });
}

/**
 *
 */
function removeFamUser() {
  storeFamUser(undefined);

  // clean up local storage for selected application
}

/**
 *
 */
function storeFamUser (famLoginUser: FamLoginUser | null | undefined) {
  if (famLoginUser) {
    localStorage.setItem(FAM_LOGIN_USER, JSON.stringify(famLoginUser));
  } else {
    localStorage.removeItem(FAM_LOGIN_USER);
  }
}

export const isCurrentAuthUser = async () => {
  try {
    // checks if the user is authenticated
    await getCurrentUser();
    // refreshes the token and stores it locally
    await refreshToken();
    return true;
  } catch (error) {
    return false;
  }
};

export const logout = async () => {
  await signOut();
  removeFamUser();
  console.log('User logged out.');
};
