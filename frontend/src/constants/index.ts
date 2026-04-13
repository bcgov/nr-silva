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

// Max length constraints for search input fields (based on database column definitions)
export const OPENING_ID_MAX_LENGTH = 10; // NUMBER(10,0)
export const FILE_ID_MAX_LENGTH = 10; // VARCHAR2(10)
export const LICENSEE_OPENING_ID_MAX_LENGTH = 30; // VARCHAR2(30)
export const CUT_BLOCK_MAX_LENGTH = 10; // VARCHAR2(10)
export const CUTTING_PERMIT_MAX_LENGTH = 3; // VARCHAR2(3)
export const TIMBER_MARK_MAX_LENGTH = 10; // VARCHAR2(10)
export const MAPSHEET_SQUARE_MAX_LENGTH = 3; // VARCHAR2(3)
export const OPENING_NUMBER_MAX_LENGTH = 4; // VARCHAR2(4)
export const INTRA_AGENCY_NUMBER_MAX_LENGTH = 10; // VARCHAR2(10)
export const SSID_MAX_LENGTH = 10; // NUMBER(10,0)
export const BGC_ZONE_MAX_LENGTH = 4; // VARCHAR2(4)
export const BGC_SUBZONE_MAX_LENGTH = 3; // VARCHAR2(3)
export const BGC_VARIANT_MAX_LENGTH = 1; // VARCHAR2(1)
export const BGC_PHASE_MAX_LENGTH = 1; // VARCHAR2(1)
export const BEC_SITE_SERIES_MAX_LENGTH = 4; // VARCHAR2(4)
export const BEC_SITE_PHASE_MAX_LENGTH = 3; // VARCHAR2(3)
