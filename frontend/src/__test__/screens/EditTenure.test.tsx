import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { describe, expect, it, beforeEach, vi } from 'vitest';

import { ApiError } from '@/services/OpenApi';
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
  mockParams,
  mockBlocker,
} = vi.hoisted(() => ({
  mockInvalidateQueries: vi.fn(),
  mockNavigate: vi.fn(),
  mockUpdateTenures: vi.fn(),
  mockUseMutation: vi.fn(),
  mockUseQuery: vi.fn(),
  mockValidateTenureList: vi.fn(),
  mockShowToastSuccess: vi.fn(),
  mockParams: { openingId: '123' as string | undefined },
  mockBlocker: { reset: vi.fn(), state: 'unblocked' as 'blocked' | 'unblocked' },
}));

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom');
  return {
    ...actual,
    useBlocker: () => mockBlocker,
    useNavigate: () => mockNavigate,
    useParams: () => mockParams,
  };
});

vi.mock('@tanstack/react-query', () => ({
  useMutation: mockUseMutation,
  useQuery: mockUseQuery,
  useQueryClient: () => ({ invalidateQueries: mockInvalidateQueries }),
}));

vi.mock('@/components/PageTitle', () => ({ default: () => <div /> }));
vi.mock('@/components/Modals/LeavePageModal', () => ({
  default: ({ open, onLeave, onStay }: { open: boolean; onLeave: () => void; onStay: () => void }) => open ? (
    <>
      <p>Leave confirmation</p>
      <button type="button" onClick={onStay}>Stay on this page</button>
      <button type="button" onClick={onLeave}>Leave page</button>
    </>
  ) : null,
}));
vi.mock('@/components/TenureListInput', () => ({
  default: ({
    fieldErrors,
    setTenures,
    tenures,
    validationResult,
  }: {
    fieldErrors?: Array<{ fileId?: boolean; cutBlock?: boolean }>;
    setTenures: (tenures: EditTenureItem[]) => void;
    tenures: EditTenureItem[];
    validationResult?: { validationResults?: Array<{ errorMessage?: string }> } | null;
  }) => (
    <>
      <p data-testid="tenure-count">{tenures.length}</p>
      {fieldErrors?.[0]?.fileId ? <p>File ID is required</p> : null}
      {validationResult?.validationResults?.[0]?.errorMessage ? <p>{validationResult.validationResults[0].errorMessage}</p> : null}
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
    mockParams.openingId = '123';
    mockBlocker.state = 'unblocked';
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

  it('shows an unavailable state instead of querying when the route omits an opening ID', () => {
    mockParams.openingId = undefined;

    render(<EditTenure />);

    expect(screen.getByText('Opening ID is missing')).toBeInTheDocument();
  });

  it('shows a not-found message when the tombstone query returns 404', () => {
    const notFound = new ApiError(
      { method: 'GET', url: '/api/openings/123/tombstone' },
      { url: '/api/openings/123/tombstone', status: 404, statusText: 'Not Found', body: undefined },
      'Not found'
    );
    mockUseQuery.mockImplementation(({ queryKey }: { queryKey: unknown[] }) => queryKey.includes('tenure')
      ? { data: undefined, error: undefined, isError: false, isFetching: false }
      : { data: undefined, error: notFound, isError: true, isFetching: false });

    render(<EditTenure />);

    expect(screen.getByText('Opening does not exist')).toBeInTheDocument();
    expect(screen.getByText('The opening you are trying to edit could not be found.')).toBeInTheDocument();
  });

  it('blocks saving when local validation finds a missing primary tenure', async () => {
    mockValidateTenureList.mockImplementation((tenures: EditTenureItem[]) => ({
      errors: [],
      hasPrimary: false,
      isValid: true,
      trimmed: tenures,
    }));
    await renderReadyForm();
    fireEvent.click(screen.getByRole('button', { name: 'Modify tenure' }));
    fireEvent.click(screen.getByRole('button', { name: 'Save tenure information' }));

    expect(screen.getByText('Primary tenure required')).toBeInTheDocument();
    expect(mockUpdateTenures).not.toHaveBeenCalled();
  });

  it('keeps the user on the form and exposes field validation errors before mutation', async () => {
    mockValidateTenureList.mockImplementation((tenures: EditTenureItem[]) => ({
      errors: [{ fileId: true, cutBlock: false }],
      hasPrimary: true,
      isValid: false,
      trimmed: tenures,
    }));
    await renderReadyForm();
    fireEvent.click(screen.getByRole('button', { name: 'Modify tenure' }));
    fireEvent.click(screen.getByRole('button', { name: 'Save tenure information' }));

    expect(screen.getByText('File ID is required')).toBeInTheDocument();
    expect(mockUpdateTenures).not.toHaveBeenCalled();
  });

  it('shows the generic load failure message when tenure data cannot be retrieved', () => {
    mockUseQuery.mockReturnValue({ data: undefined, error: new Error('Network error'), isError: true, isFetching: false });

    render(<EditTenure />);

    expect(screen.getByText('Unable to load tenure information')).toBeInTheDocument();
    expect(screen.getByText('Refresh the page and try again.')).toBeInTheDocument();
  });

  it('cancels immediately when no changes were made, but confirms discard after a change', async () => {
    await renderReadyForm();
    fireEvent.click(screen.getByRole('button', { name: 'Cancel' }));
    expect(mockNavigate).toHaveBeenCalledWith('/openings/123?tab=tenure-identification');

    mockNavigate.mockClear();
    fireEvent.click(screen.getByRole('button', { name: 'Modify tenure' }));
    fireEvent.click(screen.getByRole('button', { name: 'Cancel' }));
    expect(screen.getByText('Leave confirmation')).toBeInTheDocument();

    fireEvent.click(screen.getByRole('button', { name: 'Stay on this page' }));
    expect(mockBlocker.reset).toHaveBeenCalledOnce();

    fireEvent.click(screen.getByRole('button', { name: 'Cancel' }));
    fireEvent.click(screen.getByRole('button', { name: 'Leave page' }));
    expect(mockNavigate).toHaveBeenCalledWith('/openings/123?tab=tenure-identification');
  });

  it('shows server removal validation errors without redirecting', async () => {
    mockUpdateTenures.mockRejectedValueOnce(new ApiError(
      { method: 'PUT', url: '/api/openings/123/tenures' },
      {
        url: '/api/openings/123/tenures',
        status: 422,
        statusText: 'Unprocessable Entity',
        body: { removalErrors: [{ errorMessage: 'Tenure cannot be removed.' }] },
      },
      'Validation failed'
    ));
    await renderReadyForm();
    fireEvent.click(screen.getByRole('button', { name: 'Modify tenure' }));
    fireEvent.click(screen.getByRole('button', { name: 'Save tenure information' }));

    await waitFor(() => expect(screen.getByText('Tenure cannot be removed.')).toBeInTheDocument());
    expect(mockNavigate).not.toHaveBeenCalled();
  });

  it('passes server tenure validation to the editable list after a 422 response', async () => {
    mockUpdateTenures.mockRejectedValueOnce(new ApiError(
      { method: 'PUT', url: '/api/openings/123/tenures' },
      {
        url: '/api/openings/123/tenures',
        status: 422,
        statusText: 'Unprocessable Entity',
        body: { tenureValidation: { validationResults: [{ errorMessage: 'Tenure is stale.' }] } },
      },
      'Validation failed'
    ));
    await renderReadyForm();
    fireEvent.click(screen.getByRole('button', { name: 'Modify tenure' }));
    fireEvent.click(screen.getByRole('button', { name: 'Save tenure information' }));

    await waitFor(() => expect(screen.getByText('Tenure is stale.')).toBeInTheDocument());
  });
});
