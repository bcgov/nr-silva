import { describe, it, expect } from "vitest";
import { sortValidatedTenures } from "../../utils/TenureUtils";

describe("TenureUtils", () => {
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
