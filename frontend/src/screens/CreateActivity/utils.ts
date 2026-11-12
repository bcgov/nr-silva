import { DateTime } from 'luxon';
import { API_DATE_FORMAT } from '@/constants';
import { isValidDecimalInput } from '@/utils/InputUtils';
import { ApiError, CreateActivityRequestDto } from '@/services/OpenApi';
import { ActivityFormType } from './definitions';
import { MAX_MULTISELECT_ITEMS } from './constants';

/** Returns the API's useful error detail when it is available. */
export const getApiErrorMessage = (error: unknown, fallback: string) => {
  if (!(error instanceof ApiError)) return fallback;
  const body = error.body as { detail?: string; message?: string } | undefined;
  return body?.detail ?? body?.message ?? fallback;
};

const PROJECT_NUMBER_MAX_LEN = 10;
const LICENSEE_ACTIVITY_ID_MAX_LEN = 30;
const COMMENT_MAX_LEN = 2000;
const AREA_MAX_INTEGER_DIGITS = 10;
const AREA_MAX_DECIMALS = 1;
const COST_MAX_INTEGER_DIGITS = 9;

const isValidArea = (value: string) => isValidDecimalInput(value, AREA_MAX_INTEGER_DIGITS, AREA_MAX_DECIMALS) && Number(value) > 0;
const isValidCost = (value: string) => isValidDecimalInput(value, COST_MAX_INTEGER_DIGITS, 0) && Number(value) >= 0;

/**
 * Validates the activity form against the backend's `CreateActivityRequestDto` rules.
 * Conditional requirements (planned/completed fields) depend on the selected status, but
 * the decimal/area/cost format constraints apply to any value present regardless of status.
 */
export function validateActivityForm(form: ActivityFormType): { isValid: boolean; form: ActivityFormType } {
  let isValid = true;
  const updated = structuredClone(form);
  const isPlanned = updated.status?.value === 'PLANNED';
  const isCompleted = updated.status?.value === 'COMPLETED';

  if (!updated.baseCode?.value) {
    isValid = false;
    if (updated.baseCode) updated.baseCode.isInvalid = true;
  }

  const fundingSourceCodes = updated.fundingSourceCodes?.value ?? [];
  if (fundingSourceCodes.length === 0 || fundingSourceCodes.length > MAX_MULTISELECT_ITEMS) {
    isValid = false;
    if (updated.fundingSourceCodes) updated.fundingSourceCodes.isInvalid = true;
  }

  if ((updated.objectiveCodes?.value?.length ?? 0) > MAX_MULTISELECT_ITEMS) {
    isValid = false;
    if (updated.objectiveCodes) updated.objectiveCodes.isInvalid = true;
  }

  if (updated.projectNumber?.value && updated.projectNumber.value.length > PROJECT_NUMBER_MAX_LEN) {
    isValid = false;
    if (updated.projectNumber) updated.projectNumber.isInvalid = true;
  }

  if (updated.licenseeActivityId?.value && updated.licenseeActivityId.value.length > LICENSEE_ACTIVITY_ID_MAX_LEN) {
    isValid = false;
    if (updated.licenseeActivityId) updated.licenseeActivityId.isInvalid = true;
  }

  if (updated.comment?.value && updated.comment.value.length > COMMENT_MAX_LEN) {
    isValid = false;
    if (updated.comment) updated.comment.isInvalid = true;
  }

  // Planned date: only required (and future-dated) when the activity is planned; completed activities accept any date or none.
  const plannedDate = updated.plannedDate?.value;
  if (isPlanned) {
    const tomorrow = DateTime.now().plus({ days: 1 }).startOf('day');
    const parsed = plannedDate ? DateTime.fromFormat(plannedDate, API_DATE_FORMAT) : null;
    if (!plannedDate || !parsed?.isValid || parsed < tomorrow) {
      isValid = false;
      if (updated.plannedDate) updated.plannedDate.isInvalid = true;
    }
  } else if (plannedDate) {
    const parsed = DateTime.fromFormat(plannedDate, API_DATE_FORMAT);
    if (!parsed.isValid) {
      isValid = false;
      if (updated.plannedDate) updated.plannedDate.isInvalid = true;
    }
  }

  // Planned area/cost are always rendered and validated for format when present; only required when planned.
  const plannedArea = updated.plannedArea?.value;
  if ((isPlanned && !plannedArea) || (!!plannedArea && !isValidArea(plannedArea))) {
    isValid = false;
    if (updated.plannedArea) updated.plannedArea.isInvalid = true;
  }

  const plannedCost = updated.plannedCost?.value;
  if (plannedCost && !isValidCost(plannedCost)) {
    isValid = false;
    if (updated.plannedCost) updated.plannedCost.isInvalid = true;
  }

  if (isCompleted) {
    if (!updated.completionDate?.value) {
      isValid = false;
      if (updated.completionDate) updated.completionDate.isInvalid = true;
    }

    const treatedArea = updated.treatedArea?.value;
    if (!treatedArea || !isValidArea(treatedArea)) {
      isValid = false;
      if (updated.treatedArea) updated.treatedArea.isInvalid = true;
    }

    const actualCost = updated.actualCost?.value;
    if (actualCost && !isValidCost(actualCost)) {
      isValid = false;
      if (updated.actualCost) updated.actualCost.isInvalid = true;
    }
  }

  if (updated.file?.value && !updated.file.validatedObj) {
    isValid = false;
    updated.file = updated.file ?? {};
    updated.file.isInvalid = true;
  }

  return { isValid, form: updated };
}

