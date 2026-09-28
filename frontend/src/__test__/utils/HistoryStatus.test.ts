import { describe, expect, it } from "vitest";
import statusClass from "../../utils/HistoryStatus";

describe("HistoryStatus statusClass", () => {
  it("handles complete status", () => {
    expect(statusClass("complete")).toEqual({
      complete: true,
      current: false,
      invalid: false,
      disabled: false,
    });
  });

  it("handles current status", () => {
    expect(statusClass("current")).toEqual({
      complete: false,
      current: true,
      invalid: false,
      disabled: false,
    });
  });

  it("handles invalid status", () => {
    expect(statusClass("invalid")).toEqual({
      complete: false,
      current: false,
      invalid: true,
      disabled: false,
    });
  });

  it("handles disabled status", () => {
    expect(statusClass("disabled")).toEqual({
      complete: false,
      current: false,
      invalid: false,
      disabled: true,
    });
  });

  it("handles unknown or default status", () => {
    expect(statusClass("unknown")).toEqual({
      complete: false,
      current: false,
      invalid: false,
      disabled: false,
    });
  });
});
