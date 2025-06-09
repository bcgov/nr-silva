import { TYPES } from "@carbon/react/lib/components/Tag/Tag";

export const KNOWN_CODES = [
  "AMG", // Amalgamate
  "FG",  // Free Growing
  "AMD", // Amended
  "APP", // Approved
  "DFT", // Draft
  "RET", // Retired
  "SUB",  // Submitted
  "UNK"  // Unknown
] as const;

export type CodeType = typeof KNOWN_CODES[number];

export const codeColorMap: Record<CodeType, keyof typeof TYPES> = {
  AMG: "magenta",  // Amalgamate
  FG: "teal",      // Free Growing
  AMD: "purple",   // Amended
  APP: "green",    // Approved
  DFT: "gray",     // Draft
  RET: "red",      // Retired
  SUB: "blue",     // Submitted
  UNK: "outline"      // Unknown
};
