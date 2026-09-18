import { describe, expect, it, vi, beforeEach, afterEach } from 'vitest';

import {
  hasStandardsUnitSearchFilters,
  readStandardsUnitSearchUrlParams,
  updateStandardsUnitSearchUrlParams,
} from '@/components/StandardsUnitSearchSection/utils';

import {
  hasDisturbanceSearchFilters,
  readDisturbanceSearchUrlParams,
  updateDisturbanceSearchUrlParams,
} from '@/components/DisturbancesSearchSection/utils';

import {
  hasActivitySearchFilters,
  readActivitySearchUrlParams,
  updateActivitySearchUrlParams,
} from '@/components/ActivitySearchSection/utils';

import {
  hasStockingStandardsSearchFilters,
  readStockingStandardsSearchUrlParams,
  updateStockingStandardsSearchUrlParams,
} from '@/components/StockingStandardsSearchSection/utils';

import {
  hasForestCoverSearchFilters,
  readForestCoverSearchUrlParams,
  updateForestCoverSearchUrlParams,
} from '@/components/ForestCoverSearchSection/utils';

import {
  hasCommentSearchFilters,
  readCommentSearchUrlParams,
  updateCommentSearchUrlParams,
} from '@/components/CommentSearchSection/utils';

import {
  hasStockingStandardsCommentSearchFilters,
  readStockingStandardsCommentSearchUrlParams,
  updateStockingStandardsCommentSearchUrlParams,
} from '@/components/StockingStandardsCommentSearchSection/utils';

