import { useRef } from 'react';
import { Column, Grid, TextInput } from '@carbon/react';
import { StockingStandardsCommentSearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import CustomMultiSelect from '@/components/CustomMultiSelect';
import ForestClientMultiSelect from '@/components/ForestClientMultiSelect';
import { OrgUnitMultiSelect, SearchDateRange } from '@/components/common/SearchInput';
import { handleMultiSelectChangeHelper } from '@/utils/SearchUtils';
import { STOCKING_COMMENT_KEYWORD_MAX_LENGTH, STOCKING_COMMENT_KEYWORD_MIN_LENGTH, STOCKING_COMMENT_LOCATION_LIST } from './constants';

import './styles.scss';

type Props = {
  searchParams?: StockingStandardsCommentSearchParams;
  handleSearchFieldChange: (field: keyof StockingStandardsCommentSearchParams, value: unknown) => void;
  showValidation?: boolean;
  onSearch?: () => void;
};

const StockingStandardsCommentSearchInput = ({ searchParams, handleSearchFieldChange, showValidation, onSearch }: Props) => {
  const searchTermInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(searchTermInputRef, searchParams?.searchTerm);

  const searchTermError = showValidation
    ? !searchParams?.searchTerm || searchParams.searchTerm.length < STOCKING_COMMENT_KEYWORD_MIN_LENGTH
      ? `Minimum ${STOCKING_COMMENT_KEYWORD_MIN_LENGTH} characters required`
      : searchParams.searchTerm.length > STOCKING_COMMENT_KEYWORD_MAX_LENGTH
        ? `Maximum ${STOCKING_COMMENT_KEYWORD_MAX_LENGTH} characters allowed`
        : null
    : null;

  const handleMultiSelectChange = (field: keyof StockingStandardsCommentSearchParams) =>
    handleMultiSelectChangeHelper<StockingStandardsCommentSearchParams>(field, handleSearchFieldChange);

  return (
    <Grid className="default-search-input-grid">
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={searchTermInputRef}
          id="stocking-comment-search-term-input"
          name="searchTerm"
          labelText="Keyword"
          placeholder="Enter a full or partial keyword"
          invalid={!!searchTermError}
          invalidText={searchTermError ?? ''}
          onChange={(e) =>
            handleSearchFieldChange('searchTerm', e.target.value || undefined)
          }
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              onSearch?.();
            }
          }}
        />
      </Column>

      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="stocking-comment-location-multiselect"
          className="default-search-multi-select"
          titleText="Keywords type"
          placeholder={
            searchParams?.commentLocations?.length
              ? STOCKING_COMMENT_LOCATION_LIST
                .filter((item) => searchParams.commentLocations!.includes(item.code ?? ''))
                .map((item) => item.description ?? item.code ?? '')
                .join(', ')
              : 'Choose one or more options'
          }
          items={STOCKING_COMMENT_LOCATION_LIST}
          itemToString={(item) => item?.description ?? item?.code ?? ''}
          onChange={handleMultiSelectChange('commentLocations')}
          selectedItems={STOCKING_COMMENT_LOCATION_LIST.filter((item) =>
            searchParams?.commentLocations?.includes(item.code ?? '')
          )}
        />
      </Column>

      <Column sm={4} md={4} lg={6} max={4}>
        <ForestClientMultiSelect
          selectedClientNumbers={searchParams?.clientNumbers}
          onChange={(clientNumbers) => handleSearchFieldChange('clientNumbers', clientNumbers)}
        />
      </Column>

      {/* Org Unit */}
      <OrgUnitMultiSelect
        id="stocking-comment-org-unit-multiselect"
        selectedOrgUnits={searchParams?.orgUnits}
        onChange={(orgUnits) => handleSearchFieldChange('orgUnits', orgUnits)}
      />

      {/* Date range */}
      <SearchDateRange
        labelHtmlFor="stocking-comment-last-updated-date-range"
        startDate={searchParams?.updateDateStart}
        endDate={searchParams?.updateDateEnd}
        onStartDateChange={(date) => handleSearchFieldChange('updateDateStart', date)}
        onEndDateChange={(date) => handleSearchFieldChange('updateDateEnd', date)}
      />
    </Grid>
  );
};

export default StockingStandardsCommentSearchInput;
