import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { renderWithProviders } from '../../utils/testAuthProvider';
import StepOne from '../../../components/CreateOpeningSteps/StepOne';

vi.mock('@/services/API', () => ({
  default: {
    CodesEndpointService: {
      getOpeningOrgUnits: vi.fn(() => [{ code: 'DCR', description: 'Campbell River' }]),
      getOpeningCategories: vi.fn(() => [{ code: 'FTML', description: 'Major Licensee' }]),
    },
    ForestClientEndpointService: {
      searchByClientNumbers: vi.fn(() => [{ clientNumber: '00012797', name: 'MINISTRY OF FORESTS', acronym: 'MOF' }]),
    },
  },
}));

vi.mock('@tanstack/react-query', async () => {
  const actual = await vi.importActual('@tanstack/react-query');
  return {
    ...actual,
    useQuery: vi.fn((options: any) => {
      let rawData = [];
      try {
        rawData = options?.queryFn?.() ?? [];
      } catch {
        rawData = [];
      }
      const data = options?.select ? options.select(rawData) : rawData;
      return { data, isLoading: false, isError: false };
    }),
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
    Dropdown: ({ titleText, items, onChange, id }: any) => (
      <div>
        <span>{titleText}</span>
        <select
          data-testid={`dropdown-${id}`}
          onChange={(e) => {
            const item = items?.find((i: any) => i.id === e.target.value);
            onChange({ selectedItem: item });
          }}
        >
          <option value="">Choose an option</option>
          {items?.map((item: any) => (
            <option key={item.id} value={item.id}>{item.label}</option>
          ))}
        </select>
      </div>
    ),
  };
});

vi.mock('../../../contexts/AuthProvider', async () => {
  const actual = await vi.importActual('../../../contexts/AuthProvider');
  return {
    ...actual,
    useAuth: vi.fn(() => ({ user: { associatedClients: ['00012797'] } })),
  };
});

describe('CreateOpening StepOne', () => {
  const mockSetForm = vi.fn();
  const mockOnFileAdded = vi.fn();

  beforeEach(() => {
    mockSetForm.mockReset();
    mockSetForm.mockImplementation((updater) => {
      if (typeof updater === 'function') {
        updater(defaultForm);
      }
    });
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

  it('renders area inputs and handles onBlur when validatedObj is present', () => {
    const formWithValidatedObj = {
      ...defaultForm,
      file: {
        id: 'opening-map-file-drop-container',
        value: new File(['{}'], 'file.geojson', { type: 'application/json' }),
        validatedObj: { geoJson: { type: 'FeatureCollection', features: [] }, geometryArea: 25.4 },
      },
      openingGrossArea: { id: 'gross-area-id', value: '25.4', isInvalid: false },
      maxAllowablePermAccess: { id: 'perm-access-id', value: '7.0', isInvalid: false },
    };

    render(
      <StepOne
        form={formWithValidatedObj as any}
        setForm={mockSetForm}
        uploadError={undefined}
        onFileAdded={mockOnFileAdded}
        isUploading={false}
      />,
      renderWithProviders()
    );

    expect(screen.getAllByDisplayValue('25.4').length).toBeGreaterThan(0);

    const grossAreaInput = screen.getByLabelText(/opening gross area/i);
    fireEvent.keyDown(grossAreaInput, { key: '5' });
    fireEvent.paste(grossAreaInput, { clipboardData: { getData: () => '12.3' } });
    fireEvent.blur(grossAreaInput, { target: { value: '30.5' } });
    expect(mockSetForm).toHaveBeenCalled();

    const permAccessInput = screen.getByLabelText(/maximum allowable permanent access/i);
    fireEvent.keyDown(permAccessInput, { key: '2' });
    fireEvent.paste(permAccessInput, { clipboardData: { getData: () => '5.0' } });
    fireEvent.blur(permAccessInput, { target: { value: '8.5' } });
    expect(mockSetForm).toHaveBeenCalled();
  });

  it('renders dropdowns and handles selection for client, org unit, category, and licensee ID', async () => {
    const formWithGeneralInfo = {
      ...defaultForm,
      client: { id: 'client-id', value: '00012797', isInvalid: false },
      orgUnit: { id: 'org-unit-id', value: 'DCR', isInvalid: false },
      category: { id: 'category-id', value: 'FTML', isInvalid: false },
      licenseeOpeningId: { id: 'lic-id', value: 'LIC-001', isInvalid: false },
    };

    render(
      <StepOne
        form={formWithGeneralInfo as any}
        setForm={mockSetForm}
        uploadError={undefined}
        onFileAdded={mockOnFileAdded}
        isUploading={false}
      />,
      renderWithProviders()
    );

    const licenseeInput = screen.getByLabelText(/licensee opening id/i);
    fireEvent.blur(licenseeInput, { target: { value: 'LIC-002' } });
    expect(mockSetForm).toHaveBeenCalled();

    const clientDropdown = screen.getByTestId('dropdown-client-id');
    fireEvent.change(clientDropdown, { target: { value: '00012797' } });
    expect(mockSetForm).toHaveBeenCalled();

    const orgUnitDropdown = screen.getByTestId('dropdown-org-unit-id');
    fireEvent.change(orgUnitDropdown, { target: { value: 'DCR' } });
    expect(mockSetForm).toHaveBeenCalled();

    const catDropdown = screen.getByTestId('dropdown-category-id');
    fireEvent.change(catDropdown, { target: { value: 'FTML' } });
    expect(mockSetForm).toHaveBeenCalled();
  });

  it('renders upload error notification and allows dismissal', () => {
    const mockOnDismiss = vi.fn();

    render(
      <StepOne
        form={defaultForm as any}
        setForm={mockSetForm}
        uploadError="Spatial upload failed"
        onUploadErrorDismiss={mockOnDismiss}
        onFileAdded={mockOnFileAdded}
        isUploading={false}
      />,
      renderWithProviders()
    );

    expect(screen.getByText('Spatial upload failed')).toBeInTheDocument();
    const closeBtn = screen.getByRole('button', { name: /close notification/i });
    fireEvent.click(closeBtn);
    expect(mockOnDismiss).toHaveBeenCalled();
  });

  it('displays validating indicator when isUploading is true', () => {
    render(
      <StepOne
        form={defaultForm as any}
        setForm={mockSetForm}
        uploadError={undefined}
        onFileAdded={mockOnFileAdded}
        isUploading={true}
      />,
      renderWithProviders()
    );

    expect(screen.getByText('Validating file…')).toBeInTheDocument();
  });
});

