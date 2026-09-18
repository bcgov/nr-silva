import { Button, Column } from '@carbon/react';
import { ChevronDown, ChevronUp } from '@carbon/icons-react';

export type MoreFiltersToggleProps = {
  isExpanded: boolean;
  onToggle: (expanded: boolean) => void;
};

export const MoreFiltersToggle = ({ isExpanded, onToggle }: MoreFiltersToggleProps) => {
  return (
    <Column sm={4} md={8} lg={16}>
      <Button
        type="button"
        renderIcon={isExpanded ? ChevronUp : ChevronDown}
        title={`${isExpanded ? 'Fewer' : 'More'} filters`}
        kind="tertiary"
        onClick={() => onToggle(!isExpanded)}
      >
        {isExpanded ? 'Fewer filters' : 'More filters'}
      </Button>
    </Column>
  );
};

export default MoreFiltersToggle;
