import { TableHeaderType } from "@/types/TableHeader";
import { UnmappedAreaDto } from "../../definitions";

export const UnmappedAreaHeaders: TableHeaderType<keyof UnmappedAreaDto>[] = [
  {
    key: 'unmappedAreaId',
    header: 'Unmapped area ID'
  },
  {
    key: 'area',
    header: 'Area'
  },
  {
    key: 'stockingStatus',
    header: 'Stocking status'
  },
  {
    key: 'stockingType',
    header: 'Stocking type'
  }
]
