import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { TenureRequestDto, TenureValidationResponseDto, TenureValidationResultDto } from '@/services/OpenApi';
import TenureListInput from '../../components/TenureListInput';
import '@testing-library/jest-dom';

// Mock the TenureItemInput component
vi.mock('@/components/TenureListInput/TenureItemInput', () => ({
  default: ({ index, tenure, setTenure, onSetPrimary, deleteTenure, deleteDisabled, itemError }: any) => (
    <div data-testid={`tenure-item-${index}`} className="tenure-item">
      <input
        data-testid={`fileId-${index}`}
        value={tenure.fileId}
        onChange={(e) => setTenure({ ...tenure, fileId: e.target.value })}
        placeholder="File ID"
      />
      <input
        data-testid={`cuttingPermit-${index}`}
        value={tenure.cuttingPermit}
        onChange={(e) => setTenure({ ...tenure, cuttingPermit: e.target.value })}
        placeholder="Cutting Permit"
      />
      <input
        data-testid={`cutBlock-${index}`}
        value={tenure.cutBlock}
        onChange={(e) => setTenure({ ...tenure, cutBlock: e.target.value })}
        placeholder="Cut Block"
      />
      <button data-testid={`primary-${index}`} onClick={onSetPrimary}>
        Set Primary
      </button>
      <button
        data-testid={`delete-${index}`}
        onClick={deleteTenure}
        disabled={deleteDisabled}
      >
        Delete
      </button>
      {itemError && (
        <div data-testid={`error-${index}`} className={`error-${itemError.kind}`}>
          {itemError.subtitle}
        </div>
      )}
    </div>
  ),
}));

