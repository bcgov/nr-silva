import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { CreateOpeningForm } from '@/screens/CreateOpening/CreateOpeningForm';
import { DefaultOpeningForm } from '@/screens/CreateOpening/constants';
import { ApiError } from '@/services/OpenApi';
import API from '@/services/API';
import * as openingUtils from '@/screens/CreateOpening/utils';
import * as inputUtils from '@/utils/InputUtils';

const mockNavigate = vi.fn();
let mockBlockerState = { state: 'unblocked' as 'unblocked' | 'blocked', proceed: vi.fn(), reset: vi.fn() };

// Mock dependencies
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useBlocker: vi.fn((fn: () => boolean) => mockBlockerState),
  };
});

vi.mock('@/components/CreateOpeningSteps', () => ({
  CreateOpeningStepOne: ({ setForm, onUploadErrorDismiss, onFileAdded, uploadError }: any) => (
    <div data-testid="step-one">
      <button data-testid="add-file-btn" onClick={() => onFileAdded(new File(['data'], 'test.geojson'))}>Add File</button>
      <button data-testid="dismiss-upload-error" onClick={onUploadErrorDismiss}>Dismiss</button>
      {uploadError && <span data-testid="upload-error-text">{uploadError}</span>}
      <button
        data-testid="modify-form-btn"
        onClick={() => setForm((prev: any) => ({ ...prev, client: { ...prev.client, value: '00012345' } }))}
      >
        Modify Form
      </button>
      <button
        data-testid="set-file-btn"
        onClick={() =>
          setForm((prev: any) => ({
            ...prev,
            file: { ...prev.file, value: new Blob(['content']), validatedObj: { geometryArea: 15.5 } },
            openingGrossArea: { ...prev.openingGrossArea, value: '15.5' },
          }))
        }
      >
        Set Valid File
      </button>
    </div>
  ),
  CreateOpeningStepTwo: ({ onTenuresChange, validationResult, showNoPrimaryError, fieldErrors }: any) => (
    <div data-testid="step-two">
      <button data-testid="change-tenures-btn" onClick={onTenuresChange}>Change Tenures</button>
      {showNoPrimaryError && <span data-testid="no-primary-err">No primary</span>}
      {fieldErrors && <span data-testid="tenure-err">Field errors</span>}
      {validationResult && <span data-testid="val-result">Validation result</span>}
    </div>
  ),
  CreateOpeningStepThree: ({ setStep }: any) => (
    <div data-testid="step-three">
      <button data-testid="step3-set-step-number" onClick={() => setStep(1)}>Back to step 1</button>
      <button data-testid="step3-set-step-fn" onClick={() => setStep((prev: number) => prev - 1)}>Back via fn</button>
    </div>
  ),
}));

vi.mock('@/services/API', () => ({
  default: {
    OpeningCreateEndpointService: {
      uploadOpeningSpatialFile: vi.fn(),
      createOpening: vi.fn(),
    },
    TenureEndpointService: {
      validateTenures: vi.fn(),
    },
  },
}));

vi.mock('@/screens/CreateOpening/utils', () => ({
  validateStepOne: vi.fn((form) => ({ isValid: true, form })),
  validateStepTwo: vi.fn(() => ({
    isValid: true,
    hasPrimary: true,
    errors: undefined,
    trimmed: [{ forestFileId: 'A12345', cutBlockId: 'CB1', isPrimary: true }],
  })),
}));

vi.mock('@/utils/TenureUtils', () => ({
  sortValidatedTenures: vi.fn((tenures) => tenures),
}));

vi.mock('@/utils/InputUtils', () => ({
  scrollToSection: vi.fn(),
}));

const createApiError = (status: number, message = 'Error', body?: any) => {
  return new ApiError(
    {} as any,
    { status, statusText: 'Error', url: '/api', body: body ?? { message } } as any,
    message
  );
};

const renderWithQueryClient = (ui: React.ReactElement) => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: { retry: false },
      mutations: { retry: false },
    },
  });
  const result = render(<QueryClientProvider client={queryClient}>{ui}</QueryClientProvider>);
  return {
    ...result,
    rerenderWithQueryClient: (newUi: React.ReactElement) =>
      result.rerender(<QueryClientProvider client={queryClient}>{newUi}</QueryClientProvider>),
  };
};

