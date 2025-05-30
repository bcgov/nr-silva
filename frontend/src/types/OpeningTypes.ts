import { CodeDescriptionDto, OpeningActivityBaseDto } from "./OpenApiTypes";
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

export type OpeningActivityJuvelineDto = {
  targetIntertreeDistance: number | null,
  allowableVariationDistance: number | null,
  allowableTreePerLot: number | null,
  spacingPerHa: number | null,
};

export type OpeningActivityPruningDto = {
  totalStemsPerHa: number | null,
  stemsPerHaToPrune: number | null,
  targetIntertreeDistance: number | null,
  minimumIntertreeDistance: number | null,
  heightAboveGround: number | null,
  minimumLiveCrown: number | null,
};

export type OpeningActivitySitePrepDto = {
  targetSpot: number | null,
};

export type OpeningActivitySpeciesDetailsDto = {
  species: CodeDescriptionDto,
  plantedNumber: number | null,
  numberBeyondTransferLimit: number | null,
  cbst: boolean | null,
  requestId: number | null,
  lot: number | null,
  bidPricePerTree: number | null,
};

export type OpeningActivitySpeciesDto = {
  species: OpeningActivitySpeciesDetailsDto[],
};

export type OpeningActivitySurveyDto = {
  plotsCount: number | null,
  surveyMinPlotsPerStratum: number | null,
};

export type OpeningActivityDetail = OpeningActivityBaseDto &
  Partial<OpeningActivityJuvelineDto> &
  Partial<OpeningActivityPruningDto> &
  Partial<OpeningActivitySitePrepDto> &
  Partial<OpeningActivitySpeciesDto> &
  Partial<OpeningActivitySurveyDto>;

export type PaginatedPrimaryResponseDto<T> = PaginatedResponseType<T> & {
  primary: T | null;
  totalUnfiltered: number;
};
