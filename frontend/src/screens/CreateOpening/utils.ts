import { CreateOpeningFormType } from './definitions';

const LICENSEE_OPENING_ID_MAX_LEN = 30;

export function validateStepOne(form: CreateOpeningFormType): { isValid: boolean; form: CreateOpeningFormType } {
  let isValid = true;
  const updated = structuredClone(form);

  if (!updated.client?.value) {
    isValid = false;
    if (updated.client) updated.client.isInvalid = true;
  }
  if (!updated.locationCode?.value) {
    isValid = false;
    if (updated.locationCode) updated.locationCode.isInvalid = true;
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

  return { isValid, form: updated };
}
