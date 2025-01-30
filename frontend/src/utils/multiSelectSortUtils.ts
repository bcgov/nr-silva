import { SelectableTextValueData, SortOptions } from '@/types/GeneralTypes';

const sortItems = (
  items: SelectableTextValueData[],
  { selectedItems = [], itemToString, compareItems, locale = 'en' }: SortOptions
): SelectableTextValueData[] => {
  return items.sort((itemA, itemB) => {
    if (itemA.isSelectAll) return -1;
    if (itemB.isSelectAll) return 1;

    const hasItemA = selectedItems.some((sItem) => itemA.value === sItem.value);
    const hasItemB = selectedItems.some((sItem) => itemB.value === sItem.value);

    if (hasItemA && !hasItemB) return -1;
    if (hasItemB && !hasItemA) return 1;

    return compareItems(itemToString(itemA), itemToString(itemB), { locale });
  });
};

export { sortItems };