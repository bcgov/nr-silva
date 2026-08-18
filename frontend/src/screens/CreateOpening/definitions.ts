import FormInputType from "@/types/FormInputType";
import type { ExtractedGeoDataDto } from "@/services/OpenApi";

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
  locationCode?: FormInputType<string>;
  file?: FormInputType<File> & { validatedObj?: ExtractedGeoDataDto };
  orgUnit?: FormInputType<string>;
  category?: FormInputType<string>;
  licenseeOpeningId?: FormInputType<string>;
  openingGrossArea?: FormInputType<string>;
  maxAllowablePermAccess?: FormInputType<string>;
  tenureInfo?: FormInputType<TenureInfoDto[]>;
}
