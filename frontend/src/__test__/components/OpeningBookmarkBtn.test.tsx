import React from 'react';
import { render, fireEvent, waitFor, screen } from '@testing-library/react';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import OpeningBookmarkBtn from '../../components/OpeningBookmarkBtn';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import '@testing-library/jest-dom';

// Mock the API module used by the component (mock both alias and relative path)
vi.mock('@/services/API', () => ({
  default: {
    OpeningEndpointService: {
      getFavorites: vi.fn().mockResolvedValue([]),
      addToFavorites: vi.fn(),
      removeFromFavorites: vi.fn(),
    },
  },
}));
vi.mock('../../services/API', () => ({
  default: {
    OpeningEndpointService: {
      getFavorites: vi.fn().mockResolvedValue([]),
      addToFavorites: vi.fn(),
      removeFromFavorites: vi.fn(),
    },
  },
}));

import API from '../../services/API';

const renderWithQuery = (ui: React.ReactElement, initialFavorites: number[] = []) => {
  const qc = new QueryClient();
  // seed the cache for the single favourites query which returns an array of ids
  qc.setQueryData(['openings', 'favourites'], initialFavorites);
  return { ...render(<QueryClientProvider client={qc}>{ui}</QueryClientProvider>), qc };
};

describe('OpeningBookmarkBtn', () => {
  beforeEach(() => {
    vi.resetAllMocks();
  });

  it('renders bookmark button when not favourited', async () => {
    // ensure getFavorites resolves to an array that does NOT include 101
    (API.OpeningEndpointService.getFavorites as any).mockResolvedValue([]);

    renderWithQuery(
      <table>
        <tbody>
          <tr>
            <td>
              <OpeningBookmarkBtn openingId={101} />
            </td>
          </tr>
        </tbody>
      </table>,
      []
    );

    const btn = await waitFor(() => screen.getByRole('button', { name: /Bookmark 101/i }));
    expect(btn).toBeInTheDocument();
  });

  it('renders unbookmark state when favourited', async () => {
    (API.OpeningEndpointService.getFavorites as any).mockResolvedValue([202]);
    renderWithQuery(<OpeningBookmarkBtn openingId={202} />, [202]);

    const btn = await waitFor(() => screen.getByRole('button', { name: /Unbookmark 202/i }));
    expect(btn).toBeInTheDocument();
  });

  it('calls addToFavorites when clicking bookmark (not favourited)', async () => {
    (API.OpeningEndpointService.getFavorites as any).mockResolvedValue([]);
    renderWithQuery(<OpeningBookmarkBtn openingId={303} />, []);
    (API.OpeningEndpointService.addToFavorites as any).mockResolvedValue(undefined);
    const btn = await waitFor(() => screen.getByRole('button', { name: /Bookmark 303/i }));
    expect(btn).toBeInTheDocument();

    fireEvent.click(btn);

    await waitFor(() => {
      expect(API.OpeningEndpointService.addToFavorites).toHaveBeenCalled();
    });
  });

  it('calls removeFromFavorites when clicking unbookmark (favourited)', async () => {
    (API.OpeningEndpointService.getFavorites as any).mockResolvedValue([404]);
    renderWithQuery(<OpeningBookmarkBtn openingId={404} />, [404]);
    (API.OpeningEndpointService.removeFromFavorites as any).mockResolvedValue(undefined);
    const btn = await waitFor(() => screen.getByRole('button', { name: /Unbookmark 404/i }));
    expect(btn).toBeInTheDocument();

    fireEvent.click(btn);

    await waitFor(() => {
      expect(API.OpeningEndpointService.removeFromFavorites).toHaveBeenCalled();
    });
  });
});
