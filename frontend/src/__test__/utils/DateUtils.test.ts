import { describe, it, expect } from "vitest";
import { DateTime } from "luxon";
import {
  formatDate,
  dateStringToISO,
  formatDateForDatePicker,
  formatLocalDate,
  formatDateObjToString,
  getMonthAbbreviation,
  extractYearFromDateString,
  formatDateTime,
  isMidnight,
  getStartMaxDate,
  getEndMinDate,
  getDatePickerValue,
  formatDatePickerDate,
} from "../../utils/DateUtils";
import { PLACE_HOLDER, DATE_PICKER_FORMAT } from "../../constants";

describe("DateUtils", () => {
  describe("formatDate", () => {
    it("formats a valid YYYY-MM-DD date into long month format", () => {
      const formatted = formatDate("2024-05-15");
      expect(formatted).toContain("May");
      expect(formatted).toContain("2024");
      expect(formatted).toContain("15");
    });

    it("returns placeholder '--' when date is empty or null", () => {
      expect(formatDate("")).toBe("--");
      expect(formatDate(null as any)).toBe("--");
      expect(formatDate(undefined as any)).toBe("--");
    });
  });

  describe("dateStringToISO", () => {
    it("converts valid date string to ISO string", () => {
      const iso = dateStringToISO("2024-05-15T12:00:00Z");
      expect(iso).toBe("2024-05-15T12:00:00.000Z");
    });

    it("returns empty string when date is empty", () => {
      expect(dateStringToISO("")).toBe("");
      expect(dateStringToISO(null as any)).toBe("");
    });
  });

  describe("formatDateForDatePicker", () => {
    it("formats YYYY-MM-DD to MM/DD/YYYY", () => {
      expect(formatDateForDatePicker("2024-05-15")).toBe("05/15/2024");
    });

    it("returns empty string when date is falsy", () => {
      expect(formatDateForDatePicker("")).toBe("");
      expect(formatDateForDatePicker(null)).toBe("");
      expect(formatDateForDatePicker(undefined)).toBe("");
    });
  });

  describe("formatLocalDate", () => {
    it("formats ISO string to MMM dd, yyyy", () => {
      const formatted = formatLocalDate("2023-11-20T08:30:00");
      expect(formatted).toBe("Nov 20, 2023");
    });

    it("strips trailing Z from input before formatting", () => {
      const formatted = formatLocalDate("2023-11-20T08:30:00Z");
      expect(formatted).toBe("Nov 20, 2023");
    });

    it("returns empty string or PLACE_HOLDER when date is absent", () => {
      expect(formatLocalDate(null)).toBe("");
      expect(formatLocalDate(null, true)).toBe(PLACE_HOLDER);
      expect(formatLocalDate(undefined, true)).toBe(PLACE_HOLDER);
      expect(formatLocalDate(undefined, false)).toBe("");
    });
  });

  describe("formatDateObjToString", () => {
    it("formats valid Date object to yyyy-MM-dd", () => {
      const date = new Date(2024, 4, 15); // May 15, 2024
      expect(formatDateObjToString(date)).toBe("2024-05-15");
    });

    it("returns null for null, undefined, or invalid Date", () => {
      expect(formatDateObjToString(null)).toBeNull();
      expect(formatDateObjToString(new Date("invalid"))).toBeNull();
    });
  });

  describe("getMonthAbbreviation", () => {
    it("returns abbreviated month names for 1-12", () => {
      expect(getMonthAbbreviation(1)).toBe("Jan");
      expect(getMonthAbbreviation(6)).toBe("Jun");
      expect(getMonthAbbreviation(12)).toBe("Dec");
    });
  });

  describe("extractYearFromDateString", () => {
    it("extracts year from ISO date strings", () => {
      expect(extractYearFromDateString("2023-08-15T00:00:00Z")).toBe(2023);
    });

    it("extracts year from yyyy-MM-dd'Z' formatted strings", () => {
      expect(extractYearFromDateString("2005-05-02Z")).toBe(2005);
    });

    it("returns null for empty or invalid date strings", () => {
      expect(extractYearFromDateString("")).toBeNull();
      expect(extractYearFromDateString("not-a-date")).toBeNull();
    });
  });

  describe("formatDateTime", () => {
    it("formats ISO string with default format", () => {
      const result = formatDateTime("2024-05-15T14:30:00");
      expect(result).toContain("15/05/2024");
    });

    it("formats ISO string with custom format", () => {
      const result = formatDateTime("2024-05-15T14:30:00", "yyyy-MM-dd");
      expect(result).toBe("2024-05-15");
    });

    it("returns '--' for empty or invalid date", () => {
      expect(formatDateTime("")).toBe("--");
      expect(formatDateTime("invalid")).toBe("--");
    });
  });

  describe("isMidnight", () => {
    it("returns true when time is 00:00:00", () => {
      expect(isMidnight("2024-05-15T00:00:00")).toBe(true);
    });

    it("returns false when time is not midnight", () => {
      expect(isMidnight("2024-05-15T00:00:01")).toBe(false);
      expect(isMidnight("2024-05-15T14:30:00")).toBe(false);
    });

    it("returns false for invalid or empty input", () => {
      expect(isMidnight("")).toBe(false);
      expect(isMidnight("invalid")).toBe(false);
    });
  });

  describe("getStartMaxDate and getEndMinDate", () => {
    it("getStartMaxDate returns today's date formatted when no endDate given", () => {
      const result = getStartMaxDate();
      expect(result).toBe(DateTime.now().toFormat(DATE_PICKER_FORMAT));
    });

    it("getStartMaxDate returns formatted endDate when valid", () => {
      const result = getStartMaxDate("2024-05-15");
      expect(result).toBe("2024/05/15");
    });

    it("getStartMaxDate returns undefined when endDate is invalid", () => {
      expect(getStartMaxDate("invalid-date")).toBeUndefined();
    });

    it("getEndMinDate returns formatted startDate when valid", () => {
      const result = getEndMinDate("2024-05-15");
      expect(result).toBe("2024/05/15");
    });

    it("getEndMinDate returns undefined when startDate is absent or invalid", () => {
      expect(getEndMinDate()).toBeUndefined();
      expect(getEndMinDate("invalid-date")).toBeUndefined();
    });
  });

  describe("getDatePickerValue", () => {
    it("returns DATE_PICKER_FORMAT value when valid date provided", () => {
      expect(getDatePickerValue("2024-05-15")).toBe("2024/05/15");
    });

    it("returns undefined when date is absent or invalid", () => {
      expect(getDatePickerValue()).toBeUndefined();
      expect(getDatePickerValue("invalid")).toBeUndefined();
    });
  });

  describe("formatDatePickerDate", () => {
    it("formats the first selected Date object to API_DATE_FORMAT", () => {
      const date = new Date(2024, 4, 15);
      expect(formatDatePickerDate([date])).toBe("2024-05-15");
    });

    it("returns undefined when array is empty or undefined", () => {
      expect(formatDatePickerDate()).toBeUndefined();
      expect(formatDatePickerDate([])).toBeUndefined();
      expect(formatDatePickerDate([undefined as any])).toBeUndefined();
    });
  });
});
