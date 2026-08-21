import type { TenureDto } from "@/services/OpenApi";

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
