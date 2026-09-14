import { fireEvent, render, screen } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import * as featureFlags from '@/utils/featureFlags';

const { mockNavigate, mockUseQuery } = vi.hoisted(() => ({
  mockNavigate: vi.fn(),
  mockUseQuery: vi.fn(),
}));

vi.mock('@tanstack/react-query', () => ({ useQuery: mockUseQuery }));
vi.mock('@/utils/featureFlags');
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom');
  return { ...actual, useNavigate: () => mockNavigate };
});

import TenureIdentification from '@/components/OpeningDetails/TenureIdentification';

const tenure = {
  cboaId: 11,
  revisionCount: 1,
  primaryTenure: true,
  fileId: 'F1',
  cutBlock: 'B1',
  cuttingPermit: 'CP1',
  timberMark: null,
  status: { code: 'A', description: 'Active' },
  plannedGrossArea: null,
  plannedNetArea: null,
};

describe('TenureIdentification', () => {
  beforeEach(() => {
    mockUseQuery.mockReturnValue({
      data: {
        content: [tenure],
        page: { number: 0, size: 10, totalElements: 1, totalPages: 1 },
        primary: tenure,
        totalUnfiltered: 1,
      },
      isFetching: false,
      isLoading: false,
    });
  });

  it('does not render Edit tenure information when the feature is gated', () => {
    vi.mocked(featureFlags.gatePostgresFeature).mockReturnValue(true);

    render(<TenureIdentification openingId={123} />);

    expect(screen.queryByRole('button', { name: 'Edit tenure information' })).toBeNull();
  });

  it('takes the user to the Edit Tenure route when the feature is not gated', () => {
    vi.mocked(featureFlags.gatePostgresFeature).mockReturnValue(false);

    render(<TenureIdentification openingId={123} />);
    fireEvent.click(screen.getByRole('button', { name: 'Edit tenure information' }));

    expect(mockNavigate).toHaveBeenCalledWith('/openings/123/edit-tenure');
  });
});
