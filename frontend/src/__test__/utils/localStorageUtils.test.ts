import { describe, it, expect, beforeEach, vi } from "vitest";
import {
  defaultSearchTableHeaders,
  loadUserPreference,
  saveUserPreference,
} from "@/utils/localStorageUtils";
import { UserPreference } from "@/types/PreferencesType";

describe("localStorageUtils", () => {
  beforeEach(() => {
    localStorage.clear();
  });

  it("should provide defaultSearchTableHeaders with correct structure", () => {
    expect(Array.isArray(defaultSearchTableHeaders)).toBe(true);
    expect(defaultSearchTableHeaders[0]).toHaveProperty("key");
    expect(defaultSearchTableHeaders[0]).toHaveProperty("header");
    expect(defaultSearchTableHeaders[0]).toHaveProperty("selected");
  });

  it("should return initialValue if nothing in localStorage", () => {
    const pref = loadUserPreference();
    expect(pref).toHaveProperty("theme");
    expect(pref).toHaveProperty("visibleColumns");
    expect(pref.visibleColumns.silvicultureSearch).toEqual(defaultSearchTableHeaders);
  });

  it("should load userPreference from localStorage if present", () => {
    const mockPref: UserPreference = {
      theme: "g10",
      visibleColumns: { silvicultureSearch: [{ key: "foo", header: "Foo", selected: true }] }
    };
    localStorage.setItem("userPreference", JSON.stringify(mockPref));
    const loaded = loadUserPreference();
    expect(loaded.theme).toBe("g10");
    expect(loaded.visibleColumns.silvicultureSearch[0].key).toBe("foo");
  });

  it("should save and merge userPreference correctly", () => {
    // Save initial
    saveUserPreference({ theme: "g10" });
    let loaded = loadUserPreference();
    expect(loaded.theme).toBe("g10");

    // Save again with visibleColumns
    saveUserPreference({
      visibleColumns: { silvicultureSearch: [{ key: "bar", header: "Bar", selected: false }] }
    });
    loaded = loadUserPreference();
    expect(loaded.theme).toBe("g10");
    expect(loaded.visibleColumns.silvicultureSearch[0].key).toBe("bar");
  });
});
