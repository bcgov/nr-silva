import { OpeningTenureDto } from "@/types/OpeningTypes";


/**
 * Formats the primary tenure information into a dot-separated string,
 * skipping null, undefined, and empty string values, but keeping `0`.
 *
 * @param primary - The primary tenure object containing fileId, cutBlock, cuttingPermit, and timberMark.
 * @returns A formatted string with available parts separated by a dot (•).
 */
export function formatPrimaryTenureLabel(primary: OpeningTenureDto): string {
  const parts = [
    primary.fileId,
    primary.cutBlock,
    primary.cuttingPermit,
    primary.timberMark,
  ];

  return `Primary tenure: ${parts
    .filter((item) => item !== null && item !== undefined && item !== "")
    .join(" • ")}`;
}
