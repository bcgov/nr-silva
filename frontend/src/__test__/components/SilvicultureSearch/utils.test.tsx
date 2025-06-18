import { describe, it, expect } from "vitest";
import { hasAnyActiveFilters } from "../../../components/SilvicultureSearch/OpeningSearch/utils";
import { OpeningSearchFilterType } from "../../../components/SilvicultureSearch/OpeningSearch/definitions";
import { CodeDescriptionDto } from "../../../services/OpenApi";

describe("hasAnyActiveFilters", () => {
  const mockCodeDesc = (code: string, desc: string): CodeDescriptionDto => ({
    code,
    description: desc,
  });

  it("should return false when all filters are empty or undefined", () => {
    const filters: OpeningSearchFilterType = {};
    expect(hasAnyActiveFilters(filters)).toBe(false);
  });

  it("should return false when only dateType is set", () => {
    const filters: OpeningSearchFilterType = {
      dateType: { code: "update", description: "Update" },
    };
    expect(hasAnyActiveFilters(filters)).toBe(false);
  });

  it("should return true if an array filter has values", () => {
    const filters: OpeningSearchFilterType = {
      orgUnit: [mockCodeDesc("DAS", "District A")],
    };
    expect(hasAnyActiveFilters(filters)).toBe(true);
  });

  it("should return true if a string filter has non-empty value", () => {
    const filters: OpeningSearchFilterType = {
      mainSearchTerm: "test search",
    };
    expect(hasAnyActiveFilters(filters)).toBe(true);
  });

  it("should return false if string filter is empty or whitespace", () => {
    expect(hasAnyActiveFilters({ mainSearchTerm: "" })).toBe(false);
    expect(hasAnyActiveFilters({ mainSearchTerm: "   " })).toBe(false);
  });

  it("should return true if a boolean filter is true", () => {
    expect(hasAnyActiveFilters({ myOpenings: true })).toBe(true);
    expect(hasAnyActiveFilters({ submittedToFrpa: true })).toBe(true);
  });

  it("should return false if boolean is false or undefined", () => {
    expect(hasAnyActiveFilters({ myOpenings: false })).toBe(false);
    expect(hasAnyActiveFilters({ submittedToFrpa: false })).toBe(false);
  });

  it("should return true if a date string is set", () => {
    const filters: OpeningSearchFilterType = {
      updateDateStart: "2024-01-01",
    };
    expect(hasAnyActiveFilters(filters)).toBe(true);
  });

  it("should return true if a number filter is present", () => {
    const filters: OpeningSearchFilterType = {
      page: 1,
    };
    expect(hasAnyActiveFilters(filters)).toBe(true);
  });

  it("should return true if multiple filters are present", () => {
    const filters: OpeningSearchFilterType = {
      statusList: [mockCodeDesc("APP", "Approved")],
      mainSearchTerm: "waldo",
      cuttingPermitId: "WA-001",
      myOpenings: true,
      dateType: { code: "update", description: "Update" },
    };
    expect(hasAnyActiveFilters(filters)).toBe(true);
  });

  it("should return false if all filters are explicitly empty", () => {
    const filters: OpeningSearchFilterType = {
      mainSearchTerm: "",
      statusList: [],
      orgUnit: [],
      myOpenings: false,
      submittedToFrpa: false,
      disturbanceDateStart: "",
      updateDateEnd: "",
      dateType: { code: "update", description: "Update" },
    };
    expect(hasAnyActiveFilters(filters)).toBe(false);
  });
});
