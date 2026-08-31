import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { describe, expect, it, beforeEach, vi } from 'vitest';

import type { TenureUpdateItemDto } from '@/services/OpenApi';
import type { EditTenureItem } from '@/screens/EditTenure/utils';

const {
  mockInvalidateQueries,
  mockNavigate,
  mockUpdateTenures,
  mockUseMutation,
  mockUseQuery,
  mockValidateTenureList,
  mockShowToastSuccess,
} = vi.hoisted(() => ({
  mockInvalidateQueries: vi.fn(),
  mockNavigate: vi.fn(),
  mockUpdateTenures: vi.fn(),
  mockUseMutation: vi.fn(),
  mockUseQuery: vi.fn(),
  mockValidateTenureList: vi.fn(),
  mockShowToastSuccess: vi.fn(),
}));

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom');
  return {
    ...actual,
    useBlocker: () => ({ state: 'unblocked' }),
    useNavigate: () => mockNavigate,
    useParams: () => ({ openingId: '123' }),
  };
});

vi.mock('@tanstack/react-query', () => ({
  useMutation: mockUseMutation,
  useQuery: mockUseQuery,
  useQueryClient: () => ({ invalidateQueries: mockInvalidateQueries }),
}));

vi.mock('@/components/PageTitle', () => ({ default: () => <div /> }));
vi.mock('@/components/Modals/LeavePageModal', () => ({ default: () => null }));
vi.mock('@/components/TenureListInput', () => ({
  default: ({ setTenures, tenures }: { setTenures: (tenures: EditTenureItem[]) => void; tenures: EditTenureItem[] }) => (
    <>
      <p data-testid="tenure-count">{tenures.length}</p>
      <button
        type="button"
        onClick={() => setTenures([{ ...tenures[0], fileId: 'UPDATED' }])}
      >
        Modify tenure
      </button>
    </>
  ),
}));
vi.mock('@/services/API', () => ({
  default: {
    OpeningEndpointService: {
      getOpeningTombstone: vi.fn(),
      getTenures: vi.fn(),
    },
    TenureEndpointService: {
      updateTenures: mockUpdateTenures,
    },
  },
}));
vi.mock('@/utils/TenureUtils', () => ({
  validateTenureList: mockValidateTenureList,
}));
vi.mock('@/utils/Toast', () => ({
  showToast: {
    success: mockShowToastSuccess,
  },
}));

import EditTenure from '@/screens/EditTenure';

const initialTenure = {
  cboaId: 1,
  revisionCount: 2,
  fileId: 'FILE-1',
  cuttingPermit: 'CP-1',
  cutBlock: 'BLOCK-1',
  primaryTenure: true,
};

type MutationConfig = {
  mutationFn: (payload: TenureUpdateItemDto[]) => PromiseLike<void>;
  onError: (error: unknown) => void;
  onSuccess: () => Promise<void>;
};

const configureQueries = () => {
  mockUseQuery.mockImplementation(({ queryKey }: { queryKey: unknown[] }) => {
    if (queryKey.includes('tenure')) {
      return { data: { content: [initialTenure] }, isError: false, isFetching: false };
    }

    return {
      data: { tombstone: { client: { clientNumber: 'CLIENT-1' } } },
      isError: false,
      isFetching: false,
    };
  });
};

const configureMutation = () => {
  mockUseMutation.mockImplementation((config: MutationConfig) => ({
    isPending: false,
    mutate: (payload: TenureUpdateItemDto[]) => {
      void config.mutationFn(payload).then(
        () => void config.onSuccess(),
        (error: unknown) => config.onError(error)
      );
    },
  }));
};

describe('EditTenure', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockInvalidateQueries.mockResolvedValue(undefined);
    mockUpdateTenures.mockResolvedValue(undefined);
    mockValidateTenureList.mockImplementation((tenures: EditTenureItem[]) => ({
      errors: undefined,
      hasPrimary: true,
      isValid: true,
      trimmed: tenures,
    }));
    configureQueries();
    configureMutation();
  });

  const renderReadyForm = async () => {
    render(<EditTenure />);

    await waitFor(() => {
      expect(screen.getByTestId('tenure-count')).toHaveTextContent('1');
    });
  };

  it('returns without calling the backend and shows a toast when no tenure changed', async () => {
    await renderReadyForm();

    fireEvent.click(screen.getByRole('button', { name: 'Save tenure information' }));

    expect(mockUpdateTenures).not.toHaveBeenCalled();
    expect(mockShowToastSuccess).toHaveBeenCalledWith('No tenure changes to save.');
    expect(mockNavigate).toHaveBeenCalledWith('/openings/123?tab=tenure-identification');
  });

  it('saves changed tenures, then shows a success toast and returns to Opening Details', async () => {
    await renderReadyForm();

    fireEvent.click(screen.getByRole('button', { name: 'Modify tenure' }));
    fireEvent.click(screen.getByRole('button', { name: 'Save tenure information' }));

    await waitFor(() => {
      expect(mockUpdateTenures).toHaveBeenCalledWith(123, 'CLIENT-1', [
        {
          cboaId: undefined,
          revisionCount: undefined,
          fileId: 'UPDATED',
          cuttingPermit: 'CP-1',
          cutBlock: 'BLOCK-1',
          isPrimary: true,
        },
      ]);
      expect(mockInvalidateQueries).toHaveBeenCalledWith({ queryKey: ['opening', 123, 'tenure'] });
      expect(mockShowToastSuccess).toHaveBeenCalledWith('Tenure information saved.');
      expect(mockNavigate).toHaveBeenCalledWith('/openings/123?tab=tenure-identification');
    });
  });

  it('does not show a success toast or redirect when saving fails', async () => {
    mockUpdateTenures.mockRejectedValueOnce(new Error('Save failed'));
    await renderReadyForm();

    fireEvent.click(screen.getByRole('button', { name: 'Modify tenure' }));
    fireEvent.click(screen.getByRole('button', { name: 'Save tenure information' }));

    await waitFor(() => {
      expect(screen.getByText('Unable to save tenure information')).toBeInTheDocument();
    });
    expect(mockShowToastSuccess).not.toHaveBeenCalled();
    expect(mockNavigate).not.toHaveBeenCalled();
  });
});
