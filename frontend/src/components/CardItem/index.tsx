import React from "react";
import './styles.scss';
import { TagSkeleton, Tooltip } from "@carbon/react";
import { PLACE_HOLDER } from "../../constants";

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
  const rawContent = children ?? PLACE_HOLDER;

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
    <dl className="card-item" id={id}>
      <dt className="card-item-label">{label}</dt>
      <dd
        className="card-item-content"
        title={typeof children === 'string' && !showSkeleton ? children : undefined}
      >
        {showSkeleton ? <TagSkeleton /> : content}
      </dd>
    </dl>
  );
};

export default CardItem;
