import React from "react";
import { Construction } from "@carbon/icons-react";
import { Tag, Tooltip } from "@carbon/react";

import './styles.scss';

export type UnderConstructionTagProps = {
  type?: 'page' | 'feature';
}

/**
 * A reusable tag component that displays an "Under construction" label with a tooltip.
 *
 * Useful for indicating that a specific page or feature is still in development.
 *
 * @param {UnderConstructionTagProps} props - Component props
 * @param {'page' | 'feature'} [props.type='feature'] - Indicates whether the tag applies to a page or a feature
 *
 * @returns {JSX.Element} A styled tag wrapped in a tooltip
 */
const UnderConstructionTag = ({ type = 'feature' }: UnderConstructionTagProps) => (
  <Tooltip
    label={`This ${type} is under development. Features may be incomplete or display incorrect data.`}
    align="bottom"
  >
    <Tag
      className="under-construction-tag"
      type="cyan"
      size="md"
      renderIcon={Construction}
    >
      Under construction
    </Tag>
  </Tooltip>
);

export default UnderConstructionTag;
