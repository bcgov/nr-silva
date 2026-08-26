import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { renderWithProviders } from '../../utils/testAuthProvider';
import StepOne from '../../../components/CreateOpeningSteps/StepOne';

vi.mock('@tanstack/react-query', async () => {
  const actual = await vi.importActual('@tanstack/react-query');
  return {
    ...actual,
    useQuery: vi.fn(() => ({ data: [], isLoading: false, isError: false })),
  };
});

vi.mock('@carbon/react', async () => {
  const actual = await vi.importActual('@carbon/react');
  return {
    ...actual,
    FileUploaderItem: ({ name, onDelete }: any) => (
      <div>
        <span>{name}</span>
        <button type="button" onClick={onDelete}>Delete</button>
      </div>
    ),
  };
});

vi.mock('../../../contexts/AuthProvider', async () => {
  const actual = await vi.importActual('../../../contexts/AuthProvider');
  return {
    ...actual,
    useAuth: vi.fn(() => ({ user: { associatedClients: [] } })),
  };
});

describe('CreateOpening StepOne', () => {
  const mockSetForm = vi.fn();
  const mockOnFileAdded = vi.fn();

  beforeEach(() => {
    mockSetForm.mockReset();
    mockOnFileAdded.mockReset();
  });

  const defaultForm = {
    client: { id: 'opening-client-input' },
    file: { id: 'opening-map-file-drop-container' },
    orgUnit: { id: 'opening-org-unit-input' },
    category: { id: 'opening-category-input' },
    openingGrossArea: { id: 'opening-gross-area-input' },
    maxAllowablePermAccess: { id: 'opening-max-allowable-perm-access-input' },
  };

  function getFileInput(container: HTMLElement) {
    return container.querySelector('input[type=file]') as HTMLInputElement | null;
  }

  it('does not add unsupported file types', async () => {
    const { container } = render(
      <StepOne
        form={defaultForm as any}
        setForm={mockSetForm}
        uploadError={undefined}
        onFileAdded={mockOnFileAdded}
        isUploading={false}
      />,
      renderWithProviders()
    );

    const fileInput = getFileInput(container);
    expect(fileInput).not.toBeNull();

    const badFile = new File(['{}'], 'file.txt', { type: 'text/plain' });
    fireEvent.change(fileInput!, { target: { files: [badFile] } });

    await waitFor(() => {
      expect(mockOnFileAdded).not.toHaveBeenCalled();
      expect(mockSetForm).toHaveBeenCalled();
    });
  });

  it('does not add files that exceed the maximum allowed size', async () => {
    const { container } = render(
      <StepOne
        form={defaultForm as any}
        setForm={mockSetForm}
        uploadError={undefined}
        onFileAdded={mockOnFileAdded}
        isUploading={false}
      />,
      renderWithProviders()
    );

    const fileInput = getFileInput(container);
    expect(fileInput).not.toBeNull();

    const largeFile = new File([new Uint8Array(25 * 1024 * 1024 + 1)], 'file.geojson', { type: 'application/json' });
    fireEvent.change(fileInput!, { target: { files: [largeFile] } });

    await waitFor(() => {
      expect(mockOnFileAdded).not.toHaveBeenCalled();
      expect(mockSetForm).toHaveBeenCalled();
    });
  });

  it('calls onFileAdded when a supported file is added', async () => {
    const { container } = render(
      <StepOne
        form={defaultForm as any}
        setForm={mockSetForm}
        uploadError={undefined}
        onFileAdded={mockOnFileAdded}
        isUploading={false}
      />,
      renderWithProviders()
    );

    const fileInput = getFileInput(container);
    expect(fileInput).not.toBeNull();

    const validFile = new File(['{}'], 'file.geojson', { type: 'application/json' });
    fireEvent.change(fileInput!, { target: { files: [validFile] } });

    await waitFor(() => {
      expect(mockOnFileAdded).toHaveBeenCalledWith(validFile);
    });
  });

  it('deletes an existing file when the remove action is triggered', async () => {
    const formWithFile = {
      ...defaultForm,
      file: {
        id: 'opening-map-file-drop-container',
        value: new File(['{}'], 'file.geojson', { type: 'application/json' }),
        validatedObj: { geoJson: { type: 'FeatureCollection', features: [] }, geometryArea: 12 },
      },
    };

    const { container } = render(
      <StepOne
        form={formWithFile as any}
        setForm={mockSetForm}
        uploadError={undefined}
        onFileAdded={mockOnFileAdded}
        isUploading={false}
      />,
      renderWithProviders()
    );

    const deleteButton = screen.getByText('Delete');
    expect(deleteButton).toBeDefined();

    fireEvent.click(deleteButton);

    await waitFor(() => {
      expect(mockSetForm).toHaveBeenCalled();
    });
  });
});
