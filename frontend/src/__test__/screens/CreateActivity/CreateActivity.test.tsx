import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import CreateActivity from '../../../screens/CreateActivity';
import { ActivityFormType } from '../../../screens/CreateActivity/definitions';
import { ApiError } from '../../../services/OpenApi';
import API from '../../../services/API';
import { showToast } from '../../../utils/Toast';
import { scrollToSection } from '../../../utils/InputUtils';

const mockNavigate = vi.fn();
const mockBlocker = {
  state: 'unblocked' as 'unblocked' | 'blocked',
  proceed: vi.fn(),
  reset: vi.fn(),
};

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useParams: () => ({ openingId: '123' }),
    useBlocker: () => mockBlocker,
  };
});

vi.mock('../../../services/API', () => ({
  default: {
    OpeningEndpointService: { getOpeningTombstone: vi.fn() },
    ActivityEndpointService: { uploadActivitySpatial: vi.fn(), createActivity: vi.fn() },
  },
}));

vi.mock('../../../utils/Toast', () => ({ showToast: { success: vi.fn(), error: vi.fn() } }));
vi.mock('../../../utils/InputUtils', async () => {
  const actual = await vi.importActual<typeof import('../../../utils/InputUtils')>('../../../utils/InputUtils');
  return { ...actual, scrollToSection: vi.fn() };
});

type FormMockProps = {
  form: ActivityFormType;
  setForm: React.Dispatch<React.SetStateAction<ActivityFormType>>;
  uploadError?: string;
  onUploadErrorDismiss: () => void;
  onFileAdded: (f: File) => void;
  isUploading: boolean;
  onStatusChangeAttempt: (s: string) => void;
};

vi.mock('../../../components/AddActivityForm', () => ({
  default: ({ form, setForm, uploadError, onUploadErrorDismiss, onFileAdded, isUploading, onStatusChangeAttempt }: FormMockProps) => (
    <div>
      <span data-testid="status">{form.status?.value ?? 'none'}</span>
      <span data-testid="base">{form.baseCode?.value ?? 'none'}</span>
      <span data-testid="uploading">{String(isUploading)}</span>
      <span data-testid="validated">{String(Boolean(form.file?.validatedObj))}</span>
      <span data-testid="file-invalid">{String(Boolean(form.file?.isInvalid))}</span>
      <span data-testid="upload-error">{uploadError ?? ''}</span>
      <span data-testid="base-invalid">{String(Boolean(form.baseCode?.isInvalid))}</span>
      <button onClick={() => onStatusChangeAttempt('PLANNED')}>pick-planned</button>
      <button onClick={() => onStatusChangeAttempt('COMPLETED')}>pick-completed</button>
      <button
        onClick={() =>
          setForm((f) => ({
            ...f,
            baseCode: { ...f.baseCode, value: 'BR' },
            fundingSourceCodes: { ...f.fundingSourceCodes, value: ['VBE'] },
            completionDate: { ...f.completionDate, value: '2024-01-01' },
            treatedArea: { ...f.treatedArea, value: '2' },
          }))
        }
      >
        fill-valid
      </button>
      <button onClick={() => setForm((f) => ({ ...f, comment: { ...f.comment, value: 'x' } }))}>fill-comment</button>
      <button
        onClick={() => {
          const file = new File(['{}'], 'a.geojson');
          setForm((f) => ({ ...f, file: { ...f.file, value: file, validatedObj: undefined, isInvalid: false } }));
          onFileAdded(file);
        }}
      >
        add-file
      </button>
      <button onClick={() => setForm((f) => ({ ...f, file: { ...f.file, value: undefined, validatedObj: undefined } }))}>
        remove-file
      </button>
      <button onClick={onUploadErrorDismiss}>dismiss-upload</button>
    </div>
  ),
}));

const makeApiError = (status: number, body: unknown) =>
  new ApiError(
    { method: 'POST', url: '/x' },
    { url: '/x', ok: false, status, statusText: 'err', body },
    'err'
  );

const renderScreen = () => {
  const client = new QueryClient({ defaultOptions: { queries: { retry: false }, mutations: { retry: false } } });
  const invalidate = vi.spyOn(client, 'invalidateQueries');
  render(
    <QueryClientProvider client={client}>
      <MemoryRouter>
        <CreateActivity />
      </MemoryRouter>
    </QueryClientProvider>
  );
  return { invalidate };
};

const submit = () => userEvent.click(screen.getByRole('button', { name: 'Add activity' }));

const fillValidCompleted = async () => {
  const user = userEvent.setup();
  await user.click(screen.getByText('pick-completed'));
  await user.click(screen.getByText('fill-valid'));
  return user;
};

