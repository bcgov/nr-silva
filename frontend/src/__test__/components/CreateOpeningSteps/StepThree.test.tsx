import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import StepThree from '../../../components/CreateOpeningSteps/StepThree';

const mockUseQuery = vi.fn();

vi.mock('@tanstack/react-query', async () => {
  const actual = await vi.importActual('@tanstack/react-query');
  return {
    ...actual,
    useQuery: (config: any) => {
      const result = mockUseQuery(config);
      return {
        ...result,
        data: config.select ? config.select(result.data) : result.data,
      };
    },
  };
});

vi.mock('@/components/MapPreview', async () => {
  return {
    __esModule: true,
    default: () => <div>MockMapPreview</div>,
  };
});

vi.mock('@/components/DisplayField', async () => {
  return {
    __esModule: true,
    default: ({ label, value }: any) => <div>{label}: {String(value)}</div>,
  };
});

describe('CreateOpening StepThree', () => {
  beforeEach(() => {
    mockUseQuery.mockReset();
  });

  it('renders review information and validated tenures', () => {
    mockUseQuery.mockImplementation(({ queryKey }: any) => {
      if (Array.isArray(queryKey) && queryKey[0] === 'forest-clients') {
        return { data: [{ id: 'C1', name: 'Client', acronym: 'CL' }] };
      }
      if (Array.isArray(queryKey) && queryKey[0] === 'codes' && queryKey[1] === 'org-units') {
        return { data: [{ code: 'OU1', description: 'Unit' }] };
      }
      if (Array.isArray(queryKey) && queryKey[0] === 'codes' && queryKey[1] === 'opening-categories') {
        return { data: [{ code: 'CAT1', description: 'Category' }] };
      }
      return { data: [] };
    });

    const form = {
      client: { value: 'C1' },
      orgUnit: { value: 'OU1' },
      category: { value: 'CAT1' },
      licenseeOpeningId: { value: 'LOID' },
      openingGrossArea: { value: '10' },
      maxAllowablePermAccess: { value: '7' },
      file: { value: new File(['{}'], 'file.geojson', { type: 'application/json' }), validatedObj: { geoJson: { type: 'FeatureCollection', features: [] }, geometryArea: 12 } },
      tenureInfo: { validatedTenures: [{ fileId: 'F1', isPrimary: true, cuttingPermit: 'CP1', cutBlock: 'CB1' }] },
    };

    const setStep = vi.fn();
    render(<StepThree form={form as any} setStep={setStep} />);

    expect(screen.getByText('Review and create')).toBeDefined();
    expect(screen.getByText(/Opening gross area/i)).toBeDefined();
    expect(screen.getByText(/Maximum allowable permanent access/i)).toBeDefined();
    expect(screen.getByText(/Client.*Client, C1, CL/)).toBeDefined();
    expect(screen.getByText(/Org unit.*OU1 - Unit/)).toBeDefined();
    expect(screen.getByText(/Category.*CAT1 - Category/)).toBeDefined();
    expect(screen.getByText(/F1/)).toBeDefined();
    expect(screen.getByText('MockMapPreview')).toBeDefined();

    fireEvent.click(screen.getByRole('button', { name: /Edit opening information/i }));
    expect(setStep).toHaveBeenCalledWith(0);

    fireEvent.click(screen.getByRole('button', { name: /Edit tenure information/i }));
    expect(setStep).toHaveBeenCalledWith(1);
  });
});
