import { CreateOpeningFormType } from './definitions';
import { isValidDecimalInput } from '@/utils/InputUtils';
export { validateTenureList as validateStepTwo } from '@/utils/TenureUtils';

const LICENSEE_OPENING_ID_MAX_LEN = 30;

export function validateStepOne(form: CreateOpeningFormType): { isValid: boolean; form: CreateOpeningFormType } {
  let isValid = true;
  const updated = structuredClone(form);

  if (!updated.client?.value) {
    isValid = false;
    if (updated.client) updated.client.isInvalid = true;
  }
  if (!updated.orgUnit?.value) {
    isValid = false;
    if (updated.orgUnit) updated.orgUnit.isInvalid = true;
  }
  if (!updated.category?.value) {
    isValid = false;
    if (updated.category) updated.category.isInvalid = true;
  }
  if (updated.licenseeOpeningId?.value && updated.licenseeOpeningId.value.length > LICENSEE_OPENING_ID_MAX_LEN) {
    isValid = false;
    if (updated.licenseeOpeningId) updated.licenseeOpeningId.isInvalid = true;
  }
  if (!updated.file?.value || !updated.file.validatedObj) {
    isValid = false;
    updated.file = updated.file || {};
    updated.file.isInvalid = true;
  }
  const openingGrossArea = updated.openingGrossArea?.value ?? '';
  if (!isValidDecimalInput(openingGrossArea, 7, 4) || Number(openingGrossArea) <= 0) {
    isValid = false;
    if (updated.openingGrossArea) updated.openingGrossArea.isInvalid = true;
  }

  const maxAllowablePermAccess = updated.maxAllowablePermAccess?.value ?? '';
  if (!isValidDecimalInput(maxAllowablePermAccess, 2, 1)) {
    isValid = false;
    if (updated.maxAllowablePermAccess) updated.maxAllowablePermAccess.isInvalid = true;
  }

  return { isValid, form: updated };
}
