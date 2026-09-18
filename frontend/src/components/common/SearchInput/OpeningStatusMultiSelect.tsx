import { Column } from '@carbon/react';
import CustomMultiSelect from '@/components/CustomMultiSelect';
import { OPENING_STATUS_LIST } from '@/constants';
import { CodeDescriptionDto } from '@/services/OpenApi';
import { getMultiSelectedCodes } from '@/utils/InputUtils';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { getMultiSelectPlaceholderHelper } from '@/utils/SearchUtils';

export type OpeningStatusMultiSelectProps = {
  selectedStatuses?: string[];
  onChange: (statuses?: string[]) => void;
  placeholder?: string;
  id?: string;
};

export const OpeningStatusMultiSelect = ({
  selectedStatuses,
  onChange,
  placeholder,
  id = 'status-multiselect',
}: OpeningStatusMultiSelectProps) => {
  const handleChange = (selected: { selectedItems: CodeDescriptionDto[] }) => {
    const selectedCodes = getMultiSelectedCodes(selected);
    onChange(selectedCodes.length > 0 ? selectedCodes : undefined);
  };

  return (
    <Column sm={4} md={4} lg={6} max={4}>
      <CustomMultiSelect
        id={id}
        className="default-search-multi-select"
        titleText="Opening status"
        placeholder={placeholder ?? getMultiSelectPlaceholderHelper(selectedStatuses)}
        items={OPENING_STATUS_LIST}
        itemToString={codeDescriptionToDisplayText}
        onChange={handleChange}
        selectedItems={
          OPENING_STATUS_LIST.filter((data) =>
            selectedStatuses?.includes(data.code ?? '')
          ) ?? []
        }
      />
    </Column>
  );
};

export default OpeningStatusMultiSelect;
