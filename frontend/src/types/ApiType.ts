export type ActivitySearchParams = {
  bases?: Array<string>;
  techniques?: Array<string>;
  methods?: Array<string>;
  isComplete?: boolean;
  objectives?: Array<string>;
  fundingSources?: Array<string>;
  orgUnits?: Array<string>;
  openingCategories?: Array<string>;
  fileId?: string;
  clientNumbers?: Array<string>;
  openingStatuses?: Array<string>;
  updateDateStart?: string;
  updateDateEnd?: string;
  intraAgencyNumber?: string;
  page?: number;
  size?: number;
  sort?: Array<string>;
};

export type DisturbanceSearchParams = {
  disturbances?: Array<string>;
  silvSystems?: Array<string>;
  variants?: Array<string>;
  cutPhases?: Array<string>;
  orgUnits?: Array<string>;
  openingCategories?: Array<string>;
  fileId?: string;
  clientNumbers?: Array<string>;
  openingStatuses?: Array<string>;
  updateDateStart?: string;
  updateDateEnd?: string;
  page?: number;
  size?: number;
  sort?: Array<string>;
}