describe('CreateActivity screen', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockBlocker.state = 'unblocked';
    vi.mocked(API.OpeningEndpointService.getOpeningTombstone).mockResolvedValue({
      tombstone: { fileId: 'FID', cutBlockID: 'CB1' },
    } as never);
  });

  it('renders title, sets document title and shows opening subtitle', async () => {
    renderScreen();
    expect(screen.getByText('Add Activity - Opening 123')).toBeDefined();
    expect(document.title).toBe('Add Activity - Silva');
    expect(await screen.findByText('File ID FID, Cut block CB1')).toBeDefined();
  });

  it('uses placeholders for missing tombstone values', async () => {
    vi.mocked(API.OpeningEndpointService.getOpeningTombstone).mockResolvedValue({ tombstone: {} } as never);
    renderScreen();
    expect(await screen.findByText('File ID --, Cut block --')).toBeDefined();
  });

  it('sets status directly when the form is empty', async () => {
    const user = userEvent.setup();
    renderScreen();
    await user.click(screen.getByText('pick-planned'));
    expect(screen.getByTestId('status').textContent).toBe('PLANNED');
  });

  it('ignores selecting the current status', async () => {
    const user = userEvent.setup();
    renderScreen();
    await user.click(screen.getByText('pick-planned'));
    await user.click(screen.getByText('fill-comment'));
    await user.click(screen.getByText('pick-planned'));
    expect(screen.getByTestId('status').textContent).toBe('PLANNED');
  });

  it('confirms before switching status when data was entered and clears the form', async () => {
    const user = userEvent.setup();
    renderScreen();
    await user.click(screen.getByText('pick-planned'));
    await user.click(screen.getByText('fill-valid'));
    await user.click(screen.getByText('pick-completed'));
    expect(await screen.findByText('Switch activity status?')).toBeDefined();
    await user.click(screen.getByRole('button', { name: 'Proceed' }));
    expect(screen.getByTestId('status').textContent).toBe('COMPLETED');
    expect(screen.getByTestId('base').textContent).toBe('none');
  });

  it('keeps the form when status switch is cancelled', async () => {
    const user = userEvent.setup();
    renderScreen();
    await user.click(screen.getByText('pick-planned'));
    await user.click(screen.getByText('fill-valid'));
    await user.click(screen.getByText('pick-completed'));
    await user.click(screen.getAllByRole('button', { name: 'Cancel' }).at(-1) as HTMLElement);
    expect(screen.getByTestId('status').textContent).toBe('PLANNED');
    expect(screen.getByTestId('base').textContent).toBe('BR');
  });

  it('navigates straight back on cancel when nothing was touched', async () => {
    const user = userEvent.setup();
    renderScreen();
    await user.click(screen.getAllByRole('button', { name: 'Cancel' })[0]);
    expect(mockNavigate).toHaveBeenCalledWith('/openings/123?tab=activities');
  });

  it('asks to confirm leaving on cancel when dirty; stay and leave work', async () => {
    const user = userEvent.setup();
    renderScreen();
    await user.click(screen.getByText('pick-planned'));
    await user.click(screen.getAllByRole('button', { name: 'Cancel' })[0]);
    expect(await screen.findByText('Stay on this page')).toBeDefined();

    await user.click(screen.getByText('Stay on this page'));
    expect(mockBlocker.reset).toHaveBeenCalled();

    await user.click(screen.getAllByRole('button', { name: 'Cancel' })[0]);
    await user.click(await screen.findByText('Leave page'));
    expect(mockNavigate).toHaveBeenCalledWith('/openings/123?tab=activities');
  });

  it('proceeds through the router blocker when leaving', async () => {
    const user = userEvent.setup();
    mockBlocker.state = 'blocked';
    renderScreen();
    await user.click(await screen.findByText('Leave page'));
    expect(mockBlocker.proceed).toHaveBeenCalled();
    expect(mockNavigate).not.toHaveBeenCalled();
  });

  it('does not submit an invalid form and scrolls to the first invalid field', async () => {
    const user = userEvent.setup();
    renderScreen();
    await user.click(screen.getByText('pick-completed'));
    await submit();
    expect(API.ActivityEndpointService.createActivity).not.toHaveBeenCalled();
    expect(screen.getByTestId('base-invalid').textContent).toBe('true');
    expect(scrollToSection).toHaveBeenCalledWith('silv-base-code-dropdown');
  });

  it('creates the activity, toasts, invalidates and deep-links', async () => {
    vi.mocked(API.ActivityEndpointService.createActivity).mockResolvedValue({ activityId: 77 } as never);
    const { invalidate } = renderScreen();
    await fillValidCompleted();
    await submit();
    await waitFor(() =>
      expect(mockNavigate).toHaveBeenCalledWith('/openings/123?tab=activities&activityId=77')
    );
    expect(showToast.success).toHaveBeenCalledWith('Activity 77 created');
    expect(invalidate).toHaveBeenCalledWith({ queryKey: ['opening', 123, 'activities'], refetchType: 'all' });
    expect(invalidate).toHaveBeenCalledWith({ queryKey: ['opening', 'map', 123], refetchType: 'all' });
    expect(API.ActivityEndpointService.createActivity).toHaveBeenCalledWith(
      123,
      expect.objectContaining({ data: expect.objectContaining({ status: 'COMPLETED', baseCode: 'BR' }) })
    );
  });

  it('navigates without deep link when no id is returned', async () => {
    vi.mocked(API.ActivityEndpointService.createActivity).mockResolvedValue({} as never);
    renderScreen();
    await fillValidCompleted();
    await submit();
    await waitFor(() => expect(mockNavigate).toHaveBeenCalledWith('/openings/123?tab=activities'));
  });

  it.each([
    [400, 'Form validation failed: bad'],
    [403, 'Not authorized to create an activity for this opening.'],
    [404, 'Opening not found: bad'],
    [422, 'File rejected: bad'],
    [500, 'Server error. Please try again or contact support.'],
    [409, 'bad'],
  ])('shows a message for %s errors', async (status, expected) => {
    vi.mocked(API.ActivityEndpointService.createActivity).mockRejectedValue(makeApiError(status, { detail: 'bad' }));
    renderScreen();
    await fillValidCompleted();
    await submit();
    expect(await screen.findByText(expected)).toBeDefined();
    expect(scrollToSection).toHaveBeenCalledWith('add-activity-title-col');
    expect(mockNavigate).not.toHaveBeenCalled();
  });

  it('falls back to a generic message for non ApiError failures and can dismiss it', async () => {
    vi.mocked(API.ActivityEndpointService.createActivity).mockRejectedValue(new Error('net'));
    renderScreen();
    const user = await fillValidCompleted();
    await submit();
    expect(await screen.findByText('Creation failed. Please try again.')).toBeDefined();
    await user.click(screen.getAllByRole('button', { name: /close/i })[0]);
    expect(screen.queryByText('Creation failed. Please try again.')).toBeNull();
  });

  it('stays silent on 401 errors', async () => {
    vi.mocked(API.ActivityEndpointService.createActivity).mockRejectedValue(makeApiError(401, {}));
    renderScreen();
    await fillValidCompleted();
    await submit();
    await waitFor(() => expect(API.ActivityEndpointService.createActivity).toHaveBeenCalled());
    expect(screen.queryByText(/failed|Server error/i)).toBeNull();
  });

  describe('spatial upload', () => {
    it('marks the file validated on success', async () => {
      vi.mocked(API.ActivityEndpointService.uploadActivitySpatial).mockResolvedValue({ geoJson: {} } as never);
      const user = userEvent.setup();
      renderScreen();
      await user.click(screen.getByText('pick-planned'));
      await user.click(screen.getByText('add-file'));
      await waitFor(() => expect(screen.getByTestId('validated').textContent).toBe('true'));
    });

    it('ignores a completed upload whose file was removed meanwhile', async () => {
      let resolveUpload: (v: never) => void = () => {};
      vi.mocked(API.ActivityEndpointService.uploadActivitySpatial).mockReturnValue(
        new Promise((r) => { resolveUpload = r as never; }) as never,
      );
      const user = userEvent.setup();
      renderScreen();
      await user.click(screen.getByText('add-file'));
      await user.click(screen.getByText('remove-file'));
      resolveUpload({ geoJson: {} } as never);
      await waitFor(() => expect(screen.getByTestId('uploading').textContent).toBe('false'));
      expect(screen.getByTestId('validated').textContent).toBe('false');
    });

    it('shows API error detail and can dismiss it', async () => {
      vi.mocked(API.ActivityEndpointService.uploadActivitySpatial).mockRejectedValue(makeApiError(422, { detail: 'bad geometry' }));
      const user = userEvent.setup();
      renderScreen();
      await user.click(screen.getByText('add-file'));
      expect(await screen.findByText('bad geometry')).toBeDefined();
      await user.click(screen.getByText('dismiss-upload'));
      expect(screen.getByTestId('upload-error').textContent).toBe('');
    });

    it('uses a generic message for unknown upload errors', async () => {
      vi.mocked(API.ActivityEndpointService.uploadActivitySpatial).mockRejectedValue(new Error('x'));
      renderScreen();
      await userEvent.click(screen.getByText('add-file'));
      expect(await screen.findByText('File upload failed. Please try again.')).toBeDefined();
    });

    it('ignores 401 upload errors', async () => {
      vi.mocked(API.ActivityEndpointService.uploadActivitySpatial).mockRejectedValue(makeApiError(401, {}));
      renderScreen();
      await userEvent.click(screen.getByText('add-file'));
      await waitFor(() => expect(API.ActivityEndpointService.uploadActivitySpatial).toHaveBeenCalledWith(123, expect.anything()));
      expect(screen.getByTestId('upload-error').textContent).toBe('');
    });
  });
});
