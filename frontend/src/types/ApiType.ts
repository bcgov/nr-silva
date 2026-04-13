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
};

export type ForestCoverSearchParams = {
  openingId?: number;
  stockingStatuses?: Array<string>;
  stockingTypes?: Array<string>;
  damageAgents?: Array<string>;
  openingStatuses?: Array<string>;
  fileId?: string;
  orgUnits?: Array<string>;
  openingCategories?: Array<string>;
  updateDateStart?: string;
  updateDateEnd?: string;
  page?: number;
  size?: number;
  sort?: Array<string>;
};

export type StandardsUnitSearchParams = {
  standardsRegimeId?: number;
  preferredSpecies?: Array<string>;
  orgUnits?: Array<string>;
  clientNumbers?: Array<string>;
  bgcZone?: string;
  bgcSubZone?: string;
  bgcVariant?: string;
  bgcPhase?: string;
  becSiteSeries?: string;
  becSiteType?: string;
  becSeral?: string;
  updateDateStart?: string;
  updateDateEnd?: string;
  page?: number;
  size?: number;
  sort?: Array<string>;
};
