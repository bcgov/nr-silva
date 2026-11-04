import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import ActivityAccordion from '../../../../components/OpeningDetails/OpeningActivities/ActivityAccordion';
import API from '../../../../services/API';
import { OpeningDetailsActivitiesActivitiesDto } from '../../../../services/OpenApi';

const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom');
  return { ...actual, useNavigate: () => mockNavigate };
});

vi.mock('../../../../services/API', () => ({
  default: { OpeningEndpointService: { getOpeningActivities: vi.fn() } },
}));

vi.mock('../../../../hooks/useDeepLinkScroll', () => ({ default: vi.fn() }));

const mockAvailability = vi.fn();
vi.mock('../../../../hooks/usePolygonAvailability', () => ({
  default: (...args: unknown[]) => mockAvailability(...args),
}));

vi.mock('../../../../components/OpeningDetails/OpeningActivities/ActivityDetail', () => ({
  default: ({ activity, targetComment }: { activity: { atuId: number }; targetComment?: boolean }) => (
    <div data-testid="activity-detail">{`detail-${activity.atuId}-${String(Boolean(targetComment))}`}</div>
  ),
}));

const activities = [
  {
    atuId: 201,
    status: { code: 'PLN', description: 'Planned' },
    base: { code: 'BR', description: 'Brushing' },
    tech: { code: 'T1', description: 'Tech one' },
    method: { code: 'M1', description: 'Method one' },
    area: 12.3,
    funding: { code: 'F1', description: 'Fund one' },
    projectId: 'P1',
    lastUpdate: '2021-06-20',
    plannedDate: '2021-05-01',
    endDate: '2021-06-15',
    objective1: { code: 'O1', description: 'Obj one' },
    objective2: null,
    objective3: null,
  },
  {
    atuId: 202,
    status: { code: 'CMP', description: 'Complete' },
    base: { code: 'FG', description: 'Fertilize' },
    tech: null,
    method: null,
    area: null,
    funding: null,
    projectId: null,
    lastUpdate: null,
    plannedDate: null,
    endDate: null,
    objective1: null,
    objective2: null,
    objective3: null,
  },
] as unknown as OpeningDetailsActivitiesActivitiesDto[];

const page = (content: OpeningDetailsActivitiesActivitiesDto[], totalElements = content.length) => ({
  content,
  page: { size: 5, number: 0, totalElements, totalPages: 1 },
});

const setSelected = vi.fn();

const renderAccordion = (props: Partial<React.ComponentProps<typeof ActivityAccordion>> = {}) => {
  const client = new QueryClient({ defaultOptions: { queries: { retry: false } } });
  return render(
    <QueryClientProvider client={client}>
      <MemoryRouter>
        <ActivityAccordion
          openingId={55}
          totalUnfiltered={2}
          selectedSilvicultureActivityIds={[]}
          setSelectedSilvicultureActivityIds={setSelected}
          targetActivityId="201"
          {...props}
        />
      </MemoryRouter>
    </QueryClientProvider>
  );
};

