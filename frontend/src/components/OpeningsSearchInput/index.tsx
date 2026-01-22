import { ChangeEvent, useCallback, useEffect, useRef, useState } from "react";
import { DateTime } from "luxon";
import { OpeningSearchParamsType } from "@/types/OpeningTypes";
import { Checkbox, CheckboxGroup, Column, DatePicker, DatePickerInput, Grid, TextInput } from "@carbon/react";
import { useMutation, useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { enforceNumberInputOnKeyDown, enforceNumberInputOnPaste, getMultiSelectedCodes } from "@/utils/InputUtils";
import { API_DATE_FORMAT, DATE_PICKER_FORMAT, OPENING_STATUS_LIST } from "@/constants";
import useRefWithSearchParam from "@/hooks/useRefWithSearchParam";

import CustomMultiSelect from "../CustomMultiSelect";

import './styles.scss';
import { getClientLabel } from "../../utils/ForestClientUtils";
import { CodeDescriptionDto, ForestClientAutocompleteResultDto } from "../../services/OpenApi";
import { on } from "events";

type props = {
  searchParams: OpeningSearchParamsType | undefined;
  onSearchParamsChange: (field: keyof OpeningSearchParamsType, value: unknown) => void;
}
const OpeningsSearchInput = ({ searchParams, onSearchParamsChange }: props) => {
  const [clientSearchTerm, setClientSearchTerm] = useState<string>('');
  const [selectedClients, setSelectedClients] = useState<ForestClientAutocompleteResultDto[] | null>(null);
  const [matchingClients, setMatchingClients] = useState<ForestClientAutocompleteResultDto[]>([]);

  const openingIdInputRef = useRef<HTMLInputElement>(null); //NUMBER(10,0)
  const fileIdInputRef = useRef<HTMLInputElement>(null); // VARCHAR2(10)
  const licenseeOpeningIdInputRef = useRef<HTMLInputElement>(null); //VARCHAR2(30)
  const cutBlockInputRef = useRef<HTMLInputElement>(null); //VARCHAR2(10)
  const cuttingPermitInputRef = useRef<HTMLInputElement>(null); //VARCHAR2(3)
  const timberMarkInputRef = useRef<HTMLInputElement>(null); //VARCHAR2(10)
  const createdByMeCheckboxRef = useRef<HTMLInputElement>(null);
  const frpaSectionCheckboxRef = useRef<HTMLInputElement>(null);


  const categoryQuery = useQuery({
    queryKey: ["codes", "opening-categories"],
    queryFn: () => API.CodesEndpointService.getOpeningCategories(),
  });

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits
  });

  const clientMutation = useMutation({
    mutationKey: ["forest-clients", "byNameAcronymNumber"],
    mutationFn: (searchParam: string) => API.ForestClientEndpointService.searchForestClients(searchParam),
    onSuccess: (data: ForestClientAutocompleteResultDto[] | null) => {
      if (!data || !Array.isArray(data) || data.length === 0) return;

      setMatchingClients((prev) => {
        const existing = prev ?? [];
        const existingIds = new Set(existing.map((c) => c.id));
        const toAdd = data.filter((c) => c && c.id != null && !existingIds.has(c.id));
        return existing.concat(toAdd);
      });
    },
  });

  /* Debounce the API call by 300ms */
  useEffect(() => {
    if (clientSearchTerm.length <= 2) return;

    const handler = setTimeout(() => {
      clientMutation.mutate(clientSearchTerm);
    }, 300);

    // Clear timeout if user types again
    return () => clearTimeout(handler);
  }, [clientSearchTerm]);


  // Update text inputs with search params
  useRefWithSearchParam(openingIdInputRef, searchParams?.openingId);
  useRefWithSearchParam(fileIdInputRef, searchParams?.licenseNumber);
  useRefWithSearchParam(licenseeOpeningIdInputRef, searchParams?.licenseeOpeningId);
  useRefWithSearchParam(cutBlockInputRef, searchParams?.cutBlockId);
  useRefWithSearchParam(cuttingPermitInputRef, searchParams?.cuttingPermitId);
  useRefWithSearchParam(timberMarkInputRef, searchParams?.timberMark);

  // Update checkboxes with search params
  useRefWithSearchParam(createdByMeCheckboxRef, searchParams?.isCreatedByUser, true);
  useRefWithSearchParam(frpaSectionCheckboxRef, searchParams?.submittedToFrpa, true);

  const handleMultiSelectChange = (field: keyof OpeningSearchParamsType) => (selected: { selectedItems: CodeDescriptionDto[] }) => {
    const selectedCodes = getMultiSelectedCodes(selected);
    onSearchParamsChange(field, selectedCodes.length > 0 ? selectedCodes : undefined);
  };

  const handleCheckboxChange = (field: keyof OpeningSearchParamsType) => (e: ChangeEvent<HTMLInputElement>) => {
    onSearchParamsChange(field, e.target.checked ? true : undefined);
  };

  const getMultiSelectPlaceholder = (field: keyof OpeningSearchParamsType, defaultText = 'Choose one or more options') => {
    const values = searchParams?.[field] as unknown as string[] | undefined;
    return values && values.length > 0 ? values.join(', ') : defaultText;
  };


  const getStartMaxDate = () => {
    const maxDate = searchParams?.entryDateEnd
      ? DateTime.fromFormat(
        searchParams.entryDateEnd as string,
        API_DATE_FORMAT
      ).toFormat(DATE_PICKER_FORMAT)
      : DateTime.now().toFormat(DATE_PICKER_FORMAT);

    return maxDate;
  };

  const getEndMinDate = () => {
    const minDate = searchParams?.entryDateStart
      ? DateTime.fromFormat(
        searchParams.entryDateStart as string,
        API_DATE_FORMAT
      ).toFormat(DATE_PICKER_FORMAT)
      : undefined;

    return minDate;
  };

  const handleDateChange = (isStartDate: boolean) => (dates?: Date[]) => {
    if (!dates) return;

    const formattedDate =
      dates.length && dates[0]
        ? DateTime.fromJSDate(dates[0]).toFormat(API_DATE_FORMAT)
        : "";

    onSearchParamsChange(
      isStartDate ? "entryDateStart" : "entryDateEnd",
      formattedDate ? formattedDate : undefined
    );
  };

  const getDateValue = (isStartDate: boolean) => {
    const key = isStartDate ? 'entryDateStart' : 'entryDateEnd';
    if (searchParams?.[key]) {
      return DateTime.fromFormat(
        searchParams[key] as string,
        API_DATE_FORMAT
      ).toFormat(DATE_PICKER_FORMAT);
    }
    return undefined;
  };

  return (
    <Grid className="opening-search-input-grid">
      {/* Opening ID */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={openingIdInputRef}
          id="opening-id-input"
          name="opening-id"
          labelText="Opening ID"
          placeholder="Enter opening ID"
          onBlur={(e) => onSearchParamsChange('openingId', e.target.value ? Number(e.target.value) : undefined)}
          onKeyDown={enforceNumberInputOnKeyDown}
          onPaste={(e) => enforceNumberInputOnPaste(openingIdInputRef.current, e)}
        />
      </Column>

      {/* Opening Categories */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          placeholder={getMultiSelectPlaceholder('categories')}
          titleText="Opening category"
          id="category-multi-select"
          className="opening-search-multi-select"
          items={categoryQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('categories')}
          selectedItems={categoryQuery.data?.filter(data => searchParams?.categories?.includes(data.code ?? '')) ?? []}
        />
      </Column>

      {/* Opening Statuses */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="status-multiselect"
          className="opening-search-multi-select"
          titleText="Opening status"
          placeholder={getMultiSelectPlaceholder('openingStatuses')}
          items={OPENING_STATUS_LIST}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('openingStatuses')}
          selectedItems={OPENING_STATUS_LIST.filter(data => searchParams?.openingStatuses?.includes(data.code ?? '')) ?? []}
        />
      </Column>

      {/* File ID, aka License Number */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={fileIdInputRef}
          id="file-id-input"
          name="file-id"
          labelText="File ID"
          placeholder="Enter file ID"
          onBlur={(e) => onSearchParamsChange('licenseNumber', e.target.value ? e.target.value : undefined)}
        />
      </Column>

      {/* Licensee Opening ID */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={licenseeOpeningIdInputRef}
          id="licensee-opening-id-input"
          name="licensee-opening-id"
          labelText="Licensee opening ID"
          placeholder="Enter licensee opening ID"
          onBlur={(e) => onSearchParamsChange('licenseeOpeningId', e.target.value ? e.target.value : undefined)}
        />
      </Column>

      {/* Cut Block ID */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={cutBlockInputRef}
          id="cut-block-id-input"
          name="cut-block-id"
          labelText="Cut block"
          placeholder="Enter cut block"
          onBlur={(e) => onSearchParamsChange('cutBlockId', e.target.value ? e.target.value : undefined)}
        />
      </Column>

      {/* Client */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          placeholder={
            searchParams?.clientNumbers && searchParams.clientNumbers.length > 0
              ? searchParams?.clientNumbers.map((num) => matchingClients.find((c) => c.id === num)?.acronym ?? num).join(', ')
              : 'Choose one or more options'
          }
          titleText="Client"
          id="client-multi-select"
          className="opening-search-multi-select"
          items={matchingClients}
          itemToString={getClientLabel}
          onChange={
            (selected: { selectedItems: ForestClientAutocompleteResultDto[] }) => {
              const selectedClientNumber = selected.selectedItems.map(item => item.id) as string[];
              onSearchParamsChange('clientNumbers', selectedClientNumber.length > 0 ? selectedClientNumber : undefined);
            }
          }
          onInputValueChange={(changes) => {
            setClientSearchTerm(String(changes));
          }}
          selectedItems={matchingClients.filter(client => searchParams?.clientNumbers?.includes(client.id ?? '')) ?? []}
        />
      </Column>

      {/* Cutting Permit ID */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={cuttingPermitInputRef}
          id="cutting-permit-id-input"
          name="cutting-permit-id"
          labelText="Cutting permit"
          placeholder="Enter cutting permit"
          onBlur={(e) => onSearchParamsChange('cuttingPermitId', e.target.value ? e.target.value : undefined)}
        />
      </Column>

      {/* Org unit */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          placeholder={getMultiSelectPlaceholder('orgUnits')}
          titleText="Org unit"
          id="org-unit-multi-select"
          className="opening-search-multi-select"
          items={orgUnitQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('orgUnits')}
          selectedItems={orgUnitQuery.data?.filter(data => searchParams?.orgUnits?.includes(data.code ?? '')) ?? []}
        />
      </Column>

      {/* Timber mark */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={timberMarkInputRef}
          id="timber-mark-input"
          name="timber-mark"
          labelText="Timber mark"
          placeholder="Enter timber mark"
          onBlur={(e) => onSearchParamsChange('timberMark', e.target.value ? e.target.value : undefined)}
        />
      </Column>

      {/* Created on date range */}
      <Column sm={4} md={8} lg={16}>
        <label htmlFor="created-on-date-range" className="date-label">Created on date range</label>

        <Grid className="date-sub-grid">
          {/* Start date */}
          <Column sm={4} md={4} lg={6} max={4}>
            <DatePicker
              className="advanced-date-picker"
              datePickerType="single"
              dateFormat="Y/m/d"
              allowInput
              maxDate={getStartMaxDate()}
              onChange={handleDateChange(true)}
              value={getDateValue(true)}
            >
              <DatePickerInput
                id="start-date-picker-input-id"
                size="md"
                labelText="Start Date"
                placeholder="yyyy/mm/dd"
              />
            </DatePicker>
          </Column>

          {/* End date */}
          <Column sm={4} md={4} lg={6} max={4}>
            <DatePicker
              className="advanced-date-picker"
              datePickerType="single"
              dateFormat="Y/m/d"
              allowInput
              minDate={getEndMinDate()}
              maxDate={DateTime.now().toFormat(DATE_PICKER_FORMAT)}
              onChange={handleDateChange(false)}
              value={getDateValue(false)}
            >
              <DatePickerInput
                id="end-date-picker-input-id"
                size="md"
                labelText="End Date"
                placeholder="yyyy/mm/dd"
              />
            </DatePicker>
          </Column>
        </Grid>
      </Column>


      <Column sm={4} md={8} lg={16}>
        <CheckboxGroup
          legendText="Search openings by:"
          orientation="horizontal"
          warnText="Warning message goes here"
        >
          {/* Created by me */}
          <Checkbox
            ref={createdByMeCheckboxRef}
            id="created-by-me-checkbox"
            labelText="Created by me"
            onChange={handleCheckboxChange('isCreatedByUser')}
          />

          {/* FRPA Section 108 */}
          <Checkbox
            ref={frpaSectionCheckboxRef}
            id="frpa-section-108-checkbox"
            labelText="FRPA Section 108"
            onChange={handleCheckboxChange('submittedToFrpa')}
          />
        </CheckboxGroup>
      </Column>

    </Grid>
  );
}

export default OpeningsSearchInput;
