import { describe, it, expect } from 'vitest';
import { DateTime } from 'luxon';
import {
  buildCreateActivityPayload,
  getApiErrorMessage,
  hasActivityFormData,
  validateActivityForm,
} from '../../../screens/CreateActivity/utils';
import { DefaultActivityForm } from '../../../screens/CreateActivity/constants';
import { ActivityFormType } from '../../../screens/CreateActivity/definitions';
import { ApiError } from '../../../services/OpenApi';

const makeApiError = (body: unknown) =>
  new ApiError(
    { method: 'POST', url: '/x' },
    { url: '/x', ok: false, status: 400, statusText: 'Bad', body },
    'Bad Request'
  );

const baseForm = (overrides: ActivityFormType = {}): ActivityFormType => ({
  ...structuredClone(DefaultActivityForm),
  baseCode: { id: 'b', value: 'PL' },
  fundingSourceCodes: { id: 'f', value: ['VBE'] },
  status: { id: 's', value: 'COMPLETED' },
  completionDate: { id: 'c', value: '2024-01-01' },
  treatedArea: { id: 't', value: '1.5' },
  ...overrides,
});

describe('getApiErrorMessage', () => {
  it('returns fallback for non ApiError', () => {
    expect(getApiErrorMessage(new Error('x'), 'fb')).toBe('fb');
  });

  it('prefers detail, then message, then fallback', () => {
    expect(getApiErrorMessage(makeApiError({ detail: 'd', message: 'm' }), 'fb')).toBe('d');
    expect(getApiErrorMessage(makeApiError({ message: 'm' }), 'fb')).toBe('m');
    expect(getApiErrorMessage(makeApiError({}), 'fb')).toBe('fb');
    expect(getApiErrorMessage(makeApiError(undefined), 'fb')).toBe('fb');
  });
});

describe('validateActivityForm', () => {
  it('passes for a valid completed form', () => {
    const { isValid } = validateActivityForm(baseForm());
    expect(isValid).toBe(true);
  });

  it('does not mutate the input form', () => {
    const form = baseForm({ baseCode: { id: 'b' } });
    validateActivityForm(form);
    expect(form.baseCode?.isInvalid).toBeUndefined();
  });

  it('flags missing base code and funding sources', () => {
    const { isValid, form } = validateActivityForm(
      baseForm({ baseCode: { id: 'b' }, fundingSourceCodes: { id: 'f', value: [] } })
    );
    expect(isValid).toBe(false);
    expect(form.baseCode?.isInvalid).toBe(true);
    expect(form.fundingSourceCodes?.isInvalid).toBe(true);
  });

  it('flags too many funding sources and objectives', () => {
    const six = ['1', '2', '3', '4', '5', '6'];
    const { isValid, form } = validateActivityForm(
      baseForm({
        fundingSourceCodes: { id: 'f', value: six },
        objectiveCodes: { id: 'o', value: six },
      })
    );
    expect(isValid).toBe(false);
    expect(form.fundingSourceCodes?.isInvalid).toBe(true);
    expect(form.objectiveCodes?.isInvalid).toBe(true);
  });

  it('flags over-length text fields', () => {
    const { isValid, form } = validateActivityForm(
      baseForm({
        projectNumber: { id: 'p', value: 'x'.repeat(11) },
        licenseeActivityId: { id: 'l', value: 'x'.repeat(31) },
        comment: { id: 'c', value: 'x'.repeat(2001) },
      })
    );
    expect(isValid).toBe(false);
    expect(form.projectNumber?.isInvalid).toBe(true);
    expect(form.licenseeActivityId?.isInvalid).toBe(true);
    expect(form.comment?.isInvalid).toBe(true);
  });

  describe('planned status', () => {
    const planned = (overrides: ActivityFormType = {}) =>
      baseForm({
        status: { id: 's', value: 'PLANNED' },
        plannedDate: { id: 'd', value: DateTime.now().plus({ days: 5 }).toFormat('yyyy-MM-dd') },
        plannedArea: { id: 'a', value: '2.5' },
        ...overrides,
      });

    it('accepts a future date and area', () => {
      expect(validateActivityForm(planned()).isValid).toBe(true);
    });

    it('requires planned date and area', () => {
      const { isValid, form } = validateActivityForm(
        planned({ plannedDate: { id: 'd' }, plannedArea: { id: 'a' } })
      );
      expect(isValid).toBe(false);
      expect(form.plannedDate?.isInvalid).toBe(true);
      expect(form.plannedArea?.isInvalid).toBe(true);
    });

    it('rejects past and unparsable planned dates', () => {
      const past = validateActivityForm(planned({ plannedDate: { id: 'd', value: '2000-01-01' } }));
      expect(past.form.plannedDate?.isInvalid).toBe(true);
      const bad = validateActivityForm(planned({ plannedDate: { id: 'd', value: 'nope' } }));
      expect(bad.form.plannedDate?.isInvalid).toBe(true);
    });
  });

  it('rejects an unparsable planned date for completed activities but allows past dates', () => {
    expect(
      validateActivityForm(baseForm({ plannedDate: { id: 'd', value: '2000-01-01' } })).isValid
    ).toBe(true);
    const bad = validateActivityForm(baseForm({ plannedDate: { id: 'd', value: 'nope' } }));
    expect(bad.isValid).toBe(false);
    expect(bad.form.plannedDate?.isInvalid).toBe(true);
  });

  it('validates planned area and cost format regardless of status', () => {
    const { isValid, form } = validateActivityForm(
      baseForm({
        plannedArea: { id: 'a', value: '0' },
        plannedCost: { id: 'c', value: '1.5' },
      })
    );
    expect(isValid).toBe(false);
    expect(form.plannedArea?.isInvalid).toBe(true);
    expect(form.plannedCost?.isInvalid).toBe(true);
  });

  describe('completed status', () => {
    it('requires completion date and treated area', () => {
      const { isValid, form } = validateActivityForm(
        baseForm({ completionDate: { id: 'c' }, treatedArea: { id: 't' } })
      );
      expect(isValid).toBe(false);
      expect(form.completionDate?.isInvalid).toBe(true);
      expect(form.treatedArea?.isInvalid).toBe(true);
    });

    it('rejects invalid treated area and actual cost', () => {
      const { form } = validateActivityForm(
        baseForm({
          treatedArea: { id: 't', value: '-1' },
          actualCost: { id: 'a', value: '1.5' },
        })
      );
      expect(form.treatedArea?.isInvalid).toBe(true);
      expect(form.actualCost?.isInvalid).toBe(true);
    });

    it('accepts a valid actual cost', () => {
      expect(
        validateActivityForm(baseForm({ actualCost: { id: 'a', value: '100' } })).isValid
      ).toBe(true);
    });
  });

  it('flags a file that has not been validated by the backend', () => {
    const file = new File(['x'], 'a.zip');
    const bad = validateActivityForm(baseForm({ file: { id: 'f', value: file } }));
    expect(bad.isValid).toBe(false);
    expect(bad.form.file?.isInvalid).toBe(true);

    const ok = validateActivityForm(
      baseForm({
        file: {
          id: 'f',
          value: file,
          validatedObj: {} as NonNullable<ActivityFormType['file']>['validatedObj'],
        },
      })
    );
    expect(ok.isValid).toBe(true);
  });
});

