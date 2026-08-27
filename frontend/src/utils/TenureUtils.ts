import type { TenureDto, TenureRequestDto } from "@/services/OpenApi";

export type TenureFieldErrors = { fileId?: boolean; cutBlock?: boolean };

/** Trims tenure input while preserving edit-only metadata on each item. */
export const normalizeTenures = <T extends Partial<TenureRequestDto>>(tenures: T[] = []): T[] =>
  tenures.map(
    (tenure) =>
      ({
        ...tenure,
        fileId: tenure.fileId?.trim() ?? '',
        cuttingPermit: tenure.cuttingPermit?.trim() ?? '',
        cutBlock: tenure.cutBlock?.trim() ?? '',
      }) as T
  );

/** Validates the shared required-field and primary-tenure rules used by tenure forms. */
export const validateTenureList = <T extends Partial<TenureRequestDto>>(tenures: T[] = []) => {
  const trimmed = normalizeTenures(tenures);
  const errors: TenureFieldErrors[] = trimmed.map((tenure) => ({
    fileId: !tenure.fileId,
    cutBlock: !tenure.cutBlock,
  }));

  return {
    isValid: !errors.some((error) => error.fileId || error.cutBlock),
    hasPrimary: trimmed.some((tenure) => tenure.isPrimary),
    errors,
    trimmed,
  };
};

/**
 * Sorts validated tenure records for display and review.
 *
 * The sorted order is:
 * 1. Primary tenure first
 * 2. Remaining tenures sorted by fileId, case-insensitively
 *
 * @param tenures - The array of tenure records returned from tenure validation
 * @returns A new sorted array of TenureDto objects
 */
export const sortValidatedTenures = (tenures?: TenureDto[]): TenureDto[] => {
  if (!tenures) {
    return [];
  }

  return [...tenures].sort((a, b) => {
    const aPrimary = a.isPrimary ? 0 : 1;
    const bPrimary = b.isPrimary ? 0 : 1;
    if (aPrimary !== bPrimary) {
      return aPrimary - bPrimary;
    }

    const aFileId = a.fileId?.toLowerCase() ?? "";
    const bFileId = b.fileId?.toLowerCase() ?? "";
    return aFileId.localeCompare(bFileId);
  });
};
