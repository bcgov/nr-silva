import { describe, it, expect, beforeEach, vi } from 'vitest';
import { render, screen, fireEvent, within } from '@testing-library/react';
import StockingStandardsSearchTableRow from '@/components/StockingStandardsSearchSection/StockingStandardsSearchTableRow';
import { StockingStandardsHeaderType } from '@/types/TableHeader';
import { StockingStandardsSearchResponseDto, CodeDescriptionDto, ForestClientDto } from '@/services/OpenApi';

// Mock Carbon components to simplify testing
vi.mock('@carbon/react', () => ({
  TableRow: ({ children, ...props }: any) => <tr {...props}>{children}</tr>,
  TableCell: ({ children, ...props }: any) => <td {...props}>{children}</td>,
  Stack: ({ children, ...props }: any) => <div {...props}>{children}</div>,
  Tooltip: ({ children, label }: any) => <div title={label}>{children}</div>,
  Button: ({ children, onClick, ...props }: any) => (
    <button onClick={onClick} {...props}>{children}</button>
  ),
  DefinitionTooltip: ({ children, definition }: any) => (
    <div title={definition}>{children}</div>
  ),
}));

vi.mock('@carbon/icons-react', () => ({
  Warning: () => <span data-testid="warning-icon">⚠️</span>,
}));

vi.mock('@/components/StackedTooltip', () => ({
  default: ({ items, unit, displayLimit }: any) => (
    <div data-testid="stacked-tooltip">
      {items.length} {unit}
    </div>
  ),
}));

vi.mock('@/utils/DateUtils', () => ({
  formatLocalDate: (date: string) => new Date(date).toLocaleDateString(),
}));

const defaultHeaders: StockingStandardsHeaderType[] = [
  { key: 'standardsRegimeId', header: 'Regime ID', selected: true },
  { key: 'bgc', header: 'BGC', selected: true },
  { key: 'clients', header: 'Clients', selected: true },
  { key: 'fspIds', header: 'FSP IDs', selected: true },
  { key: 'orgUnits', header: 'Org Units', selected: true },
  { key: 'preferredSpecies', header: 'Preferred Species', selected: true },
  { key: 'updateTimestamp', header: 'Updated', selected: true },
];

const createMockRowData = (overrides?: Partial<StockingStandardsSearchResponseDto>): StockingStandardsSearchResponseDto => ({
  standardsRegimeId: 'SR-001',
  bgcZone: 'IDF',
  bgcSubZone: 'c',
  bgcVariant: 'w',
  bgcPhase: 'a',
  becSiteSeries: 'SS1',
  becSiteType: 'ST1',
  becSeral: 'Seral1',
  clients: [],
  fspIds: [],
  orgUnits: [],
  preferredSpecies: [],
  updateTimestamp: '2024-01-15T10:00:00Z',
  isExpired: false,
  ...overrides,
});

