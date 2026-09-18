import { Column } from '@carbon/react';
import { useQuery } from '@tanstack/react-query';
import API from '@/services/API';
import CustomMultiSelect from '@/components/CustomMultiSelect';
import { CodeDescriptionDto } from '@/services/OpenApi';
import { getMultiSelectedCodes } from '@/utils/InputUtils';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { getMultiSelectPlaceholderHelper } from '@/utils/SearchUtils';

export type OrgUnitMultiSelectProps = {
  type?: 'district';
  selectedOrgUnits?: string[];
  onChange: (orgUnits?: string[]) => void;
  placeholder?: string;
  id?: string;
};

export const OrgUnitMultiSelect = ({
  type,
  selectedOrgUnits,
  onChange,
  placeholder,
  id = 'org-unit-multiselect',
}: OrgUnitMultiSelectProps) => {
  const orgUnitQuery = useQuery({
    queryKey: type ? ['codes', 'org-units', { type }] : ['codes', 'org-units'],
    queryFn: () => (type ? API.CodesEndpointService.getOpeningOrgUnits(type) : API.CodesEndpointService.getOpeningOrgUnits()),
  });

  const handleChange = (selected: { selectedItems: CodeDescriptionDto[] }) => {
    const selectedCodes = getMultiSelectedCodes(selected);
    onChange(selectedCodes.length > 0 ? selectedCodes : undefined);
  };

  return (
    <Column sm={4} md={4} lg={6} max={4}>
      <CustomMultiSelect
        id={id}
        className="default-search-multi-select"
        titleText="Org unit"
        placeholder={placeholder ?? getMultiSelectPlaceholderHelper(selectedOrgUnits)}
        items={orgUnitQuery.data ?? []}
        itemToString={codeDescriptionToDisplayText}
        onChange={handleChange}
        selectedItems={(orgUnitQuery.data ?? []).filter((data) =>
          selectedOrgUnits?.includes(data.code ?? '')
        )}
      />
    </Column>
  );
};

export default OrgUnitMultiSelect;
