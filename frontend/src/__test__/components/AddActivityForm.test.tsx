import { describe, it, expect, vi, beforeEach } from 'vitest';
import { useState } from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import AddActivityForm from '../../components/AddActivityForm';
import { ActivityFormType } from '../../screens/CreateActivity/definitions';
import { DefaultActivityForm } from '../../screens/CreateActivity/constants';
import { CodeDescriptionDto } from '../../services/OpenApi';

const baseCodes: CodeDescriptionDto[] = [
  { code: 'DN', description: 'Disturbance' },
  { code: 'SU', description: 'Survey' },
  { code: 'BR', description: 'Brushing' },
];
const simpleCodes: CodeDescriptionDto[] = [
  { code: 'A', description: 'Alpha' },
  { code: 'B', description: 'Beta' },
];
const manyCodes: CodeDescriptionDto[] = ['1', '2', '3', '4', '5', '6'].map((c) => ({
  code: c,
  description: `D${c}`,
}));

vi.mock('../../services/API', () => ({
  default: {
    CodesEndpointService: {
      getSilvBaseCodes: vi.fn(() => Promise.resolve(baseCodes)),
      getSilvTechniqueCodes: vi.fn(() => Promise.resolve(simpleCodes)),
      getSilvMethodCodes: vi.fn(() => Promise.resolve(simpleCodes)),
      getSilvObjectiveCodes: vi.fn(() => Promise.resolve(manyCodes)),
      getSilvFundSourceCodes: vi.fn(() => Promise.resolve(manyCodes)),
    },
  },
}));

vi.mock('../../components/MapPreview', () => ({
  default: ({ geojson }: { geojson: unknown }) => (
    <div data-testid="map-preview">{geojson ? 'has-geojson' : 'no-geojson'}</div>
  ),
}));

type MultiSelectMockProps = {
  id: string;
  items: CodeDescriptionDto[];
  itemToElement: (item: CodeDescriptionDto) => React.ReactNode;
  selectedItems: CodeDescriptionDto[];
  onChange: (e: { selectedItems: CodeDescriptionDto[] }) => void;
  invalidText?: string;
  invalid?: boolean;
};

vi.mock('../../components/CustomMultiSelect', () => ({
  default: ({ id, items, itemToElement, selectedItems, onChange, invalid, invalidText }: MultiSelectMockProps) => (
    <div data-testid={id}>
      <span data-testid={`${id}-count`}>{selectedItems.length}</span>
      {items.length > 0 ? <span data-testid={`${id}-element`}>{itemToElement(items[5] ?? items[0])}</span> : null}
      <button onClick={() => onChange({ selectedItems: items.slice(0, 2) })}>{`${id}-pick2`}</button>
      <button onClick={() => onChange({ selectedItems: items.slice(0, 5) })}>{`${id}-pick5`}</button>
      <button onClick={() => onChange({ selectedItems: items.slice(0, 6) })}>{`${id}-pick6`}</button>
      <button onClick={() => onChange({ selectedItems: [] })}>{`${id}-clear`}</button>
      {invalid ? <span>{invalidText}</span> : null}
    </div>
  ),
}));

const onFileAdded = vi.fn();
const onStatusChangeAttempt = vi.fn();
const onUploadErrorDismiss = vi.fn();
let latestForm: ActivityFormType = {};

const Harness = ({
  initial,
  uploadError,
  isUploading = false,
}: {
  initial?: ActivityFormType;
  uploadError?: string;
  isUploading?: boolean;
}) => {
  const [form, setForm] = useState<ActivityFormType>(initial ?? structuredClone(DefaultActivityForm));
  latestForm = form;
  return (
    <AddActivityForm
      form={form}
      setForm={setForm}
      uploadError={uploadError}
      onUploadErrorDismiss={onUploadErrorDismiss}
      onFileAdded={onFileAdded}
      isUploading={isUploading}
      onStatusChangeAttempt={onStatusChangeAttempt}
    />
  );
};

const renderForm = (props: React.ComponentProps<typeof Harness> = {}) => {
  const client = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  return render(
    <QueryClientProvider client={client}>
      <Harness {...props} />
    </QueryClientProvider>
  );
};

