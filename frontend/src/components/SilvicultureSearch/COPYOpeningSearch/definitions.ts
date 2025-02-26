export type OpeningSearchFilterType = {
  mainSearchTerm?: string;
  orgUnit?: string[];
  category?: string[];
  statusList?: string[];
  myOpenings?: boolean;
  submittedToFrpa?: boolean;
  disturbanceDateStart?: string;
  disturbanceDateEnd?: string;
  regenDelayDateStart?: string;
  regenDelayDateEnd?: string;
  freeGrowingDateStart?: string;
  freeGrowingDateEnd?: string;
  updateDateStart?: string;
  updateDateEnd?: string;
  cuttingPermitId?: string;
  cutBlockId?: string;
  clientLocationCode?: string;
  clientNumber?: string;
  timberMark?: string;
}
