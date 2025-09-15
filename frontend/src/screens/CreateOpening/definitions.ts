import FormInputType from "@/types/FormInputType";
import { CodeDescriptionDto } from "@/services/OpenApi";

export type TenureInfoDto = {
  displayId: string;
  isPrimary: boolean;
  forestFileId: FormInputType<string>;
  cutBlock: FormInputType<string>;
  cuttingPermit: FormInputType<string>;
  timberMark: FormInputType<string>;
}

export type CreateOpeningFormType = {
  client?: FormInputType<string>;
  file?: FormInputType<File>;
  geojson?: FormInputType<GeoJSON.FeatureCollection>;
  isGeoJsonMissing?: FormInputType<boolean>;
  orgUnit: FormInputType<CodeDescriptionDto>;
  category: FormInputType<CodeDescriptionDto>;
  openingGrossArea: FormInputType<string>;
  maxAllowablePermAccess: FormInputType<string>;
  tenureInfo: FormInputType<TenureInfoDto[]>;
}
