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
