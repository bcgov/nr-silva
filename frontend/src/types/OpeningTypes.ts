import { CodeDescriptionDto, OpeningActivityBaseDto } from "@/services/OpenApi";
import { GOV_FUNDED_OPENING, TENURED_OPENING } from "@/constants";

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

export type OpeningTypes = typeof TENURED_OPENING | typeof GOV_FUNDED_OPENING;

export type OpeningSearchParamsType = {
  openingId?: number,
  categories?: Array<string>,
  openingStatuses?: Array<string>,
  licenseNumber?: string,
  licenseeOpeningId?: string,
  entryDateStart?: string,
  entryDateEnd?: string,
  cutBlockId?: string,
  cuttingPermitId?: string,
  timberMark?: string,
  orgUnits?: Array<string>,
  clientNumbers?: Array<string>,
  isCreatedByUser?: boolean,
  submittedToFrpa?: boolean,
  mapsheetGrid?: string,
  mapsheetLetter?: string,
  mapsheetSquare?: string,
  mapsheetQuad?: string,
  mapsheetSubQuad?: string,
  openingNumber?: string,
  page?: number,
  size?: number,
  sort?: Array<string>,
}
