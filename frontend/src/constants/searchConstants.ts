import { IdTextValueData } from '@/types/GeneralTypes';

export const dateTypes: IdTextValueData[] = [
  { id: "disturbance", text: "Disturbance" },
  { id: "regenDelay", text: "Regen Delay" },
  { id: "freeGrowing", text: "Free Growing" },
  { id: "update", text: "Update" }
];

export const statusTypes: IdTextValueData[]= [
  {id:'AMG',text:'Amalgamate'},
  {id:'AMD',text:'Amended'},
  {id:'APP',text:'Approved'},
  {id:'DFT',text:'Draft'},
  {id:'FG',text:'Free Growing'},
  {id:'RMD',text:'Removed'},
  {id:'RET',text:'Retired'},
  {id:'SUB',text:'Submitted'}
  ];

export const blockStatuses: IdTextValueData[] = [
  { id: "AP", text: "AP  -  Appeal Pending" },
  { id: "HB", text: "HB  -  Approved for Harvesting" },
  { id: "HX", text: "HX  -  Cancelled" },
  { id: "DD", text: "DD  -  Disallowed by District" },
  { id: "EE", text: "EE  -  Entered In Error" },
  { id: "HD", text: "HD  -  Harvesting Deferred" },
  { id: "HRS", text: "HRS  -  Harvesting Rights Surrendered" },
  { id: "ID", text: "ID  -  Inactive - Closed (Deactivated)" },
  { id: "LC", text: "LC  -  Logging Complete" },
  { id: "PD", text: "PD  -  Pending Deferred" },
  { id: "PE", text: "PE  -  Pending Electronic" },
  { id: "PL", text: "PL  -  Planned TSL" },
  { id: "PP", text: "PP  -  Proposed" },
  { id: "SP", text: "SP  -  SP only, no application received" },
  { id: "S", text: "S  -  Silviculture" },
  { id: "HS", text: "HS  -  Suspended" }
];