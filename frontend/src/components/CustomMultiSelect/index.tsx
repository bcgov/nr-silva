import React from "react";
import { FilterableMultiSelect, TextInputSkeleton, type FilterableMultiSelectProps } from "@carbon/react";

/**
 * Props for the CustomMultiSelect component.
 *
 * @template ItemType The type of the items in the multi-select.
 */
interface CustomMultiSelectProps<ItemType> extends FilterableMultiSelectProps<ItemType> {
  /**
   * If true, displays a skeleton loader instead of the multi-select component.
   * @default false
   */
  showSkeleton?: boolean;
}

/**
 * A custom multi-select component based on Carbon's `FilterableMultiSelect`.
 * It conditionally renders a skeleton loader (`TextInputSkeleton`) if `showSkeleton` is true.
 *
 * @template ItemType The type of the items in the multi-select.
 *
 * @param {CustomMultiSelectProps<ItemType>} props - The component props.
 * @returns {React.ReactElement} The rendered component.
 */
const CustomMultiSelect = <ItemType,>({
  showSkeleton = false,
  selectionFeedback = "top-after-reopen",
  ...props
}: CustomMultiSelectProps<ItemType>) => {
  return showSkeleton ? (
    <TextInputSkeleton hideLabel />
  ) : (
    <FilterableMultiSelect {...props} selectionFeedback={selectionFeedback} />
  );
};

export default CustomMultiSelect;
