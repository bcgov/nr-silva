import { ReactNode } from "react";
import { Information } from "@carbon/icons-react"
import { PopoverAlignment, Tooltip } from "@carbon/react"

import './styles.scss';

type TooltipLabelProps = {
  label: string;
  tooltip: ReactNode;
  htmlFor?: string;
  className?: string;
  id?: string;
  align?: PopoverAlignment;
  autoAlign?: boolean;
  useLabel02?: boolean;
}

const TooltipLabel = ({
  label,
  tooltip,
  htmlFor,
  className,
  id,
  align,
  autoAlign,
  useLabel02
}: TooltipLabelProps) => (
  <div className={`silva-tooltip-label ${className ?? ''}`} id={id}>
    <label
      className={`${useLabel02 ? 'default-label-02' : 'default-label'}`}
      htmlFor={htmlFor}>
      {label}
    </label>

    <Tooltip label={tooltip} align={align} autoAlign={autoAlign}>
      <Information />
    </Tooltip>
  </div>
);

export default TooltipLabel;
