import { Stack } from "@carbon/react";
import React from "react";

import './styles.scss';

type RequiredLabelProps = React.ComponentPropsWithoutRef<"label">;

const RequiredLabel = React.forwardRef<HTMLLabelElement, RequiredLabelProps>(
  ({ children, className, ...rest }, ref) => {
    const mergedClassName = ["required-label", "default-label", className].filter(Boolean).join(" ");

    return (
      <label ref={ref} className={mergedClassName} {...rest}>
        <Stack className="label-stack" orientation="horizontal" gap="0.25rem">
          <span className="rhcp">*</span>
          <span>{children}</span>
        </Stack>
      </label>
    );
  }
);

export default RequiredLabel;
