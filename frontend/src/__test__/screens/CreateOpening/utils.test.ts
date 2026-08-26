import { describe, it, expect } from 'vitest';
import { validateStepOne, validateStepTwo } from '../../../screens/CreateOpening/utils';
import { CreateOpeningFormType } from '../../../screens/CreateOpening/definitions';

describe('CreateOpening utils', () => {
  describe('validateStepOne', () => {
    it('marks missing required form fields as invalid and returns false', () => {
      const form: CreateOpeningFormType = {
        client: { id: 'client-id' },
        orgUnit: { id: 'org-unit-id' },
        category: { id: 'category-id' },
        licenseeOpeningId: { id: 'licensee-opening-id' },
        openingGrossArea: { id: 'opening-gross-area-input' },
        maxAllowablePermAccess: { id: 'max-allowable-perm-access-input' },
      };

      const { isValid, form: validated } = validateStepOne(form);

      expect(isValid).toBe(false);
      expect(validated.client?.isInvalid).toBe(true);
      expect(validated.orgUnit?.isInvalid).toBe(true);
      expect(validated.category?.isInvalid).toBe(true);
      expect(validated.file?.isInvalid).toBe(true);
      expect(validated.openingGrossArea?.isInvalid).toBe(true);
      expect(validated.maxAllowablePermAccess?.isInvalid).toBe(true);
    });

    it('marks licensee opening id invalid when it exceeds max length', () => {
      const longValue = 'a'.repeat(31);
      const form: CreateOpeningFormType = {
        client: { id: 'client-id', value: 'client-1' },
        orgUnit: { id: 'org-unit-id', value: 'OU' },
        category: { id: 'category-id', value: 'CAT' },
        licenseeOpeningId: { id: 'licensee-opening-id', value: longValue },
        file: { id: 'file-id', value: new File(['{}'], 'file.geojson', { type: 'application/json' }) },
        openingGrossArea: { id: 'opening-gross-area-input', value: '1' },
        maxAllowablePermAccess: { id: 'max-allowable-perm-access-input', value: '7' },
      };

      const { isValid, form: validated } = validateStepOne(form);

      expect(isValid).toBe(false);
      expect(validated.licenseeOpeningId?.isInvalid).toBe(true);
    });

    it('returns true when all required fields are valid', () => {
      const form: CreateOpeningFormType = {
        client: { id: 'client-id', value: 'client-1' },
        orgUnit: { id: 'org-unit-id', value: 'OU' },
        category: { id: 'category-id', value: 'CAT' },
        file: {
          id: 'file-id',
          value: new File(['{}'], 'file.geojson', { type: 'application/json' }),
          validatedObj: { geoJson: { type: 'FeatureCollection', features: [] }, geometryArea: 1 },
        },
        openingGrossArea: { id: 'opening-gross-area-input', value: '10' },
        maxAllowablePermAccess: { id: 'max-allowable-perm-access-input', value: '7' },
      };

      const { isValid, form: validated } = validateStepOne(form);

      expect(isValid).toBe(true);
      expect(validated.client?.isInvalid).not.toBe(true);
      expect(validated.file?.isInvalid).not.toBe(true);
    });

    it('marks invalid opening gross area values as invalid', () => {
      const form: CreateOpeningFormType = {
        client: { id: 'client-id', value: 'client-1' },
        orgUnit: { id: 'org-unit-id', value: 'OU' },
        category: { id: 'category-id', value: 'CAT' },
        file: { id: 'file-id', value: new File(['{}'], 'file.geojson', { type: 'application/json' }) },
        openingGrossArea: { id: 'opening-gross-area-input', value: '12345678' },
        maxAllowablePermAccess: { id: 'max-allowable-perm-access-input', value: '7' },
      };

      const { isValid, form: validated } = validateStepOne(form);
      expect(isValid).toBe(false);
      expect(validated.openingGrossArea?.isInvalid).toBe(true);
    });

    it('marks invalid max allowable permanent access values as invalid', () => {
      const form: CreateOpeningFormType = {
        client: { id: 'client-id', value: 'client-1' },
        orgUnit: { id: 'org-unit-id', value: 'OU' },
        category: { id: 'category-id', value: 'CAT' },
        file: { id: 'file-id', value: new File(['{}'], 'file.geojson', { type: 'application/json' }) },
        openingGrossArea: { id: 'opening-gross-area-input', value: '10' },
        maxAllowablePermAccess: { id: 'max-allowable-perm-access-input', value: '999' },
      };

      const { isValid, form: validated } = validateStepOne(form);
      expect(isValid).toBe(false);
      expect(validated.maxAllowablePermAccess?.isInvalid).toBe(true);
    });
  });

  describe('validateStepTwo', () => {
    it('returns invalid when fileId or cutBlock are missing', () => {
      const tenures = [
        { fileId: '  ', cuttingPermit: 'CP1', cutBlock: 'CB1', isPrimary: true },
        { fileId: 'F2', cuttingPermit: 'CP2', cutBlock: '  ', isPrimary: false },
      ];

      const result = validateStepTwo(tenures);

      expect(result.isValid).toBe(false);
      expect(result.trimmed[0].fileId).toBe('');
      expect(result.trimmed[1].cutBlock).toBe('');
      expect(result.errors).toEqual([
        { fileId: true, cutBlock: false },
        { fileId: false, cutBlock: true },
      ]);
    });

    it('returns hasPrimary false when no primary tenure exists', () => {
      const tenures = [
        { fileId: 'F1', cuttingPermit: 'CP1', cutBlock: 'CB1', isPrimary: false },
      ];

      const result = validateStepTwo(tenures);

      expect(result.isValid).toBe(true);
      expect(result.hasPrimary).toBe(false);
      expect(result.errors).toEqual([{ fileId: false, cutBlock: false }]);
    });

    it('returns valid when tenures are trimmed and a primary tenure exists', () => {
      const tenures = [
        { fileId: ' F1 ', cuttingPermit: ' CP1 ', cutBlock: ' CB1 ', isPrimary: true },
      ];

      const result = validateStepTwo(tenures);

      expect(result.isValid).toBe(true);
      expect(result.hasPrimary).toBe(true);
      expect(result.errors).toEqual([{ fileId: false, cutBlock: false }]);
      expect(result.trimmed[0]).toMatchObject({ fileId: 'F1', cuttingPermit: 'CP1', cutBlock: 'CB1' });
    });
  });
});
