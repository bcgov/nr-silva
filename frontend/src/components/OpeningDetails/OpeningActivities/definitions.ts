import CodeDescriptionDto from "@/types/CodeDescriptionType";
import { PaginatedResponseType } from "@/types/PaginationTypes";

export type MockedDisturbanceDetailType = {
  licenseeActivityId: string | null;
  disturbanceLocation: string | null;
  licenceNumber: string | null;
  cuttingPermit: string | null;
  cutBlock: string | null;
  comment: string | null;
}

export type MockedDisturbanceType = {
  activityId: number;
  disturbance: CodeDescriptionDto;
  silvicultureSystem: CodeDescriptionDto;
  variant: CodeDescriptionDto | null;
  cutPhase: CodeDescriptionDto;
  disturbanceArea: number;
  updateTimestamp: string;
  startDate: string | null;
  endDate: string | null;
} & MockedDisturbanceDetailType;


export type MockedBaseActivityDetail = {
  activityId: number;
  base: CodeDescriptionDto;
  licenseeActivityId: string | null;
  intraAgencyNumber: number | null;
  activityLocation: string | null;
  plannedAmount: number | null;
  treatedAmount: number | null;
  plannedCost: number | null;
  actualCost: number | null;
  comment: string | null;
}

export type MockedDirectSeedingSpecies = {
  speciesType: CodeDescriptionDto;
  numberPlanted: number;
  numberBeyondTransferLimit: number;
  cbst: boolean | null;
  lot: number;
}

export type MockedPlantingSpecies = MockedDirectSeedingSpecies & {
  requestId: number | null;
  bidPricePerTree: number | null;
}

// Base type = DS
export type MockedDirectSeedingDetail = {
  plantingSpecification: {
    totalPlanting: number;
    totalSpecies: number;
    species: MockedDirectSeedingSpecies[]
  }
}

// Base type = JS
export type MockedJuvenileSpacingDetail = {
  spacingSpecification: {
    targetInterTreeDistance: number | null;
    allowableVariationInterTreeDistance: number | null;
    allowableTreesPerPlot: number | null;
    spacingPerHa: number | null;
  }
}

// Base type = PL
export type MockedPlantingDetail = {
  plantingSpecification: {
    targetInterTreeDistance: number | null;
    allowableVariationInterTreeDistance: number | null;
    allowableTreesPerPlot: number | null;
    spacingPerHa: number | null;
  }
}

// Base type = PR
export type MockedPruningDetail = {
  pruningSpecification: {
    totalStemsPerHa: number | null;
    stemsperHaToPrune: number | null;
    targetInterTreeDistance: number | null;
    minInterTreeDistance: number | null;
    heightAboveGround: number | null;
    minLiveCrown: number | null;
  }
}

// Base type = SP
export type MockedSitePrepDetail = {
  sitePrepSpecification: {
    targetPreparedSpotPerHa: number | null;
  }
}

// Base type = SU
export type MockedSurveyDetail = {
  surveySpecification: {
    numberOfPlots: number | null;
    minPlotsPerStratum: number | null;
  }
}

export type MockedActivityDetailType =
  MockedBaseActivityDetail & Partial<(
    | MockedDirectSeedingDetail
    | MockedJuvenileSpacingDetail
    | MockedPlantingDetail
    | MockedPruningDetail
    | MockedSitePrepDetail
    | MockedSurveyDetail
  )>;

export type MockedActivityType = {
  activityId: number;
  status: CodeDescriptionDto;
  base: CodeDescriptionDto;
  tech: CodeDescriptionDto;
  method: CodeDescriptionDto;
  objective: CodeDescriptionDto;
  area: number | null;
  fundingSource: CodeDescriptionDto;
  projectId: number | null;
  updateTimestamp: string;
  plannedDate: string | null;
  endDate: string | null;
};


export type MockedActivityResponseType = PaginatedResponseType<MockedActivityType>;

export type ComplexActivityDetailProps = {
  activityDetail?: MockedActivityDetailType;
  isPlanning?: boolean;
  isComplex?: boolean;
  isLoading?: boolean;
};