describe('StockingStandardsSearchTableRow', () => {
  it('should render table row with basic data', () => {
    const rowData = createMockRowData();
    const { container } = render(
      <table>
        <tbody>
          <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
        </tbody>
      </table>
    );

    expect(container.querySelector('tr')).toBeInTheDocument();
  });

  it('should render standardsRegimeId cell with data', () => {
    const rowData = createMockRowData();
    render(
      <table>
        <tbody>
          <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
        </tbody>
      </table>
    );

    expect(screen.getByText('SR-001')).toBeInTheDocument();
  });

  it('should render expired warning icon when regime is expired', () => {
    const rowData = createMockRowData({ isExpired: true });
    render(
      <table>
        <tbody>
          <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
        </tbody>
      </table>
    );

    expect(screen.getByTestId('warning-icon')).toBeInTheDocument();
  });

  it('should render BGC components formatted as dot-separated string', () => {
    const rowData = createMockRowData();
    render(
      <table>
        <tbody>
          <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
        </tbody>
      </table>
    );

    expect(screen.getByText(/IDF\.c\.w\.a\.SS1\.ST1\.Seral1/)).toBeInTheDocument();
  });

  describe('Clients expansion', () => {
    it('should show placeholder when no clients', () => {
      const rowData = createMockRowData({ clients: [] });
      const { container } = render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      const placeholders = screen.getAllByText('--');
      expect(placeholders.length).toBeGreaterThan(0);
    });

    it('should show single client without expand button', () => {
      const clients: ForestClientDto[] = [
        { clientNumber: 'C001', clientName: 'Test Client', acronym: 'TC' },
      ];
      const rowData = createMockRowData({ clients });
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      expect(screen.getByText('TC')).toBeInTheDocument();
    });

    it('should expand and collapse multiple clients', () => {
      const clients: ForestClientDto[] = [
        { clientNumber: 'C001', clientName: 'Client 1', acronym: 'C1' },
        { clientNumber: 'C002', clientName: 'Client 2', acronym: 'C2' },
      ];
      const rowData = createMockRowData({ clients });
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      // Initially shows first client and "1 more" button
      expect(screen.getByText('C1')).toBeInTheDocument();
      const showMoreBtn = screen.getByRole('button', { name: /1 more/i });
      expect(showMoreBtn).toBeInTheDocument();

      // Click to expand
      fireEvent.click(showMoreBtn);

      // Now shows "show fewer" button and all clients
      expect(screen.getByRole('button', { name: /show fewer/i })).toBeInTheDocument();
      expect(screen.getByText('C2')).toBeInTheDocument();

      // Click to collapse
      fireEvent.click(screen.getByRole('button', { name: /show fewer/i }));
      expect(screen.queryByText('C2')).not.toBeInTheDocument();
    });

    it('should use stable key with index fallback when clientNumber is null', () => {
      const clients: ForestClientDto[] = [
        { clientNumber: null as any, clientName: 'Unknown 1', acronym: 'U1' },
        { clientNumber: null as any, clientName: 'Unknown 2', acronym: 'U2' },
      ];
      const rowData = createMockRowData({ clients });

      const { container } = render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      fireEvent.click(screen.getByRole('button', { name: /1 more/i }));

      // Both should be rendered without key warning
      expect(screen.getByText('U1')).toBeInTheDocument();
      expect(screen.getByText('U2')).toBeInTheDocument();

      // Verify the structure contains the expected elements
      const spans = container.querySelectorAll('.expandable-items__item');
      expect(spans.length).toBe(2);
    });
  });

  describe('FSP IDs expansion', () => {
    it('should show placeholder when no FSP IDs', () => {
      const rowData = createMockRowData({ fspIds: [] });
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      const placeholders = screen.getAllByText('--');
      expect(placeholders.length).toBeGreaterThan(0);
    });

    it('should show single FSP ID without expand button', () => {
      const rowData = createMockRowData({ fspIds: ['FSP-001'] });
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      expect(screen.getByText('FSP-001')).toBeInTheDocument();
    });

    it('should expand and collapse multiple FSP IDs', () => {
      const rowData = createMockRowData({ fspIds: ['FSP-001', 'FSP-002', 'FSP-003'] });
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      // Initially shows first ID and "2 more" button
      expect(screen.getByText('FSP-001')).toBeInTheDocument();
      const showMoreBtn = screen.getByRole('button', { name: /2 more/i });
      expect(showMoreBtn).toBeInTheDocument();

      // Click to expand
      fireEvent.click(showMoreBtn);

      // Now shows all IDs and "show fewer" button
      expect(screen.getByRole('button', { name: /show fewer/i })).toBeInTheDocument();
      expect(screen.getByText('FSP-002')).toBeInTheDocument();
      expect(screen.getByText('FSP-003')).toBeInTheDocument();
    });
  });

  describe('Org Units expansion', () => {
    it('should show placeholder when no org units', () => {
      const rowData = createMockRowData({ orgUnits: [] });
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      const placeholders = screen.getAllByText('--');
      expect(placeholders.length).toBeGreaterThan(0);
    });

    it('should show single org unit without expand button', () => {
      const orgUnits: CodeDescriptionDto[] = [
        { code: 'OU-001', description: 'Org Unit 1' },
      ];
      const rowData = createMockRowData({ orgUnits });
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      expect(screen.getByText('OU-001')).toBeInTheDocument();
    });

    it('should expand and collapse multiple org units', () => {
      const orgUnits: CodeDescriptionDto[] = [
        { code: 'OU-001', description: 'Org Unit 1' },
        { code: 'OU-002', description: 'Org Unit 2' },
      ];
      const rowData = createMockRowData({ orgUnits });
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      // Initially shows first unit and "1 more" button
      expect(screen.getByText('OU-001')).toBeInTheDocument();
      const showMoreBtn = screen.getByRole('button', { name: /1 more/i });
      expect(showMoreBtn).toBeInTheDocument();

      // Click to expand
      fireEvent.click(showMoreBtn);

      // Now shows all units and "show fewer" button
      expect(screen.getByRole('button', { name: /show fewer/i })).toBeInTheDocument();
      expect(screen.getByText('OU-002')).toBeInTheDocument();
    });

    it('should use stable key with index fallback when code is null', () => {
      const orgUnits: CodeDescriptionDto[] = [
        { code: null as any, description: 'Unnamed 1' },
        { code: null as any, description: 'Unnamed 2' },
        { code: 'OU-003', description: 'Org Unit 3' },
      ];
      const rowData = createMockRowData({ orgUnits });

      const { container } = render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      fireEvent.click(screen.getByRole('button', { name: /2 more/i }));

      // All should be rendered without key warning
      expect(screen.getByText('OU-003')).toBeInTheDocument();

      // Verify the structure contains the expected elements without duplicate key errors
      const spans = container.querySelectorAll('.expandable-items__item');
      expect(spans.length).toBe(3);
    });

    it('should display placeholder for null code in org unit tooltip', () => {
      const orgUnits: CodeDescriptionDto[] = [
        { code: null as any, description: 'Unknown Org Unit' },
      ];
      const rowData = createMockRowData({ orgUnits });

      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      const placeholders = screen.getAllByText('--');
      expect(placeholders.length).toBeGreaterThan(0);
    });
  });

  describe('Preferred Species', () => {
    it('should show placeholder when no species', () => {
      const rowData = createMockRowData({ preferredSpecies: [] });
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      const placeholders = screen.getAllByText('--');
      expect(placeholders.length).toBeGreaterThan(0);
    });

    it('should render StackedTooltip with species data', () => {
      const species = [{ speciesCode: 'SP1' }, { speciesCode: 'SP2' }] as any;
      const rowData = createMockRowData({ preferredSpecies: species });
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      expect(screen.getByTestId('stacked-tooltip')).toBeInTheDocument();
      expect(screen.getByText('2 species')).toBeInTheDocument();
    });
  });

  describe('Update timestamp', () => {
    it('should format and display update timestamp', () => {
      const rowData = createMockRowData({ updateTimestamp: '2024-01-15T10:00:00Z' });
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      // Should display formatted date (exact format depends on locale)
      const cells = screen.getAllByRole('cell');
      expect(cells.length).toBeGreaterThan(0);
    });
  });

  describe('Column filtering', () => {
    it('should only render selected columns', () => {
      const selectedHeaders = defaultHeaders.map((h, i) => ({
        ...h,
        selected: i === 0 || i === 1, // Only show standardsRegimeId and bgc
      }));

      const rowData = createMockRowData();
      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={selectedHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      const cells = screen.getAllByRole('cell');
      expect(cells.length).toBe(2);
    });

    it('should render deselected columns', () => {
      const noClientsHeaders = defaultHeaders.map((h) => ({
        ...h,
        selected: h.key !== 'clients',
      }));

      const rowData = createMockRowData({
        clients: [{ clientNumber: 'C001', clientName: 'Test', acronym: 'T' }],
      });

      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={noClientsHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      // Client should not be rendered
      expect(screen.queryByText('T')).not.toBeInTheDocument();
    });
  });

  describe('Placeholder handling', () => {
    it('should render placeholder for null/undefined values', () => {
      const rowData = createMockRowData({
        standardsRegimeId: null as any,
        clients: [],
        fspIds: [],
        orgUnits: [],
      });

      render(
        <table>
          <tbody>
            <StockingStandardsSearchTableRow headers={defaultHeaders} rowData={rowData} />
          </tbody>
        </table>
      );

      // Should render multiple placeholders
      const placeholders = screen.getAllByText('--');
      expect(placeholders.length).toBeGreaterThan(0);
    });
  });
});
