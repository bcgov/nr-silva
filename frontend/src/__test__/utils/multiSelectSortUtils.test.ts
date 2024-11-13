import { describe, it, expect } from 'vitest';
import { sortItems, type TextValueData, type SelectableTextValueData, type SortOptions } from '../../utils/multiSelectSortUtils';

describe('multiSelectSortUtils', () => {
  const itemToString = (item: TextValueData) => item.value;
  const compareItems = (a: string, b: string, options: { locale: string }) => a.localeCompare(b, options.locale);

  it('should sort items with "Select All" option first', () => {
    const items: SelectableTextValueData[] = [
      { value: 'Banana' },
      { value: 'Apple' },
      { value: 'Select All', isSelectAll: true },
      { value: 'Cherry' },
    ];

    const sortedItems = sortItems(items, { itemToString, compareItems });

    expect(sortedItems[0].value).toBe('Select All');
  });

  it('should sort selected items before unselected items', () => {
    const items: SelectableTextValueData[] = [
      { value: 'Banana' },
      { value: 'Apple' },
      { value: 'Cherry' },
    ];

    const selectedItems: TextValueData[] = [
      { value: 'Cherry' },
    ];

    const sortedItems = sortItems(items, { selectedItems, itemToString, compareItems });

    expect(sortedItems[0].value).toBe('Cherry');
  });

  it('should sort items alphabetically if no "Select All" or selected items', () => {
    const items: SelectableTextValueData[] = [
      { value: 'Banana' },
      { value: 'Apple' },
      { value: 'Cherry' },
    ];

    const sortedItems = sortItems(items, { itemToString, compareItems });

    expect(sortedItems[0].value).toBe('Apple');
    expect(sortedItems[1].value).toBe('Banana');
    expect(sortedItems[2].value).toBe('Cherry');
  });

  it('should handle empty items array', () => {
    const items: SelectableTextValueData[] = [];

    const sortedItems = sortItems(items, { itemToString, compareItems });

    expect(sortedItems).toEqual([]);
  });

  it('should handle empty selectedItems array', () => {
    const items: SelectableTextValueData[] = [
      { value: 'Banana' },
      { value: 'Apple' },
      { value: 'Cherry' },
    ];

    const sortedItems = sortItems(items, { selectedItems: [], itemToString, compareItems });

    expect(sortedItems[0].value).toBe('Apple');
    expect(sortedItems[1].value).toBe('Banana');
    expect(sortedItems[2].value).toBe('Cherry');
  });
});