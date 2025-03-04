import CodeDescriptionDto from "./CodeDescriptionType";
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
