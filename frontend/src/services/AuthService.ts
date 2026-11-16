import { User } from 'oidc-client-ts';

import { FamLoginUser, IdpProviderType, validIdpProviders } from '@/types/AuthTypes';
import { extractGroups, extractUniqueClients, parsePrivileges } from '@/utils/famUtils';

// Define a global variable to store the active bearer token string
let authIdToken: string | null = null;

// Function to set the authIdToken variable
export const setAuthIdToken = (token: string | null) => {
  authIdToken = token;
};

// Function to get the authIdToken variable
export const getAuthIdToken = () => {
  return authIdToken;
};

const getStringClaim = (claims: Record<string, unknown>, key: string): string => {
  const value = claims[key];
  return typeof value === 'string' ? value : '';
};

/**
 * Decodes the payload section of a JWT string with UTF-8 safe base64url decoding.
 */
export const decodeJwtPayload = (
  token: string | undefined
): Record<string, unknown> | undefined => {
  if (!token) {
    return undefined;
  }

  const segments = token.split('.');
  if (segments.length < 2 || !segments[1]) {
    return undefined;
  }

  try {
    const base64 = segments[1].replace(/-/g, '+').replace(/_/g, '/');
    const padded = base64.padEnd(base64.length + ((4 - (base64.length % 4)) % 4), '=');
    const binary = atob(padded);
    const bytes = Uint8Array.from(binary, (char) => char.charCodeAt(0));
    const json = new TextDecoder().decode(bytes);
    const parsed: unknown = JSON.parse(json);
    return parsed && typeof parsed === 'object' ? (parsed as Record<string, unknown>) : undefined;
  } catch {
    return undefined;
  }
};

const normalizeIdpProvider = (rawProvider: string): IdpProviderType | undefined => {
  const upper = rawProvider.trim().toUpperCase();
  if (upper === 'AZUREIDIR' || upper === 'IDIR') {
    return 'IDIR';
  }
  return validIdpProviders.includes(upper as IdpProviderType)
    ? (upper as IdpProviderType)
    : undefined;
};

const parseNames = (
  displayName: string,
  givenName: string,
  familyName: string
): { firstName: string; lastName: string } => {
  const trimmedDisplay = displayName.trim();
  if (!trimmedDisplay) {
    return {
      firstName: givenName.trim(),
      lastName: familyName.trim(),
    };
  }

  if (trimmedDisplay.includes(',')) {
    const commaIdx = trimmedDisplay.indexOf(',');
    const lastName = trimmedDisplay.slice(0, commaIdx).trim();
    const firstPart = trimmedDisplay.slice(commaIdx + 1).trim();
    const firstName = firstPart.split(' ')[0]?.trim() ?? '';
    return { firstName, lastName };
  }

  if (trimmedDisplay.includes(' ')) {
    const firstSpaceIdx = trimmedDisplay.indexOf(' ');
    const firstName = trimmedDisplay.slice(0, firstSpaceIdx).trim();
    const lastName = trimmedDisplay.slice(firstSpaceIdx + 1).trim();
    return { firstName, lastName };
  }

  return {
    firstName: givenName.trim() || trimmedDisplay,
    lastName: familyName.trim(),
  };
};

/**
 * Parses an OIDC {@link User} from BC Gov SSO Keycloak into Silva's {@link FamLoginUser}.
 */
export const parseToken = (oidcUser: User | null | undefined): FamLoginUser | undefined => {
  if (!oidcUser || !oidcUser.access_token) {
    setAuthIdToken(null);
    return undefined;
  }

  setAuthIdToken(oidcUser.access_token);

  const accessClaims = decodeJwtPayload(oidcUser.access_token) ?? {};
  const profileClaims = (oidcUser.profile ?? {}) as Record<string, unknown>;
  const claims: Record<string, unknown> = {
    ...profileClaims,
    ...accessClaims,
  };

  const preferredUsername = getStringClaim(claims, 'preferred_username');
  const preferredParts = preferredUsername.split('@');
  const preferredGuid = preferredParts[0] ?? '';
  const preferredIdpSuffix = preferredParts.length > 1 ? preferredParts[preferredParts.length - 1] : '';

  const rawProvider =
    getStringClaim(claims, 'identity_provider') ||
    preferredIdpSuffix ||
    getStringClaim(claims, 'custom:idp_name');
  const idpProvider = normalizeIdpProvider(rawProvider);

  const userName =
    getStringClaim(claims, 'idir_username') ||
    getStringClaim(claims, 'bceid_username') ||
    getStringClaim(claims, 'custom:idp_username') ||
    (
      getStringClaim(claims, 'idir_user_guid') ||
      getStringClaim(claims, 'bceid_user_guid') ||
      preferredGuid
    ).toUpperCase();

  const displayName =
    getStringClaim(claims, 'display_name') ||
    getStringClaim(claims, 'custom:idp_display_name') ||
    getStringClaim(claims, 'name');

  const { firstName, lastName } = parseNames(
    displayName,
    getStringClaim(claims, 'given_name'),
    getStringClaim(claims, 'family_name')
  );

  const email = getStringClaim(claims, 'email');
  const roles = extractGroups(claims);
  const exp = typeof claims.exp === 'number' ? claims.exp : oidcUser.expires_at;

  return {
    userName,
    displayName,
    email,
    idpProvider,
    roles,
    authToken: oidcUser.access_token,
    exp,
    privileges: parsePrivileges(roles),
    associatedClients: extractUniqueClients(roles),
    firstName,
    lastName,
    providerUsername: `${idpProvider}\\${userName}`,
  };
};
