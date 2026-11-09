
import { useState } from "react";
import {
  Column,
  DatePicker,
  DatePickerInput,
  Dropdown,
  DropdownSkeleton,
  FileUploaderDropContainer,
  FileUploaderItem,
  Form,
  Grid,
  InlineLoading,
  InlineNotification,
  RadioTile,
  Stack,
  TextArea,
  TextInput,
} from "@carbon/react";
import { useQuery } from "@tanstack/react-query";
import { DateTime } from "luxon";
import API from "@/services/API";
import { CodeDescriptionDto } from "@/services/OpenApi";
import RequiredLabel from "@/components/RequiredLabel";
import EmptySection from "@/components/EmptySection";
import CustomMultiSelect from "@/components/CustomMultiSelect";
import MapPreview from "@/components/MapPreview";
import { codeDescriptionToDisplayText, extractCodesFromCodeDescriptionArr } from "@/utils/multiSelectUtils";
import { getMultiSelectPlaceholderHelper } from "@/utils/SearchUtils";
import {
  enforceDecimalInputOnKeyDown,
  enforceDecimalInputOnPaste,
  isValidDecimalInput,
} from "@/utils/InputUtils";
import { formatDatePickerDate, getDatePickerValue } from "@/utils/DateUtils";
import { DATE_PICKER_FORMAT } from "@/constants";
import { MAX_FILE_SIZE, ACCEPTED_FILE_TYPES, MAX_FILE_MB } from "@/components/CreateOpeningSteps/StepOne/constants";
import { ActivityFormType } from "@/screens/CreateActivity/definitions";
import { MAX_MULTISELECT_ITEMS, NON_BASIC_SILV_BASE_CODES } from "@/screens/CreateActivity/constants";

import './styles.scss';

type AddActivityFormProps = {
  form: ActivityFormType;
  setForm: React.Dispatch<React.SetStateAction<ActivityFormType>>;
  uploadError?: string;
  onUploadErrorDismiss?: () => void;
  onFileAdded: (file: File) => void;
  isUploading: boolean;
  onStatusChangeAttempt: (status: string) => void;
};

const AREA_MAX_INTEGER_DIGITS = 10;
const AREA_MAX_DECIMALS = 1;
const COST_MAX_INTEGER_DIGITS = 9;

