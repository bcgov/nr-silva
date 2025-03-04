import CodeDescriptionDto from "../../../types/CodeDescriptionType";

export type OpeningSearchFilterType = {
  mainSearchTerm?: string;
  orgUnit?: CodeDescriptionDto[];
  category?: CodeDescriptionDto[];
  statusList?: CodeDescriptionDto[];
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
  page?: number;
  size?: number;
}

export type FilterDisplayNameMapType = {
  [key in keyof OpeningSearchFilterType]: string
}
