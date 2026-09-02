import { describe, expect, it } from 'vitest';

import { normalizeTenures, sortValidatedTenures, validateTenureList } from '@/utils/TenureUtils';

describe("TenureUtils", () => {
  it('normalizes whitespace without discarding edit-only metadata', () => {
    expect(normalizeTenures([
      { cboaId: 1, fileId: ' F1 ', cuttingPermit: ' CP1 ', cutBlock: ' B1 ', isPrimary: true },
    ])).toEqual([
      { cboaId: 1, fileId: 'F1', cuttingPermit: 'CP1', cutBlock: 'B1', isPrimary: true },
    ]);
  });

  it('reports per-row required-field errors and separately identifies a missing primary tenure', () => {
    const validation = validateTenureList([
      { fileId: ' ', cuttingPermit: 'CP1', cutBlock: 'B1', isPrimary: false },
      { fileId: 'F2', cuttingPermit: '', cutBlock: ' ', isPrimary: false },
    ]);

    expect(validation).toMatchObject({ isValid: false, hasPrimary: false });
    expect(validation.errors).toEqual([
      { fileId: true, cutBlock: false },
      { fileId: false, cutBlock: true },
    ]);
  });

  it("returns an empty array when no tenures are provided", () => {
    expect(sortValidatedTenures(undefined)).toEqual([]);
  });

  it("places the primary tenure first and sorts the rest by fileId case-insensitively", () => {
    const tenures = [
      { fileId: "b-002", cutBlock: "B", isPrimary: false },
      { fileId: "A-001", cutBlock: "A", isPrimary: true },
      { fileId: "c-003", cutBlock: "C", isPrimary: false },
      { fileId: "a-100", cutBlock: "D", isPrimary: false },
    ];

    const sorted = sortValidatedTenures(tenures);

    expect(sorted).toEqual([
      { fileId: "A-001", cutBlock: "A", isPrimary: true },
      { fileId: "a-100", cutBlock: "D", isPrimary: false },
      { fileId: "b-002", cutBlock: "B", isPrimary: false },
      { fileId: "c-003", cutBlock: "C", isPrimary: false },
    ]);
  });

  it("sorts tenures by fileId case-insensitively when no primary tenure exists", () => {
    const tenures = [
      { fileId: "b-002", cutBlock: "B" },
      { fileId: "A-001", cutBlock: "A" },
      { fileId: "c-003", cutBlock: "C" },
      { fileId: "a-100", cutBlock: "D" },
    ];

    expect(sortValidatedTenures(tenures).map((tenure) => tenure.fileId)).toEqual([
      "A-001",
      "a-100",
      "b-002",
      "c-003",
    ]);
  });
});
