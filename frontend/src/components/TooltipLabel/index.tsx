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
}

const TooltipLabel = ({
  label,
  tooltip,
  htmlFor,
  className,
  id,
  align,
  autoAlign
}: TooltipLabelProps) => (
  <div className={`silva-tooltip-label ${className ?? ''}`} id={id}>
    <label className="default-label" htmlFor={htmlFor}>{label}</label>

    <Tooltip label={tooltip} align={align} autoAlign={autoAlign}>
      <Information />
    </Tooltip>
  </div>
);

export default TooltipLabel;
