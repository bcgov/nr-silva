import { ROLE_TYPE, SILVA_ROLES, USER_PRIVILEGE_TYPE } from '@/types/AuthTypes';

const FAM_SIDECAR_PREFIX = 'FAM:';
const SCOPE_FOREST_CLIENT = 'FOREST_CLIENT';
const CSS_SCOPE_TYPES = [SCOPE_FOREST_CLIENT, 'DISTRICT', 'REGION'] as const;

interface CssScope {
  type: string;
  value: string;
}

interface ParsedCssRole {
  baseRoleName: string;
  scopes: CssScope[];
}

const stripOneScope = (
  name: string
): { prefix: string; scope: CssScope } | null => {
  let bestMarkerIndex = -1;
  let bestScopeType = '';
  let bestMarker = '';

  for (const scopeType of CSS_SCOPE_TYPES) {
    const marker = `_${scopeType}-`;
    const idx = name.lastIndexOf(marker);
    if (idx > 0 && idx > bestMarkerIndex) {
      const candidateValue = name.slice(idx + marker.length);
      if (candidateValue.length > 0) {
        bestMarkerIndex = idx;
        bestScopeType = scopeType;
        bestMarker = marker;
      }
    }
  }

  if (bestMarkerIndex < 0) {
    return null;
  }

  return {
    prefix: name.slice(0, bestMarkerIndex),
    scope: {
      type: bestScopeType,
      value: name.slice(bestMarkerIndex + bestMarker.length),
    },
  };
};

/**
 * Parses a CSS scoped role string from right to left (mirroring backend `CssRoleNaming.parse`).
 */
export const parseCssRole = (roleString: string): ParsedCssRole => {
  const reversedScopes: CssScope[] = [];
  let current = roleString;

  while (true) {
    const stripped = stripOneScope(current);
    if (!stripped) {
      break;
    }
    reversedScopes.push(stripped.scope);
    current = stripped.prefix;
  }

  return {
    baseRoleName: current,
    scopes: reversedScopes.reverse(),
  };
};

const normalizeSilvaRole = (rawBaseRole: string): ROLE_TYPE | '' => {
  const upperBase = rawBaseRole.trim().toUpperCase();
  const matched = SILVA_ROLES.find((role) => {
    const upperRole = role.toUpperCase();
    return upperBase === upperRole || upperBase.endsWith(`_${upperRole}`);
  });
  return matched ?? '';
};

/**
 * Extracts the normalized Silva role name and optional Forest Client ID from CSS scoped roles
 * (`SUBMITTER_FOREST_CLIENT-00012797`, `SILVA_SUBMITTER_FOREST_CLIENT-00012797`, compound scopes)
 * as well as legacy Cognito role names (`Viewer`, `Submitter_00012345`).
 */
function parseRoleAndClient(roleString: string): { roleName: string; clientId?: string } {
  if (!roleString || roleString.startsWith(FAM_SIDECAR_PREFIX)) {
    return { roleName: '' };
  }

  const parsedCss = parseCssRole(roleString);
  if (parsedCss.scopes.length > 0) {
    const forestClientScope = parsedCss.scopes.find(
      (scope) => scope.type.toUpperCase() === SCOPE_FOREST_CLIENT
    );
    const normalizedRole = normalizeSilvaRole(parsedCss.baseRoleName);
    if (!forestClientScope) {
      return { roleName: '' };
    }
    return {
      roleName: normalizedRole,
      clientId: forestClientScope.value,
    };
  }

  const parts = roleString.split('_');
  const last = parts[parts.length - 1];

  if (parts.length > 1 && last !== undefined && /^\d+$/.test(last)) {
    return {
      roleName: normalizeSilvaRole(parts.slice(0, -1).join('_')),
      clientId: last,
    };
  }

  return { roleName: normalizeSilvaRole(roleString) };
}

/**
 * Parses an array of role strings into a privilege map keyed by TitleCase `SILVA_ROLES`.
 * Scoped roles map to deduplicated client ID arrays; concrete roles map to `null`.
 */
export function parsePrivileges(groups: string[]): USER_PRIVILEGE_TYPE {
  const result: USER_PRIVILEGE_TYPE = {};

  for (const group of groups) {
    const { roleName, clientId } = parseRoleAndClient(group);

    if (SILVA_ROLES.includes(roleName as ROLE_TYPE)) {
      const role = roleName as ROLE_TYPE;
      if (clientId) {
        const existing = result[role];
        const clients = Array.isArray(existing) ? existing : [];
        if (!clients.includes(clientId)) {
          clients.push(clientId);
        }
        result[role] = clients;
      } else {
        result[role] = null;
      }
    }
  }

  return result;
}

export function hasCreateOpeningPrivilege(privileges: USER_PRIVILEGE_TYPE): boolean {
  const requiredRoles: ROLE_TYPE[] = ['Submitter', 'Approver', 'Planner', 'Admin'];
  return requiredRoles.some((role) => privileges[role] !== undefined);
}

/**
 * Extracts all unique client numbers from an array of role strings, ignoring `FAM:` sidecars.
 */
export function extractUniqueClients(groups: string[]): string[] {
  const clientIds = new Set<string>();

  for (const group of groups) {
    const { clientId } = parseRoleAndClient(group);
    if (clientId) {
      clientIds.add(clientId);
    }
  }

  return [...clientIds];
}

const filterRoleStrings = (raw: unknown): string[] => {
  if (!Array.isArray(raw)) {
    return [];
  }
  return raw.filter(
    (entry): entry is string =>
      typeof entry === 'string' &&
      entry.trim().length > 0 &&
      !entry.startsWith(FAM_SIDECAR_PREFIX)
  );
};

/**
 * Extracts normalized roles from decoded Keycloak or legacy Cognito JWT claims, filtering out
 * `FAM:` metadata sidecar entries.
 *
 * Extraction order:
 * 1. `client_roles`
 * 2. `resource_access[azp].roles`
 * 3. `cognito:groups` (legacy fallback)
 */
export function extractGroups(decodedClaims: object | undefined): string[] {
  if (!decodedClaims || typeof decodedClaims !== 'object') {
    return [];
  }

  const claims = decodedClaims as Record<string, unknown>;

  const clientRoles = filterRoleStrings(claims.client_roles);
  if (clientRoles.length > 0) {
    return clientRoles;
  }

  const azp = typeof claims.azp === 'string' ? claims.azp : '';
  const resourceAccess = claims.resource_access;
  if (azp && resourceAccess && typeof resourceAccess === 'object') {
    const clientEntry = (resourceAccess as Record<string, unknown>)[azp];
    if (clientEntry && typeof clientEntry === 'object') {
      const resourceRoles = filterRoleStrings(
        (clientEntry as Record<string, unknown>).roles
      );
      if (resourceRoles.length > 0) {
        return resourceRoles;
      }
    }
  }

  return filterRoleStrings(claims['cognito:groups']);
}
