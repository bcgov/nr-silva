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
  file?: FormInputType<File> & { validatedObj?: ActivitySpatialUploadResponseDto };
}
