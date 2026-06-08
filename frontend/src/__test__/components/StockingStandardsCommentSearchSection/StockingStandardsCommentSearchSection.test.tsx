import React from 'react';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import StockingStandardsCommentSearchSection from '../../../components/StockingStandardsCommentSearchSection';
import { renderWithProviders, queryClient } from '../../utils/testAuthProvider';
import * as sectionUtils from '../../../components/StockingStandardsCommentSearchSection/utils';
import API from '../../../services/API';

// ─── Fixtures ──────────────────────────────────────────────────────────────

const mockResponse = {
  content: [
    {
      standardsRegimeId: 1001,
      commentLocation: 'STANDARDS_NAME',
      commentText: 'Test comment with keyword in it',
      updateTimestamp: '2024-03-15T10:00:00',
    },
    {
      standardsRegimeId: 1002,
      commentLocation: 'ADDITIONAL_STANDARDS',
      commentText: 'Another comment mentioning keyword again',
      updateTimestamp: '2024-03-14T14:30:00',
    },
  ],
  page: { totalElements: 2, size: 20, page: 0, totalPages: 1 },
};

const mockEmptyResponse = {
  content: [],
  page: { totalElements: 0, size: 20, page: 0, totalPages: 0 },
};

// ─── Mocks ─────────────────────────────────────────────────────────────────

vi.mock('../../../services/API', () => ({
  default: {
    SearchEndpointService: {
      searchStockingStandardsComments: vi.fn(),
    },
    CodesEndpointService: {
      getOpeningOrgUnits: vi.fn(),
    },
  },
}));

vi.mock('../../../components/StockingStandardsCommentSearchSection/utils', () => ({
  readStockingStandardsCommentSearchUrlParams: vi.fn(() => ({})),
  updateStockingStandardsCommentSearchUrlParams: vi.fn(),
  hasStockingStandardsCommentSearchFilters: vi.fn((params) => {
    if (!params) return false;
    return Object.values(params).some((v) => v !== undefined && v !== '');
  }),
}));

vi.mock('../../../components/StockingStandardsCommentSearchSection/StockingStandardsCommentSearchInput', () => ({
  default: ({ searchParams, handleSearchFieldChange, showValidation, onSearch }: any) => (
    <div data-testid="ss-comment-search-input">
      <input
        data-testid="search-term-input"
        value={searchParams?.searchTerm ?? ''}
        onChange={(e) => handleSearchFieldChange('searchTerm', e.target.value || undefined)}
        placeholder="Enter keywords"
      />
      <button data-testid="input-search-button" onClick={() => onSearch?.()}>Trigger Input Search</button>
      {showValidation && !searchParams?.searchTerm && (
        <span data-testid="validation-error">Minimum 3 characters required</span>
      )}
    </div>
  ),
}));

vi.mock('../../../components/StockingStandardsCommentSearchSection/StockingStandardsCommentSearchCard', () => ({
  default: ({ commentDto, keyword, index }: any) => (
    <div
      data-testid={`ss-comment-card-${index}`}
      className={index % 2 !== 0 ? 'shaded' : ''}
    >
      <span data-testid={`card-ssid-${index}`}>{commentDto.standardsRegimeId}</span>
      <span data-testid={`card-keyword-${index}`}>{keyword}</span>
      <span data-testid={`card-text-${index}`}>{commentDto.commentText}</span>
    </div>
  ),
}));

vi.mock('../../../hooks/useScrollToSearchResults', () => ({
  default: vi.fn(),
}));

vi.mock('../../../constants/tanstackConfig', () => ({
  isAuthRefreshInProgress: vi.fn(() => false),
}));

vi.mock('../../../components/EmptySection', () => ({
  default: () => <div data-testid="empty-section">No results found</div>,
}));

// ─── Helpers ───────────────────────────────────────────────────────────────

const typeAndSearch = async (user: ReturnType<typeof userEvent.setup>, term: string) => {
  const input = screen.getByTestId('search-term-input');
  await user.clear(input);
  await user.type(input, term);
  await user.click(screen.getByRole('button', { name: /^Search$/i }));
};

// ─── Tests ─────────────────────────────────────────────────────────────────

