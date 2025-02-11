interface TextValueData {
  value: string;
  text: string;
}

interface SelectableTextValueData extends TextValueData {
  isSelectAll?: boolean;
}

interface SortOptions {
  selectedItems?: TextValueData[];
  itemToString: (item: TextValueData) => string;
  compareItems: (a: string, b: string, options: { locale: string }) => number;
  locale?: string;
}

const sortItems = (
  items: SelectableTextValueData[] | TextValueData[],
  { selectedItems = [], itemToString, compareItems, locale = 'en' }: SortOptions
): SelectableTextValueData[] | TextValueData[] => {
  return items.sort((itemA, itemB) => {
    if ("isSelectAll" in itemA && itemA.isSelectAll) return -1;
    if ("isSelectAll" in itemB && itemB.isSelectAll) return 1;

    const hasItemA = selectedItems.some((sItem) => itemA.value === sItem.value);
    const hasItemB = selectedItems.some((sItem) => itemB.value === sItem.value);

    if (hasItemA && !hasItemB) return -1;
    if (hasItemB && !hasItemA) return 1;

    return compareItems(itemToString(itemA), itemToString(itemB), { locale });
  });
};

export { sortItems, type TextValueData, type SelectableTextValueData, type SortOptions };
