import CodeDescriptionDto from "../types/CodeDescriptionType";
import { TextInputEvent } from "../types/GeneralTypes";

export const PLACE_HOLDER = "—"

export const OPENING_STATUS_LIST: CodeDescriptionDto[] = [
  { code: 'AMG', description: 'Amalgamate' },
  { code: 'AMD', description: 'Amended' },
  { code: 'APP', description: 'Approved' },
  { code: 'DFT', description: 'Draft' },
  { code: 'FG', description: 'Free Growing' },
  { code: 'RMD', description: 'Removed' },
  { code: 'RET', description: 'Retired' },
  { code: 'SUB', description: 'Submitted' }
];

export const DATE_TYPE_LIST: CodeDescriptionDto[] = [
  { code: "disturbance", description: "Disturbance" },
  { code: "regenDelay", description: "Regen Delay" },
  { code: "freeGrowing", description: "Free Growing" },
  { code: "update", description: "Update" }
];

export const API_DATE_FORMAT = "yyyy-MM-dd";

export const DATE_PICKER_FORMAT = "yyyy/MM/dd";
