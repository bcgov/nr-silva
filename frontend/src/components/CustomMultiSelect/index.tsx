import React, { useRef, useEffect } from "react";
import { FilterableMultiSelect, TextInputSkeleton, type FilterableMultiSelectProps } from "@carbon/react";
import { CARBON_CLASS_PREFIX } from "@/constants";

import './styles.scss';

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
  onChange,
  ...props
}: CustomMultiSelectProps<ItemType>): React.ReactElement => {
  const wrapperRef = useRef<HTMLDivElement>(null);

  // Defer onChange callback to prevent "Cannot update a component while rendering a different component" warning
  const deferredOnChange = React.useCallback((changes: any) => {
    if (onChange) {
      // Use queueMicrotask to defer the callback until after the current render cycle
      queueMicrotask(() => {
        onChange(changes);
      });
    }
  }, [onChange]);

  // This is to handle the case where the user clicks outside the multi-select
  // and we want to force the component to lose focus and close the menu.
  // Otherwise, Carbon's internal focus management can cause the menu to appear to be focused
  // even after clicking outside.
  useEffect(() => {
    const wrapper = wrapperRef.current;
    if (!wrapper) return;

    // Handle clicks outside the component
    const handleDocumentClick = (e: MouseEvent) => {
      const target = e.target as Node;
      const multiSelect = wrapper.querySelector(`.${CARBON_CLASS_PREFIX}--multi-select`);

      // If click is outside the multiselect, force blur on the container
      if (multiSelect && !multiSelect.contains(target)) {
        const container = multiSelect as HTMLElement;
        // Remove focus by calling blur
        container.blur();
        // Also blur the input if it exists
        const input = container.querySelector(`.${CARBON_CLASS_PREFIX}--text-input`) as HTMLInputElement;
        if (input) {
          input.blur();
        }
      }
    };

    // Add listener with a slight delay to ensure it runs after Carbon's internal handlers
    document.addEventListener('mousedown', handleDocumentClick, true);

    return () => {
      document.removeEventListener('mousedown', handleDocumentClick, true);
    };
  }, []);

  return (
    <div ref={wrapperRef} className="custom-multi-select-wrapper">
      <FilterableMultiSelect {...props} onChange={deferredOnChange} selectionFeedback={selectionFeedback} />
    </div>
  );
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