describe('CreateOpeningForm', () => {
  const mockSetCurrentStep = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    mockBlockerState = { state: 'unblocked', proceed: vi.fn(), reset: vi.fn() };
    vi.mocked(openingUtils.validateStepOne).mockImplementation((form) => ({ isValid: true, form }));
    vi.mocked(openingUtils.validateStepTwo).mockImplementation(() => ({
      isValid: true,
      hasPrimary: true,
      errors: undefined,
      trimmed: [{ forestFileId: 'A12345', cutBlockId: 'CB1', isPrimary: true }],
    }));
  });

  it('renders Step 0 form initially', () => {
    renderWithQueryClient(
      <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
    );
    expect(screen.getByTestId('step-one')).toBeInTheDocument();
  });

  it('renders Step 1 form when currentStep is 1', () => {
    renderWithQueryClient(
      <CreateOpeningForm type="TENURED" currentStep={1} setCurrentStep={mockSetCurrentStep} />
    );
    expect(screen.getByTestId('step-two')).toBeInTheDocument();
  });

  it('renders Step 2 form when currentStep is 2', () => {
    renderWithQueryClient(
      <CreateOpeningForm type="TENURED" currentStep={2} setCurrentStep={mockSetCurrentStep} />
    );
    expect(screen.getByTestId('step-three')).toBeInTheDocument();
  });

  it('prevents default on form submission', () => {
    const { container } = renderWithQueryClient(
      <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
    );
    const form = container.querySelector('form');
    expect(form).toHaveAttribute('novalidate');
    const submitEvent = fireEvent.submit(form!);
    expect(submitEvent).toBe(false);
  });

  describe('Step 0 Next navigation', () => {
    it('advances to step 1 and scrolls when step 1 validation succeeds', () => {
      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /next/i }));
      expect(mockSetCurrentStep).toHaveBeenCalledWith(1);
      expect(inputUtils.scrollToSection).toHaveBeenCalledWith('title-col');
    });

    it('stays on step 0 and scrolls to client input when step 1 validation fails', () => {
      vi.mocked(openingUtils.validateStepOne).mockReturnValue({
        isValid: false,
        form: { ...DefaultOpeningForm, client: { ...DefaultOpeningForm.client, isInvalid: true } },
      });

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /next/i }));
      expect(mockSetCurrentStep).not.toHaveBeenCalled();
      expect(inputUtils.scrollToSection).toHaveBeenCalledWith('opening-client-input');
    });
  });

  describe('Step 1 Next navigation & Tenure validation', () => {
    it('shows field errors and scrolls when step 2 validation fails', () => {
      vi.mocked(openingUtils.validateStepTwo).mockReturnValue({
        isValid: false,
        hasPrimary: true,
        errors: [{ fileId: true }],
        trimmed: [],
      });

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={1} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /next/i }));
      expect(screen.getByTestId('tenure-err')).toBeInTheDocument();
      expect(inputUtils.scrollToSection).toHaveBeenCalledWith('title-col');
    });

    it('shows no-primary error when step 2 has no primary tenure', () => {
      vi.mocked(openingUtils.validateStepTwo).mockReturnValue({
        isValid: true,
        hasPrimary: false,
        errors: undefined,
        trimmed: [{ forestFileId: 'A12345', cutBlockId: 'CB1', isPrimary: false }],
      });

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={1} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /next/i }));
      expect(screen.getByTestId('no-primary-err')).toBeInTheDocument();
      expect(inputUtils.scrollToSection).toHaveBeenCalledWith('title-col');
    });

    it('resets validation errors when tenures change', () => {
      vi.mocked(openingUtils.validateStepTwo).mockReturnValue({
        isValid: true,
        hasPrimary: false,
        errors: undefined,
        trimmed: [],
      });

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={1} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /next/i }));
      expect(screen.getByTestId('no-primary-err')).toBeInTheDocument();

      fireEvent.click(screen.getByTestId('change-tenures-btn'));
      expect(screen.queryByTestId('no-primary-err')).not.toBeInTheDocument();
    });

    it('advances to step 2 when tenure validation succeeds and isValid is true', async () => {
      vi.mocked(API.TenureEndpointService.validateTenures).mockResolvedValueOnce({
        isValid: true,
        tenures: [{ forestFileId: 'A12345', cutBlockId: 'CB1' }],
      } as any);

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={1} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /next/i }));

      await waitFor(() => {
        expect(mockSetCurrentStep).toHaveBeenCalledWith(2);
        expect(inputUtils.scrollToSection).toHaveBeenCalledWith('title-col');
      });
    });

    it('sets tenure validation result when tenure validation succeeds but isValid is false', async () => {
      vi.mocked(API.TenureEndpointService.validateTenures).mockResolvedValueOnce({
        isValid: false,
        tenures: [{ forestFileId: 'A12345', cutBlockId: 'CB1', isValid: false }],
      } as any);

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={1} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /next/i }));

      await waitFor(() => {
        expect(screen.getByTestId('val-result')).toBeInTheDocument();
        expect(mockSetCurrentStep).not.toHaveBeenCalledWith(2);
      });
    });

    it('handles tenure validation 401 error silently', async () => {
      vi.mocked(API.TenureEndpointService.validateTenures).mockRejectedValueOnce(
        createApiError(401, 'Unauthorized')
      );

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={1} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /next/i }));

      await waitFor(() => {
        expect(screen.queryByRole('status')).not.toBeInTheDocument();
      });
    });

    it('handles tenure validation non-401 error by displaying warning notification', async () => {
      vi.mocked(API.TenureEndpointService.validateTenures).mockRejectedValueOnce(
        createApiError(500, 'Tenure service unavailable', { detail: 'Service down' })
      );

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={1} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /next/i }));

      await waitFor(() => {
        expect(screen.getByText('Service down')).toBeInTheDocument();
      });

      // Dismiss warning
      const closeBtn = screen.getByRole('button', { name: /close notification/i });
      fireEvent.click(closeBtn);
      expect(screen.queryByText('Service down')).not.toBeInTheDocument();
    });
  });

  describe('File Upload Mutation', () => {
    it('handles successful file upload and updates form', async () => {
      vi.mocked(API.OpeningCreateEndpointService.uploadOpeningSpatialFile).mockResolvedValueOnce({
        geometryArea: 42.5,
        geoJson: { type: 'FeatureCollection', features: [] },
      } as any);

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByTestId('add-file-btn'));

      await waitFor(() => {
        expect(inputUtils.scrollToSection).toHaveBeenCalled();
      });
    });

    it('handles 401 file upload error silently', async () => {
      vi.mocked(API.OpeningCreateEndpointService.uploadOpeningSpatialFile).mockRejectedValueOnce(
        createApiError(401, 'Unauthorized')
      );

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByTestId('add-file-btn'));

      await waitFor(() => {
        expect(screen.queryByTestId('upload-error-text')).not.toBeInTheDocument();
      });
    });

    it('handles non-401 file upload error and allows dismiss', async () => {
      vi.mocked(API.OpeningCreateEndpointService.uploadOpeningSpatialFile).mockRejectedValueOnce(
        createApiError(400, 'Invalid GeoJSON', { detail: 'Corrupted coordinates' })
      );

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByTestId('add-file-btn'));

      await waitFor(() => {
        expect(screen.getByTestId('upload-error-text')).toHaveTextContent('Corrupted coordinates');
      });

      fireEvent.click(screen.getByTestId('dismiss-upload-error'));
      expect(screen.queryByTestId('upload-error-text')).not.toBeInTheDocument();
    });
  });

  describe('Step 2 & Creation', () => {
    it('allows StepThree to jump steps using number and functional updater', () => {
      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={2} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByTestId('step3-set-step-number'));
      expect(mockSetCurrentStep).toHaveBeenCalledWith(1);

      fireEvent.click(screen.getByTestId('step3-set-step-fn'));
      expect(mockSetCurrentStep).toHaveBeenCalledWith(1);
    });

    it('handles creation error when no file blob is present', async () => {
      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={2} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /create new opening/i }));

      await waitFor(() => {
        expect(screen.getByText('Creation failed. Please try again.')).toBeInTheDocument();
        expect(inputUtils.scrollToSection).toHaveBeenCalledWith('title-col');
      });
    });

    it('successfully creates opening and navigates to success page', async () => {
      vi.mocked(API.OpeningCreateEndpointService.createOpening).mockResolvedValueOnce({
        openingId: 99999,
      } as any);

      const { rerenderWithQueryClient } = renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByTestId('set-file-btn'));

      rerenderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={2} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /create new opening/i }));

      await waitFor(() => {
        expect(mockNavigate).toHaveBeenCalledWith('/openings/create/success?openingId=99999');
      });
    });

    it.each([
      [400, 'Form validation failed: Invalid request'],
      [403, 'Not authorized to create opening for this client.'],
      [404, 'Required resource not found: Missing org unit'],
      [422, 'File or coordinates invalid: Invalid polygon'],
      [500, 'Server error. Please try again or contact support.'],
    ])('maps creation error status %i to user-friendly message', async (status, expectedMsg) => {
      vi.mocked(API.OpeningCreateEndpointService.createOpening).mockRejectedValueOnce(
        createApiError(status, 'Detailed err', {
          message: status === 400 ? 'Invalid request' : status === 404 ? 'Missing org unit' : status === 422 ? 'Invalid polygon' : 'Internal error',
        })
      );

      const { rerenderWithQueryClient } = renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByTestId('set-file-btn'));

      rerenderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={2} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /create new opening/i }));

      await waitFor(() => {
        expect(screen.getByText(expectedMsg)).toBeInTheDocument();
      });
    });

    it('ignores 401 creation error silently', async () => {
      vi.mocked(API.OpeningCreateEndpointService.createOpening).mockRejectedValueOnce(
        createApiError(401, 'Unauthorized')
      );

      const { rerenderWithQueryClient } = renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByTestId('set-file-btn'));

      rerenderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={2} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /create new opening/i }));

      await waitFor(() => {
        expect(screen.queryByText(/unauthorized/i)).not.toBeInTheDocument();
      });
    });
  });

  describe('Navigation & Cancel Dialog', () => {
    it('navigates directly on Cancel when form is unmodified and on step 0', () => {
      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /cancel/i }));
      expect(mockNavigate).toHaveBeenCalledWith('/openings');
    });

    it('opens LeavePageModal on Cancel when navigation is blocked (e.g. form modified)', () => {
      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByTestId('modify-form-btn'));
      fireEvent.click(screen.getByRole('button', { name: /cancel/i }));

      expect(screen.getByRole('button', { name: /stay on this page/i })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /leave page/i })).toBeInTheDocument();
    });

    it('stays on page when clicking Stay in LeavePageModal', () => {
      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByTestId('modify-form-btn'));
      fireEvent.click(screen.getByRole('button', { name: /cancel/i }));

      fireEvent.click(screen.getByRole('button', { name: /stay on this page/i }));
      expect(mockBlockerState.reset).toHaveBeenCalled();
    });

    it('leaves page when clicking Leave in LeavePageModal without blocker', () => {
      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByTestId('modify-form-btn'));
      fireEvent.click(screen.getByRole('button', { name: /cancel/i }));

      fireEvent.click(screen.getByRole('button', { name: /leave page/i }));
      expect(mockNavigate).toHaveBeenCalledWith('/openings');
    });

    it('proceeds blocker when leaving page if blocker is in blocked state', () => {
      mockBlockerState = { state: 'blocked', proceed: vi.fn(), reset: vi.fn() };

      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={0} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByTestId('modify-form-btn'));
      fireEvent.click(screen.getByRole('button', { name: /cancel/i }));

      fireEvent.click(screen.getByRole('button', { name: /leave page/i }));
      expect(mockBlockerState.proceed).toHaveBeenCalled();
    });

    it('handles Previous button on step 1 to go back to step 0', () => {
      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={1} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /previous/i }));
      expect(mockSetCurrentStep).toHaveBeenCalledWith(0);
    });

    it('handles Previous button on step 2 to go back to step 1', () => {
      renderWithQueryClient(
        <CreateOpeningForm type="TENURED" currentStep={2} setCurrentStep={mockSetCurrentStep} />
      );

      fireEvent.click(screen.getByRole('button', { name: /previous/i }));
      expect(mockSetCurrentStep).toHaveBeenCalledWith(1);
    });
  });
});


