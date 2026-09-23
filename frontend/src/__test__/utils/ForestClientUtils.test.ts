import { describe, it, expect } from "vitest";
import {
  getClientLabel,
  getClientLocationLabel,
  sortLocationOptions,
  formatForestClient,
  getClientSimpleLabel,
  getClientNameAcronym,
} from "../../utils/ForestClientUtils";
import { PLACE_HOLDER } from "../../constants";
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

  it("should return PLACE_HOLDER when returnPlaceHolder is true and client is empty or null", () => {
    expect(getClientLabel(null, true)).toBe(PLACE_HOLDER);
    expect(getClientLabel(undefined, true)).toBe(PLACE_HOLDER);
    expect(getClientLabel({ name: "", id: "", acronym: "" }, true)).toBe(PLACE_HOLDER);
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

  it("should return an empty string when code and description are empty", () => {
    expect(getClientLocationLabel({ code: "", description: "" })).toBe("");
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

  it("handles null or undefined locations gracefully", () => {
    expect(sortLocationOptions(null)).toEqual([]);
    expect(sortLocationOptions(undefined)).toEqual([]);
  });

  it("handles decimal numbers and multiple dots in codes", () => {
    const locations: CodeDescriptionDto[] = [
      { code: "1.2", description: "One point two" },
      { code: "1.1", description: "One point one" },
      { code: "1.1.1", description: "Multiple dots" },
      { code: ".", description: "Just dot" },
    ];

    const sorted = sortLocationOptions(locations);
    const sortedIds = sorted.map((option) => option.id);

    expect(sortedIds).toEqual(["1.1", "1.2", ".", "1.1.1"]);
  });
});

describe("formatForestClient", () => {
  it("returns empty string when client is null or undefined", () => {
    expect(formatForestClient(null)).toBe("");
    expect(formatForestClient(undefined)).toBe("");
  });

  it("formats ACRONYM - Client Name when both exist", () => {
    expect(
      formatForestClient({ acronym: "TEST", clientName: "Test Client" })
    ).toBe("TEST - Test Client");
  });

  it("returns only acronym or clientName when one is missing", () => {
    expect(formatForestClient({ acronym: "TEST" })).toBe("TEST");
    expect(formatForestClient({ clientName: "Test Client" })).toBe("Test Client");
    expect(formatForestClient({})).toBe("");
  });
});

describe("getClientSimpleLabel", () => {
  it("prefers acronym if available", () => {
    expect(
      getClientSimpleLabel({
        acronym: "TEST",
        name: "Test Client Name",
        id: "00000001",
      })
    ).toBe("TEST");
  });

  it("falls back to generated acronym from name", () => {
    expect(
      getClientSimpleLabel({
        name: "TEST CLIENT FOREST PARTNERSHIP",
        id: "00000001",
      })
    ).toBe("TCFP");
  });

  it("falls back to id when no acronym and no name", () => {
    expect(getClientSimpleLabel({ id: "00000001" })).toBe("00000001");
  });

  it("returns PLACE_HOLDER when client is empty or null", () => {
    expect(getClientSimpleLabel(null)).toBe(PLACE_HOLDER);
    expect(getClientSimpleLabel(undefined)).toBe(PLACE_HOLDER);
    expect(getClientSimpleLabel({})).toBe(PLACE_HOLDER);
  });
});

describe("getClientNameAcronym", () => {
  it("returns acronym of multi-word client names", () => {
    expect(getClientNameAcronym("TEST CLIENT FORESTRY LTD.")).toBe("TCFL");
  });

  it("returns empty string for empty, null, or undefined clientName", () => {
    expect(getClientNameAcronym("")).toBe("");
    expect(getClientNameAcronym(null)).toBe("");
    expect(getClientNameAcronym(undefined)).toBe("");
  });
});