/**
 * Builds the create-activity request payload. Completion-only fields are only sent when
 * completed, since the backend rejects them for planned activities.
 */
export function buildCreateActivityPayload(form: ActivityFormType): CreateActivityRequestDto {
  const isPlanned = form.status?.value === 'PLANNED';

  return {
    status: isPlanned ? CreateActivityRequestDto.status.PLANNED : CreateActivityRequestDto.status.COMPLETED,
    baseCode: form.baseCode?.value ?? '',
    techniqueCode: form.techniqueCode?.value || undefined,
    methodCode: form.methodCode?.value || undefined,
    objectiveCodes: form.objectiveCodes?.value?.length ? form.objectiveCodes.value : undefined,
    fundingSourceCodes: form.fundingSourceCodes?.value ?? [],
    projectNumber: form.projectNumber?.value || undefined,
    licenseeActivityId: form.licenseeActivityId?.value || undefined,
    comment: form.comment?.value || undefined,
    plannedDate: form.plannedDate?.value || undefined,
    plannedArea: form.plannedArea?.value ? Number(form.plannedArea.value) : undefined,
    plannedCost: form.plannedCost?.value ? Number(form.plannedCost.value) : undefined,
    completionDate: isPlanned ? undefined : form.completionDate?.value,
    treatedArea: isPlanned ? undefined : (form.treatedArea?.value ? Number(form.treatedArea.value) : undefined),
    actualCost: isPlanned ? undefined : (form.actualCost?.value ? Number(form.actualCost.value) : undefined),
  };
}

/** Whether the user has entered anything beyond the initial empty state (excluding the status itself). */
export function hasActivityFormData(form: ActivityFormType): boolean {
  return Boolean(
    form.baseCode?.value
    || form.techniqueCode?.value
    || form.methodCode?.value
    || form.objectiveCodes?.value?.length
    || form.fundingSourceCodes?.value?.length
    || form.projectNumber?.value
    || form.licenseeActivityId?.value
    || form.comment?.value
    || form.plannedDate?.value
    || form.plannedArea?.value
    || form.plannedCost?.value
    || form.completionDate?.value
    || form.treatedArea?.value
    || form.actualCost?.value
    || form.file?.value
  );
}
