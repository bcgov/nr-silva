import React from "react";
import UnderConstructionTag, { type UnderConstructionTagProps } from ".";

type TagWrapperProps = {
  children: React.ReactElement;
  position?: 'left' | 'right';
} & UnderConstructionTagProps;

/**
 * Wraps a child React element with an `UnderConstructionTag`, either to the left or right.
 *
 * Useful for annotating headers, labels, or UI components with an "Under construction" tag,
 * while preserving layout flexibility.
 *
 * @param {TagWrapperProps} props - The component props
 * @param {React.ReactElement} props.children - The content to wrap
 * @param {'left' | 'right'} [props.position='right'] - Position of the tag relative to the children
 * @param {'page' | 'feature'} [props.type='feature'] - Indicates whether the tag applies to a page or a feature
 *
 * @returns {JSX.Element} A wrapped element with an `UnderConstructionTag`
 */
const TagWrapper = ({ children, position = 'right', type = 'feature' }: TagWrapperProps) => (
  <div className="under-const-wrapper">
    {
      position === 'right'
        ? (
          <>
            {children}
            <UnderConstructionTag type={type} />
          </>
        )
        : (
          <>
            <UnderConstructionTag type={type} />
            {children}
          </>
        )
    }
  </div>
);

export default TagWrapper;
