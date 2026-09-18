import { describe, expect, it, vi } from 'vitest';
import {
  getArrayParam,
  getBooleanParam,
  getMultiSelectPlaceholderHelper,
  getNumericParam,
  getStringParam,
  handleMultiSelectChangeHelper,
  hasActiveSearchFilters,
  replaceWindowUrl,
} from '@/utils/SearchUtils';
import { formatDatePickerDate } from '@/utils/DateUtils';

describe('SearchUtils', () => {
  describe('hasActiveSearchFilters', () => {
    it('returns false when params is undefined', () => {
      expect(hasActiveSearchFilters(undefined)).toBe(false);
    });

    it('returns false when only pagination/sort keys are present', () => {
      expect(hasActiveSearchFilters({ page: 0, size: 20, sort: 'name' })).toBe(false);
    });

    it('returns false when values are empty strings or empty arrays', () => {
      expect(hasActiveSearchFilters({ keyword: '', items: [] })).toBe(false);
    });

    it('returns true when non-excluded filter has value', () => {
      expect(hasActiveSearchFilters({ keyword: 'test' })).toBe(true);
      expect(hasActiveSearchFilters({ items: ['a'] })).toBe(true);
      expect(hasActiveSearchFilters({ count: 0 })).toBe(true);
      expect(hasActiveSearchFilters({ active: false })).toBe(true);
    });

    it('honors custom excludeKeys', () => {
      expect(hasActiveSearchFilters({ custom: 'val' }, ['custom'])).toBe(false);
    });
  });

  describe('getMultiSelectPlaceholderHelper', () => {
    it('returns default text when values are undefined or empty', () => {
      expect(getMultiSelectPlaceholderHelper(undefined)).toBe('Choose one or more options');
      expect(getMultiSelectPlaceholderHelper([])).toBe('Choose one or more options');
      expect(getMultiSelectPlaceholderHelper([], 'Custom default')).toBe('Custom default');
    });

    it('returns comma-separated string when values are present', () => {
      expect(getMultiSelectPlaceholderHelper(['A', 'B'])).toBe('A, B');
    });
  });

  describe('handleMultiSelectChangeHelper', () => {
    it('calls onChange with codes when items selected', () => {
      const mockChange = vi.fn();
      const handler = handleMultiSelectChangeHelper('category', mockChange);

      handler({
        selectedItems: [
          { code: 'CAT1', description: 'Cat 1' },
          { code: 'CAT2', description: 'Cat 2' },
        ],
      });

      expect(mockChange).toHaveBeenCalledWith('category', ['CAT1', 'CAT2']);
    });

    it('calls onChange with undefined when items empty', () => {
      const mockChange = vi.fn();
      const handler = handleMultiSelectChangeHelper('category', mockChange);

      handler({ selectedItems: [] });

      expect(mockChange).toHaveBeenCalledWith('category', undefined);
    });
  });

  describe('URL param extractors', () => {
    const params = new URLSearchParams(
      '?num=123&invalidNum=abc&boolTrue=true&boolFalse=false&arr=x&arr=y&str=hello'
    );

    it('getNumericParam parses finite numbers or returns undefined', () => {
      expect(getNumericParam(params, 'num')).toBe(123);
      expect(getNumericParam(params, 'invalidNum')).toBeUndefined();
      expect(getNumericParam(params, 'missing')).toBeUndefined();
    });

    it('getBooleanParam parses booleans or returns undefined', () => {
      expect(getBooleanParam(params, 'boolTrue')).toBe(true);
      expect(getBooleanParam(params, 'boolFalse')).toBe(false);
      expect(getBooleanParam(params, 'missing')).toBeUndefined();
    });

    it('getArrayParam extracts multiple values or returns undefined', () => {
      expect(getArrayParam(params, 'arr')).toEqual(['x', 'y']);
      expect(getArrayParam(params, 'missing')).toBeUndefined();
    });

    it('getStringParam extracts string or returns undefined', () => {
      expect(getStringParam(params, 'str')).toBe('hello');
      expect(getStringParam(params, 'missing')).toBeUndefined();
    });
  });

  describe('replaceWindowUrl', () => {
    it('replaces window url with query string', () => {
      const replaceSpy = vi.spyOn(window.history, 'replaceState').mockImplementation(() => {});

      const params = new URLSearchParams('foo=bar');
      replaceWindowUrl(params);

      expect(replaceSpy).toHaveBeenCalledWith({}, '', expect.stringContaining('foo=bar'));

      replaceWindowUrl(undefined);
      expect(replaceSpy).toHaveBeenCalledWith({}, '', window.location.pathname);

      replaceSpy.mockRestore();
    });
  });

  describe('formatDatePickerDate', () => {
    it('returns undefined when dates array is undefined, empty, or has no valid first element', () => {
      expect(formatDatePickerDate(undefined)).toBeUndefined();
      expect(formatDatePickerDate([])).toBeUndefined();
      expect(formatDatePickerDate([undefined as any])).toBeUndefined();
    });

    it('formats valid Date to API_DATE_FORMAT (YYYY-MM-DD)', () => {
      const date = new Date(2024, 0, 15);
      expect(formatDatePickerDate([date])).toBe('2024-01-15');
    });
  });
});
