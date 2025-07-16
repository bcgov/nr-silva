import { ROLE_TYPE, SILVA_ROLES, USER_PRIVILEGE_TYPE } from '@/types/AuthTypes';


/**
 * Extracts the role name and optional numeric client ID from a role string.
 * If the last part of the string is a number, it's treated as a client ID.
 *
 * @param roleString - The full role string (e.g., "OPENING_ADMIN_123456" or "OPENING_ADMIN")
 * @returns An object containing the base role name and optional client ID
 */
function parseRoleAndClient(roleString: string): { roleName: string; clientId?: string } {
  const parts = roleString.split("_");
  const last = parts[parts.length - 1];
  const isClientId = last && Number.isFinite(Number(last));

  return isClientId
    ? { roleName: parts.slice(0, -1).join("_"), clientId: last }
    : { roleName: roleString };
}

/**
 * Parses an array of role group strings into a privilege map.
 * Each recognized role is mapped to either a list of associated client IDs or `null` if no ID is attached.
 *
 * @param groups - Cognito group strings, such as ["OPENING_ADMIN_123", "OPENING_VIEWER"]
 * @returns USER_PRIVILEGE_TYPE map of roles to client IDs or null
 */
export function parsePrivileges(groups: string[]): USER_PRIVILEGE_TYPE {
  const result: USER_PRIVILEGE_TYPE = {};

  for (const group of groups) {
    const { roleName, clientId } = parseRoleAndClient(group);

    if (SILVA_ROLES.includes(roleName as ROLE_TYPE)) {
      const role = roleName as ROLE_TYPE;
      if (clientId) {
        if (!result[role]) result[role] = [];
        (result[role] as string[]).push(clientId);
      } else {
        result[role] = null;
      }
    }
  }

  return result;
}

/**
 * Extracts all unique client numbers from an array of role group strings.
 * Only numeric suffixes from valid role strings are included.
 *
 * @param groups - Cognito group strings, such as ["OPENING_ADMIN_123", "OPENING_VIEWER"]
 * @returns A list of unique client numbers as strings
 */
export function extractUniqueClients(groups: string[]): string[] {
  const clientIds = new Set<string>();

  for (const group of groups) {
    const { clientId } = parseRoleAndClient(group);
    if (clientId) clientIds.add(clientId);
  }

  return [...clientIds];
}


/**
 * Extract groups from the decoded token.
 *
 * @param {object | undefined} decodedIdToken Decoded token with payload.
 * @returns {string[]} Array of groups.
 */
export function extractGroups(decodedIdToken: object | undefined): string[] {
  if (!decodedIdToken) {
    return [];
  }

  if ('cognito:groups' in decodedIdToken) {
    return decodedIdToken['cognito:groups'] as string[];
  }

  return [];
}
