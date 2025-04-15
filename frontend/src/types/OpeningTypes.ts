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
