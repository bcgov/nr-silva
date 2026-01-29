import { CodeDescriptionDto } from "@/services/OpenApi";

export const CARBON_CLASS_PREFIX = "bx" as const;

export const ACCESS_TOKEN_KEY = "ACCESS_TOKEN" as const;

export const PLACE_HOLDER = "--" as const;

export const OPENING_STATUS_LIST: CodeDescriptionDto[] = [
  { code: 'AMG', description: 'Amalgamate' },
  { code: 'AMD', description: 'Amended' },
  { code: 'APP', description: 'Approved' },
  { code: 'DFT', description: 'Draft' },
  { code: 'FG', description: 'Free Growing' },
  { code: 'RMD', description: 'Removed' },
  { code: 'RET', description: 'Retired' },
  { code: 'SUB', description: 'Submitted' }
] as const;

export const DATE_TYPE_LIST: CodeDescriptionDto[] = [
  { code: "disturbance", description: "Disturbance" },
  { code: "regenDelay", description: "Regen Delay" },
  { code: "freeGrowing", description: "Free Growing" },
  { code: "update", description: "Update" }
] as const;

export const API_DATE_FORMAT = "yyyy-MM-dd";

export const DATE_PICKER_FORMAT = "yyyy/MM/dd";

export enum UNIQUE_CHARACTERS_UNICODE {
  NEW_LINE = "\u000A",
  BULLET = "\u2022",
  ORDINAL_INDICATOR = "\u00BA",
  PIPE = "\u007C",
};

export const NOT_APPLICABLE = "N/A" as const;

export const REDIRECT_KEY = "postLoginRedirect" as const;

export const SELECTED_CLIENT_KEY = 'SELECTED_CLIENT' as const;

export const TENURED_OPENING = "TENURED_OPENING" as const;

export const GOV_FUNDED_OPENING = "GOV_FUNDED_OPENING" as const;

export const VALID_MAPSHEET_GRID_LIST = ["82", "83", "92", "93", "94", "082", "083", "092", "093", "094", "102", "103", "104", "105", "114"];

/**
 * Allowed mapsheet letters (A-P and W)
 */
export const VALID_MAPSHEET_LETTER_LIST = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'W'];

/**
 * Allowed mapsheet quad and sub-quad numbers (0-4)
 */
export const VALID_MAPSHEET_QUAD_LIST = ['0', '1', '2', '3', '4'];
