import FormInputType from "@/types/FormInputType";
import { CodeDescriptionDto } from "@/services/OpenApi";

export type TenureInfoDto = {
  isPrimary: boolean;
  forestFileId: string;
  cutBlock: string;
  cuttingPermit: string;
  timberMark: string;
}

export type CreateOpeningFormType = {
  client?: FormInputType<string>;
  file?: FormInputType<File>;
  geojson?: FormInputType<GeoJSON.FeatureCollection>;
  isGeoJsonMissing?: FormInputType<boolean>;
  orgUnit?: FormInputType<CodeDescriptionDto>;
  category?: FormInputType<CodeDescriptionDto>;
  openingGrossArea?: FormInputType<number>;
  maxAllowablePermAccess?: FormInputType<number>;
  tenureInfo?: FormInputType<TenureInfoDto[]>;
}