describe('TenureListInput Component', () => {
  const mockSetTenures = vi.fn();

  it('renders empty tenure on mount with empty array', () => {
    const tenures: TenureRequestDto[] = [];
    render(
      <TenureListInput tenures={tenures} setTenures={mockSetTenures} />
    );

    expect(mockSetTenures).toHaveBeenCalled();
  });

  it('renders multiple tenure items', () => {
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: true },
      { fileId: 'FILE002', cuttingPermit: 'CP002', cutBlock: 'CB002', isPrimary: false },
    ];

    render(
      <TenureListInput tenures={tenures} setTenures={mockSetTenures} />
    );

    expect(screen.getByTestId('tenure-item-0')).toBeInTheDocument();
    expect(screen.getByTestId('tenure-item-1')).toBeInTheDocument();
  });

  it('adds new tenure when add button clicked', () => {
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
    ];

    render(
      <TenureListInput tenures={tenures} setTenures={mockSetTenures} />
    );

    const addButton = screen.getByRole('button', { name: /add/i });
    fireEvent.click(addButton);

    expect(mockSetTenures).toHaveBeenCalledWith([
      tenures[0],
      expect.objectContaining({
        fileId: '',
        cuttingPermit: '',
        cutBlock: '',
        isPrimary: false,
      }),
    ]);
  });

  it('updates tenure when item changed', () => {
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
    ];

    render(
      <TenureListInput tenures={tenures} setTenures={mockSetTenures} />
    );

    const fileIdInput = screen.getByTestId('fileId-0');
    fireEvent.change(fileIdInput, { target: { value: 'FILE002' } });

    expect(mockSetTenures).toHaveBeenCalledWith([
      expect.objectContaining({ fileId: 'FILE002' }),
    ]);
  });

  it('sets tenure as primary', () => {
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
      { fileId: 'FILE002', cuttingPermit: 'CP002', cutBlock: 'CB002', isPrimary: true },
    ];

    render(
      <TenureListInput tenures={tenures} setTenures={mockSetTenures} />
    );

    const primaryButton = screen.getByTestId('primary-0');
    fireEvent.click(primaryButton);

    expect(mockSetTenures).toHaveBeenCalledWith([
      expect.objectContaining({ isPrimary: true }),
      expect.objectContaining({ isPrimary: false }),
    ]);
  });

  it('deletes tenure when delete button clicked', () => {
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
      { fileId: 'FILE002', cuttingPermit: 'CP002', cutBlock: 'CB002', isPrimary: false },
    ];

    render(
      <TenureListInput tenures={tenures} setTenures={mockSetTenures} />
    );

    const deleteButton = screen.getByTestId('delete-0');
    fireEvent.click(deleteButton);

    expect(mockSetTenures).toHaveBeenCalledWith([tenures[1]]);
  });

  it('disables delete when only one tenure exists', () => {
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
    ];

    render(
      <TenureListInput tenures={tenures} setTenures={mockSetTenures} />
    );

    const deleteButton = screen.getByTestId('delete-0');
    expect(deleteButton).toBeDisabled();
  });

  it('displays combo error when validation has field invalid error', () => {
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
    ];

    const validationResult: TenureValidationResponseDto = {
      validationResults: [
        {
          isValid: false,
          errorCode: TenureValidationResultDto.errorCode.FIELD_INVALID,
        },
      ],
    };

    render(
      <TenureListInput
        tenures={tenures}
        setTenures={mockSetTenures}
        validationResult={validationResult}
      />
    );

    const error = screen.getByTestId('error-0');
    expect(error).toBeInTheDocument();
    expect(error).toHaveClass('error-combo');
    expect(error.textContent).toContain('The combination of File ID');
  });

  it('displays duplicate error when duplicate conflict exists', () => {
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
    ];

    const validationResult: TenureValidationResponseDto = {
      validationResults: [{ isValid: true }, { isValid: true }],
      duplicateConflicts: [
        {
          duplicateIndices: [0, 1],
        },
      ],
    };

    render(
      <TenureListInput
        tenures={tenures}
        setTenures={mockSetTenures}
        validationResult={validationResult}
      />
    );

    const error = screen.getByTestId('error-0');
    expect(error).toHaveClass('error-duplicate');
    expect(error.textContent).toContain('This tenure is a duplicate of tenure #2');
  });

  it('displays existing opening error', () => {
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
    ];

    const validationResult: TenureValidationResponseDto = {
      validationResults: [
        {
          isValid: false,
          errorCode: TenureValidationResultDto.errorCode.TENURE_DUPLICATE_OPENING,
        },
      ],
    };

    render(
      <TenureListInput
        tenures={tenures}
        setTenures={mockSetTenures}
        validationResult={validationResult}
      />
    );

    const error = screen.getByTestId('error-0');
    expect(error.textContent).toContain('already linked to an existing opening');
  });

  it('calls onTenuresChange when tenure is updated', () => {
    const mockOnChange = vi.fn();
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
    ];

    render(
      <TenureListInput
        tenures={tenures}
        setTenures={mockSetTenures}
        onTenuresChange={mockOnChange}
      />
    );

    const fileIdInput = screen.getByTestId('fileId-0');
    fireEvent.change(fileIdInput, { target: { value: 'FILE002' } });

    expect(mockOnChange).toHaveBeenCalled();
  });

  it('calls onTenuresChange when tenure is deleted', () => {
    const mockOnChange = vi.fn();
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
      { fileId: 'FILE002', cuttingPermit: 'CP002', cutBlock: 'CB002', isPrimary: false },
    ];

    render(
      <TenureListInput
        tenures={tenures}
        setTenures={mockSetTenures}
        onTenuresChange={mockOnChange}
      />
    );

    const deleteButton = screen.getByTestId('delete-0');
    fireEvent.click(deleteButton);

    expect(mockOnChange).toHaveBeenCalled();
  });

  it('handles field errors prop correctly', () => {
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
      { fileId: 'FILE002', cuttingPermit: 'CP002', cutBlock: 'CB002', isPrimary: false },
    ];

    const fieldErrors = [{ fileId: true }, { cutBlock: true }];

    render(
      <TenureListInput
        tenures={tenures}
        setTenures={mockSetTenures}
        fieldErrors={fieldErrors}
      />
    );

    expect(screen.getByTestId('tenure-item-0')).toBeInTheDocument();
    expect(screen.getByTestId('tenure-item-1')).toBeInTheDocument();
  });

  it('renders Add button with correct icon', () => {
    const tenures: TenureRequestDto[] = [
      { fileId: 'FILE001', cuttingPermit: 'CP001', cutBlock: 'CB001', isPrimary: false },
    ];

    render(
      <TenureListInput tenures={tenures} setTenures={mockSetTenures} />
    );

    const addButton = screen.getByRole('button', { name: /add/i });
    expect(addButton).toBeInTheDocument();
  });
});
