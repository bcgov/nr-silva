import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import StepTwo from '../../../components/CreateOpeningSteps/StepTwo';

vi.mock('@/components/TenureListInput', async () => {
  const actual = await vi.importActual('@/components/TenureListInput');
  return {
    __esModule: true,
    default: vi.fn(({ tenures }) => <div>Tenure count: {tenures.length}</div>),
    ...actual,
  };
});

describe('CreateOpening StepTwo', () => {
  const defaultForm = {
    tenureInfo: {
      value: [
        { fileId: 'F1', cuttingPermit: 'CP1', cutBlock: 'CB1', isPrimary: true },
      ],
    },
  };

  it('renders the tenure information section and hidden notification by default', () => {
    render(
      <StepTwo form={defaultForm as any} setForm={vi.fn()} />
    );

    expect(screen.getByText('Tenure information')).toBeDefined();
    expect(screen.queryByText('Primary tenure required')).toBeNull();
    expect(screen.getByText('Primary tenure')).toBeDefined();
  });

  it('renders primary tenure error when showNoPrimaryError is true', () => {
    render(
      <StepTwo
        form={{ tenureInfo: { value: [] } } as any}
        setForm={vi.fn()}
        showNoPrimaryError
      />
    );

    expect(screen.getByText('Primary tenure required')).toBeDefined();
    expect(screen.getByText('At least one tenure must be set as primary.')).toBeDefined();
  });
});
