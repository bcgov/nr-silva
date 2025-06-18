import { TYPES } from "@carbon/react/lib/components/Tag/Tag";

export const KNOWN_CODES = [
  "FG",
  "RG",
  "PH",
  "NR",
  "EX",
  "UN"
] as const;

export type CodeType = typeof KNOWN_CODES[number];

export const codeColorMap: Record<CodeType, keyof typeof TYPES> = {
  FG: "teal",
  RG: "teal",
  PH: "teal",
  NR: "teal",
  EX: "teal",
  UN: "gray"
};

export const codeNameMap: Record<CodeType, string> = {
  FG: "Free Growing",
  RG: "Regeneration",
  PH: "Post Harvest",
  NR: "No Regeneration",
  EX: "Declared to the extent practicable",
  UN: "Unknown",
};
