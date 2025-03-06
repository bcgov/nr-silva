import { describe, it, expect } from "vitest";
import { getClientLabel, getClientLocationLabel } from "../../utils/ForestClientUtils";
import { ForestClientAutocomplete } from "../../services/OpeningClientLocationService";
import CodeDescriptionDto from "../../types/CodeDescriptionType";

describe("getClientLabel", () => {
  it("should return a properly formatted label with name, id, and acronym", () => {
    const client: ForestClientAutocomplete = {
      name: "Forest Corp",
      id: "12345",
      acronym: "FC",
    };

    expect(getClientLabel(client)).toBe("Forest Corp, 12345, FC");
  });

  it("should omit empty values", () => {
    const client: ForestClientAutocomplete = {
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
    const client: Partial<ForestClientAutocomplete> = {
      name: "Forest Corp",
    };

    expect(getClientLabel(client as ForestClientAutocomplete)).toBe("Forest Corp");
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
