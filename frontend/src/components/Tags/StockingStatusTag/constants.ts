import { TYPES } from "@carbon/react/lib/components/Tag/Tag";

export const KNOWN_CODES = [
  "A", "AF", "C", "DSD", "G", "IMM", "L", "M", "MAT",
  "NC", "NF", "NP", "NSR", "OR", "R", "RES", "S", "U"
] as const;

export type CodeType = typeof KNOWN_CODES[number];

export const codeColorMap: Record<CodeType, keyof typeof TYPES> = {
  A: "gray",         // Alpine
  AF: "green",       // Alpine Forest
  C: "teal",         // Cultivated
  DSD: "cool-gray",  // Disturbed Stocking Doubtful
  G: "gray",         // Gravel Bar
  IMM: "green",      // Immature
  L: "blue",         // Lake
  M: "green",        // Meadow
  MAT: "green",      // Mature
  NC: "gray",        // Non-commercial
  NF: "gray",        // Non-forest
  NP: "red",         // Non-productive
  NSR: "magenta",    // Not Satisfactorily Restocked
  OR: "gray",        // Open Range
  R: "warm-gray",    // Rock
  RES: "outline",    // Residual
  S: "cyan",         // Swamp
  U: "gray"          // Urban
};
