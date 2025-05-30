import { SortDirectionType } from "@/types/PaginationTypes";
import { OpeningDetailsActivitiesActivitiesDto } from "@/types/OpenApiTypes";

export type ActivityFilterType = {
  page: number;
  size: number;
  filter?: string;
  sortField?: keyof OpeningDetailsActivitiesActivitiesDto;
  sortDirection?: SortDirectionType;
};
