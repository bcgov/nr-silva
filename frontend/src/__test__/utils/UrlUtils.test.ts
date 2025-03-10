import { describe, it, expect } from "vitest";
import { buildQueryString } from "../../utils/UrlUtils";

describe("buildQueryString", () => {
  it("should build query string from flat key-value pairs", () => {
    const params = {
      page: 1,
      search: "tree",
      active: true,
    };

    const query = buildQueryString(params);
    expect(query).toBe("page=1&search=tree&active=true");
  });

  it("should omit keys with undefined or null values", () => {
    const params = {
      search: "forest",
      sort: undefined,
      filter: null,
    };

    const query = buildQueryString(params);
    expect(query).toBe("search=forest");
  });

  it("should convert arrays into comma-separated strings", () => {
    const params = {
      orgUnit: ["A", "B", "C"],
      status: ["Open", "Closed"],
    };

    const query = buildQueryString(params);
    expect(query).toBe("orgUnit=A%2CB%2CC&status=Open%2CClosed");
  });

  it("should skip empty arrays", () => {
    const params = {
      orgUnit: [],
      region: "North",
    };

    const query = buildQueryString(params);
    expect(query).toBe("region=North");
  });

  it("should handle mixed types and skip empty or falsy fields properly", () => {
    const params = {
      page: 1,
      dateType: "Update",
      updateDateStart: "2024-03-01",
      updateDateEnd: "2024-03-10",
      tags: [],
      extra: null,
      debug: undefined,
    };

    const query = buildQueryString(params);
    expect(query).toBe("page=1&dateType=Update&updateDateStart=2024-03-01&updateDateEnd=2024-03-10");
  });

  it("should return empty string if all values are null/undefined/empty arrays", () => {
    const params = {
      foo: undefined,
      bar: null,
      list: [],
    };

    const query = buildQueryString(params);
    expect(query).toBe("");
  });
});
