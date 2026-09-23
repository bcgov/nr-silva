import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import {
  hasActiveFilters,
  serializeOpeningSearchParams,
  deserializeOpeningSearchParams,
  updateOpeningSearchUrlParams,
  readOpeningSearchUrlParams,
} from '../../utils/OpeningSearchParamsUtils';
import { OpeningSearchParamsType } from '@/types/OpeningTypes';

describe('OpeningSearchParamsUtils', () => {
  describe('hasActiveFilters', () => {
    it('returns false when params is undefined', () => {
      expect(hasActiveFilters(undefined)).toBe(false);
    });

    it('returns false when only pagination (page, size) are provided', () => {
      expect(hasActiveFilters({ page: 0, size: 10 })).toBe(false);
      expect(hasActiveFilters({ page: 1, size: 20 })).toBe(false);
    });

    it('returns false when filter values are empty strings, null, or undefined', () => {
      expect(
        hasActiveFilters({
          page: 0,
          size: 10,
          licenseNumber: '',
          openingId: undefined,
        })
      ).toBe(false);
    });

    it('returns true when any non-pagination filter has a value', () => {
      expect(hasActiveFilters({ openingId: 12345 })).toBe(true);
      expect(hasActiveFilters({ page: 0, size: 10, licenseNumber: 'A123' })).toBe(true);
      expect(hasActiveFilters({ categories: ['CAT1'] })).toBe(true);
      expect(hasActiveFilters({ isCreatedByUser: true })).toBe(true);
    });

    it('returns false when array filters are empty', () => {
      expect(hasActiveFilters({ categories: [], openingStatuses: [], orgUnits: [] })).toBe(false);
    });
  });

  describe('serializeOpeningSearchParams', () => {
    it('returns empty URLSearchParams when params is undefined', () => {
      const searchParams = serializeOpeningSearchParams(undefined);
      expect(searchParams.toString()).toBe('');
    });

    it('omits undefined, null, and empty string values', () => {
      const searchParams = serializeOpeningSearchParams({
        openingId: 100,
        licenseNumber: '',
        cutBlockId: undefined,
      });
      expect(searchParams.get('openingId')).toBe('100');
      expect(searchParams.has('licenseNumber')).toBe(false);
      expect(searchParams.has('cutBlockId')).toBe(false);
    });

    it('appends multiple values for array fields', () => {
      const searchParams = serializeOpeningSearchParams({
        categories: ['CAT1', 'CAT2'],
        orgUnits: ['ORG1'],
      });
      expect(searchParams.getAll('categories')).toEqual(['CAT1', 'CAT2']);
      expect(searchParams.getAll('orgUnits')).toEqual(['ORG1']);
    });

    it('handles booleans and number fields properly', () => {
      const searchParams = serializeOpeningSearchParams({
        isCreatedByUser: true,
        submittedToFrpa: false,
        page: 2,
        size: 50,
      });
      expect(searchParams.get('isCreatedByUser')).toBe('true');
      expect(searchParams.get('submittedToFrpa')).toBe('false');
      expect(searchParams.get('page')).toBe('2');
      expect(searchParams.get('size')).toBe('50');
    });
  });

  describe('deserializeOpeningSearchParams', () => {
    it('deserializes arrays, booleans, numbers, and strings correctly', () => {
      const urlParams = new URLSearchParams();
      urlParams.append('categories', 'CAT1');
      urlParams.append('categories', 'CAT2');
      urlParams.append('openingStatuses', 'OPEN');
      urlParams.append('isCreatedByUser', 'true');
      urlParams.append('submittedToFrpa', 'false');
      urlParams.append('openingId', '10001');
      urlParams.append('page', '3');
      urlParams.append('size', '20');
      urlParams.append('licenseNumber', 'LIC123');

      const result = deserializeOpeningSearchParams(urlParams);

      expect(result.categories).toEqual(['CAT1', 'CAT2']);
      expect(result.openingStatuses).toEqual(['OPEN']);
      expect(result.isCreatedByUser).toBe(true);
      expect(result.submittedToFrpa).toBe(false);
      expect(result.openingId).toBe(10001);
      expect(result.page).toBe(3);
      expect(result.size).toBe(20);
      expect(result.licenseNumber).toBe('LIC123');
    });

    it('returns empty object for empty URLSearchParams', () => {
      const result = deserializeOpeningSearchParams(new URLSearchParams(''));
      expect(result).toEqual({});
    });
  });

  describe('updateOpeningSearchUrlParams and readOpeningSearchUrlParams', () => {
    const originalLocation = window.location;
    let replaceStateSpy: ReturnType<typeof vi.fn>;

    beforeEach(() => {
      replaceStateSpy = vi.spyOn(window.history, 'replaceState').mockImplementation(() => {});
    });

    afterEach(() => {
      replaceStateSpy.mockRestore();
    });

    it('updateOpeningSearchUrlParams updates history with query string when params exist', () => {
      updateOpeningSearchUrlParams({ openingId: 999 });
      expect(replaceStateSpy).toHaveBeenCalledWith(
        {},
        '',
        expect.stringContaining('openingId=999')
      );
    });

    it('updateOpeningSearchUrlParams updates history with bare pathname when params are undefined or empty', () => {
      updateOpeningSearchUrlParams(undefined);
      expect(replaceStateSpy).toHaveBeenCalledWith({}, '', window.location.pathname);
    });

    it('readOpeningSearchUrlParams reads and deserializes search params from window.location.search', () => {
      const originalUrl = window.location.href;
      window.history.pushState({}, '', '/openings?openingId=555&licenseNumber=TEST');

      const params = readOpeningSearchUrlParams();
      expect(params.openingId).toBe(555);
      expect(params.licenseNumber).toBe('TEST');

      window.history.pushState({}, '', originalUrl);
    });
  });
});
