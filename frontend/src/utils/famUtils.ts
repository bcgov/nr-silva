import { ROLE_TYPE, SILVA_ROLES, USER_PRIVILEGE_TYPE } from '@/types/AuthTypes';


export function parsePrivileges(input: string[]): USER_PRIVILEGE_TYPE {
  const result: USER_PRIVILEGE_TYPE = {};

  for (const item of input) {
    const parts = item.split("_");
    const last = parts[parts.length - 1] ?? "";
    const isNumeric = last !== "" && Number.isFinite(Number(last));

    if (isNumeric) {
      const roleName = parts.slice(0, -1).join("_");
      if (SILVA_ROLES.includes(roleName as ROLE_TYPE)) {
        const role = roleName as ROLE_TYPE;
        if (!result[role]) result[role] = [];
        (result[role] as string[]).push(last);
      }
    } else {
      if (SILVA_ROLES.includes(item as ROLE_TYPE)) {
        result[item as ROLE_TYPE] = null;
      }
    }
  }

  return result;
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