describe('ActivityAccordion', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockAvailability.mockReturnValue({ isAvailable: true, isLoading: false });
    vi.mocked(API.OpeningEndpointService.getOpeningActivities).mockResolvedValue(page(activities) as never);
  });

  it('renders title, total and rows with placeholders for empty cells', async () => {
    renderAccordion();
    expect(screen.getByText('Total activities: 2')).toBeDefined();
    expect(await screen.findByText('201')).toBeDefined();
    expect(screen.getByText('Planned')).toBeDefined();
    expect(screen.getByText('Complete')).toBeDefined();
    expect(screen.getAllByText('--').length).toBeGreaterThan(3);
  });

  it('navigates to the create activity page when Add activity is clicked', async () => {
    renderAccordion();
    await userEvent.click(await screen.findByRole('button', { name: 'Add activity' }));
    expect(mockNavigate).toHaveBeenCalledWith('/openings/55/create-activity');
  });

  it('reports open state changes', async () => {
    const onOpenChange = vi.fn();
    renderAccordion({ targetActivityId: undefined, onOpenChange });
    await userEvent.click(screen.getByText('Silviculture activities'));
    expect(onOpenChange).toHaveBeenCalledWith(true);
  });

  it('sorts ascending, descending, then clears', async () => {
    const user = userEvent.setup();
    renderAccordion();
    await screen.findByText('201');
    const sortBtn = async () => (await screen.findByText('Activity treatment unit ID')).closest('button') as HTMLElement;
    const mock = vi.mocked(API.OpeningEndpointService.getOpeningActivities);

    await user.click(await sortBtn());
    await waitFor(() => expect(mock).toHaveBeenLastCalledWith(55, undefined, 0, expect.any(Number), ['atuId,ASC']));
    await user.click(await sortBtn());
    await waitFor(() => expect(mock).toHaveBeenLastCalledWith(55, undefined, 0, expect.any(Number), ['atuId,DESC']));
    await user.click(await sortBtn());
    await waitFor(() => expect(mock).toHaveBeenLastCalledWith(55, undefined, 0, expect.any(Number), undefined));
  });

  it('searches with the trimmed keyword via the Search button', async () => {
    renderAccordion();
    const input = await screen.findByPlaceholderText('Search by keyword');
    const mock = vi.mocked(API.OpeningEndpointService.getOpeningActivities);

    fireEvent.change(input, { target: { value: '  abc  ' } });
    await userEvent.click(screen.getByRole('button', { name: 'Search' }));
    await waitFor(() => expect(mock).toHaveBeenLastCalledWith(55, 'abc', 0, expect.any(Number), expect.anything()));
  });

  it('searches on Enter and ignores input beyond the max length', async () => {
    renderAccordion();
    const input = (await screen.findByPlaceholderText('Search by keyword')) as HTMLInputElement;
    const mock = vi.mocked(API.OpeningEndpointService.getOpeningActivities);

    fireEvent.change(input, { target: { value: 'xyz' } });
    fireEvent.change(input, { target: { value: 'x'.repeat(60) } });
    expect(input.value).toBe('xyz');
    fireEvent.keyDown(input, { key: 'Enter' });
    await waitFor(() => expect(mock).toHaveBeenLastCalledWith(55, 'xyz', 0, expect.any(Number), expect.anything()));
  });

  it('clears the search filter', async () => {
    renderAccordion();
    const input = (await screen.findByPlaceholderText('Search by keyword')) as HTMLInputElement;
    const mock = vi.mocked(API.OpeningEndpointService.getOpeningActivities);

    fireEvent.change(input, { target: { value: 'abc' } });
    fireEvent.keyDown(input, { key: 'Enter' });
    await waitFor(() => expect(mock).toHaveBeenLastCalledWith(55, 'abc', 0, expect.any(Number), expect.anything()));

    await userEvent.click(screen.getByRole('button', { name: /clear search input/i }));
    await waitFor(() => expect(mock).toHaveBeenLastCalledWith(55, undefined, 0, expect.any(Number), expect.anything()));
    expect(input.value).toBe('');
  });

  it('submits an empty search by dropping the filter', async () => {
    const user = userEvent.setup();
    renderAccordion();
    await screen.findByText('201');
    await user.click(screen.getByRole('button', { name: 'Search' }));
    const mock = vi.mocked(API.OpeningEndpointService.getOpeningActivities);
    await waitFor(() => expect(mock).toHaveBeenCalled());
    expect(mock.mock.calls.every((c) => c[1] === undefined)).toBe(true);
  });

  it('selects and deselects a row polygon', async () => {
    const user = userEvent.setup();
    renderAccordion();
    await screen.findByText('201');
    await user.click(screen.getByTestId('activities-map-checkbox-201-0').querySelector('input') ?? screen.getByTestId('activities-map-checkbox-201-0'));
    const updater = setSelected.mock.calls[0][0] as (ids: string[]) => string[];
    expect(updater([])).toEqual(['201-BR']);
  });

  it('removes a selected polygon when unchecked', async () => {
    const user = userEvent.setup();
    renderAccordion({ selectedSilvicultureActivityIds: ['201-BR'] });
    await screen.findByText('201');
    const checkbox = screen.getByTestId('activities-map-checkbox-201-0');
    await user.click(checkbox.querySelector('input') ?? checkbox);
    const updater = setSelected.mock.calls[0][0] as (ids: string[]) => string[];
    expect(updater(['201-BR', 'other'])).toEqual(['other']);
  });

  it('select-all toggles every id on the page', async () => {
    const user = userEvent.setup();
    renderAccordion();
    await screen.findByText('201');
    const all = screen.getByTestId('activities-map-select-all');
    await user.click(all.querySelector('input') ?? all);
    const add = setSelected.mock.calls[0][0] as (ids: string[]) => string[];
    expect(add(['x'])).toEqual(['x', '201-BR', '202-FG']);
  });

  it('unselects all on page when all are selected', async () => {
    const user = userEvent.setup();
    renderAccordion({ selectedSilvicultureActivityIds: ['201-BR', '202-FG', 'x'] });
    await screen.findByText('201');
    const all = screen.getByTestId('activities-map-select-all');
    await user.click(all.querySelector('input') ?? all);
    const remove = setSelected.mock.calls[0][0] as (ids: string[]) => string[];
    expect(remove(['201-BR', '202-FG', 'x'])).toEqual(['x']);
  });

  it('disables checkboxes when polygons are unavailable or loading', async () => {
    mockAvailability.mockReturnValue({ isAvailable: false, isLoading: false });
    const { unmount } = renderAccordion();
    await screen.findByText('201');
    const cb = screen.getByTestId('activities-map-checkbox-201-0');
    expect((cb.querySelector('input') ?? cb).hasAttribute('disabled')).toBe(true);
    unmount();

    mockAvailability.mockReturnValue({ isAvailable: true, isLoading: true });
    renderAccordion();
    await screen.findByText('201');
    const cb2 = screen.getByTestId('activities-map-checkbox-201-0');
    expect((cb2.querySelector('input') ?? cb2).hasAttribute('disabled')).toBe(true);
  });

  it('expands a row to show the activity detail', async () => {
    const user = userEvent.setup();
    renderAccordion();
    await screen.findByText('201');
    expect(screen.queryByTestId('activity-detail')).toBeNull();
    await user.click(screen.getByRole('button', { name: 'Expand row for Activity ID 201' }));
    expect(await screen.findByText('detail-201-false')).toBeDefined();
  });

  it('auto-expands the targeted row for comment deep links', async () => {
    const client = new QueryClient({ defaultOptions: { queries: { retry: false } } });
    render(
      <QueryClientProvider client={client}>
        <MemoryRouter initialEntries={['/?section=activity-comment']}>
          <ActivityAccordion
            openingId={55}
            totalUnfiltered={2}
            selectedSilvicultureActivityIds={[]}
            setSelectedSilvicultureActivityIds={setSelected}
            targetActivityId="201"
          />
        </MemoryRouter>
      </QueryClientProvider>
    );
    expect(await screen.findByText('detail-201-true')).toBeDefined();
  });

  it('shows empty state when a search has no results', async () => {
    vi.mocked(API.OpeningEndpointService.getOpeningActivities).mockResolvedValue(page([], 0) as never);
    renderAccordion();
    expect(await screen.findByText(/No results for/)).toBeDefined();
    const all = screen.getByTestId('activities-map-select-all');
    expect((all.querySelector('input') ?? all).hasAttribute('disabled')).toBe(true);
  });

  it('requests the next page when paginating', async () => {
    vi.mocked(API.OpeningEndpointService.getOpeningActivities).mockResolvedValue(page(activities, 40) as never);
    const user = userEvent.setup();
    renderAccordion();
    await screen.findByText('201');
    await user.click(screen.getByRole('button', { name: /next page/i }));
    const mock = vi.mocked(API.OpeningEndpointService.getOpeningActivities);
    await waitFor(() => expect(mock).toHaveBeenLastCalledWith(55, undefined, 1, expect.any(Number), expect.anything()));
  });
});
