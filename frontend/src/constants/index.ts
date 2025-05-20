import CodeDescriptionDto from "../types/CodeDescriptionType";
import { DATE_TYPES } from "../types/DateTypes";

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

export const DATE_TYPE_LIST: CodeDescriptionDto<DATE_TYPES>[] = [
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
