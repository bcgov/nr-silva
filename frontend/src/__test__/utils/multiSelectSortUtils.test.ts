import { describe, it, expect } from "vitest";
import {
  codeDescriptionToDisplayText,
  extractCodesFromCodeDescriptionArr,
  filterCodeDescriptionItems,
} from "../../utils/multiSelectUtils";
import { SelectableCodeDescriptionDto } from "../../utils/multiSelectUtils";

describe("multiSelectUtils", () => {
  describe("codeDescriptionToDisplayText", () => {
    it("should format CodeDescriptionDto correctly", () => {
      const input = { code: "ABC", description: "Example Description" };
      expect(codeDescriptionToDisplayText(input)).toBe("ABC - Example Description");
    });

    it("should return an empty string if input is null", () => {
      expect(codeDescriptionToDisplayText(null)).toBe("");
    });

    it("should return an empty string if input is undefined", () => {
      expect(codeDescriptionToDisplayText(undefined)).toBe("");
    });
  });

  describe("extractCodesFromCodeDescriptionArr", () => {
    it("should extract codes from CodeDescriptionDto array", () => {
      const input = [
        { code: "A1", description: "First" },
        { code: "B2", description: "Second" },
      ];
      expect(extractCodesFromCodeDescriptionArr(input)).toEqual(["A1", "B2"]);
    });

    it("should return an empty array for an empty input array", () => {
      expect(extractCodesFromCodeDescriptionArr([])).toEqual([]);
    });
  });

  describe("filterCodeDescriptionItems", () => {
    const itemToString = (item: SelectableCodeDescriptionDto | null | undefined) =>
      item ? `${item.code} - ${item.description}` : "";

    it("should filter items based on input value", () => {
      const items = [
        { code: "A1", description: "Apple" },
        { code: "B2", description: "Banana" },
        { code: "C3", description: "Cherry" },
      ];

      const filtered = filterCodeDescriptionItems(items, { inputValue: "Ban", itemToString });

      expect(filtered).toEqual([{ code: "B2", description: "Banana" }]);
    });

    it("should return all items if input value is empty", () => {
      const items = [
        { code: "A1", description: "Apple" },
        { code: "B2", description: "Banana" },
        { code: "C3", description: "Cherry" },
      ];

      const filtered = filterCodeDescriptionItems(items, { inputValue: "", itemToString });

      expect(filtered).toEqual(items);
    });

    it("should return an empty array if no match is found", () => {
      const items = [
        { code: "A1", description: "Apple" },
        { code: "B2", description: "Banana" },
        { code: "C3", description: "Cherry" },
      ];

      const filtered = filterCodeDescriptionItems(items, { inputValue: "Orange", itemToString });

      expect(filtered).toEqual([]);
    });

    it("should return an empty array if input array is empty", () => {
      const filtered = filterCodeDescriptionItems([], { inputValue: "A", itemToString });
      expect(filtered).toEqual([]);
    });
  });
});
