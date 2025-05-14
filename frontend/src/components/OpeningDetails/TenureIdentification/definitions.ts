import { OpeningTenureDto } from "@/types/OpeningTypes"
import { SortDirectionType } from "@/types/PaginationTypes"

export type TenureFilterType = {
  page: number,
  size: number,
  filter?: string,
  sortField?: keyof OpeningTenureDto,
  sortDirection?: SortDirectionType
}
