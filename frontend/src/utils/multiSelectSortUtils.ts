import { SortItemsOptions } from "@carbon/react/lib/components/MultiSelect/MultiSelectPropTypes";

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

const sortItems = <T extends TextValueData>(
  items: readonly T[],
  { selectedItems = [], itemToString, locale = "en" }: SortItemsOptions<T & { value: string }>
): T[] => {
  return [...items].sort((itemA, itemB) => {
    if ("isSelectAll" in itemA && itemA.isSelectAll) return -1;
    if ("isSelectAll" in itemB && itemB.isSelectAll) return 1;

    const hasItemA = selectedItems.some((sItem) => itemA.value === sItem.value);
    const hasItemB = selectedItems.some((sItem) => itemB.value === sItem.value);

    if (hasItemA && !hasItemB) return -1;
    if (hasItemB && !hasItemA) return 1;

    const itemAString = itemToString?.(itemA) ?? "";
    const itemBString = itemToString?.(itemB) ?? "";

    return itemAString.localeCompare(itemBString, locale);
  });
};

/**
 * Filters a list of TextValueData items based on the input value.
 *
 * @param {readonly (TextValueData | undefined)[]} items - The list of items to filter.
 * @param {object} extra - Additional parameters including inputValue and itemToString function.
 * @returns {(TextValueData | undefined)[]} The filtered list of items.
 */
const filterTextValueItems = (
  items: readonly (TextValueData | undefined)[],
  { inputValue, itemToString }: { inputValue: string | null; itemToString: (item: TextValueData | null | undefined) => string }
): (TextValueData | undefined)[] => {
  if (!inputValue) return [...items];

  return items.filter((item) => {
    if (!item) return false;
    return itemToString(item).toLowerCase().includes(inputValue.toLowerCase());
  });
};

export {
  filterTextValueItems,
  sortItems,
  textValueToDisplayText,
  extractValsFromTextValueArr,
  type TextValueData,
  type SelectableTextValueData,
  type SortItemsOptions as SortOptions
};
