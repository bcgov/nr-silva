import { TableHeaderType } from "@/types/TableHeader";
import { TenureRequestDto } from "@/services/OpenApi";

export const TenureHeaderConfig: TableHeaderType<keyof TenureRequestDto | 'timberMark'>[] = [
  {
    key: 'fileId',
    header: 'File ID'
  },
  {
    key: 'cuttingPermit',
    header: 'Cutting permit'
  },
  {
    key: 'cutBlock',
    header: 'Cut block'
  },
  {
    key: 'timberMark',
    header: 'Timber mark'
  },
];
