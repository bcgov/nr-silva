import React from "react";
import './styles.scss';
import { TagSkeleton, TextInputSkeleton, Tooltip } from "@carbon/react";
import { PLACE_HOLDER } from "@/constants";

/**
 * Props for the `CardItem` component.
 */
interface CardItemProps {
  /**
   * The label displayed above the content.
   */
  label: string;

  /**
   * The children to display inside the card. If `showSkeleton` is false, this will be shown.
   */
  children?: React.ReactNode;

  /**
   * Whether to show the provided content or a `TagSkeleton` as a placeholder.
   * If true, renders `TagSkeleton`; otherwise, renders `children`.
   * @default false
   */
  showSkeleton?: boolean;

  /**
   * The html id of the component
   */
  id?: string;

  /**
   * Text used for tooltip
   */
  tooltipText?: string
}

/**
 * A UI component that displays a labeled piece of content inside a card-like container.
 * Can optionally render a skeleton placeholder instead of content.
 *
 * Uses semantic HTML (`<dl>`, `<dt>`, `<dd>`) to ensure proper accessibility,
 * allowing screen readers to correctly associate the label (`<dt>`) with its corresponding value (`<dd>`).
 *
 * If `tooltipText` is provided, the content will be wrapped with Carbon's `Tooltip`.
 *
 * @param {CardItemProps} props - The props for the component.
 * @returns {React.ReactElement} The rendered `CardItem` component.
 */
const CardItem = ({
  showSkeleton = false,
  label,
  children,
  id,
  tooltipText
}: CardItemProps): React.ReactElement => {

  if (showSkeleton) {
    return (
      <TextInputSkeleton />
    )
  }

  const rawContent = children ?? PLACE_HOLDER;

  /**
   * Final content to be rendered in the card item.
   *
   * - If `tooltipText` is provided and the content is not in skeleton state,
   *   wraps the content in a Carbon `Tooltip`.
   * - If the content is not a valid React element, wraps it in a `<span>` to ensure it can be rendered inside the tooltip.
   * - Otherwise, renders the raw content or a fallback placeholder.
   */
  let content: React.ReactNode = rawContent;

  if (tooltipText && !showSkeleton) {
    content = (
      <Tooltip description={tooltipText}>
        {
          React.isValidElement(rawContent)
            ? rawContent
            : <span>{String(rawContent)}</span>
        }
      </Tooltip>
    );
  }

  return (
    <dl className="card-item" id={id} data-testid={`card-item-${label.replace(/\s+/g, '-').toLowerCase()}`}>
      <dt className="card-item-label">{label}</dt>
      <dd
        className="card-item-content"
        data-testid={`card-item-content-${label.replace(/\s+/g, '-').toLowerCase()}`}
        title={typeof children === 'string' && !showSkeleton ? children : undefined}
      >
        {content}
      </dd>
    </dl>
  );
};

export default CardItem;
