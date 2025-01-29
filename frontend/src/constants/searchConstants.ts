import { TextValueData } from '@/utils/multiSelectSortUtils';

export const dateTypes: TextValueData[] = [
  { value: "disturbance", text: "Disturbance" },
  { value: "regenDelay", text: "Regen Delay" },
  { value: "freeGrowing", text: "Free Growing" },
  { value: "update", text: "Update" }
];

export const statusTypes: TextValueData[]= [
  {value:'AMG',text:'Amalgamate'},
  {value:'AMD',text:'Amended'},
  {value:'APP',text:'Approved'},
  {value:'DFT',text:'Draft'},
  {value:'FG',text:'Free Growing'},
  {value:'RMD',text:'Removed'},
  {value:'RET',text:'Retired'},
  {value:'SUB',text:'Submitted'}
  ];

export const blockStatuses: TextValueData[] = [
  { value: "AP", text: "AP  -  Appeal Pending" },
  { value: "HB", text: "HB  -  Approved for Harvesting" },
  { value: "HX", text: "HX  -  Cancelled" },
  { value: "DD", text: "DD  -  Disallowed by District" },
  { value: "EE", text: "EE  -  Entered In Error" },
  { value: "HD", text: "HD  -  Harvesting Deferred" },
  { value: "HRS", text: "HRS  -  Harvesting Rights Surrendered" },
  { value: "ID", text: "ID  -  Inactive - Closed (Deactivated)" },
  { value: "LC", text: "LC  -  Logging Complete" },
  { value: "PD", text: "PD  -  Pending Deferred" },
  { value: "PE", text: "PE  -  Pending Electronic" },
  { value: "PL", text: "PL  -  Planned TSL" },
  { value: "PP", text: "PP  -  Proposed" },
  { value: "SP", text: "SP  -  SP only, no application received" },
  { value: "S", text: "S  -  Silviculture" },
  { value: "HS", text: "HS  -  Suspended" }
];