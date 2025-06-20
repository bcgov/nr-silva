export const SILVA_ROLES = [
  "Viewer",
  "Submitter",
  "Approver",
  "Planner",
  "Admin",
] as const;

export type ROLE_TYPE = typeof SILVA_ROLES[number];

type RoleValue = string[] | null;

export type USER_PRIVILEGE_TYPE = Partial<Record<ROLE_TYPE, RoleValue>>;
