import CodeDescriptionDto from "./CodeDescriptionType";
import { CommentDto } from "./CommentTypes";
import { ForestClientType } from "./ForestClientTypes/ForestClientType";
import { PaginatedResponseType } from "./PaginationTypes";

export interface StatusCategory {
  code: string;
  description: string;
}

export interface RecentOpeningApi {
  openingId: number;
  forestFileId: string;
  cuttingPermit: string | null;
  timberMark: string | null;
  cutBlock: string | null;
  grossAreaHa: number | null;
  status: StatusCategory | null;
  category: StatusCategory | null;
  disturbanceStart: string | null;
  entryTimestamp: string | null;
  updateTimestamp: string | null;
}

export interface IOpeningPerYear {
  orgUnitCode: string[] | null;
  statusCode: string[] | null;
  entryDateStart: string | null;
  entryDateEnd: string | null;
}

export interface OpeningSearchResponseDto {
  openingId: number;
  openingNumber: string | null;
  category: CodeDescriptionDto | null;
  status: CodeDescriptionDto | null;
  cuttingPermitId: number | null;
  timberMark: string | null;
  cutBlockId: number | null;
  openingGrossAreaHa: number | null;
  disturbanceStartDate: string | null;
  orgUnitCode: string;
  orgUnitName: string;
  clientNumber: string | null;
  clientAcronym: string | null;
  clientName: string | null;
  regenDelayDate: string;
  updateTimestamp: string;
  entryUserId: string;
  submittedToFrpa: boolean;
  forestFileId: string | null;
  silvaReliefAppId: string | null;
  favourite: boolean;
  earlyFreeGrowingDate: string | null;
}

export type PaginatedRecentOpeningsDto = PaginatedResponseType<OpeningSearchResponseDto>;

/**
 * Corresponds to the OpeningsPerYearDto in the Postgres backend.
 */
export type OpeningsPerYearDto = {
  month: number,
  year: number,
  amount: number,
  statusCounts: {
    [key: string]: number
  }
}

export type OpeningDetailsTombstoneDto = {
  openingNumber: string | null,
  openingStatus: CodeDescriptionDto,
  orgUnitCode: string | null,
  orgUnitName: string | null,
  openCategory: CodeDescriptionDto,
  client: ForestClientType,
  fileId: string | null,
  cutBlockID: string | null,
  cuttingPermitId: string | null,
  timberMark: string | null,
  maxAllowedAccess: string | null,
  openingGrossArea: number | null,
  createdBy: string | null,
  createdOn: string | null,
  lastUpdatedOn: string | null,
  disturbanceStartDate: string | null
};

export type OpeningDetailsOverviewOpeningDto = {
  licenseeId: string | null,
  tenureType: CodeDescriptionDto,
  managementUnitType: CodeDescriptionDto,
  managementUnitId: string | null,
  timberSaleOffice: CodeDescriptionDto,
  comments: CommentDto[]
};


export type OpeningDetailsOverviewMilestoneDto = {
  standardsUnitId: string | null,
  postHarvestDeclaredDate: string | null,
  regenDeclaredDate: string | null,
  regenOffsetYears: number | null,
  regenDueDate: string | null,
  freeGrowingDeclaredDate: string | null,
  freeGrowingOffsetYears: number | null,
  freeGrowingDueDate: string | null
};

export type OpeningDetailsOverviewDto = {
  opening: OpeningDetailsOverviewOpeningDto,
  milestones: OpeningDetailsOverviewMilestoneDto
};

export type OpeningDetailsTombstoneOverviewDto = {
  openingId: number,
  tombstone: OpeningDetailsTombstoneDto,
  overview: OpeningDetailsOverviewDto
};

export type OpeningDetailsStockingDetailsDto = {
  stockingStandardUnit: string | null,
  ssid: number | null,
  defaultMof: boolean,
  manualEntry: boolean,
  fspId: number | null,
  netArea: number | null,
  soilDisturbancePercent: number | null,
  bec: OpeningDetailsBecDto,
  regenDelay: number | null,
  freeGrowingLate: number | null,
  freeGrowingEarly: number | null,
  additionalStandards: string | null,
};

export type OpeningDetailsBecDto = {
  becZoneCode: string | null,
  becSubzoneCode: string | null,
  becVariant: string | null,
  becPhase: string | null,
  becSiteSeries: string | null,
  becSiteType: string | null,
  becSeral: string | null,
};

export type OpeningDetailsStockingSpeciesDto = {
  species: CodeDescriptionDto,
  minHeight: number | null,
}

export type OpeningDetailsStockingLayerDto = {
  minWellspacedTrees: number | null,
  minPreferredWellspacedTrees: number | null,
  minHorizontalDistanceWellspacedTrees: number | null,
  targetWellspacedTrees: number | null,
  minResidualBasalArea: number | null,
  minPostspacingDensity: number | null,
  maxPostspacingDensity: number | null,
  maxConiferous: number | null,
  heightRelativeToComp: number | null,
};

export type OpeningDetailsStockingDto = {
  stocking: OpeningDetailsStockingDetailsDto,
  preferredSpecies: OpeningDetailsStockingSpeciesDto[],
  acceptableSpecies: OpeningDetailsStockingSpeciesDto[],
  layer: OpeningDetailsStockingLayerDto | null,
  comments: CommentDto[],
};

export type OpeningTenureDto = {
    id: number,
    primaryTenure: boolean,
    fileId: string | null,
    cutBlock: string | null,
    cuttingPermit: string | null,
    timberMark: string | null,
    status: CodeDescriptionDto | null,
    plannedGrossArea: number | null,
    plannedNetArea: number | null
}

export type PaginatedTenureResponseDto<OpeningTenureDto> = PaginatedResponseType<OpeningTenureDto> & {
    primary: OpeningTenureDto | null;
};
