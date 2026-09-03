import { ApiError } from '@/services/OpenApi';
import type {
  OpeningDetailsTenureDto,
  TenureRequestDto,
  TenureUpdateItemDto,
} from '@/services/OpenApi';

export type EditTenureItem = TenureRequestDto & {
  cboaId?: number;
  revisionCount?: number;
};

/** Returns whether the editable list differs from the initially loaded CBOA snapshot. */
export const hasTenureChanges = (tenures: EditTenureItem[], initialTenures: EditTenureItem[]) =>
  JSON.stringify(tenures) !== JSON.stringify(initialTenures);

const tenureKey = (tenure: Pick<EditTenureItem, 'fileId' | 'cuttingPermit' | 'cutBlock'>) =>
  [tenure.fileId.trim(), tenure.cuttingPermit?.trim() || '', tenure.cutBlock.trim()].join('|');

/** Maps an opening tenure response to the editable form shape. */
export const toEditTenure = (tenure: OpeningDetailsTenureDto): EditTenureItem => ({
  cboaId: tenure.cboaId,
  revisionCount: tenure.revisionCount,
  fileId: tenure.fileId ?? '',
  cuttingPermit: tenure.cuttingPermit ?? '',
  cutBlock: tenure.cutBlock ?? '',
  isPrimary: tenure.primaryTenure,
});

/** Returns the API's useful error detail when it is available. */
export const getApiErrorMessage = (error: unknown, fallback: string) => {
  if (!(error instanceof ApiError)) return fallback;
  const body = error.body as { detail?: string; message?: string } | undefined;
  return body?.detail ?? body?.message ?? fallback;
};

/**
 * Creates the update payload, restoring CBOA identity only for a final tenure
 * whose normalized key still matches an initially associated tenure.
 */
export const buildTenureUpdatePayload = (
  tenures: EditTenureItem[],
  initialTenures: EditTenureItem[]
): TenureUpdateItemDto[] => {
  const originalsByKey = new Map(initialTenures.map((tenure) => [tenureKey(tenure), tenure]));
  return tenures.map((tenure) => {
    const original = originalsByKey.get(tenureKey(tenure));
    return {
      cboaId: original?.cboaId,
      revisionCount: original?.revisionCount,
      fileId: tenure.fileId.trim(),
      cuttingPermit: tenure.cuttingPermit?.trim() || undefined,
      cutBlock: tenure.cutBlock.trim(),
      isPrimary: tenure.isPrimary,
    };
  });
};
