import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import OpeningStandardUnits from '@/components/OpeningDetails/OpeningStandardUnits';
import API from '@/services/API';
import {
  OpeningDetailsStockingDto,
  OpeningStockingHistoryOverviewDto,
} from '@/services/OpenApi';

vi.mock('@/services/API', () => ({
  default: {
    OpeningEndpointService: {
      getOpeningSsu: vi.fn(),
      getOpeningSsuHistory: vi.fn(),
      getOpeningSsuHistoryDetails: vi.fn(),
    },
  },
}));

const mockUseDeepLinkScroll = vi.fn();
vi.mock('@/hooks/useDeepLinkScroll', () => ({
  default: (...args: any[]) => mockUseDeepLinkScroll(...args),
}));

vi.mock('@carbon/react', async (importOriginal) => {
  const actual = await importOriginal<typeof import('@carbon/react')>();
  return {
    ...actual,
    Tooltip: ({ children, label, ...props }: any) => (
      <div data-testid="tooltip" title={typeof label === 'string' ? label : undefined} {...props}>
        {children}
      </div>
    ),
    DefinitionTooltip: ({ children, definition, ...props }: any) => (
      <span title={typeof definition === 'string' ? definition : undefined} {...props}>
        {children}
      </span>
    ),
  };
});

describe('OpeningStandardUnits', () => {
  let queryClient: QueryClient;

  const mockHistoryList: OpeningStockingHistoryOverviewDto[] = [
    {
      stockingEventHistoryId: 101,
      eventTimestamp: '2023-01-15T10:30:00Z',
      auditAction: { code: 'U', description: 'Updated' },
      isLatest: true,
      isOldest: false,
      amendmentNumber: 1,
      suCount: 1,
      totalNar: 25.4,
      esfSubmissionId: null,
      submittedByUserId: null,
      approvedByUserId: null,
    },
    {
      stockingEventHistoryId: 100,
      eventTimestamp: '2022-01-10T09:00:00Z',
      auditAction: { code: 'I', description: 'Inserted' },
      isLatest: false,
      isOldest: true,
      amendmentNumber: 0,
      suCount: 1,
      totalNar: 25.4,
      esfSubmissionId: null,
      submittedByUserId: null,
      approvedByUserId: null,
    },
  ];

  const mockStandardUnits: OpeningDetailsStockingDto[] = [
    {
      stocking: {
        ssuId: 1,
        stockingStandardUnit: 'SU-01',
        netArea: 25.4,
        soilDisturbancePercent: 5,
        srid: 9999,
        standardsObjective: 'Timber objective',
        additionalStandards: 'Special pruning',
        regenDelay: 2,
        freeGrowingEarly: 4,
        freeGrowingLate: 8,
        possibleFspIds: [501, 502],
        bec: {
          becZoneCode: 'CWH',
          becSubzoneCode: 'dk',
          becVariant: '1',
          becPhase: 'a',
          becSiteSeries: '01',
          becSiteType: 'p',
          becSeral: 'cl',
        },
        milestones: {
          extentDeclared: true,
          postHarvestDeclaredDate: '2021-06-01',
          noRegenIndicated: false,
          regenOffsetYears: 3,
          regenDeclaredDate: '2022-05-01',
          regenDueDate: '2023-05-01',
          freeGrowingOffsetYears: 7,
          freeGrowingDeclaredDate: '2025-05-01',
          freeGrowingDueDate: '2026-05-01',
          comments: [
            {
              commentSource: { code: 'SILVA', description: 'Silva' },
              commentType: { code: 'COMM', description: 'General' },
              commentText: 'Milestone on track',
            },
          ],
        },
      },
      comments: [
        {
          commentSource: { code: 'SILVA', description: 'Silva' },
          commentType: { code: 'COMM', description: 'General' },
          commentText: 'Standard unit comment',
        },
      ],
      preferredSpecies: [
        {
          species: { code: 'FD', description: 'Douglas Fir' },
          layer: '1',
          minHeight: 1.5,
        },
      ],
      acceptableSpecies: [
        {
          species: { code: 'PLI', description: 'Lodgepole Pine' },
          layer: '1',
          minHeight: 1.2,
        },
      ],
      layers: [
        {
          layer: { code: '1', description: 'Layer 1' },
          targetWellspacedTrees: 1200,
          minWellspacedTrees: 800,
          minPreferredWellspacedTrees: 600,
          minHorizontalDistanceWellspacedTrees: 2.0,
          minResidualBasalArea: 10,
          minPostspacingDensity: 1000,
          maxPostspacingDensity: 2000,
          maxConiferous: 1400,
          heightRelativeToComp: 150,
        },
      ],
    },
  ];

  beforeEach(() => {
    vi.clearAllMocks();
    queryClient = new QueryClient({
      defaultOptions: {
        queries: { retry: false },
      },
    });

    vi.mocked(API.OpeningEndpointService.getOpeningSsu).mockResolvedValue(mockStandardUnits);
    vi.mocked(API.OpeningEndpointService.getOpeningSsuHistory).mockResolvedValue(mockHistoryList);
    vi.mocked(API.OpeningEndpointService.getOpeningSsuHistoryDetails).mockResolvedValue(mockStandardUnits);
  });

  const renderComponent = (openingId = 123, initialRoute = '/openings/123?tab=standards-units&ssuId=1') => {
    return render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter initialEntries={[initialRoute]}>
          <OpeningStandardUnits openingId={openingId} />
        </MemoryRouter>
      </QueryClientProvider>
    );
  };

  it('renders loading skeleton while fetching standards units', () => {
    vi.mocked(API.OpeningEndpointService.getOpeningSsu).mockReturnValue(new Promise(() => {}));

    renderComponent();
    expect(document.querySelector('.cds--skeleton')).toBeInTheDocument();
  });

  it('renders header with count, BEC information, milestones, and species table', async () => {
    renderComponent();

    await waitFor(() => {
      expect(screen.getByText(/1 standards unit in the opening area/i)).toBeInTheDocument();
    });

    expect(screen.getByText('SU-01')).toBeInTheDocument();
    expect(screen.getByTestId('card-item-content-net-area-to-be-reforested-(ha)')).toHaveTextContent('25.4');
    expect(screen.getByTestId('card-item-content-max-soil-allowable-disturbance-(%)')).toHaveTextContent('5');
    expect(screen.getByText('CWH')).toBeInTheDocument();
    expect(screen.getByText('dk')).toBeInTheDocument();
    expect(screen.getByText(/SSID 9999/)).toBeInTheDocument();
    expect(screen.getByText('Objective: Timber objective')).toBeInTheDocument();
    expect(screen.getByText('Special pruning')).toBeInTheDocument();
    expect(screen.getByText('FSP ID 501')).toBeInTheDocument();
    expect(screen.getByText('FSP ID 502')).toBeInTheDocument();
    expect(screen.getByText(/2 species in a single layer/i)).toBeInTheDocument();
    expect(screen.getByText(/Target: 1200/)).toBeInTheDocument();
    expect(screen.getByText('10 (m²/ha)')).toBeInTheDocument();
    expect(screen.getByText('1400 (st/ha)')).toBeInTheDocument();
    expect(screen.getByText('150 (cm/%)')).toBeInTheDocument();
  });

  it('opens and closes the history overview modal', async () => {
    renderComponent();

    await waitFor(() => {
      expect(screen.getByTestId('view-history-overview-link')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByTestId('view-history-overview-link'));

    expect(screen.getByText('Standards units history overview')).toBeInTheDocument();
    expect(screen.getByTestId('ssu-history-row-101')).toBeInTheDocument();
    expect(screen.getByText('Updated')).toBeInTheDocument();

    const closeBtn = screen.getByRole('button', { name: /close/i });
    fireEvent.click(closeBtn);

    await waitFor(() => {
      expect(screen.queryByTestId('opening-standards-units-history-modal')).not.toHaveClass('is-visible');
    });
  });

  it('switches history version via the action date dropdown', async () => {
    renderComponent();

    const dropdownButton = await screen.findByRole('combobox', { name: 'Action date' });
    await waitFor(() => {
      expect(dropdownButton).toHaveTextContent(/Latest/);
    });

    fireEvent.click(dropdownButton);

    const oldestItem = await screen.findByText(/Oldest/i);
    fireEvent.click(oldestItem);

    await waitFor(() => {
      expect(API.OpeningEndpointService.getOpeningSsuHistoryDetails).toHaveBeenCalledWith(123, 100);
    });
  });

  it('renders noRegenIndicated milestones layout when noRegenIndicated is true', async () => {
    const noRegenStandardUnit: OpeningDetailsStockingDto[] = [
      {
        ...mockStandardUnits[0],
        stocking: {
          ...mockStandardUnits[0].stocking,
          srid: undefined,
          standardsObjective: undefined,
          possibleFspIds: [],
          milestones: {
            extentDeclared: false,
            postHarvestDeclaredDate: '2021-06-01',
            noRegenIndicated: true,
            noRegenOffsetYears: 5,
            noRegenDeclaredDate: '2023-01-01',
            noRegenDueDate: '2024-01-01',
            comments: [],
          },
        },
      },
    ];

    vi.mocked(API.OpeningEndpointService.getOpeningSsu).mockResolvedValue(noRegenStandardUnit);
    renderComponent();

    await waitFor(() => {
      expect(screen.getByText('No regeneration offset (Years)')).toBeInTheDocument();
    });
    expect(screen.getAllByText('Manual stocking requirement').length).toBeGreaterThanOrEqual(1);
    expect(screen.getByText('No regeneration declared date')).toBeInTheDocument();
    expect(screen.getByText('No regeneration due date')).toBeInTheDocument();
  });

  it('renders multi-layer table header when there are multiple layers', async () => {
    const multiLayerStandardUnit: OpeningDetailsStockingDto[] = [
      {
        ...mockStandardUnits[0],
        preferredSpecies: [],
        acceptableSpecies: [],
        layers: [
          {
            layer: { code: '1', description: 'Overstory' },
            targetWellspacedTrees: 1000,
            minWellspacedTrees: 600,
            minPreferredWellspacedTrees: 500,
            minHorizontalDistanceWellspacedTrees: 2.0,
          },
          {
            layer: { code: '2', description: 'Understory' },
            targetWellspacedTrees: 800,
            minWellspacedTrees: 400,
            minPreferredWellspacedTrees: 300,
            minHorizontalDistanceWellspacedTrees: 1.5,
          },
        ],
      },
    ];

    vi.mocked(API.OpeningEndpointService.getOpeningSsu).mockResolvedValue(multiLayerStandardUnit);
    renderComponent();

    await waitFor(() => {
      expect(screen.getByText(/0 species in a multi layer/i)).toBeInTheDocument();
    });
    expect(screen.getByRole('columnheader', { name: /Layer/i })).toBeInTheDocument();
    expect(screen.getByText('Layer 1 - Overstory')).toBeInTheDocument();
    expect(screen.getByText('Layer 2 - Understory')).toBeInTheDocument();
  });

  it('renders single FSP link without tooltip when only one FSP ID exists', async () => {
    const singleFspStandardUnit: OpeningDetailsStockingDto[] = [
      {
        ...mockStandardUnits[0],
        stocking: {
          ...mockStandardUnits[0].stocking,
          possibleFspIds: [999],
        },
      },
    ];

    vi.mocked(API.OpeningEndpointService.getOpeningSsu).mockResolvedValue(singleFspStandardUnit);
    renderComponent();

    await waitFor(() => {
      expect(screen.getByText('FSP ID 999')).toBeInTheDocument();
    });
    expect(screen.queryByTestId('fsp-ids-tooltip')).toBeNull();
  });

  it('renders zero standards units message when list is empty', async () => {
    vi.mocked(API.OpeningEndpointService.getOpeningSsu).mockResolvedValue([]);
    renderComponent();

    await waitFor(() => {
      expect(screen.getByText(/No standards units in the opening area/i)).toBeInTheDocument();
    });
  });

  it('handles deep linking for ssuComment', async () => {
    renderComponent(123, '/openings/123?tab=standards-units&ssuId=1&section=ssu-comment');

    await waitFor(() => {
      expect(mockUseDeepLinkScroll).toHaveBeenCalledWith('ssu-comment-1', true);
    });
  });

  it('handles deep linking for milestoneComment', async () => {
    renderComponent(123, '/openings/123?tab=standards-units&ssuId=1&section=milestone-comment');

    await waitFor(() => {
      expect(mockUseDeepLinkScroll).toHaveBeenCalledWith('milestone-comment-1', true);
    });
  });

  it('handles deep linking for ssu accordion default when section is not provided', async () => {
    renderComponent(123, '/openings/123?tab=standards-units&ssuId=1');

    await waitFor(() => {
      expect(mockUseDeepLinkScroll).toHaveBeenCalledWith('ssu-accordion-1', true);
    });
  });
});