describe('StockingStandardsCommentSearchSection', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    queryClient.clear();
    // Reset URL util mocks to defaults (vi.clearAllMocks only clears calls, not implementations)
    (sectionUtils.readStockingStandardsCommentSearchUrlParams as any).mockReturnValue({});
    (sectionUtils.hasStockingStandardsCommentSearchFilters as any).mockReturnValue(false);
    (sectionUtils.updateStockingStandardsCommentSearchUrlParams as any).mockReturnValue(undefined);
    (API.SearchEndpointService.searchStockingStandardsComments as any).mockResolvedValue(mockResponse);
    (API.CodesEndpointService.getOpeningOrgUnits as any).mockResolvedValue([]);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  // ── Initial state ──────────────────────────────────────────────────────

  describe('Initial state', () => {
    it('renders search input', () => {
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      expect(screen.getByTestId('ss-comment-search-input')).toBeInTheDocument();
    });

    it('shows no results on initial load', () => {
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      expect(screen.queryByText('Search results')).not.toBeInTheDocument();
    });

    it('shows Clear all and Search buttons', () => {
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      expect(screen.getByRole('button', { name: /Clear all/i })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /^Search$/i })).toBeInTheDocument();
    });
  });

  // ── URL param rehydration ──────────────────────────────────────────────

  describe('URL param rehydration', () => {
    it('triggers search on mount when URL has searchTerm', async () => {
      (sectionUtils.readStockingStandardsCommentSearchUrlParams as any).mockReturnValue({ searchTerm: 'hello', page: 0, size: 20 });
      (sectionUtils.hasStockingStandardsCommentSearchFilters as any).mockReturnValue(true);

      render(<StockingStandardsCommentSearchSection />, renderWithProviders());

      await waitFor(() => {
        expect(API.SearchEndpointService.searchStockingStandardsComments).toHaveBeenCalledWith(
          'hello',
          undefined,
          undefined,
          undefined,
          undefined,
          undefined,
          0,
          20,
          undefined,
        );
      });
    });

    it('does not search on mount when URL is empty', () => {
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      expect(API.SearchEndpointService.searchStockingStandardsComments).not.toHaveBeenCalled();
    });

    it('restores page and size from URL', async () => {
      (sectionUtils.readStockingStandardsCommentSearchUrlParams as any).mockReturnValue({ searchTerm: 'test', page: 2, size: 50 });
      (sectionUtils.hasStockingStandardsCommentSearchFilters as any).mockReturnValue(true);

      render(<StockingStandardsCommentSearchSection />, renderWithProviders());

      await waitFor(() => {
        expect(API.SearchEndpointService.searchStockingStandardsComments).toHaveBeenCalledWith(
          'test', undefined, undefined, undefined, undefined, undefined, 2, 50, undefined,
        );
      });
    });
  });

  // ── Validation ────────────────────────────────────────────────────────

  describe('Validation', () => {
    it('shows validation error when Search clicked with no term', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await user.click(screen.getByRole('button', { name: /^Search$/i }));
      expect(screen.getByTestId('validation-error')).toBeInTheDocument();
    });

    it('does not call API when term is too short', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await user.type(screen.getByTestId('search-term-input'), 'ab');
      await user.click(screen.getByRole('button', { name: /^Search$/i }));
      expect(API.SearchEndpointService.searchStockingStandardsComments).not.toHaveBeenCalled();
    });

    it('does not call API when term exceeds max length', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      const input = screen.getByTestId('search-term-input');
      // Use fireEvent to avoid typing 2001 characters one by one
      fireEvent.change(input, { target: { value: 'a'.repeat(2001) } });
      await user.click(screen.getByRole('button', { name: /^Search$/i }));
      expect(API.SearchEndpointService.searchStockingStandardsComments).not.toHaveBeenCalled();
    });

    it('calls API for valid 3-character term', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'abc');
      await waitFor(() => {
        expect(API.SearchEndpointService.searchStockingStandardsComments).toHaveBeenCalled();
      });
    });
  });

  // ── Search flow ───────────────────────────────────────────────────────

  describe('Search button', () => {
    it('calls the API with correct search term', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'keyword');
      await waitFor(() => {
        expect(API.SearchEndpointService.searchStockingStandardsComments).toHaveBeenCalledWith(
          'keyword', undefined, undefined, undefined, undefined, undefined, 0, 20, undefined,
        );
      });
    });

    it('resets to page 0 on new search', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'test');
      await waitFor(() => {
        const calls = (API.SearchEndpointService.searchStockingStandardsComments as any).mock.calls;
        expect(calls[0][6]).toBe(0);
      });
    });

    it('updates URL params on search', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'test');
      await waitFor(() => {
        expect(sectionUtils.updateStockingStandardsCommentSearchUrlParams).toHaveBeenCalled();
      });
    });

    it('also triggers search via the input component\'s onSearch callback', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await user.type(screen.getByTestId('search-term-input'), 'test');
      await user.click(screen.getByTestId('input-search-button'));
      await waitFor(() => {
        expect(API.SearchEndpointService.searchStockingStandardsComments).toHaveBeenCalled();
      });
    });
  });

  // ── Clear all ─────────────────────────────────────────────────────────

  describe('Clear all button', () => {
    it('hides results after clearing', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'test');
      await waitFor(() => expect(screen.getByText('Search results')).toBeInTheDocument());

      await user.click(screen.getByRole('button', { name: /Clear all/i }));
      expect(screen.queryByText('Search results')).not.toBeInTheDocument();
    });

    it('clears the search input', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');
      await user.click(screen.getByRole('button', { name: /Clear all/i }));
      expect(input).toHaveValue('');
    });

    it('clears validation error after reset', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await user.click(screen.getByRole('button', { name: /^Search$/i }));
      expect(screen.getByTestId('validation-error')).toBeInTheDocument();

      await user.click(screen.getByRole('button', { name: /Clear all/i }));
      expect(screen.queryByTestId('validation-error')).not.toBeInTheDocument();
    });

    it('calls updateStockingStandardsCommentSearchUrlParams with undefined', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await user.click(screen.getByRole('button', { name: /Clear all/i }));
      expect(sectionUtils.updateStockingStandardsCommentSearchUrlParams).toHaveBeenCalledWith(undefined);
    });
  });

  // ── Results display ───────────────────────────────────────────────────

  describe('Results display', () => {
    it('shows "Search results" banner after valid search', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'test');
      await waitFor(() => expect(screen.getByText('Search results')).toBeInTheDocument());
    });

    it('shows correct total element count', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'test');
      await waitFor(() => expect(screen.getByText('2')).toBeInTheDocument());
    });

    it('renders a card for each result', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'test');
      await waitFor(() => {
        expect(screen.getByTestId('ss-comment-card-0')).toBeInTheDocument();
        expect(screen.getByTestId('ss-comment-card-1')).toBeInTheDocument();
      });
    });

    it('passes keyword to each card', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'testkey');
      await waitFor(() => {
        expect(screen.getByTestId('card-keyword-0')).toHaveTextContent('testkey');
        expect(screen.getByTestId('card-keyword-1')).toHaveTextContent('testkey');
      });
    });

    it('applies zebra shading: odd-index cards are shaded', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'test');
      await waitFor(() => {
        expect(screen.getByTestId('ss-comment-card-0')).not.toHaveClass('shaded');
        expect(screen.getByTestId('ss-comment-card-1')).toHaveClass('shaded');
      });
    });

    it('renders pagination when results exist', async () => {
      const user = userEvent.setup();
      const { container } = render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'test');
      await waitFor(() => {
        expect(container.querySelector('.cds--pagination')).toBeInTheDocument();
      });
    });
  });

  // ── Empty results ─────────────────────────────────────────────────────

  describe('Empty results', () => {
    it('shows empty section when API returns zero results', async () => {
      (API.SearchEndpointService.searchStockingStandardsComments as any).mockResolvedValue(mockEmptyResponse);
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'nothinghere');
      await waitFor(() => {
        expect(screen.getByTestId('empty-section')).toBeInTheDocument();
      });
    });

    it('does not show cards when there are no results', async () => {
      (API.SearchEndpointService.searchStockingStandardsComments as any).mockResolvedValue(mockEmptyResponse);
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'nothinghere');
      await waitFor(() => {
        expect(screen.queryByTestId('ss-comment-card-0')).not.toBeInTheDocument();
      });
    });
  });

  // ── StockingStandardsCommentSearchCard rendering ──────────────────────

  describe('StockingStandardsCommentSearchCard rendering (via section)', () => {
    it('passes standardsRegimeId to card', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'test');
      await waitFor(() => {
        expect(screen.getByTestId('card-ssid-0')).toHaveTextContent('1001');
        expect(screen.getByTestId('card-ssid-1')).toHaveTextContent('1002');
      });
    });

    it('passes comment text to card', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      await typeAndSearch(user, 'test');
      await waitFor(() => {
        expect(screen.getByTestId('card-text-0')).toHaveTextContent('Test comment with keyword in it');
      });
    });
  });

  // ── StockingStandardsCommentSearchInput rendering ─────────────────────

  describe('StockingStandardsCommentSearchInput rendering (via section)', () => {
    it('renders the input component', () => {
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      expect(screen.getByTestId('ss-comment-search-input')).toBeInTheDocument();
    });

    it('passes searchParams.searchTerm to the input', async () => {
      const user = userEvent.setup();
      render(<StockingStandardsCommentSearchSection />, renderWithProviders());
      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'hello');
      expect(input).toHaveValue('hello');
    });
  });
});
