import React from "react";
import './styles.scss';
import { TagSkeleton } from "@carbon/react";
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
}

/**
 * A UI component that displays a labeled piece of content inside a card-like container.
 * Can optionally render a skeleton placeholder instead of content.
 *
 * @param {CardItemProps} props - The props for the component.
 * @returns {React.ReactElement} The rendered `CardItem` component.
 */
const CardItem = ({
  showSkeleton,
  label,
  children,
  id
}: CardItemProps) => (
  <div className="card-item" id={id}>
    <label className="card-item-label">
      {label}
    </label>
    <div className="card-item-content">
      {showSkeleton
        ? <TagSkeleton />
        : (children ?? PLACE_HOLDER)
      }
    </div>
  </div>
);

export default CardItem;
