import { OpeningSearchResponseDto } from "../../types/OpeningTypes";

export type OpendingHeaderKeyType = keyof OpeningSearchResponseDto | 'actions';

export type OpeningHeaderType = {
  key: OpendingHeaderKeyType;
  header: string;
}

export type OpeningSearchDisplayType = {
  openingId: number;
  openingNumber: string;
  category: string;
  status: string;
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
};
