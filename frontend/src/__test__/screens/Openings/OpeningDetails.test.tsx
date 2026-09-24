import React from 'react';
import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import OpeningDetails from '@/screens/Openings/OpeningDetails';
import API from '@/services/API';
import { useAuth } from '@/contexts/AuthProvider';

vi.mock('@/services/API', () => ({
  default: {
    OpeningEndpointService: {
      getOpeningTombstone: vi.fn(),
    },
    UserRecentOpeningEndpointService: {
      recordUserViewedOpening: vi.fn(),
    },
  },
}));

vi.mock('@/contexts/AuthProvider', () => ({
  useAuth: vi.fn(),
}));

vi.mock('@/components/OpeningBookmarkBtn', () => ({
  default: () => <button data-testid="opening-bookmark-btn">Bookmark</button>,
}));

vi.mock('@/components/OpeningDetails', () => ({
  OpeningSummary: () => <div data-testid="opening-summary">Opening Summary</div>,
}));

vi.mock('@/components/OpeningDetails/OpeningNotifications', () => ({
  default: () => <div data-testid="opening-notifications">Notifications Alert</div>,
}));

vi.mock('@/components/OpeningDetails/OpeningOverview', () => ({
  default: () => <div data-testid="overview-tab-content">Overview Tab Content</div>,
}));

vi.mock('@/components/OpeningDetails/TenureIdentification', () => ({
  default: () => <div data-testid="tenure-tab-content">Tenure Tab Content</div>,
}));

vi.mock('@/components/OpeningDetails/OpeningStandardUnits', () => ({
  default: () => <div data-testid="standards-units-tab-content">Standards Units Tab Content</div>,
}));

vi.mock('@/components/OpeningDetails/OpeningActivities', () => ({
  default: () => <div data-testid="activities-tab-content">Activities Tab Content</div>,
}));

vi.mock('@/components/OpeningDetails/OpeningForestCover', () => ({
  default: () => <div data-testid="forest-cover-tab-content">Forest Cover Tab Content</div>,
}));

vi.mock('@/components/OpeningDetails/OpeningAttachment', () => ({
  default: () => <div data-testid="attachment-tab-content">Attachments Tab Content</div>,
}));

describe('OpeningDetails Screen', () => {
  let queryClient: QueryClient;

  const mockTombstoneData = {
    tombstone: { openingId: 12345 },
    overview: { openingId: 12345 },
    notifications: [],
  };

  beforeEach(() => {
    vi.clearAllMocks();
    queryClient = new QueryClient({
      defaultOptions: { queries: { retry: false } },
    });

    vi.mocked(useAuth).mockReturnValue({
      user: { idpProvider: 'IDIR' } as any,
    } as any);

    vi.mocked(API.OpeningEndpointService.getOpeningTombstone).mockResolvedValue(mockTombstoneData as any);
    vi.mocked(API.UserRecentOpeningEndpointService.recordUserViewedOpening).mockResolvedValue({} as any);
  });

  const renderComponent = (initialRoute = '/openings/12345') => {
    return render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter initialEntries={[initialRoute]}>
          <Routes>
            <Route path="/openings/:openingId" element={<OpeningDetails />} />
          </Routes>
        </MemoryRouter>
      </QueryClientProvider>
    );
  };

  it('updates document title on mount and resets on unmount', () => {
    const { unmount } = renderComponent();
    expect(document.title).toBe('Opening 12345 - Silva');

    unmount();
    expect(document.title).toBe('Silva');
  });

  it('calls recordUserViewedOpening mutation when tombstone query succeeds', async () => {
    renderComponent();

    await waitFor(() => {
      expect(API.UserRecentOpeningEndpointService.recordUserViewedOpening).toHaveBeenCalledWith(12345);
    });
  });

  it('renders 404 empty state when tombstone request returns 404', async () => {
    const error: any = new Error('Not found');
    error.response = { status: 404 };
    vi.mocked(API.OpeningEndpointService.getOpeningTombstone).mockRejectedValue(error);

    renderComponent();

    await waitFor(() => {
      expect(screen.getByText('Opening 12345 not found')).toBeInTheDocument();
    });
  });

  it('renders general error empty state when tombstone request fails with 500', async () => {
    const error: any = new Error('Database error');
    error.response = { status: 500 };
    vi.mocked(API.OpeningEndpointService.getOpeningTombstone).mockRejectedValue(error);

    renderComponent();

    await waitFor(() => {
      expect(screen.getByText('Error fetching data for Opening 12345')).toBeInTheDocument();
      expect(screen.getByText('Database error')).toBeInTheDocument();
    });
  });

  it('renders notifications when notifications array has items', async () => {
    vi.mocked(API.OpeningEndpointService.getOpeningTombstone).mockResolvedValue({
      ...mockTombstoneData,
      notifications: [{ notificationId: 1, message: 'Important notice' }],
    } as any);

    renderComponent();

    const notifications = await screen.findByTestId('opening-notifications');
    expect(notifications).toBeInTheDocument();
  });

  it('renders all tabs for IDIR user including Attachments tab', async () => {
    renderComponent();

    expect(await screen.findByText('Overview')).toBeInTheDocument();
    expect(screen.getByText('Tenure identification')).toBeInTheDocument();
    expect(screen.getByText('Standards units')).toBeInTheDocument();
    expect(screen.getByText('Activities')).toBeInTheDocument();
    expect(screen.getByText('Forest cover')).toBeInTheDocument();
    expect(screen.getByText('Attachments')).toBeInTheDocument();

    const overviewContent = await screen.findByTestId('overview-tab-content');
    expect(overviewContent).toBeInTheDocument();
  });

  it('hides Attachments tab for non-IDIR users', async () => {
    vi.mocked(useAuth).mockReturnValue({
      user: { idpProvider: 'BCEID' } as any,
    } as any);

    renderComponent();

    await screen.findByText('Overview');
    expect(screen.queryByText('Attachments')).toBeNull();
  });

  it('opens Tenure identification tab via URL search params', async () => {
    renderComponent('/openings/12345?tab=tenure-identification');

    const tenureContent = await screen.findByTestId('tenure-tab-content');
    expect(tenureContent).toBeInTheDocument();
  });

  it('opens Standards units tab via URL search params', async () => {
    renderComponent('/openings/12345?tab=standards-units');

    const suContent = await screen.findByTestId('standards-units-tab-content');
    expect(suContent).toBeInTheDocument();
  });

  it('opens Activities tab via URL search params', async () => {
    renderComponent('/openings/12345?tab=activities');

    const activitiesContent = await screen.findByTestId('activities-tab-content');
    expect(activitiesContent).toBeInTheDocument();
  });

  it('opens Forest cover tab via URL search params', async () => {
    renderComponent('/openings/12345?tab=forest-cover');

    const fcContent = await screen.findByTestId('forest-cover-tab-content');
    expect(fcContent).toBeInTheDocument();
  });

  it('opens Attachments tab via URL search params for IDIR user', async () => {
    renderComponent('/openings/12345?tab=attachments');

    const attachContent = await screen.findByTestId('attachment-tab-content');
    expect(attachContent).toBeInTheDocument();
  });

  it('switches tabs on tab click', async () => {
    renderComponent();

    await screen.findByText('Overview');
    const tenureTab = screen.getByRole('tab', { name: /Tenure identification/i });
    fireEvent.click(tenureTab);

    const tenureContent = await screen.findByTestId('tenure-tab-content');
    expect(tenureContent).toBeInTheDocument();
  });
});
