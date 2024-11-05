import { describe, it, expect } from 'vitest';
import { formatDate, dateStringToISO } from '../../utils/DateUtils';

describe('formatDate', () => {
  it('should format a valid date string to "Month Day, Year"', () => {
    const result = formatDate('2023-11-05');
    expect(result).toBe('November 5, 2023');
  });

  it('should return "--" for an empty string', () => {
    const result = formatDate('');
    expect(result).toBe('--');
  });

  it('should return "--" for a null value', () => {
    // @ts-ignore
    const result = formatDate(null);
    expect(result).toBe('--');
  });
});

describe('dateStringToISO', () => {
  it('should convert a date string to ISO format', () => {
    const result = dateStringToISO('2023-11-05');
    expect(result).toBe('2023-11-05T00:00:00.000Z');
  });

  it('should return an empty string for an invalid date', () => {
    const result = dateStringToISO('');
    expect(result).toBe('');
  });

  it('should return an empty string for a null value', () => {
    // @ts-ignore
    const result = dateStringToISO(null);
    expect(result).toBe('');
  });
});
