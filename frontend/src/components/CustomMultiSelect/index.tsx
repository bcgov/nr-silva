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
 * Renders the skeleton loader for the CustomMultiSelect component.
 *
 * @returns {React.ReactElement} The skeleton loader component.
 */
const renderSkeleton = (): React.ReactElement => {
  return <TextInputSkeleton hideLabel />;
};

/**
 * Renders the FilterableMultiSelect component.
 *
 * @template ItemType The type of the items in the multi-select.
 *
 * @param {CustomMultiSelectProps<ItemType>} props - The component props.
 * @returns {React.ReactElement} The multi-select component.
 */
const renderMultiSelect = <ItemType,>({
  selectionFeedback = "top-after-reopen",
  ...props
}: CustomMultiSelectProps<ItemType>): React.ReactElement => {
  return <FilterableMultiSelect {...props} selectionFeedback={selectionFeedback} />;
};

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
  ...props
}: CustomMultiSelectProps<ItemType>): React.ReactElement => {
  return showSkeleton ? renderSkeleton() : renderMultiSelect(props);
};

export default CustomMultiSelect;
