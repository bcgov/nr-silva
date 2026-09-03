import { fireEvent, render, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';

const { mockNavigate, mockUseQuery } = vi.hoisted(() => ({
  mockNavigate: vi.fn(),
  mockUseQuery: vi.fn(),
}));

vi.mock('@tanstack/react-query', () => ({ useQuery: mockUseQuery }));
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
  it('takes the user to the Edit Tenure route for the displayed opening', () => {
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

    render(<TenureIdentification openingId={123} />);
    fireEvent.click(screen.getByRole('button', { name: 'Edit tenure information' }));

    expect(mockNavigate).toHaveBeenCalledWith('/openings/123/edit-tenure');
  });
});
