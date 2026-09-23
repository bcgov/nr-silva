import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import {
  convertToCSV,
  downloadCSV,
  downloadXLSX,
  formatBytesDecimal,
} from '../../utils/FileUtils';

describe('FileUtils', () => {
  describe('convertToCSV', () => {
    it('converts headers and rows to CSV formatted string', () => {
      const headers = [
        { key: 'id', header: 'ID', selected: true },
        { key: 'name', header: 'Name', selected: true },
        { key: 'actions', header: 'Actions', selected: true }, // should be ignored
        { key: 'unselected', header: 'Unselected', selected: false }, // should be ignored
      ];

      const rows = [
        { id: 1, name: 'Alice, Smith', unselected: 'Skip' },
        { id: 2, name: 'Bob "The Builder"', unselected: 'Skip' },
      ];

      const csv = convertToCSV(headers, rows);
      const lines = csv.split('\n');

      expect(lines[0]).toBe('ID,Name');
      expect(lines[1]).toBe('1,"Alice, Smith"');
      expect(lines[2]).toBe('2,"Bob ""The Builder"""');
    });

    it('handles rows with numbers and null/undefined values', () => {
      const headers = [
        { key: 'id', header: 'ID', selected: true },
        { key: 'status', header: 'Status', selected: true },
      ];
      const rows = [{ id: 123, status: null }];

      const csv = convertToCSV(headers, rows);
      expect(csv).toBe('ID,Status\n123,');
    });
  });

  describe('downloadCSV and downloadXLSX', () => {
    let createObjectURLSpy: ReturnType<typeof vi.fn>;
    let revokeObjectURLSpy: ReturnType<typeof vi.fn>;
    let clickSpy: ReturnType<typeof vi.fn>;
    let appendChildSpy: ReturnType<typeof vi.spyOn>;
    let removeChildSpy: ReturnType<typeof vi.spyOn>;

    beforeEach(() => {
      createObjectURLSpy = vi.fn().mockReturnValue('blob:mock-url');
      revokeObjectURLSpy = vi.fn();
      window.URL.createObjectURL = createObjectURLSpy;
      window.URL.revokeObjectURL = revokeObjectURLSpy;
      clickSpy = vi.fn();
      vi.spyOn(document, 'createElement').mockImplementation((tagName: string) => {
        const el = document.createElementNS('http://www.w3.org/1999/xhtml', tagName);
        if (tagName === 'a') {
          el.click = clickSpy;
        }
        return el;
      });
      appendChildSpy = vi.spyOn(document.body, 'appendChild');
      removeChildSpy = vi.spyOn(document.body, 'removeChild');
    });

    afterEach(() => {
      vi.restoreAllMocks();
    });

    it('downloadCSV creates a blob link, clicks it, and cleans up', () => {
      downloadCSV('col1,col2\nval1,val2', 'export.csv');

      expect(createObjectURLSpy).toHaveBeenCalled();
      expect(clickSpy).toHaveBeenCalled();
      expect(appendChildSpy).toHaveBeenCalled();
      expect(removeChildSpy).toHaveBeenCalled();
      expect(revokeObjectURLSpy).toHaveBeenCalledWith('blob:mock-url');
    });

    it('downloadXLSX converts to CSV and triggers download', () => {
      const headers = [{ key: 'col1', header: 'Column 1', selected: true }];
      const rows = [{ col1: 'val1' }];

      downloadXLSX(headers, rows, 'export.xlsx');

      expect(createObjectURLSpy).toHaveBeenCalled();
      expect(clickSpy).toHaveBeenCalled();
      expect(revokeObjectURLSpy).toHaveBeenCalledWith('blob:mock-url');
    });
  });

  describe('formatBytesDecimal', () => {
    it('returns "0 B" when bytes is 0', () => {
      expect(formatBytesDecimal(0)).toBe('0 B');
    });

    it('formats bytes in decimal units (KB, MB, GB, TB, PB)', () => {
      expect(formatBytesDecimal(500)).toBe('500 B');
      expect(formatBytesDecimal(1500)).toBe('1.5 KB');
      expect(formatBytesDecimal(2500000)).toBe('2.5 MB');
      expect(formatBytesDecimal(3500000000)).toBe('3.5 GB');
      expect(formatBytesDecimal(4500000000000)).toBe('4.5 TB');
      expect(formatBytesDecimal(5500000000000000)).toBe('5.5 PB');
    });

    it('respects decimal places parameter', () => {
      expect(formatBytesDecimal(1234, 2)).toBe('1.23 KB');
      expect(formatBytesDecimal(1234, 0)).toBe('1 KB');
    });
  });
});