const formWith = (status: 'PLANNED' | 'COMPLETED', baseCode?: string): ActivityFormType => ({
  ...structuredClone(DefaultActivityForm),
  status: { id: 'activity-status-input', value: status },
  ...(baseCode ? { baseCode: { id: 'silv-base-code-dropdown', value: baseCode } } : {}),
});

describe('AddActivityForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    latestForm = {};
  });

  it('shows only status tiles until a status is chosen', () => {
    renderForm();
    expect(screen.getByText('Select activity status')).toBeDefined();
    expect(screen.queryByText('Activity base')).toBeNull();
  });

  it('reports status change attempts', async () => {
    const user = userEvent.setup();
    renderForm();
    await user.click(screen.getByText('A silviculture treatment or survey performed and reported.'));
    expect(onStatusChangeAttempt).toHaveBeenCalledWith('COMPLETED');
    await user.click(screen.getByText('A silviculture treatment or survey scheduled for future execution.'));
    expect(onStatusChangeAttempt).toHaveBeenCalledWith('PLANNED');
  });

  it('shows the base dropdown (without DN) once status is set and hides details until base chosen', async () => {
    renderForm({ initial: formWith('PLANNED') });
    const dropdown = await screen.findByRole('combobox', { name: /Activity base/ });
    expect(screen.queryByText('Activity information')).toBeNull();
    await userEvent.click(dropdown);
    expect(await screen.findByText(/Brushing/)).toBeDefined();
    expect(screen.queryByText(/Disturbance/)).toBeNull();
  });

  it('selects a base code and reveals the form and spatial section', async () => {
    const user = userEvent.setup();
    renderForm({ initial: formWith('PLANNED') });
    await user.click(await screen.findByRole('combobox', { name: /Activity base/ }));
    await user.click(await screen.findByText(/Brushing/));
    expect(latestForm.baseCode?.value).toBe('BR');
    expect(await screen.findByText('Activity information')).toBeDefined();
    expect(screen.getByText('Spatial information')).toBeDefined();
    expect(screen.getByTestId('map-preview').textContent).toBe('no-geojson');
  });

  it('shows unsupported message for non-basic base codes', async () => {
    renderForm({ initial: formWith('PLANNED', 'SU') });
    expect(await screen.findByText('This base code is not yet supported')).toBeDefined();
    expect(screen.queryByText('Spatial information')).toBeNull();
  });

  it('shows invalid base code text', async () => {
    const form = formWith('PLANNED');
    form.baseCode = { ...form.baseCode, isInvalid: true };
    renderForm({ initial: form });
    expect(await screen.findByText('Select an activity base')).toBeDefined();
  });

  it('selects technique and method', async () => {
    const user = userEvent.setup();
    renderForm({ initial: formWith('PLANNED', 'BR') });
    await user.click(await screen.findByRole('combobox', { name: /Technique/ }));
    await user.click(await screen.findByText(/Alpha/));
    expect(latestForm.techniqueCode?.value).toBe('A');
    await user.click(screen.getByRole('combobox', { name: /Method/ }));
    await user.click(await screen.findByText(/Beta/));
    expect(latestForm.methodCode?.value).toBe('B');
  });

  it('updates objectives and funding sources, ignoring selections above the cap', async () => {
    const user = userEvent.setup();
    renderForm({ initial: formWith('PLANNED', 'BR') });
    const obj = 'activity-objective-codes-input';
    const fund = 'activity-funding-source-input';
    await screen.findByTestId(obj);

    await user.click(screen.getByText(`${obj}-pick2`));
    expect(latestForm.objectiveCodes?.value).toEqual(['1', '2']);
    await user.click(screen.getByText(`${obj}-pick6`));
    expect(latestForm.objectiveCodes?.value).toEqual(['1', '2']);

    await user.click(screen.getByText(`${fund}-pick5`));
    expect(latestForm.fundingSourceCodes?.value).toHaveLength(5);
    await user.click(screen.getByText(`${fund}-pick6`));
    expect(latestForm.fundingSourceCodes?.value).toHaveLength(5);
    await user.click(screen.getByText(`${fund}-clear`));
    expect(latestForm.fundingSourceCodes?.value).toEqual([]);
    expect(latestForm.fundingSourceCodes?.isInvalid).toBe(true);
    expect(screen.getByText('Select at least one funding source')).toBeDefined();
  });

  it('marks items beyond the cap as disabled', async () => {
    const form = formWith('PLANNED', 'BR');
    form.objectiveCodes = { id: 'activity-objective-codes-input', value: ['1', '2', '3', '4', '5'] };
    renderForm({ initial: form });
    const el = await screen.findByTestId('activity-objective-codes-input-element');
    expect(el.querySelector('.capped-multiselect-item--disabled')).not.toBeNull();
  });

  it('stores text fields on blur', async () => {
    const user = userEvent.setup();
    renderForm({ initial: formWith('COMPLETED', 'BR') });
    const project = await screen.findByLabelText('Project number');
    await user.type(project, 'P1');
    await user.tab();
    expect(latestForm.projectNumber?.value).toBe('P1');

    await user.type(screen.getByLabelText('Licensee activity ID'), 'L1');
    await user.tab();
    expect(latestForm.licenseeActivityId?.value).toBe('L1');

    await user.type(screen.getByLabelText('Comment'), 'note');
    await user.tab();
    expect(latestForm.comment?.value).toBe('note');

    await user.clear(project);
    await user.tab();
    expect(latestForm.projectNumber?.value).toBeUndefined();
  });

  it('validates planned area on blur (required when planned)', async () => {
    const user = userEvent.setup();
    renderForm({ initial: formWith('PLANNED', 'BR') });
    const area = await screen.findByLabelText(/Planned area/);
    await user.click(area);
    await user.tab();
    expect(latestForm.plannedArea?.isInvalid).toBe(true);
    expect(screen.getByText('Must be greater than 0')).toBeDefined();

    await user.type(area, '2.5');
    await user.tab();
    expect(latestForm.plannedArea?.value).toBe('2.5');
    expect(latestForm.plannedArea?.isInvalid).toBe(false);
  });

  it('allows empty planned area when completed but rejects zero', async () => {
    const user = userEvent.setup();
    renderForm({ initial: formWith('COMPLETED', 'BR') });
    const area = await screen.findByLabelText(/Planned area/);
    await user.click(area);
    await user.tab();
    expect(latestForm.plannedArea?.isInvalid).toBe(false);

    await user.type(area, '0');
    await user.tab();
    expect(latestForm.plannedArea?.isInvalid).toBe(true);
  });

  it('validates costs on blur', async () => {
    const user = userEvent.setup();
    renderForm({ initial: formWith('COMPLETED', 'BR') });
    const planned = await screen.findByLabelText('Planned cost');
    await user.type(planned, '100');
    await user.tab();
    expect(latestForm.plannedCost?.value).toBe('100');
    expect(latestForm.plannedCost?.isInvalid).toBe(false);

    const actual = screen.getByLabelText('Actual cost');
    fireEvent.change(actual, { target: { value: '1.5' } });
    fireEvent.blur(actual);
    expect(latestForm.actualCost?.isInvalid).toBe(true);
  });

  it('blocks non-numeric keystrokes in area input', async () => {
    const user = userEvent.setup();
    renderForm({ initial: formWith('PLANNED', 'BR') });
    const area = await screen.findByLabelText(/Planned area/);
    await user.type(area, 'ab1');
    expect((area as HTMLInputElement).value).toBe('1');
  });

  it('shows completion-only fields only when completed', async () => {
    const { unmount } = renderForm({ initial: formWith('PLANNED', 'BR') });
    await screen.findByLabelText(/Planned area/);
    expect(screen.queryByLabelText('Actual cost')).toBeNull();
    unmount();

    renderForm({ initial: formWith('COMPLETED', 'BR') });
    expect(await screen.findByLabelText('Actual cost')).toBeDefined();
    expect(screen.getByLabelText(/Treated area/)).toBeDefined();
    expect(screen.getByLabelText(/Completion date/)).toBeDefined();
  });

  it('validates treated area on blur', async () => {
    const user = userEvent.setup();
    renderForm({ initial: formWith('COMPLETED', 'BR') });
    const treated = await screen.findByLabelText(/Treated area/);
    await user.click(treated);
    await user.tab();
    expect(latestForm.treatedArea?.isInvalid).toBe(true);
  });

  it('sets planned and completion dates from the pickers', async () => {
    renderForm({ initial: formWith('COMPLETED', 'BR') });
    const planned = await screen.findByLabelText('Planned date');
    fireEvent.change(planned, { target: { value: '2099/01/02' } });
    fireEvent.blur(planned);
    await waitFor(() => expect(latestForm.plannedDate?.value).toBe('2099-01-02'));

    const completion = screen.getByLabelText(/Completion date/);
    fireEvent.change(completion, { target: { value: '2024/03/04' } });
    fireEvent.blur(completion);
    await waitFor(() => expect(latestForm.completionDate?.value).toBe('2024-03-04'));
  });

  describe('file upload', () => {
    const drop = async (file: File) => {
      const input = document.querySelector('input[type="file"]') as HTMLInputElement;
      await userEvent.setup({ applyAccept: false }).upload(input, file);
    };

    it('accepts a supported file and notifies parent', async () => {
      renderForm({ initial: formWith('PLANNED', 'BR') });
      await screen.findByText('Spatial information');
      const file = new File(['{}'], 'shape.geojson', { type: 'application/json' });
      await drop(file);
      await waitFor(() => expect(onFileAdded).toHaveBeenCalledWith(file));
      expect(latestForm.file?.value).toBe(file);
      expect(screen.getByText('shape.geojson')).toBeDefined();
    });

    it('rejects unsupported extensions', async () => {
      renderForm({ initial: formWith('PLANNED', 'BR') });
      await screen.findByText('Spatial information');
      await drop(new File(['x'], 'shape.txt', { type: 'text/plain' }));
      expect(await screen.findByText(/File not supported/)).toBeDefined();
      expect(onFileAdded).not.toHaveBeenCalled();
      expect(latestForm.file?.isInvalid).toBe(true);
    });

    it('rejects files that are too large', async () => {
      renderForm({ initial: formWith('PLANNED', 'BR') });
      await screen.findByText('Spatial information');
      const big = new File(['x'], 'big.geojson');
      Object.defineProperty(big, 'size', { value: 1024 * 1024 * 1024 });
      await drop(big);
      expect(await screen.findByText(/exceeds/)).toBeDefined();
      expect(onFileAdded).not.toHaveBeenCalled();
    });

    it('removes the file when deleted', async () => {
      const form = formWith('PLANNED', 'BR');
      form.file = { id: 'f', value: new File(['x'], 'shape.geojson') };
      renderForm({ initial: form });
      await screen.findByText('shape.geojson');
      const del = document.querySelector('.cds--file-close') as HTMLElement;
      await userEvent.click(del);
      expect(latestForm.file?.value).toBeUndefined();
    });

    it('shows upload error and dismisses it', async () => {
      const user = userEvent.setup();
      renderForm({ initial: formWith('PLANNED', 'BR'), uploadError: 'boom' });
      expect(await screen.findByText('boom')).toBeDefined();
      expect(screen.getByText('File upload failed')).toBeDefined();
      await user.click(screen.getByRole('button', { name: /close/i }));
      expect(onUploadErrorDismiss).toHaveBeenCalled();
    });

    it('shows validating indicator while uploading and passes geojson to the map', async () => {
      const form = formWith('PLANNED', 'BR');
      form.file = {
        id: 'f',
        value: new File(['x'], 'shape.geojson'),
        validatedObj: { geoJson: { type: 'FeatureCollection', features: [] } } as never,
      };
      renderForm({ initial: form, isUploading: true });
      expect(await screen.findByText('Validating file…')).toBeDefined();
      expect(screen.getByTestId('map-preview').textContent).toBe('has-geojson');
    });
  });
});
