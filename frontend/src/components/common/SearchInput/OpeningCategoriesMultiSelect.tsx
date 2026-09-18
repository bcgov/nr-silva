import { Column } from '@carbon/react';
import { useQuery } from '@tanstack/react-query';
import API from '@/services/API';
import CustomMultiSelect from '@/components/CustomMultiSelect';
import { CodeDescriptionDto } from '@/services/OpenApi';
import { getMultiSelectedCodes } from '@/utils/InputUtils';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { getMultiSelectPlaceholderHelper } from '@/utils/SearchUtils';

export type OpeningCategoriesMultiSelectProps = {
  selectedCategories?: string[];
  onChange: (categories?: string[]) => void;
  placeholder?: string;
  id?: string;
};

export const OpeningCategoriesMultiSelect = ({
  selectedCategories,
  onChange,
  placeholder,
  id = 'category-multi-select',
}: OpeningCategoriesMultiSelectProps) => {
  const categoryQuery = useQuery({
    queryKey: ['codes', 'opening-categories'],
    queryFn: () => API.CodesEndpointService.getOpeningCategories(),
  });

  const handleChange = (selected: { selectedItems: CodeDescriptionDto[] }) => {
    const selectedCodes = getMultiSelectedCodes(selected);
    onChange(selectedCodes.length > 0 ? selectedCodes : undefined);
  };

  return (
    <Column sm={4} md={4} lg={6} max={4}>
      <CustomMultiSelect
        placeholder={placeholder ?? getMultiSelectPlaceholderHelper(selectedCategories)}
        titleText="Opening category"
        id={id}
        className="default-search-multi-select"
        items={categoryQuery.data ?? []}
        itemToString={codeDescriptionToDisplayText}
        onChange={handleChange}
        selectedItems={
          categoryQuery.data?.filter((data) =>
            selectedCategories?.includes(data.code ?? '')
          ) ?? []
        }
      />
    </Column>
  );
};

export default OpeningCategoriesMultiSelect;
