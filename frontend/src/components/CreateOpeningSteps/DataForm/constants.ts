import { TableHeaderType } from "@/types/TableHeader";
import { TenureInfoDto } from "@/screens/CreateOpening/definitions";

export const TenureHeaderConfig: TableHeaderType<keyof TenureInfoDto | 'action'>[] = [
  {
    key: 'isPrimary',
    header: 'Primary licence'
  },
  {
    key: 'forestFileId',
    header: 'Forest file ID'
  },
  {
    key: 'cutBlock',
    header: 'Cut block'
  },
  {
    key: 'cuttingPermit',
    header: 'Cutting permit'
  },
  {
    key: 'timberMark',
    header: 'Timber mark'
  },
  {
    key: 'action',
    header: 'Actions'
  },
];

export const getNewTenureForm = (): TenureInfoDto => ({
  displayId: crypto.randomUUID(),
  isPrimary: false,
  forestFileId: {
    id: 'tenure-forest-file-id-input'
  },
  cutBlock: {
    id: 'tenure-cut-block-input'
  },
  cuttingPermit: {
    id: 'tenure-cutting-permit-input'
  },
  timberMark: {
    id: 'tenure-form-timber-input'
  },
});
