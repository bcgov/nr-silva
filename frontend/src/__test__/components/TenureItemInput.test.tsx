import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { TenureRequestDto } from '@/services/OpenApi';
import TenureItemInput from '@/components/TenureListInput/TenureItemInput';
import '@testing-library/jest-dom';

// Mock utils
vi.mock('@/utils/InputUtils', () => ({
  handleAutoUpperInput: vi.fn((e, max) => {
    e.currentTarget.value = e.currentTarget.value.toUpperCase();
  }),
  handleAutoUpperPaste: vi.fn((e, max) => {
    const text = e.clipboardData.getData('text');
    e.currentTarget.value = text.toUpperCase();
  }),
}));

// Mock RequiredLabel
vi.mock('@/components/RequiredLabel', () => ({
  default: ({ children, ...props }: any) => <label {...props}>{children}*</label>,
}));

describe('TenureItemInput Component', () => {
  const mockSetTenure = vi.fn();
  const mockOnSetPrimary = vi.fn();
  const mockDeleteTenure = vi.fn();

  beforeEach(() => {
    mockSetTenure.mockClear();
    mockOnSetPrimary.mockClear();
    mockDeleteTenure.mockClear();
  });

  it('renders tenure item input fields', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
      />
    );

    const fileIdInput = screen.getByDisplayValue('FILE001');
    const permitInput = screen.getByDisplayValue('CP001');
    const blockInput = screen.getByDisplayValue('CB001');

    expect(fileIdInput).toBeInTheDocument();
    expect(permitInput).toBeInTheDocument();
    expect(blockInput).toBeInTheDocument();
  });

  it('displays "Primary tenure" label when isPrimary is true', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: true,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
      />
    );

    expect(screen.getByText('Primary tenure')).toBeInTheDocument();
  });

  it('does not display "Primary tenure" label when isPrimary is false', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
      />
    );

    expect(screen.queryByText('Primary tenure')).not.toBeInTheDocument();
  });

  it('displays "Set as primary tenure" checkbox when isPrimary is false', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
      />
    );

    expect(screen.getByLabelText('Set as primary tenure')).toBeInTheDocument();
  });

  it('calls setTenure when fileId input changes', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
      />
    );

    const fileIdInput = screen.getByDisplayValue('FILE001') as HTMLInputElement;
    fireEvent.change(fileIdInput, { target: { value: 'FILE002' } });

    expect(mockSetTenure).toHaveBeenCalledWith(
      expect.objectContaining({ fileId: 'FILE002' })
    );
  });

  it('calls setTenure when fileId input is pasted', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
      />
    );

    const fileIdInput = screen.getByDisplayValue('FILE001') as HTMLInputElement;
    fireEvent.paste(fileIdInput, {
      clipboardData: {
        getData: () => ' file002 ',
      },
    } as any);

    expect(mockSetTenure).toHaveBeenCalledWith(
      expect.objectContaining({ fileId: 'FILE002' })
    );
  });

  it('calls setTenure when cuttingPermit input changes', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
      />
    );

    const permitInput = screen.getByDisplayValue('CP001') as HTMLInputElement;
    fireEvent.change(permitInput, { target: { value: 'CP002' } });

    expect(mockSetTenure).toHaveBeenCalledWith(
      expect.objectContaining({ cuttingPermit: 'CP002' })
    );
  });

  it('calls setTenure when cutBlock input changes', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
      />
    );

    const blockInput = screen.getByDisplayValue('CB001') as HTMLInputElement;
    fireEvent.change(blockInput, { target: { value: 'CB002' } });

    expect(mockSetTenure).toHaveBeenCalledWith(
      expect.objectContaining({ cutBlock: 'CB002' })
    );
  });

  it('calls onSetPrimary when primary tenure checkbox is clicked', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
      />
    );

    const checkbox = screen.getByLabelText('Set as primary tenure');
    fireEvent.click(checkbox);

    expect(mockOnSetPrimary).toHaveBeenCalled();
  });

  it('displays delete button with correct text', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
      />
    );

    const removeButton = screen.getByText('Remove');
    expect(removeButton).toBeInTheDocument();
  });

  it('hides delete button when deleteDisabled is true', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteDisabled={true}
        deleteTenure={mockDeleteTenure}
      />
    );

    // When deleteDisabled is true, the delete button should not be rendered
    const removeButtons = screen.queryAllByText(/Remove/i);
    expect(removeButtons).toHaveLength(0);
  });

  it('calls deleteTenure when delete button is clicked', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteDisabled={false}
        deleteTenure={mockDeleteTenure}
      />
    );

    const removeButton = screen.getByText('Remove');
    fireEvent.click(removeButton);
    expect(mockDeleteTenure).toHaveBeenCalled();
  });

  it('displays file ID error when fieldErrors.fileId is true', () => {
    const tenure: TenureRequestDto = {
      fileId: '',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
        fieldErrors={{ fileId: true }}
      />
    );

    expect(screen.getByText('File ID is required.')).toBeInTheDocument();
  });

  it('displays cut block error when fieldErrors.cutBlock is true', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: '',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
        fieldErrors={{ cutBlock: true }}
      />
    );

    expect(screen.getByText('Cut block is required.')).toBeInTheDocument();
  });

  it('displays combo error when itemError.kind is combo', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
        itemError={{
          kind: 'combo',
          subtitle: 'The combination is incorrect.',
        }}
      />
    );

    expect(screen.getByText('Incorrect information:')).toBeInTheDocument();
    expect(screen.getByText('The combination is incorrect.')).toBeInTheDocument();
  });

  it('displays duplicate error when itemError.kind is duplicate', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
        itemError={{
          kind: 'duplicate',
          subtitle: 'This tenure is a duplicate.',
        }}
      />
    );

    expect(screen.getByText('Duplicate tenure')).toBeInTheDocument();
    expect(screen.getByText('This tenure is a duplicate.')).toBeInTheDocument();
  });

  it('trims input values when setTenure is called', () => {
    const tenure: TenureRequestDto = {
      fileId: 'FILE001',
      cuttingPermit: 'CP001',
      cutBlock: 'CB001',
      isPrimary: false,
    };

    render(
      <TenureItemInput
        index={0}
        tenure={tenure}
        setTenure={mockSetTenure}
        onSetPrimary={mockOnSetPrimary}
        deleteTenure={mockDeleteTenure}
      />
    );

    const fileIdInput = screen.getByDisplayValue('FILE001') as HTMLInputElement;
    fireEvent.change(fileIdInput, { target: { value: '  FILE002  ' } });

    expect(mockSetTenure).toHaveBeenCalledWith(
      expect.objectContaining({ fileId: 'FILE002' })
    );
  });
});
