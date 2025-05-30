import { OpeningDetailsTenureDto } from "@/types/OpenApiTypes";
import { SortDirectionType } from "@/types/PaginationTypes";

export type TenureFilterType = {
  page: number,
  size: number,
  filter?: string,
  sortField?: keyof OpeningDetailsTenureDto,
  sortDirection?: SortDirectionType
}
