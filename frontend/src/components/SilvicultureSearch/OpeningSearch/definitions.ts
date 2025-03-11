import CodeDescriptionDto from "@/types/CodeDescriptionType";
import { DATE_TYPES } from "@/types/DateTypes";

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
  dateType?: CodeDescriptionDto<DATE_TYPES>;
}

export type FilterDisplayNameMapType = {
  [key in keyof OpeningSearchFilterType]: string
}
