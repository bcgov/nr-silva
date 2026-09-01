import { describe, expect, it } from 'vitest';

import { ApiError } from '@/services/OpenApi';
import type { OpeningDetailsTenureDto } from '@/services/OpenApi';
import {
  buildTenureUpdatePayload,
  getApiErrorMessage,
  hasTenureChanges,
  toEditTenure,
} from '@/screens/EditTenure/utils';

const createApiError = (body: unknown) => new ApiError(
  { method: 'PUT', url: '/api/openings/123/tenures' },
  { url: '/api/openings/123/tenures', status: 422, statusText: 'Unprocessable Entity', body },
  'Request failed'
);

describe('EditTenure utilities', () => {
  it('maps nullable API tenures to editable values while retaining association metadata', () => {
    const tenure: OpeningDetailsTenureDto = {
      cboaId: 11,
      revisionCount: 4,
      primaryTenure: true,
      fileId: null,
      cuttingPermit: null,
      cutBlock: null,
      timberMark: null,
      status: { code: 'A', description: 'Active' },
      plannedGrossArea: null,
      plannedNetArea: null,
    };

    expect(toEditTenure(tenure)).toEqual({
      cboaId: 11,
      revisionCount: 4,
      fileId: '',
      cuttingPermit: '',
      cutBlock: '',
      isPrimary: true,
    });
  });

  it('detects only value changes against the loaded baseline', () => {
    const baseline = [{ fileId: 'F1', cuttingPermit: '', cutBlock: 'B1', isPrimary: true }];

    expect(hasTenureChanges(structuredClone(baseline), baseline)).toBe(false);
    expect(hasTenureChanges([{ ...baseline[0], cutBlock: 'B2' }], baseline)).toBe(true);
  });

  it('retains CBOA identity only when a normalized tenure key still matches the baseline', () => {
    const initialTenures = [
      { cboaId: 1, revisionCount: 2, fileId: 'F1', cuttingPermit: 'CP1', cutBlock: 'B1', isPrimary: true },
    ];

    expect(buildTenureUpdatePayload([
      { fileId: ' F1 ', cuttingPermit: ' CP1 ', cutBlock: ' B1 ', isPrimary: false },
      { fileId: 'F2', cuttingPermit: '', cutBlock: 'B2', isPrimary: true },
    ], initialTenures)).toEqual([
      { cboaId: 1, revisionCount: 2, fileId: 'F1', cuttingPermit: 'CP1', cutBlock: 'B1', isPrimary: false },
      { cboaId: undefined, revisionCount: undefined, fileId: 'F2', cuttingPermit: undefined, cutBlock: 'B2', isPrimary: true },
    ]);
  });

  it('uses API detail, then API message, then the fallback for save errors', () => {
    expect(getApiErrorMessage(createApiError({ detail: 'Detailed failure' }), 'Fallback')).toBe('Detailed failure');
    expect(getApiErrorMessage(createApiError({ message: 'Message failure' }), 'Fallback')).toBe('Message failure');
    expect(getApiErrorMessage(new Error('Unknown'), 'Fallback')).toBe('Fallback');
  });
});
