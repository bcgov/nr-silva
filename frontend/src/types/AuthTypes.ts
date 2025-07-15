export const SILVA_ROLES = [
  "Viewer",
  "Submitter",
  "Approver",
  "Planner",
  "Admin",
] as const;

export type ROLE_TYPE = typeof SILVA_ROLES[number];

type ClientNumbers = string[] | null;

export type USER_PRIVILEGE_TYPE = Partial<Record<ROLE_TYPE, ClientNumbers>>;

export const validIdpProviders = ['IDIR', 'BCEIDBUSINESS'] as const;

export type IdpProviderType = typeof validIdpProviders[number];

export interface FamLoginUser {
  providerUsername?: string;
  userName?: string;
  displayName?: string;
  email?: string;
  idpProvider?: IdpProviderType;
  roles?: string[];
  authToken?: string;
  exp?: number;
  privileges: USER_PRIVILEGE_TYPE;
  associatedClients: string[];
  firstName?: string;
  lastName?: string;
}
