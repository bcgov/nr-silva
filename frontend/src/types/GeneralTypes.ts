import { SyntheticEvent } from "react";

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

interface IdTextValueData {
  id: string;
  text: string;
}

interface TextInputEvent extends SyntheticEvent {
  target: HTMLInputElement;
}

interface SelectEvents {
  selectedItems: IdTextValueData[];
}

interface SelectEvent {
  selectedItem: IdTextValueData;
}

interface AutocompleteProps {
  id: string,
  label: string,
}

interface AutocompleteComboboxProps{
  selectedItem: AutocompleteProps
}

export { 
  type TextValueData, 
  type SelectableTextValueData, 
  type SortOptions,
  type IdTextValueData,
  type TextInputEvent,
  type SelectEvents,
  type SelectEvent,
  type AutocompleteProps,
  type AutocompleteComboboxProps
};
