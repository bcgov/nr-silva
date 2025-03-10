import { DATE_TYPES } from "../../types/DateTypes";

type BaseParams = {
  tab: "openings";
  orgUnit?: string[];
  status?: string[];
};

type DateParams = {
  dateType: DATE_TYPES;
  dateStart: string;
  dateEnd: string;
};

export type SilvicultureSearchParams = BaseParams & (DateParams);
