interface TextValueData {
  value: string;
  text: string;
}

/**
 * Formats the TextValueData to a string for display.
 */
const textValueToDisplayText = (textValueData?: TextValueData | null): string => (
  textValueData ? `${textValueData.value} - ${textValueData.text}` : ""
);

/**
 * Extracts the `value` field from an array of `TextValueData` objects.
 *
 * @param {TextValueData[]} arr - An array of objects containing `text` and `value` properties.
 * @returns {string[]} An array of extracted `value` strings.
 */
const extractValsFromTextValueArr = (arr: TextValueData[]): string[] => (
  arr.map((item) => item.value)
)

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

export {
  sortItems,
  textValueToDisplayText,
  extractValsFromTextValueArr,
  type TextValueData,
  type SelectableTextValueData,
  type SortOptions
};
