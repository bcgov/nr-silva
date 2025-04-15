import { SortItemsOptions } from "@carbon/react/lib/components/MultiSelect/MultiSelectPropTypes";
import CodeDescriptionDto from "../types/CodeDescriptionType";
import { PLACE_HOLDER } from "../constants";

interface MultiSelectEvent {
  selectedItems: CodeDescriptionDto[];
}

/**
 * Formats the CodeDescriptionDto to a string for display.
 * - Returns "code - description" if both exist
 * - Returns only code if description is null/undefined
 * - Returns only description if code is null/undefined
 * - Returns PLACE_HOLDER if both are missing or dto is null/undefined
 */
const codeDescriptionToDisplayText = (codeDescriptionDto?: CodeDescriptionDto | null): string => {
  if (!codeDescriptionDto) {
    return PLACE_HOLDER;
  }

  const { code, description } = codeDescriptionDto;

  if (code && description) {
    return `${code} - ${description}`;
  }

  if (code) {
    return code;
  }

  if (description) {
    return description;
  }

  return PLACE_HOLDER;
};

/**
 * Extracts the `code` field from an array of `CodeDescriptionDto` objects.
 *
 * @param {CodeDescriptionDto[]} arr - An array of objects containing `code` and `description` properties.
 * @returns {string[]} An array of extracted `code` strings.
 */
const extractCodesFromCodeDescriptionArr = (arr: CodeDescriptionDto[]): string[] => (
  arr.map((item) => item.code)
);

interface SelectableCodeDescriptionDto extends CodeDescriptionDto {
  isSelectAll?: boolean;
}

/**
 * Filters a list of CodeDescriptionDto items based on the input value.
 *
 * @param {readonly (CodeDescriptionDto | undefined)[]} items - The list of items to filter.
 * @param {object} extra - Additional parameters including inputValue and itemToString function.
 * @returns {(CodeDescriptionDto | undefined)[]} The filtered list of items.
 */
const filterCodeDescriptionItems = (
  items: readonly (CodeDescriptionDto | undefined)[],
  { inputValue, itemToString }: { inputValue: string | null; itemToString: (item: CodeDescriptionDto | null | undefined) => string }
): (CodeDescriptionDto | undefined)[] => {
  if (!inputValue) return [...items];

  return items.filter((item) => {
    if (!item) return false;
    return itemToString(item).toLowerCase().includes(inputValue.toLowerCase());
  });
};

export {
  filterCodeDescriptionItems,
  codeDescriptionToDisplayText,
  extractCodesFromCodeDescriptionArr,
  type SelectableCodeDescriptionDto,
  type SortItemsOptions as SortOptions,
  type MultiSelectEvent
};