describe('Search Sections utils.ts Unit Tests', () => {
  let replaceSpy: any;

  beforeEach(() => {
    replaceSpy = vi.spyOn(window.history, 'replaceState').mockImplementation(() => {});
  });

  afterEach(() => {
    replaceSpy.mockRestore();
  });

  describe('StandardsUnitSearchSection utils', () => {
    it('handles hasStandardsUnitSearchFilters', () => {
      expect(hasStandardsUnitSearchFilters(undefined)).toBe(false);
      expect(hasStandardsUnitSearchFilters({ page: 0, size: 20 })).toBe(false);
      expect(hasStandardsUnitSearchFilters({ bgcZone: 'SBS' })).toBe(true);
    });

    it('reads and updates URL params', () => {
      const params = readStandardsUnitSearchUrlParams('?standardsRegimeId=123&bgcZone=SBS&preferredSpecies=PL');
      expect(params.standardsRegimeId).toBe(123);
      expect(params.bgcZone).toBe('SBS');
      expect(params.preferredSpecies).toEqual(['PL']);

      updateStandardsUnitSearchUrlParams({ standardsRegimeId: 456, bgcZone: 'ICH' });
      expect(replaceSpy).toHaveBeenCalledWith(
        {},
        '',
        expect.stringContaining('standardsRegimeId=456')
      );

      updateStandardsUnitSearchUrlParams(undefined);
      expect(replaceSpy).toHaveBeenCalledWith({}, '', window.location.pathname);
    });
  });

  describe('DisturbancesSearchSection utils', () => {
    it('handles hasDisturbanceSearchFilters', () => {
      expect(hasDisturbanceSearchFilters(undefined)).toBe(false);
      expect(hasDisturbanceSearchFilters({ fileId: 'TFL' } as any)).toBe(true);
    });

    it('reads and updates URL params', () => {
      const params = readDisturbanceSearchUrlParams('?fileId=TFL123&disturbances=HARV');
      expect(params.fileId).toBe('TFL123');
      expect(params.disturbances).toEqual(['HARV']);

      updateDisturbanceSearchUrlParams({ fileId: 'TFL999' });
      expect(replaceSpy).toHaveBeenCalledWith(
        {},
        '',
        expect.stringContaining('fileId=TFL999')
      );

      updateDisturbanceSearchUrlParams(undefined);
      expect(replaceSpy).toHaveBeenCalledWith({}, '', window.location.pathname);
    });
  });

  describe('ActivitySearchSection utils', () => {
    it('handles hasActivitySearchFilters', () => {
      expect(hasActivitySearchFilters(undefined)).toBe(false);
      expect(hasActivitySearchFilters({ page: 0, size: 10 } as any)).toBe(false);
      expect(hasActivitySearchFilters({ fileId: 'ACT123' } as any)).toBe(true);
    });

    it('reads and updates URL params', () => {
      const params = readActivitySearchUrlParams('?fileId=ACT123&isComplete=true&bases=BR');
      expect(params.fileId).toBe('ACT123');
      expect(params.isComplete).toBe(true);
      expect(params.bases).toEqual(['BR']);

      updateActivitySearchUrlParams({ fileId: 'ACT456', isComplete: false });
      expect(replaceSpy).toHaveBeenCalledWith(
        {},
        '',
        expect.stringContaining('fileId=ACT456')
      );

      updateActivitySearchUrlParams(undefined);
      expect(replaceSpy).toHaveBeenCalledWith({}, '', window.location.pathname);
    });
  });

  describe('StockingStandardsSearchSection utils', () => {
    it('handles hasStockingStandardsSearchFilters', () => {
      expect(hasStockingStandardsSearchFilters(undefined)).toBe(false);
      expect(hasStockingStandardsSearchFilters({ fspId: 'FSP1' } as any)).toBe(true);
    });

    it('reads and updates URL params', () => {
      const params = readStockingStandardsSearchUrlParams('?fspId=FSP123&defaultStandardsInd=true&standardsRegimeId=50');
      expect(params.fspId).toBe('FSP123');
      expect(params.defaultStandardsInd).toBe(true);
      expect(params.standardsRegimeId).toBe(50);

      updateStockingStandardsSearchUrlParams({ fspId: 'FSP999' });
      expect(replaceSpy).toHaveBeenCalledWith(
        {},
        '',
        expect.stringContaining('fspId=FSP999')
      );

      updateStockingStandardsSearchUrlParams(undefined);
      expect(replaceSpy).toHaveBeenCalledWith({}, '', window.location.pathname);
    });
  });

  describe('ForestCoverSearchSection utils', () => {
    it('handles hasForestCoverSearchFilters', () => {
      expect(hasForestCoverSearchFilters(undefined)).toBe(false);
      expect(hasForestCoverSearchFilters({ fileId: 'FC123' } as any)).toBe(true);
    });

    it('reads and updates URL params', () => {
      const params = readForestCoverSearchUrlParams('?fileId=FC123&openingId=789&stockingStatuses=IMM');
      expect(params.fileId).toBe('FC123');
      expect(params.openingId).toBe(789);
      expect(params.stockingStatuses).toEqual(['IMM']);

      updateForestCoverSearchUrlParams({ fileId: 'FC999' });
      expect(replaceSpy).toHaveBeenCalledWith(
        {},
        '',
        expect.stringContaining('fileId=FC999')
      );

      updateForestCoverSearchUrlParams(undefined);
      expect(replaceSpy).toHaveBeenCalledWith({}, '', window.location.pathname);
    });
  });

  describe('CommentSearchSection utils', () => {
    it('handles hasCommentSearchFilters', () => {
      expect(hasCommentSearchFilters(undefined)).toBe(false);
      expect(hasCommentSearchFilters({ page: 0, size: 10 })).toBe(false);
      expect(hasCommentSearchFilters({ searchTerm: 'keyword' })).toBe(true);
    });

    it('reads and updates URL params', () => {
      const params = readCommentSearchUrlParams('?searchTerm=seedling&commentLocation=OPN');
      expect(params.searchTerm).toBe('seedling');
      expect(params.commentLocation).toEqual(['OPN']);

      updateCommentSearchUrlParams({ searchTerm: 'planted' });
      expect(replaceSpy).toHaveBeenCalledWith(
        {},
        '',
        expect.stringContaining('searchTerm=planted')
      );

      updateCommentSearchUrlParams(undefined);
      expect(replaceSpy).toHaveBeenCalledWith({}, '', window.location.pathname);
    });
  });

  describe('StockingStandardsCommentSearchSection utils', () => {
    it('handles hasStockingStandardsCommentSearchFilters', () => {
      expect(hasStockingStandardsCommentSearchFilters(undefined)).toBe(false);
      expect(hasStockingStandardsCommentSearchFilters({ searchTerm: 'ss-comment' })).toBe(true);
    });

    it('reads and updates URL params', () => {
      const params = readStockingStandardsCommentSearchUrlParams('?searchTerm=regen&commentLocations=STD');
      expect(params.searchTerm).toBe('regen');
      expect(params.commentLocations).toEqual(['STD']);

      updateStockingStandardsCommentSearchUrlParams({ searchTerm: 'free-growing' });
      expect(replaceSpy).toHaveBeenCalledWith(
        {},
        '',
        expect.stringContaining('searchTerm=free-growing')
      );

      updateStockingStandardsCommentSearchUrlParams(undefined);
      expect(replaceSpy).toHaveBeenCalledWith({}, '', window.location.pathname);
    });
  });
});
