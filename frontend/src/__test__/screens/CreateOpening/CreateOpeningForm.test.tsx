import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { CreateOpeningForm } from '@/screens/CreateOpening/CreateOpeningForm';
import { DefaultOpeningForm } from '@/screens/CreateOpening/constants';

// Mock dependencies
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: vi.fn(),
    useSearchParams: vi.fn(),
    useBlocker: vi.fn(() => ({ state: 'unblocked', proceed: vi.fn(), reset: vi.fn() })),
  };
});

vi.mock('@tanstack/react-query', () => ({
  useMutation: vi.fn((config) => ({
    mutate: vi.fn(),
    isPending: false,
    isError: false,
    error: null,
  })),
}));

vi.mock('@/components/CreateOpeningSteps', () => ({
  CreateOpeningStepOne: ({ form }: any) => <div data-testid="step-one">{form?.client?.value}</div>,
  CreateOpeningStepTwo: ({ form }: any) => <div data-testid="step-two">{form?.tenureInfo?.value?.length}</div>,
  CreateOpeningStepThree: ({ form }: any) => <div data-testid="step-three">{form?.client?.value}</div>,
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
    trimmed: [],
  })),
}));

vi.mock('@/utils/TenureUtils', () => ({
  sortValidatedTenures: vi.fn((tenures) => tenures),
}));

vi.mock('@/utils/InputUtils', () => ({
  scrollToSection: vi.fn(),
}));

describe('CreateOpeningForm', () => {
  const mockSetCurrentStep = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders Step 0 form initially', () => {
    render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={0}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.getByTestId('step-one')).toBeInTheDocument();
  });

  it('renders Step 1 form when currentStep is 1', () => {
    render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={1}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.getByTestId('step-two')).toBeInTheDocument();
  });

  it('renders Step 2 form when currentStep is 2', () => {
    render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={2}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.getByTestId('step-three')).toBeInTheDocument();
  });

  it('renders Cancel button on Step 0', () => {
    render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={0}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.getByRole('button', { name: /cancel/i })).toBeInTheDocument();
  });

  it('renders Previous button on Step 1', () => {
    render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={1}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.getByRole('button', { name: /previous/i })).toBeInTheDocument();
  });

  it('renders Previous button on Step 2', () => {
    render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={2}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.getByRole('button', { name: /previous/i })).toBeInTheDocument();
  });

  it('renders Next button on Steps 0 and 1', () => {
    const { rerender } = render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={0}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.getByRole('button', { name: /next/i })).toBeInTheDocument();

    rerender(
      <CreateOpeningForm
        type="TENURED"
        currentStep={1}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.getByRole('button', { name: /next/i })).toBeInTheDocument();
  });

  it('renders Create new opening button on Step 2', () => {
    render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={2}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.getByRole('button', { name: /create new opening/i })).toBeInTheDocument();
  });

  it('accepts type and currentStep props', () => {
    const { rerender } = render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={0}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.getByTestId('step-one')).toBeInTheDocument();

    rerender(
      <CreateOpeningForm
        type="TENURED"
        currentStep={1}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.getByTestId('step-two')).toBeInTheDocument();
  });

  it('renders form element with noValidate attribute', () => {
    const { container } = render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={0}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    const form = container.querySelector('form');
    expect(form).toHaveAttribute('novalidate');
  });

  it('does not render warning notification when no warning text', () => {
    render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={0}
        setCurrentStep={mockSetCurrentStep}
      />
    );
    expect(screen.queryByRole('status')).not.toBeInTheDocument();
  });

  it('handles multiple steps rendering correctly', () => {
    const { rerender } = render(
      <CreateOpeningForm
        type="TENURED"
        currentStep={0}
        setCurrentStep={mockSetCurrentStep}
      />
    );

    expect(screen.getByTestId('step-one')).toBeInTheDocument();
    expect(screen.queryByTestId('step-two')).not.toBeInTheDocument();
    expect(screen.queryByTestId('step-three')).not.toBeInTheDocument();

    rerender(
      <CreateOpeningForm
        type="TENURED"
        currentStep={1}
        setCurrentStep={mockSetCurrentStep}
      />
    );

    expect(screen.queryByTestId('step-one')).not.toBeInTheDocument();
    expect(screen.getByTestId('step-two')).toBeInTheDocument();
    expect(screen.queryByTestId('step-three')).not.toBeInTheDocument();

    rerender(
      <CreateOpeningForm
        type="TENURED"
        currentStep={2}
        setCurrentStep={mockSetCurrentStep}
      />
    );

    expect(screen.queryByTestId('step-one')).not.toBeInTheDocument();
    expect(screen.queryByTestId('step-two')).not.toBeInTheDocument();
    expect(screen.getByTestId('step-three')).toBeInTheDocument();
  });
});
