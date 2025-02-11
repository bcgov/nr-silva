import CodeDescriptionDto from "./CodeDescriptionType";
import { PaginationMetaType } from "./PaginationTypes";

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

export interface IFreeGrowingProps {
  orgUnitCode: string;
  clientNumber: string;
  entryDateStart: string | null;
  entryDateEnd: string | null;
}

export interface IFreeGrowingChartData {
  group: string;
  value: number;
}

export interface OpeningSearchResponseDto {
  openingId: number;
  openingNumber: string;
  category: CodeDescriptionDto;
  status: CodeDescriptionDto;
  cuttingPermitId: number | null;
  timberMark: string | null;
  cutBlockId: number | null;
  openingGrossAreaHa: number | null;
  disturbanceStartDate: string | null;
  orgUnitCode: string;
  orgUnitName: string;
  clientNumber: string | null;
  clientAcronym: string | null;
  regenDelayDate: string;
  freeGrowingDate: string;
  updateTimestamp: string;
  entryUserId: string;
  submittedToFrpa: boolean;
  forestFileId: string | null;
  silvaReliefAppId: string | null;
  favourite: boolean;
}

export type PaginatedRecentOpeningsDto = PaginationMetaType & {
  data: OpeningSearchResponseDto[]
}

// TODO: too be removed
export interface OpeningsSearch {
  openingId: number;
  forestFileId: string | null;
  categoryCode: string | null;
  categoryDescription: string | null;
  statusCode: string;
  statusDescription: string;
  cuttingPermitId: string | null;
  cutBlockId: string | null;
  orgUnitName: string;
  updateTimestamp: string;
  favourite: boolean;
}

/**
 * Corresponds to the OrgUnitEntity in the Oracle backend.
 */
export interface OrgUnitEntity {
  orgUnitNo: number;
  orgUnitCode: string;
  orgUnitName: string;
  locationCode: string;
  areaCode: string;
  telephoneNo: string;
  orgLevelCode: string;
  officeNameCode: string;
  rollupRegionNo: number;
  rollupRegionCode: string;
  rollupDistNo: number;
  rollupDistCode: string;
  effectiveDate: string;
  expiryDate: string;
  updateTimestamp: string;
}


/**
 * Corresponds to the OpeningsPerYearDto in the Postgres backend.
 */
export type OpeningsPerYearDto = {
  month: number,
  year: number,
  monthName: string,
  amount: number,
  statusCounts: {
    [key: string]: number
  }
}
