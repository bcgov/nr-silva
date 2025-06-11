import React from "react";
import UnderConstructionTag from ".";

type TagWrapperProps = {
  children: React.ReactElement;
  position?: 'left' | 'right';
}

const TagWrapper = ({ children, position = 'right' }: TagWrapperProps) => (
  <div className="under-const-wrapper">
    {
      position === 'right'
        ? (
          <>
            {children}
            <UnderConstructionTag />
          </>
        )
        : (
          <>
            <UnderConstructionTag />
            {children}
          </>
        )
    }
  </div>
);

export default TagWrapper;
