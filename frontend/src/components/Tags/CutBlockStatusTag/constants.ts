import { TYPES } from "@carbon/react/lib/components/Tag/Tag";

export const KNOWN_CODES = [
  "HRS", "AP", "DD", "EE", "HB", "HD", "HS", "HX",
  "ID", "LC", "PD", "PE", "PL", "PP", "S", "SP"
] as const;

export type CodeType = typeof KNOWN_CODES[number];

export const codeColorMap: Record<CodeType, keyof typeof TYPES> = {
  HRS: "gray", // Harvesting Rights Surrendered
  AP: "magenta",  // Appeal Pending
  DD: "red",  // Disallowed by District
  EE: "red",  // Entered In Error
  HB: "green",  // Approved for Harvesting
  HD: "gray",  // Harvesting Deferred
  HS: "magenta",  // Suspended
  HX: "red",  // Cancelled
  ID: "gray",  // Inactive - Closed (Deactivated)
  LC: "teal",  // Logging Complete
  PD: "magenta",  // Pending Deferred
  PE: "magenta",  // Pending Electronic
  PL: "gray",  // Planned TSL
  PP: "outline",  // Proposed
  S: "blue",  // Silviculture
  SP: "gray"   // SP only, no application received
};
