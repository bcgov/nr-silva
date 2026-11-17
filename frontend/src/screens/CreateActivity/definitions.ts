import type { ComponentType, Dispatch, SetStateAction } from "react";
import type { CreateActivityRequestDto } from "@/services/OpenApi";
import FormInputType from "@/types/FormInputType";
import { ActivitySpatialUploadResponseDto } from "@/services/OpenApi";

export type ActivityStatusValue = 'PLANNED' | 'COMPLETED';

export type ActivityFormType = {
  status?: FormInputType<ActivityStatusValue>;
  baseCode?: FormInputType<string>;
  techniqueCode?: FormInputType<string>;
  methodCode?: FormInputType<string>;
  objectiveCodes?: FormInputType<string[]>;
  fundingSourceCodes?: FormInputType<string[]>;
  projectNumber?: FormInputType<string>;
  licenseeActivityId?: FormInputType<string>;
  comment?: FormInputType<string>;
  plannedDate?: FormInputType<string>;
  plannedArea?: FormInputType<string>;
  plannedCost?: FormInputType<string>;
  completionDate?: FormInputType<string>;
  treatedArea?: FormInputType<string>;
  actualCost?: FormInputType<string>;
  targetPreparedSpots?: FormInputType<string>;
  surveyPlannedNumPlots?: FormInputType<string>;
  surveyActualNumPlots?: FormInputType<string>;
  surveyMinPlotsPerStratum?: FormInputType<string>;
  file?: FormInputType<File> & { validatedObj?: ActivitySpatialUploadResponseDto };
}

export type SpecFieldsProps = {
  form: ActivityFormType;
  setForm: Dispatch<SetStateAction<ActivityFormType>>;
};

/** Everything a base-specific "Activity specifications" section owns. */
export type SpecModule = {
  Fields: ComponentType<SpecFieldsProps>;
  /** Marks invalid inputs on the (cloned) form and returns whether all are valid. */
  validate: (form: ActivityFormType) => boolean;
  toPayload: (form: ActivityFormType) => Partial<CreateActivityRequestDto>;
  hasData: (form: ActivityFormType) => boolean;
};
