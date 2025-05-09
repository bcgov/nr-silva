import { OpeningHeaderType } from "@/types/TableHeader";
import { FilterDisplayNameMapType } from "./definitions";

export const defaultSearchTableHeaders: OpeningHeaderType[] = [
  { key: 'actions', header: 'Actions', selected: true },
  { key: 'openingId', header: 'Opening Id', selected: true },
  { key: 'openingNumber', header: 'Opening number', selected: false },
  { key: 'forestFileId', header: 'File Id', selected: true },
  { key: 'category', header: 'Category', selected: true },
  { key: 'orgUnitName', header: 'Org unit', selected: true },
  { key: 'status', header: 'Status', selected: true },
  { key: 'clientName', header: 'Client', selected: false },
  { key: 'timberMark', header: 'Timber mark', selected: false },
  { key: 'cuttingPermitId', header: 'Cutting permit', selected: true },
  { key: 'cutBlockId', header: 'Cut block', selected: true },
  { key: 'openingGrossAreaHa', header: 'Gross Area (ha)', selected: true },
  { key: 'disturbanceStartDate', header: 'Disturbance date', selected: true },
  { key: 'regenDelayDate', header: 'Regen delay due date', selected: false },
  { key: 'earlyFreeGrowingDate', header: 'Free growing due date', selected: false },
  { key: 'updateTimestamp', header: 'Update date', selected: false }
];


export const filterDisplayNameMap: FilterDisplayNameMapType = {
  mainSearchTerm: 'Search Term',
  orgUnit: 'Org Unit',
  category: 'Category',
  statusList: 'Status',
  myOpenings: 'My Openings',
  submittedToFrpa: 'FRPA Section 108',
  disturbanceDateStart: 'Disturbance date start',
  disturbanceDateEnd: 'Disturbance date end',
  regenDelayDateStart: 'Regen delay date start',
  regenDelayDateEnd: 'Regen delay date end',
  freeGrowingDateStart: 'Free growing date start',
  freeGrowingDateEnd: 'Free growing date end',
  updateDateStart: 'Update date start',
  updateDateEnd: 'Update date end',
  cuttingPermitId: 'Cutting permit id',
  cutBlockId: 'Cut block id',
  clientLocationCode: 'Client location code',
  clientNumber: 'Client number',
  timberMark: 'Timber mark'
};

export const MAX_TEXT_INPUT_LEN = 50;
