import FormInputType from "@/types/FormInputType";
import type { ExtractedGeoDataDto, TenureRequestDto } from "@/services/OpenApi";

export type CreateOpeningFormType = {
  client?: FormInputType<string>;
  file?: FormInputType<File> & { validatedObj?: ExtractedGeoDataDto };
  orgUnit?: FormInputType<string>;
  category?: FormInputType<string>;
  licenseeOpeningId?: FormInputType<string>;
  openingGrossArea?: FormInputType<string>;
  maxAllowablePermAccess?: FormInputType<string>;
  tenureInfo?: FormInputType<TenureRequestDto[]>;
}
