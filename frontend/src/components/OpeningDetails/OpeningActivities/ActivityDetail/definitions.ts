import { CodeDescriptionDto } from "@/services/OpenApi";
import {
  OpeningActivityDetail,
} from "@/types/OpeningTypes";

export type ActivityDetailProps = {
  activityDetail?: OpeningActivityDetail;
  base?: CodeDescriptionDto;
  isPlanning?: boolean;
  isComplex?: boolean;
  isLoading?: boolean;
};