describe('buildCreateActivityPayload', () => {
  it('builds a completed payload with numeric conversions', () => {
    const payload = buildCreateActivityPayload(
      baseForm({
        techniqueCode: { id: 't', value: 'TQ' },
        methodCode: { id: 'm', value: 'MQ' },
        objectiveCodes: { id: 'o', value: ['O1'] },
        projectNumber: { id: 'p', value: 'P1' },
        licenseeActivityId: { id: 'l', value: 'L1' },
        comment: { id: 'c', value: 'hi' },
        plannedArea: { id: 'pa', value: '3' },
        plannedCost: { id: 'pc', value: '10' },
        actualCost: { id: 'ac', value: '20' },
      })
    );
    expect(payload).toMatchObject({
      status: 'COMPLETED',
      baseCode: 'PL',
      techniqueCode: 'TQ',
      methodCode: 'MQ',
      objectiveCodes: ['O1'],
      fundingSourceCodes: ['VBE'],
      projectNumber: 'P1',
      licenseeActivityId: 'L1',
      comment: 'hi',
      plannedArea: 3,
      plannedCost: 10,
      completionDate: '2024-01-01',
      treatedArea: 1.5,
      actualCost: 20,
    });
  });

  it('omits completion-only and empty fields for planned activities', () => {
    const payload = buildCreateActivityPayload(
      baseForm({
        status: { id: 's', value: 'PLANNED' },
        plannedDate: { id: 'd', value: '2999-01-01' },
      })
    );
    expect(payload.status).toBe('PLANNED');
    expect(payload.completionDate).toBeUndefined();
    expect(payload.treatedArea).toBeUndefined();
    expect(payload.actualCost).toBeUndefined();
    expect(payload.objectiveCodes).toBeUndefined();
    expect(payload.techniqueCode).toBeUndefined();
    expect(payload.plannedDate).toBe('2999-01-01');
  });

  it('falls back to safe defaults for an empty form', () => {
    const payload = buildCreateActivityPayload({});
    expect(payload.baseCode).toBe('');
    expect(payload.fundingSourceCodes).toEqual([]);
    expect(payload.treatedArea).toBeUndefined();
  });
});

describe('hasActivityFormData', () => {
  it('is false for the default form', () => {
    expect(hasActivityFormData(structuredClone(DefaultActivityForm))).toBe(false);
    expect(hasActivityFormData({})).toBe(false);
  });

  it.each([
    'baseCode',
    'techniqueCode',
    'methodCode',
    'projectNumber',
    'licenseeActivityId',
    'comment',
    'plannedDate',
    'plannedArea',
    'plannedCost',
    'completionDate',
    'treatedArea',
    'actualCost',
  ] as const)('is true when %s has a value', (key) => {
    expect(hasActivityFormData({ [key]: { id: 'x', value: 'v' } })).toBe(true);
  });

  it('is true for arrays and file input', () => {
    expect(hasActivityFormData({ objectiveCodes: { id: 'x', value: ['a'] } })).toBe(true);
    expect(hasActivityFormData({ fundingSourceCodes: { id: 'x', value: ['a'] } })).toBe(true);
    expect(hasActivityFormData({ file: { id: 'x', value: new File(['x'], 'a.zip') } })).toBe(true);
  });

  it('ignores status alone', () => {
    expect(hasActivityFormData({ status: { id: 's', value: 'PLANNED' } })).toBe(false);
  });
});
