import { describe, it, expect } from "vitest";
import { getClientLabel, getClientLocationLabel, sortLocationOptions } from "../../utils/ForestClientUtils";
import { CodeDescriptionDto, ForestClientAutocompleteResultDto } from "../../services/OpenApi";

describe("getClientLabel", () => {
  it("should return a properly formatted label with name, id, and acronym", () => {
    const client: ForestClientAutocompleteResultDto = {
      name: "Forest Corp",
      id: "12345",
      acronym: "FC",
    };

    expect(getClientLabel(client)).toBe("Forest Corp, 12345, FC");
  });

  it("should omit empty values", () => {
    const client: ForestClientAutocompleteResultDto = {
      name: "Forest Corp",
      id: "",
      acronym: "FC",
    };

    expect(getClientLabel(client)).toBe("Forest Corp, FC");
  });

  it("should return an empty string when client is null", () => {
    expect(getClientLabel(null)).toBe("");
  });

  it("should return an empty string when client is undefined", () => {
    expect(getClientLabel(undefined)).toBe("");
  });

  it("should handle missing properties", () => {
    const client: Partial<ForestClientAutocompleteResultDto> = {
      name: "Forest Corp",
    };

    expect(getClientLabel(client as ForestClientAutocompleteResultDto)).toBe("Forest Corp");
  });
});

describe("getClientLocationLabel", () => {
  it("should return a properly formatted label with code and description", () => {
    const location: CodeDescriptionDto = {
      code: "LOC123",
      description: "Forest Region",
    };

    expect(getClientLocationLabel(location)).toBe("LOC123 - Forest Region");
  });

  it("should omit empty values", () => {
    const location: CodeDescriptionDto = {
      code: "LOC123",
      description: "",
    };

    expect(getClientLocationLabel(location)).toBe("LOC123");
  });

  it("should return an empty string when location is null", () => {
    expect(getClientLocationLabel(null)).toBe("");
  });

  it("should return an empty string when location is undefined", () => {
    expect(getClientLocationLabel(undefined)).toBe("");
  });

  it("should handle missing properties", () => {
    const location: Partial<CodeDescriptionDto> = {
      description: "Forest Region",
    };

    expect(getClientLocationLabel(location as CodeDescriptionDto)).toBe("Forest Region");
  });
});

describe("sortLocationOptions", () => {
  it("should sort fully numeric codes numerically and alphanumeric codes alphabetically", () => {
    const locations: CodeDescriptionDto[] = [
      { code: "12A", description: "Twelve A" },
      { code: "12", description: "Twelve" },
      { code: "2", description: "Two" },
      { code: "10", description: "Ten" },
      { code: "A1", description: "A One" },
      { code: "01", description: "Zero One" },
    ];

    const sorted = sortLocationOptions(locations);
    const sortedIds = sorted.map((option) => option.id);

    expect(sortedIds).toEqual(["01", "2", "10", "12", "12A", "A1"]);
  });

  it("should place numeric codes before alphabetic-only values", () => {
    const locations: CodeDescriptionDto[] = [
      { code: "B2", description: "Bee Two" },
      { code: "3", description: "Three" },
      { code: "1", description: "One" },
      { code: "C", description: "See" },
    ];

    const sorted = sortLocationOptions(locations);
    const sortedIds = sorted.map((option) => option.id);

    expect(sortedIds).toEqual(["1", "3", "B2", "C"]);
  });
});
