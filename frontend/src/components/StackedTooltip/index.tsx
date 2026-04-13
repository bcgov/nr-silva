import { DefinitionTooltip, PopoverAlignment, Stack } from "@carbon/react";
import { CodeDescriptionDto } from "@/services/OpenApi";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { UNIQUE_CHARACTERS_UNICODE } from "@/constants";

import "./styles.scss";

type props = {
  items?: CodeDescriptionDto[] | null;
  unit: string;
  displayLimit?: number;
  align?: PopoverAlignment;
}

const DEFAULT_DISPLAY_LIMIT = 3;

const StackedTooltip = ({ items, unit, displayLimit = DEFAULT_DISPLAY_LIMIT, align = "right" }: props) => {

  if (!items || items.length === 0) {
    return null;
  }

  const tooltipContent = (
    <Stack gap={2}>
      {items.map((item) => (
        <div key={item.code} className="stacked-tooltip-item">
          <span>{codeDescriptionToDisplayText(item)}</span>
        </div>
      ))}
    </Stack>
  );

  const triggerText = items.length <= displayLimit
    ? items.map(item => item.code).join(` ${UNIQUE_CHARACTERS_UNICODE.BULLET} `)
    : `${items.length} ${unit}`;

  return (
    <DefinitionTooltip
      className="stacked-tooltip"
      openOnHover
      definition={tooltipContent}
      align={align}
    >
      <span>{triggerText}</span>
    </DefinitionTooltip>
  );
}

export default StackedTooltip;
