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

export const forestFileId = 'tenure-forest-file-id-input' as const;
export const cutBlockId = 'tenure-cut-block-input' as const;
export const cuttingPermitId = 'tenure-cutting-permit-input' as const;


export const getNewTenureForm = (): TenureInfoDto => ({
  displayId: crypto.randomUUID(),
  isPrimary: false,
  forestFileId: {
    id: forestFileId
  },
  cutBlock: {
    id: cutBlockId
  },
  cuttingPermit: {
    id: cuttingPermitId
  },
  timberMark: {
    id: 'tenure-form-timber-input'
  },
});
