import { useRef } from 'react';
import { Column, Grid, TextInput } from '@carbon/react';
import { CommentSearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import CustomMultiSelect from '@/components/CustomMultiSelect';
import ForestClientMultiSelect from '@/components/ForestClientMultiSelect';
import { OrgUnitMultiSelect, SearchDateRange } from '@/components/common/SearchInput';
import { handleMultiSelectChangeHelper } from '@/utils/SearchUtils';
import { COMMENT_KEYWORD_MAX_LENGTH, COMMENT_KEYWORD_MIN_LENGTH, COMMENT_LOCATION_LIST } from './constants';

import './styles.scss';

type Props = {
  searchParams?: CommentSearchParams;
  handleSearchFieldChange: (field: keyof CommentSearchParams, value: unknown) => void;
  showValidation?: boolean;
  onSearch?: () => void;
};

const CommentSearchInput = ({ searchParams, handleSearchFieldChange, showValidation, onSearch }: Props) => {
  const searchTermInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(searchTermInputRef, searchParams?.searchTerm);

  const searchTermError = showValidation
    ? !searchParams?.searchTerm || searchParams.searchTerm.length < COMMENT_KEYWORD_MIN_LENGTH
      ? `Minimum ${COMMENT_KEYWORD_MIN_LENGTH} characters required`
      : searchParams.searchTerm.length > COMMENT_KEYWORD_MAX_LENGTH
        ? `Maximum ${COMMENT_KEYWORD_MAX_LENGTH} characters allowed`
        : null
    : null;

  const handleMultiSelectChange = (field: keyof CommentSearchParams) =>
    handleMultiSelectChangeHelper<CommentSearchParams>(field, handleSearchFieldChange);

  return (
    <Grid className="default-search-input-grid">
      {/* Search term */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={searchTermInputRef}
          id="comment-search-term-input"
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

      {/* Comment Location */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="comment-location-multiselect"
          className="default-search-multi-select"
          titleText="Comment location"
          placeholder={
            searchParams?.commentLocation?.length
              ? COMMENT_LOCATION_LIST
                .filter((item) => searchParams.commentLocation!.includes(item.code ?? ''))
                .map((item) => item.description ?? item.code ?? '')
                .join(', ')
              : 'Choose one or more options'
          }
          items={COMMENT_LOCATION_LIST}
          itemToString={(item) => item?.description ?? item?.code ?? ''}
          onChange={handleMultiSelectChange('commentLocation')}
          selectedItems={COMMENT_LOCATION_LIST.filter((item) =>
            searchParams?.commentLocation?.includes(item.code ?? '')
          )}
        />
      </Column>

      {/* Client */}
      <Column sm={4} md={4} lg={6} max={4}>
        <ForestClientMultiSelect
          selectedClientNumbers={searchParams?.clientNumbers}
          onChange={(clientNumbers) => handleSearchFieldChange('clientNumbers', clientNumbers)}
        />
      </Column>

      {/* Org Unit */}
      <OrgUnitMultiSelect
        id="comment-org-unit-multiselect"
        selectedOrgUnits={searchParams?.orgUnits}
        onChange={(orgUnits) => handleSearchFieldChange('orgUnits', orgUnits)}
      />

      {/* Date range */}
      <SearchDateRange
        labelHtmlFor="comment-last-updated-date-range"
        startDate={searchParams?.updateDateStart}
        endDate={searchParams?.updateDateEnd}
        onStartDateChange={(date) => handleSearchFieldChange('updateDateStart', date)}
        onEndDateChange={(date) => handleSearchFieldChange('updateDateEnd', date)}
      />
    </Grid>
  );
};

export default CommentSearchInput;