const AddActivityForm = ({
  form,
  setForm,
  uploadError,
  onUploadErrorDismiss,
  onFileAdded,
  isUploading,
  onStatusChangeAttempt,
}: AddActivityFormProps) => {
  const [fileError, setFileError] = useState<string | null>(null);

  const status = form.status?.value;
  const isPlanned = status === 'PLANNED';
  const isCompleted = status === 'COMPLETED';

  const selectedBaseCode = form.baseCode?.value;
  const isNoneBasicCode = selectedBaseCode
    ? NON_BASIC_SILV_BASE_CODES.has(selectedBaseCode)
    : null;

  const silvBaseCodeQuery = useQuery({
    queryKey: ["codes", "silv-base"],
    queryFn: () => API.CodesEndpointService.getSilvBaseCodes(false),
    select: (data) => data.filter((base) => base.code !== "DN"),
  });

  const silvTechniqueCodeQuery = useQuery({
    queryKey: ["codes", "silv-technique"],
    queryFn: () => API.CodesEndpointService.getSilvTechniqueCodes(false),
  });

  const silvMethodCodeQuery = useQuery({
    queryKey: ["codes", "silv-method"],
    queryFn: () => API.CodesEndpointService.getSilvMethodCodes(false),
  });

  const silvObjectiveCodeQuery = useQuery({
    queryKey: ["codes", "silv-objective"],
    queryFn: () => API.CodesEndpointService.getSilvObjectiveCodes(false),
  });

  const silvFundSourceCodeQuery = useQuery({
    queryKey: ["codes", "silv-fund-source"],
    queryFn: () => API.CodesEndpointService.getSilvFundSourceCodes(false),
  });

  const selectedObjectiveCodes = form.objectiveCodes?.value ?? [];
  const selectedFundingSourceCodes = form.fundingSourceCodes?.value ?? [];

  const selectedObjectiveItems = (silvObjectiveCodeQuery.data ?? [])
    .filter((item) => selectedObjectiveCodes.includes(item.code ?? ''));
  const selectedFundingSourceItems = (silvFundSourceCodeQuery.data ?? [])
    .filter((item) => selectedFundingSourceCodes.includes(item.code ?? ''));

  // Visual cue only; the real limit is enforced by ignoring onChange past MAX_MULTISELECT_ITEMS below.
  const renderCappedItem = (item: CodeDescriptionDto, selectedCodes: string[]) => {
    const atCap = selectedCodes.length >= MAX_MULTISELECT_ITEMS && !selectedCodes.includes(item.code ?? '');
    return (
      <span className={atCap ? 'capped-multiselect-item--disabled' : undefined}>
        {codeDescriptionToDisplayText(item)}
      </span>
    );
  };

  const handleObjectivesChange = ({ selectedItems }: { selectedItems: CodeDescriptionDto[] }) => {
    if (selectedItems.length > MAX_MULTISELECT_ITEMS) return;
    setForm((prev) => ({
      ...prev,
      objectiveCodes: { ...prev.objectiveCodes, value: extractCodesFromCodeDescriptionArr(selectedItems), isInvalid: false },
    }));
  };

  const handleFundingSourceChange = ({ selectedItems }: { selectedItems: CodeDescriptionDto[] }) => {
    if (selectedItems.length > MAX_MULTISELECT_ITEMS) return;
    setForm((prev) => ({
      ...prev,
      fundingSourceCodes: {
        ...prev.fundingSourceCodes,
        value: extractCodesFromCodeDescriptionArr(selectedItems),
        isInvalid: selectedItems.length === 0,
      },
    }));
  };

  const minPlannedDate = DateTime.now().plus({ days: 1 }).toFormat(DATE_PICKER_FORMAT);

  const handlePlannedDateChange = (dates?: Date[]) => {
    const value = formatDatePickerDate(dates);
    setForm((prev) => ({
      ...prev,
      plannedDate: { ...prev.plannedDate, value, isInvalid: false },
    }));
  };

  const handleCompletionDateChange = (dates?: Date[]) => {
    const value = formatDatePickerDate(dates);
    setForm((prev) => ({
      ...prev,
      completionDate: { ...prev.completionDate, value, isInvalid: false },
    }));
  };

  const handleAreaBlur = (field: 'plannedArea' | 'treatedArea', required: boolean) =>
    (e: React.FocusEvent<HTMLInputElement>) => {
      const value = e.target.value;
      const invalid = required
        ? !value || !isValidDecimalInput(value, AREA_MAX_INTEGER_DIGITS, AREA_MAX_DECIMALS) || Number(value) <= 0
        : !!value && (!isValidDecimalInput(value, AREA_MAX_INTEGER_DIGITS, AREA_MAX_DECIMALS) || Number(value) <= 0);
      setForm((prev) => ({
        ...prev,
        [field]: { ...prev[field], value: value || undefined, isInvalid: invalid },
      }));
    };

  const handleCostBlur = (field: 'plannedCost' | 'actualCost') =>
    (e: React.FocusEvent<HTMLInputElement>) => {
      const value = e.target.value;
      const invalid = !!value && (!isValidDecimalInput(value, COST_MAX_INTEGER_DIGITS, 0) || Number(value) < 0);
      setForm((prev) => ({
        ...prev,
        [field]: { ...prev[field], value: value || undefined, isInvalid: invalid },
      }));
    };

  const validateFile = (f: File) => {
    if (f.size > MAX_FILE_SIZE) return `"${f.name}" exceeds ${MAX_FILE_MB} MB.`;
    const lc = f.name.toLowerCase();
    const byExt = [".geojson", ".gml", ".json"].some((ext) => lc.endsWith(ext));
    if (!byExt) return "File not supported. Please upload a valid file type: GeoJSON, GML.";
    return null;
  };

  const handleAddFile = (addedFiles: File[]) => {
    setFileError(null);
    const f = addedFiles?.[0];
    if (!f) return;

    const err = validateFile(f);
    if (err) {
      setForm((prev) => ({
        ...prev,
        file: { ...prev.file, value: undefined, validatedObj: undefined, isInvalid: true },
      }));
      setFileError(err);
      return;
    }

    setForm((prev) => ({
      ...prev,
      file: { ...prev.file, value: f, validatedObj: undefined, isInvalid: false },
    }));
    onFileAdded(f);
  };

  const handleFileDelete = () => {
    setFileError(null);
    setForm((prev) => ({
      ...prev,
      file: { ...prev.file, value: undefined, validatedObj: undefined, isInvalid: false },
    }));
  };

  const handleFileNotificationDismiss = () => {
    setFileError(null);
    onUploadErrorDismiss?.();
  };

  return (
    <Grid className="add-activity-form-grid">
      {/* Activity status */}
      <Column sm={4} md={8} lg={16}>
        <Stack orientation="vertical" gap={6}>
          <h3 className="default-heading-20px">Activity status</h3>
          <Stack className="activity-status-tile-group-stack" orientation="vertical" gap={4}>
            <RequiredLabel id="activity-status-label" htmlFor="activity-status-input">
              Select activity status
            </RequiredLabel>

            <Grid className="activity-status-tile-group-grid">
              <Column sm={4} md={8} lg={8}>
                <RadioTile
                  id="activity-status-planned-tile"
                  className="default-radio-tile"
                  name="activity-status"
                  value="PLANNED"
                  checked={status === "PLANNED"}
                  onChange={() => onStatusChangeAttempt("PLANNED")}
                >
                  <Stack orientation="vertical" gap={4}>
                    <div>Planned</div>
                    <div>A silviculture treatment or survey scheduled for future execution.</div>
                  </Stack>
                </RadioTile>
              </Column>

              <Column sm={4} md={8} lg={8}>
                <RadioTile
                  id="activity-status-completed-tile"
                  className="default-radio-tile"
                  name="activity-status"
                  value="COMPLETED"
                  checked={status === "COMPLETED"}
                  onChange={() => onStatusChangeAttempt("COMPLETED")}
                >
                  <Stack orientation="vertical" gap={4}>
                    <div>Completed</div>
                    <div>A silviculture treatment or survey performed and reported.</div>
                  </Stack>
                </RadioTile>
              </Column>
            </Grid>
          </Stack>
        </Stack>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <hr className="default-hr" />
      </Column>

      {status ? (
        <>
          {/* Activity base */}
          <Column sm={4} md={8} lg={16} max={8}>
            <Stack orientation="vertical" gap={6}>
              <h3 className="default-heading-20px">Activity base</h3>
              {silvBaseCodeQuery.isLoading ? (
                <DropdownSkeleton />
              ) : (
                <Dropdown
                  id="silv-base-code-dropdown"
                  titleText={
                    <RequiredLabel id="silv-base-code-label" htmlFor="silv-base-code-dropdown">
                      Activity base
                    </RequiredLabel>
                  }
                  label="Choose an option"
                  items={silvBaseCodeQuery.data ?? []}
                  itemToString={(item) => codeDescriptionToDisplayText(item)}
                  selectedItem={silvBaseCodeQuery.data?.find((item) => item.code === selectedBaseCode) ?? null}
                  onChange={({ selectedItem }) =>
                    setForm((prev) => ({
                      ...prev,
                      baseCode: { ...prev.baseCode, value: selectedItem?.code ?? undefined, isInvalid: false },
                    }))
                  }
                  invalid={form.baseCode?.isInvalid}
                  invalidText="Select an activity base"
                />
              )}
            </Stack>
          </Column>

          {/* Base form */}
          {selectedBaseCode ? (
            <Column sm={4} md={8} lg={16}>
              {
                isNoneBasicCode ? (
                  <EmptySection
                    title="This base code is not yet supported"
                    description="Please choose a different base code."
                    icon="Construction"
                  />
                ) : (
                  <Form>
                    <Grid className="activity-information-grid">
                      <Column sm={4} md={8} lg={16}>
                        <h3 className="default-heading-20px">Activity information</h3>
                      </Column>

                      <Column sm={4} md={4} lg={8}>
                        <Dropdown
                          id="silv-technique-dropdown"
                          titleText="Technique"
                          label="Choose an option"
                          items={silvTechniqueCodeQuery.data ?? []}
                          itemToString={(item) => codeDescriptionToDisplayText(item)}
                          selectedItem={silvTechniqueCodeQuery.data?.find((item) => item.code === form.techniqueCode?.value) ?? null}
                          onChange={({ selectedItem }) =>
                            setForm((prev) => ({
                              ...prev,
                              techniqueCode: { ...prev.techniqueCode, value: selectedItem?.code ?? undefined },
                            }))
                          }
                        />
                      </Column>

                      <Column sm={4} md={4} lg={8}>
                        <Dropdown
                          id="silv-method-dropdown"
                          titleText="Method"
                          label="Choose an option"
                          items={silvMethodCodeQuery.data ?? []}
                          itemToString={(item) => codeDescriptionToDisplayText(item)}
                          selectedItem={silvMethodCodeQuery.data?.find((item) => item.code === form.methodCode?.value) ?? null}
                          onChange={({ selectedItem }) =>
                            setForm((prev) => ({
                              ...prev,
                              methodCode: { ...prev.methodCode, value: selectedItem?.code ?? undefined },
                            }))
                          }
                        />
                      </Column>

                      <Column sm={4} md={4} lg={8}>
                        <CustomMultiSelect
                          id="activity-objective-codes-input"
                          className="default-search-multi-select"
                          titleText="Objectives"
                          placeholder={getMultiSelectPlaceholderHelper(selectedObjectiveCodes, 'Choose up to 5 options')}
                          items={silvObjectiveCodeQuery.data ?? []}
                          itemToString={(item) => codeDescriptionToDisplayText(item)}
                          itemToElement={(item) => renderCappedItem(item, selectedObjectiveCodes)}
                          selectedItems={selectedObjectiveItems}
                          onChange={handleObjectivesChange}
                          showSkeleton={silvObjectiveCodeQuery.isLoading}
                        />
                      </Column>

                      <Column sm={4} md={4} lg={8}>
                        <CustomMultiSelect
                          id="activity-funding-source-input"
                          className="default-search-multi-select"
                          titleText={
                            <RequiredLabel id="activity-funding-source-label" htmlFor="activity-funding-source-input">
                              Funding source
                            </RequiredLabel>
                          }
                          placeholder={getMultiSelectPlaceholderHelper(selectedFundingSourceCodes, 'Choose up to 5 options')}
                          items={silvFundSourceCodeQuery.data ?? []}
                          itemToString={(item) => codeDescriptionToDisplayText(item)}
                          itemToElement={(item) => renderCappedItem(item, selectedFundingSourceCodes)}
                          selectedItems={selectedFundingSourceItems}
                          onChange={handleFundingSourceChange}
                          invalid={form.fundingSourceCodes?.isInvalid}
                          invalidText="Select at least one funding source"
                          showSkeleton={silvFundSourceCodeQuery.isLoading}
                        />
                      </Column>

                      <Column sm={4} md={4} lg={8}>
                        <TextInput
                          id={form.projectNumber?.id ?? ''}
                          labelText="Project number"
                          placeholder="Enter project number"
                          defaultValue={form.projectNumber?.value ?? ''}
                          maxLength={10}
                          onBlur={(e) =>
                            setForm((prev) => ({
                              ...prev,
                              projectNumber: { ...prev.projectNumber, value: e.target.value || undefined },
                            }))
                          }
                        />
                      </Column>

                      <Column sm={4} md={4} lg={8}>
                        <TextInput
                          id={form.licenseeActivityId?.id ?? ''}
                          labelText="Licensee activity ID"
                          placeholder="Enter licensee activity ID"
                          defaultValue={form.licenseeActivityId?.value ?? ''}
                          maxLength={30}
                          onBlur={(e) =>
                            setForm((prev) => ({
                              ...prev,
                              licenseeActivityId: { ...prev.licenseeActivityId, value: e.target.value || undefined },
                            }))
                          }
                        />
                      </Column>

                      <Column sm={4} md={4} lg={8}>
                        <DatePicker
                          datePickerType="single"
                          dateFormat="Y/m/d"
                          minDate={isPlanned ? minPlannedDate : undefined}
                          value={getDatePickerValue(form.plannedDate?.value)}
                          onChange={handlePlannedDateChange}
                        >
                          <DatePickerInput
                            id={form.plannedDate?.id ?? ''}
                            size="md"
                            placeholder="yyyy/mm/dd"
                            labelText={
                              isPlanned ? (
                                <RequiredLabel id="activity-planned-date-label" htmlFor={form.plannedDate?.id ?? ''}>
                                  Planned date
                                </RequiredLabel>
                              ) : "Planned date"
                            }
                            invalid={form.plannedDate?.isInvalid}
                            invalidText={isPlanned ? "Planned date must be after today" : "Invalid date"}
                          />
                        </DatePicker>
                      </Column>

                      <Column sm={4} md={4} lg={8}>
                        <TextInput
                          id={form.plannedArea?.id ?? ''}
                          labelText={
                            isPlanned ? (
                              <RequiredLabel id="activity-planned-area-label" htmlFor={form.plannedArea?.id ?? ''}>
                                Planned area (ha)
                              </RequiredLabel>
                            ) : "Planned area (ha)"
                          }
                          placeholder="Enter planned area"
                          defaultValue={form.plannedArea?.value ?? ''}
                          invalid={form.plannedArea?.isInvalid}
                          invalidText="Must be greater than 0"
                          onKeyDown={(e) => enforceDecimalInputOnKeyDown(e, AREA_MAX_INTEGER_DIGITS, AREA_MAX_DECIMALS)}
                          onPaste={(e) => enforceDecimalInputOnPaste(e.currentTarget, e, AREA_MAX_INTEGER_DIGITS, AREA_MAX_DECIMALS)}
                          onBlur={handleAreaBlur('plannedArea', isPlanned)}
                        />
                      </Column>

                      <Column sm={4} md={4} lg={8}>
                        <TextInput
                          id={form.plannedCost?.id ?? ''}
                          labelText="Planned cost"
                          placeholder="Enter planned cost"
                          defaultValue={form.plannedCost?.value ?? ''}
                          invalid={form.plannedCost?.isInvalid}
                          invalidText="Must be a non-negative whole number"
                          onKeyDown={(e) => enforceDecimalInputOnKeyDown(e, COST_MAX_INTEGER_DIGITS, 0)}
                          onPaste={(e) => enforceDecimalInputOnPaste(e.currentTarget, e, COST_MAX_INTEGER_DIGITS, 0)}
                          onBlur={handleCostBlur('plannedCost')}
                        />
                      </Column>

                      <Column sm={0} md={4} lg={8} aria-hidden="true" />

                      {isCompleted ? (
                        <>
                          <Column sm={4} md={4} lg={8}>
                            <DatePicker
                              datePickerType="single"
                              dateFormat="Y/m/d"
                              value={getDatePickerValue(form.completionDate?.value)}
                              onChange={handleCompletionDateChange}
                            >
                              <DatePickerInput
                                id={form.completionDate?.id ?? ''}
                                size="md"
                                placeholder="yyyy/mm/dd"
                                labelText={
                                  <RequiredLabel id="activity-completion-date-label" htmlFor={form.completionDate?.id ?? ''}>
                                    Completion date
                                  </RequiredLabel>
                                }
                                invalid={form.completionDate?.isInvalid}
                                invalidText="Completion date is required"
                              />
                            </DatePicker>
                          </Column>

                          <Column sm={4} md={4} lg={8}>
                            <TextInput
                              id={form.treatedArea?.id ?? ''}
                              labelText={
                                <RequiredLabel id="activity-treated-area-label" htmlFor={form.treatedArea?.id ?? ''}>
                                  Treated area (ha)
                                </RequiredLabel>
                              }
                              placeholder="Enter treated area"
                              defaultValue={form.treatedArea?.value ?? ''}
                              invalid={form.treatedArea?.isInvalid}
                              invalidText="Must be greater than 0"
                              onKeyDown={(e) => enforceDecimalInputOnKeyDown(e, AREA_MAX_INTEGER_DIGITS, AREA_MAX_DECIMALS)}
                              onPaste={(e) => enforceDecimalInputOnPaste(e.currentTarget, e, AREA_MAX_INTEGER_DIGITS, AREA_MAX_DECIMALS)}
                              onBlur={handleAreaBlur('treatedArea', true)}
                            />
                          </Column>

                          <Column sm={4} md={4} lg={8}>
                            <TextInput
                              id={form.actualCost?.id ?? ''}
                              labelText="Actual cost"
                              placeholder="Enter actual cost"
                              defaultValue={form.actualCost?.value ?? ''}
                              invalid={form.actualCost?.isInvalid}
                              invalidText="Must be a non-negative whole number"
                              onKeyDown={(e) => enforceDecimalInputOnKeyDown(e, COST_MAX_INTEGER_DIGITS, 0)}
                              onPaste={(e) => enforceDecimalInputOnPaste(e.currentTarget, e, COST_MAX_INTEGER_DIGITS, 0)}
                              onBlur={handleCostBlur('actualCost')}
                            />
                          </Column>
                        </>
                      ) : null}

                      <Column sm={4} md={8} lg={16}>
                        <TextArea
                          id={form.comment?.id ?? ''}
                          labelText="Comment"
                          placeholder="Enter a comment"
                          defaultValue={form.comment?.value ?? ''}
                          maxLength={2000}
                          onBlur={(e) =>
                            setForm((prev) => ({
                              ...prev,
                              comment: { ...prev.comment, value: e.target.value || undefined },
                            }))
                          }
                        />
                      </Column>
                    </Grid>
                  </Form>
                )}
            </Column>
          ) : null}

          {/* Spatial information */}
          {selectedBaseCode && !isNoneBasicCode ? (
            <Column sm={4} md={8} lg={16}>
              <Stack gap={6} className="file-uploader-container">
                <h3 className="default-heading-20px">Spatial information</h3>

                {
                  uploadError || fileError
                    ? (
                      <InlineNotification
                        kind="error"
                        lowContrast
                        title={uploadError ? "File upload failed" : "Invalid file"}
                        subtitle={uploadError || fileError || ''}
                        onCloseButtonClick={handleFileNotificationDismiss}
                      />
                    )
                    : null
                }

                <div className="file-uploader-title">
                  <label htmlFor="activity-spatial-file-drop-container" className="default-label">
                    Upload activity map geometry
                  </label>
                  <p className="file-type-p">
                    Supported file types are .GeoJSON and .GML
                  </p>
                </div>

                <FileUploaderDropContainer
                  id="activity-spatial-file-drop-container"
                  accept={ACCEPTED_FILE_TYPES}
                  multiple={false}
                  disabled={isUploading}
                  labelText="Drag and drop the map file here or click to upload (max 25 MB)"
                  onAddFiles={(_evt, { addedFiles }) => handleAddFile(addedFiles)}
                  maxFileSize={MAX_FILE_SIZE}
                />

                {isUploading ? <InlineLoading description="Validating file…" status="active" /> : null}

                <MapPreview
                  geojson={form.file?.validatedObj?.geoJson as GeoJSON.FeatureCollection ?? null}
                  openingGeojson={form.file?.validatedObj?.openingGeoJson as GeoJSON.FeatureCollection ?? null}
                  mapType="activity"
                />

                {
                  form.file?.value
                    ? (
                      <FileUploaderItem
                        className="default-file-uploader-item"
                        name={form.file?.value?.name ?? "No file selected"}
                        status={form.file?.validatedObj ? "complete" : "edit"}
                        onDelete={handleFileDelete}
                        invalid={!!fileError}
                        errorSubject={fileError ?? undefined}
                      />
                    )
                    : null
                }
              </Stack>
            </Column>
          ) : null}
        </>
      ) : null}
    </Grid>
  );
};

export default AddActivityForm;

