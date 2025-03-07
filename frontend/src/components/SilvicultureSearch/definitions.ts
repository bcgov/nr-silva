type BaseParams = {
  tab: "openings";
  orgUnit?: string[];
  status?: string[];
};

type UpdateDateParams = {
  dateType: "update";
  updateDateStart: string;
  updateDateEnd: string;
};

export type SilvicultureSearchParams = BaseParams & (UpdateDateParams);
