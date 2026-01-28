import { ChangeEvent, use, useEffect, useRef, useState } from "react";
import { DateTime } from "luxon";
import { OpeningSearchParamsType } from "@/types/OpeningTypes";
import { Checkbox, CheckboxGroup, Column, ComboBox, DatePicker, DatePickerInput, Grid, Stack, TextInput, Tooltip } from "@carbon/react";
import { useMutation, useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { comboBoxStringFilter, enforceNumberInputOnKeyDown, enforceNumberInputOnPaste, getMultiSelectedCodes, handleAutoUpperInput, handleAutoUpperPaste } from "@/utils/InputUtils";
import { API_DATE_FORMAT, DATE_PICKER_FORMAT, OPENING_STATUS_LIST, VALID_MAPSHEET_GRID_LIST, VALID_MAPSHEET_LETTER_LIST, VALID_MAPSHEET_QUAD_LIST } from "@/constants";
import useRefWithSearchParam from "@/hooks/useRefWithSearchParam";
import { getClientLabel, getClientSimpleLabel } from "@/utils/ForestClientUtils";
import { CodeDescriptionDto, ForestClientAutocompleteResultDto } from "@/services/OpenApi";
import MapsheetKeyImg from "@/assets/img/opening-mapsheet-key-example.png";
import useBreakpoint from "@/hooks/UseBreakpoint";

import CustomMultiSelect from "../CustomMultiSelect";
import TooltipLabel from "../TooltipLabel";

import './styles.scss';


type props = {
  searchParams: OpeningSearchParamsType | undefined;
  onSearchParamsChange: (field: keyof OpeningSearchParamsType, value: unknown) => void;
}
const OpeningsSearchInput = ({ searchParams, onSearchParamsChange }: props) => {
  const breakpoint = useBreakpoint();
  const [clientSearchTerm, setClientSearchTerm] = useState<string>('');
  const [matchingClients, setMatchingClients] = useState<ForestClientAutocompleteResultDto[]>([]);

  const openingIdInputRef = useRef<HTMLInputElement>(null); //NUMBER(10,0)
  const fileIdInputRef = useRef<HTMLInputElement>(null); // VARCHAR2(10)
  const licenseeOpeningIdInputRef = useRef<HTMLInputElement>(null); //VARCHAR2(30)
  const cutBlockInputRef = useRef<HTMLInputElement>(null); //VARCHAR2(10)
  const cuttingPermitInputRef = useRef<HTMLInputElement>(null); //VARCHAR2(3)
  const timberMarkInputRef = useRef<HTMLInputElement>(null); //VARCHAR2(10)
  const createdByMeCheckboxRef = useRef<HTMLInputElement>(null);
  const frpaSectionCheckboxRef = useRef<HTMLInputElement>(null);
  const mapsheetSquareInputRef = useRef<HTMLInputElement>(null); //VARCHAR2(3)
  const openingNumberInputRef = useRef<HTMLInputElement>(null); // VARCHAR2(4)

  // Store initial clientNumbers on mount to prefetch only once
  const initialClientNumbersRef = useRef<string[] | undefined>(undefined);
  const hasLoadedInitialClientsRef = useRef(false);

  // Capture clientNumbers on first arrival of searchParams
  useEffect(() => {
    if (!hasLoadedInitialClientsRef.current && searchParams?.clientNumbers && searchParams.clientNumbers.length > 0) {
      initialClientNumbersRef.current = searchParams.clientNumbers;
    }
  }, [searchParams?.clientNumbers]);

  // Prefetch client data when searchParams contains clientNumbers
  const initialClientsQuery = useQuery({
    queryKey: ['forest-clients', 'byClientNumbers', initialClientNumbersRef.current],
    queryFn: () => API.ForestClientEndpointService.searchByClientNumbers(
      initialClientNumbersRef.current!,
      0,
      initialClientNumbersRef.current!.length
    ),
    enabled: !hasLoadedInitialClientsRef.current && !!(initialClientNumbersRef.current && initialClientNumbersRef.current.length > 0),
  });

  // Merge prefetched clients into matchingClients
  useEffect(() => {
    if (initialClientsQuery.data && initialClientsQuery.data.length > 0) {
      setMatchingClients((prev) => {
        const existing = prev ?? [];
        const existingIds = new Set(existing.map((c) => c.id));
        const toAdd = initialClientsQuery.data
          .filter((c) => c && c.clientNumber != null && !existingIds.has(c.clientNumber))
          .map((c) => ({
            id: c.clientNumber,
            acronym: c.acronym,
            name: c.name,
          }));
        return existing.concat(toAdd);
      });
      hasLoadedInitialClientsRef.current = true;
    }
  }, [initialClientsQuery.data]);

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
    }, 200);

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
  useRefWithSearchParam(mapsheetSquareInputRef, searchParams?.mapsheetSquare);
  useRefWithSearchParam(openingNumberInputRef, searchParams?.openingNumber);

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
    const maxDate = searchParams?.updateDateEnd
      ? DateTime.fromFormat(
        searchParams.updateDateEnd,
        API_DATE_FORMAT
      ).toFormat(DATE_PICKER_FORMAT)
      : DateTime.now().toFormat(DATE_PICKER_FORMAT);

    return maxDate;
  };

  const getEndMinDate = () => {
    const minDate = searchParams?.updateDateStart
      ? DateTime.fromFormat(
        searchParams.updateDateStart,
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
      isStartDate ? "updateDateStart" : "updateDateEnd",
      formattedDate ? formattedDate : undefined
    );
  };

  const getDateValue = (isStartDate: boolean) => {
    const key = isStartDate ? 'updateDateStart' : 'updateDateEnd';
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


      {/* Mapsheet Key aka Opening Number */}
      <Column sm={4} md={8} lg={16}>
        <TooltipLabel
          id="opening-mapsheet-key-label"
          label="Mapsheet key"
          align={(breakpoint === 'sm') ? 'bottom' : 'bottom-left'}
          tooltip={
            <div className="opening-mapsheet-tooltip">
              The mapsheet key consists of the mapsheet grid, letter, square, quad, sub-quad,
              and opening number.
              <img src={MapsheetKeyImg} alt="Mapsheet Key Example" className="example-image" />
              Example: An opening with 92L 045 0.0 123 would have:
              <ul className="default-bullet-list">
                <li>Mapsheet grid: 92</li>
                <li>Mapsheet letter: L</li>
                <li>Mapsheet square: 045</li>
                <li>Mapsheet quad: 0</li>
                <li>Mapsheet sub-quad: 0</li>
                <li>Opening number: 123</li>
              </ul>
            </div>
          }
        />

        <Stack
          orientation={(breakpoint === 'sm' || breakpoint === 'md') ? "vertical" : "horizontal"}
          gap={1}
          className="opening-mapsheet-key-stack"
        >
          {/* Mapsheet Grid */}
          <ComboBox
            id="mapsheet-grid-combobox"
            items={VALID_MAPSHEET_GRID_LIST}
            className="mapsheet-wide-input"
            placeholder="Grid"
            onChange={(data) => onSearchParamsChange('mapsheetGrid', data.selectedItem ? String(data.selectedItem) : undefined)}
            selectedItem={searchParams?.mapsheetGrid ?? null}
            shouldFilterItem={comboBoxStringFilter}
          />

          {/* Mapsheet Letter */}
          <ComboBox
            id="mapsheet-letter-combobox"
            className="mapsheet-narrow-input"
            items={VALID_MAPSHEET_LETTER_LIST}
            placeholder="Letter"
            onChange={(data) => onSearchParamsChange('mapsheetLetter', data.selectedItem ? String(data.selectedItem) : undefined)}
            selectedItem={searchParams?.mapsheetLetter ?? null}
            shouldFilterItem={comboBoxStringFilter}
          />

          {/* Mapsheet Square */}
          <TextInput
            ref={mapsheetSquareInputRef}
            id="mapsheet-square-input"
            className="mapsheet-narrow-input"
            name="mapsheet-square"
            labelText=""
            placeholder="Square"
            onInput={handleAutoUpperInput}
            onPaste={handleAutoUpperPaste}
            onBlur={(e) => onSearchParamsChange('mapsheetSquare', e.target.value ? e.target.value : undefined)}
          />

          {/* Mapsheet Quad */}
          <ComboBox
            id="mapsheet-quad-combobox"
            className="mapsheet-narrow-input"
            items={VALID_MAPSHEET_QUAD_LIST}
            placeholder="Quad"
            onChange={(data) => onSearchParamsChange('mapsheetQuad', data.selectedItem ? String(data.selectedItem) : undefined)}
            selectedItem={searchParams?.mapsheetQuad ?? null}
            shouldFilterItem={comboBoxStringFilter}
          />

          {
            (breakpoint !== 'sm' && breakpoint !== 'md')
              ? (<div className="quad-subquad-separator">.</div>)
              : null
          }

          {/* Mapsheet Sub-quad */}
          <ComboBox
            id="mapsheet-subquad-combobox"
            className="mapsheet-narrow-input"
            items={VALID_MAPSHEET_QUAD_LIST}
            placeholder="Subquad"
            onChange={(data) => onSearchParamsChange('mapsheetSubQuad', data.selectedItem ? String(data.selectedItem) : undefined)}
            selectedItem={searchParams?.mapsheetSubQuad ?? null}
            shouldFilterItem={comboBoxStringFilter}
          />

          {/* Opening Number */}
          <TextInput
            ref={openingNumberInputRef}
            id="opening-number-input"
            className="mapsheet-wide-input"
            name="opening-number"
            labelText=""
            placeholder="Opening Number"
            onInput={handleAutoUpperInput}
            onPaste={handleAutoUpperPaste}
            onBlur={(e) => onSearchParamsChange('openingNumber', e.target.value ? e.target.value : undefined)}
          />
        </Stack>

      </Column>

      {/* File ID, aka License Number */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={fileIdInputRef}
          id="file-id-input"
          name="file-id"
          labelText="File ID"
          placeholder="Enter file ID"
          onInput={handleAutoUpperInput}
          onPaste={handleAutoUpperPaste}
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
          onInput={handleAutoUpperInput}
          onPaste={handleAutoUpperPaste}
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
          onInput={handleAutoUpperInput}
          onPaste={handleAutoUpperPaste}
          onBlur={(e) => onSearchParamsChange('cutBlockId', e.target.value ? e.target.value : undefined)}
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
          onInput={handleAutoUpperInput}
          onPaste={handleAutoUpperPaste}
          onBlur={(e) => onSearchParamsChange('cuttingPermitId', e.target.value ? e.target.value : undefined)}
        />
      </Column>

      {/* Client */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          key={String(hasLoadedInitialClientsRef.current)}
          placeholder={
            searchParams?.clientNumbers && searchParams.clientNumbers.length > 0
              ? searchParams?.clientNumbers.map((num) => getClientSimpleLabel(matchingClients.find((c) => c.id === num))).join(', ')
              : 'Choose one or more options'
          }
          titleText={
            <TooltipLabel
              align={(breakpoint === 'sm') ? 'top' : 'top-left'}
              label="Client"
              tooltip="Type at least 2 characters to search clients, matching options will be loaded."
            />
          }
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
          showSkeleton={initialClientsQuery.isLoading}
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
          onInput={handleAutoUpperInput}
          onPaste={handleAutoUpperPaste}
          onBlur={(e) => onSearchParamsChange('timberMark', e.target.value ? e.target.value : undefined)}
        />
      </Column>

      {/* Checkboxes */}
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

      {/* Updated on date range */}
      <Column sm={4} md={8} lg={16}>
        <label htmlFor="updated-on-date-range" className="date-label">Updated on date range</label>

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
    </Grid>
  );
}

export default OpeningsSearchInput;
