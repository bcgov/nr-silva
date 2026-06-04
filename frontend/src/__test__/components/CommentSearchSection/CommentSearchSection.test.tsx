import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import CommentSearchSection from '../../../components/CommentSearchSection';
import { renderWithProviders, queryClient } from '../../utils/testAuthProvider';
import * as commentSearchUtils from '../../../components/CommentSearchSection/utils';
import API from '../../../services/API';

const mockCommentSearchResponse = {
  content: [
    {
      openingId: 100,
      commentLocation: 'OPENING',
      activityKind: null,
      activityTreatmentUnitId: null,
      standardsUnitId: null,
      standardsUnitName: null,
      commentText: 'Test comment with keyword in it',
      updateTimestamp: '2024-03-15T10:00:00',
    },
    {
      openingId: 101,
      commentLocation: 'STANDARDS_UNIT',
      activityKind: null,
      activityTreatmentUnitId: null,
      standardsUnitId: 5,
      standardsUnitName: 'SU-001',
      commentText: 'Another comment mentioning keyword',
      updateTimestamp: '2024-03-14T14:30:00',
    },
  ],
  page: { totalElements: 2, size: 20, page: 0, totalPages: 1 },
};

const mockEmptyResponse = {
  content: [],
  page: { totalElements: 0, size: 20, page: 0, totalPages: 0 },
};

// Mock API
vi.mock('../../../services/API', () => ({
  default: {
    SearchEndpointService: {
      searchComments: vi.fn(),
    },
    CodesEndpointService: {
      getOpeningOrgUnits: vi.fn(),
    },
  },
}));

// Mock utils
vi.mock('../../../components/CommentSearchSection/utils', () => ({
  readCommentSearchUrlParams: vi.fn(() => ({})),
  updateCommentSearchUrlParams: vi.fn(),
  hasCommentSearchFilters: vi.fn((params) => {
    if (!params) return false;
    return Object.values(params).some((val) => val !== undefined && val !== '');
  }),
}));

// Mock CommentSearchInput
vi.mock('../../../components/CommentSearchSection/CommentSearchInput', () => ({
  default: ({ searchParams, handleSearchFieldChange, showValidation, onSearch }: any) => (
    <div data-testid="comment-search-input">
      <input
        data-testid="search-term-input"
        value={searchParams?.searchTerm || ''}
        onChange={(e) => handleSearchFieldChange('searchTerm', e.target.value)}
        placeholder="Search term"
      />
      <button data-testid="search-button" onClick={() => onSearch?.()}>
        Search
      </button>
      {showValidation && !searchParams?.searchTerm && (
        <span data-testid="validation-error">Minimum 3 characters required</span>
      )}
    </div>
  ),
}));

// Mock CommentSearchCard
vi.mock('../../../components/CommentSearchSection/CommentSearchCard', () => ({
  default: ({ commentDto, keyword, index }: any) => (
    <div data-testid={`comment-card-${index}`} className={index % 2 !== 0 ? 'shaded' : ''}>
      <span data-testid={`comment-text-${index}`}>{commentDto.commentText}</span>
      <span data-testid={`comment-keyword-${index}`}>{keyword}</span>
    </div>
  ),
}));

// Mock useScrollToSearchResults
vi.mock('../../../hooks/useScrollToSearchResults', () => ({
  default: vi.fn(),
}));

// Mock isAuthRefreshInProgress
vi.mock('../../../constants/tanstackConfig', () => ({
  isAuthRefreshInProgress: vi.fn(() => false),
}));

// Mock PageContent
vi.mock('../../../components/PageContent', () => ({
  default: ({ children }: any) => <div>{children}</div>,
}));

// Mock EmptySection
vi.mock('../../../components/EmptySection', () => ({
  default: () => <div>No results found</div>,
}));


describe('CommentSearchSection', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    queryClient.clear();
    (API.SearchEndpointService.searchComments as any).mockResolvedValue(
      mockCommentSearchResponse
    );
    (API.CodesEndpointService.getOpeningOrgUnits as any).mockResolvedValue([]);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  describe('Initial State & URL Params', () => {
    it('should render without search results on initial load', () => {
      render(<CommentSearchSection />, renderWithProviders());
      expect(screen.queryByText('Search results')).not.toBeInTheDocument();
    });

    it('should load URL params on mount', () => {
      (commentSearchUtils.readCommentSearchUrlParams as any).mockReturnValue({
        searchTerm: 'test',
      });
      (commentSearchUtils.hasCommentSearchFilters as any).mockReturnValue(true);

      render(<CommentSearchSection />, renderWithProviders());
      expect(commentSearchUtils.readCommentSearchUrlParams).toHaveBeenCalled();
    });

    it('should restore pagination from URL params', async () => {
      (commentSearchUtils.readCommentSearchUrlParams as any).mockReturnValue({
        searchTerm: 'test',
        page: 1,
        size: 50,
      });
      (commentSearchUtils.hasCommentSearchFilters as any).mockReturnValue(true);

      render(<CommentSearchSection />, renderWithProviders());

      await waitFor(() => {
        expect(API.SearchEndpointService.searchComments).toHaveBeenCalledWith(
          'test',
          undefined,
          undefined,
          undefined,
          undefined,
          undefined,
          1,
          50,
          undefined
        );
      });
    });

    it('should handle missing URL params gracefully', () => {
      (commentSearchUtils.readCommentSearchUrlParams as any).mockReturnValue({});
      (commentSearchUtils.hasCommentSearchFilters as any).mockReturnValue(false);

      render(<CommentSearchSection />, renderWithProviders());
      expect(screen.queryByText('Search results')).not.toBeInTheDocument();
    });
  });

  describe('Search Input & Validation', () => {
    it('should update searchParams when input value changes', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      expect(input).toHaveValue('test');
    });

    it('should not commit searchParams to queryParams until Search button clicked', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      // Query should not be called yet
      expect(API.SearchEndpointService.searchComments).not.toHaveBeenCalled();
    });

    it('should reject searchTerm less than 3 characters', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'ab');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      // Should not call API
      expect(API.SearchEndpointService.searchComments).not.toHaveBeenCalled();
    });

    it('should reject searchTerm greater than 2000 characters', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      const longText = 'a'.repeat(2001);
      await user.clear(input);
      await user.paste(longText);

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      // Should not call API
      expect(API.SearchEndpointService.searchComments).not.toHaveBeenCalled();
    });

    it('should show validation error when showValidation is true and searchTerm is empty', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      expect(screen.getByTestId('validation-error')).toBeInTheDocument();
    });

    it('should accept searchTerm with 3 or more characters', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(API.SearchEndpointService.searchComments).toHaveBeenCalledWith(
          'test',
          undefined,
          undefined,
          undefined,
          undefined,
          undefined,
          0,
          20,
          undefined
        );
      });
    });
  });

  describe('Search Button', () => {
    it('should commit searchParams to queryParams on valid search', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(API.SearchEndpointService.searchComments).toHaveBeenCalled();
      });
    });

    it('should reset pagination to page 0 on search', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        const calls = (API.SearchEndpointService.searchComments as any).mock.calls;
        expect(calls[0][6]).toBe(0); // page param is 0
      });
    });

    it('should update URL params on search', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(commentSearchUtils.updateCommentSearchUrlParams).toHaveBeenCalled();
      });
    });

    it('should not call API on search with invalid term', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'ab');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      expect(API.SearchEndpointService.searchComments).not.toHaveBeenCalled();
    });
  });

  describe('Reset Button', () => {
    it('should clear all state on reset', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const clearButton = screen.getByRole('button', { name: /Clear all/i });
      await user.click(clearButton);

      expect(input).toHaveValue('');
    });

    it('should reset showValidation flag', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      const clearButton = screen.getByRole('button', { name: /Clear all/i });
      await user.click(clearButton);

      expect(screen.queryByTestId('validation-error')).not.toBeInTheDocument();
    });

    it('should call updateCommentSearchUrlParams(undefined)', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const clearButton = screen.getByRole('button', { name: /Clear all/i });
      await user.click(clearButton);

      expect(commentSearchUtils.updateCommentSearchUrlParams).toHaveBeenCalledWith(
        undefined
      );
    });
  });

  describe('Query Execution', () => {
    it('should execute query when queryParams and searchTerm valid', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(API.SearchEndpointService.searchComments).toHaveBeenCalled();
      });
    });

    it('should not execute query when searchTerm invalid', () => {
      render(<CommentSearchSection />, renderWithProviders());
      expect(API.SearchEndpointService.searchComments).not.toHaveBeenCalled();
    });

    it('should pass searchTerm to API', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'keyword');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(API.SearchEndpointService.searchComments).toHaveBeenCalledWith(
          'keyword',
          undefined,
          undefined,
          undefined,
          undefined,
          undefined,
          0,
          20,
          undefined
        );
      });
    });

    it('should pass commentLocation filters to API', async () => {
      (commentSearchUtils.readCommentSearchUrlParams as any).mockReturnValue({
        searchTerm: 'test',
        commentLocation: ['OPENING', 'STANDARDS_UNIT'],
        page: 0,
        size: 20,
      });
      (commentSearchUtils.hasCommentSearchFilters as any).mockReturnValue(true);

      render(<CommentSearchSection />, renderWithProviders());

      await waitFor(() => {
        expect(API.SearchEndpointService.searchComments).toHaveBeenCalledWith(
          'test',
          ['OPENING', 'STANDARDS_UNIT'],
          undefined,
          undefined,
          undefined,
          undefined,
          0,
          20,
          undefined
        );
      });
    });
  });

  describe('Results Display', () => {
    it('should show search results banner when search valid', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(screen.getByText('Search results')).toBeInTheDocument();
      });
    });

    it('should display total count of results', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(screen.getByText('2')).toBeInTheDocument();
      });
    });

    it('should show loading indicator during query', async () => {
      let resolveQuery: any;
      const queryPromise = new Promise((resolve) => {
        resolveQuery = resolve;
      });
      (API.SearchEndpointService.searchComments as any).mockReturnValue(queryPromise);

      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      // Results banner should appear but query still loading
      await waitFor(() => {
        expect(screen.getByText('Search results')).toBeInTheDocument();
      });

      resolveQuery(mockCommentSearchResponse);
    });
  });

  describe('Empty Results', () => {
    it('should show EmptySection when no results found', async () => {
      (API.SearchEndpointService.searchComments as any).mockResolvedValue(mockEmptyResponse);

      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(screen.getByText('No results found')).toBeInTheDocument();
      });
    });

    it('should not show EmptySection when results exist', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(screen.queryByText('No results found')).not.toBeInTheDocument();
      });
    });
  });

  describe('Results Grid & CommentSearchCard', () => {
    it('should render CommentSearchCard for each result', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(screen.getByTestId('comment-card-0')).toBeInTheDocument();
        expect(screen.getByTestId('comment-card-1')).toBeInTheDocument();
      });
    });

    it('should pass correct keyword to CommentSearchCard', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'testkey');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(screen.getByTestId('comment-keyword-0')).toHaveTextContent('testkey');
      });
    });

    it('should pass index for zebra striping', async () => {
      const user = userEvent.setup();
      render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        // Index 0: even, not shaded
        expect(screen.getByTestId('comment-card-0')).not.toHaveClass('shaded');
        // Index 1: odd, shaded
        expect(screen.getByTestId('comment-card-1')).toHaveClass('shaded');
      });
    });
  });

  describe('Pagination', () => {
    it('should render pagination when results exist', async () => {
      const user = userEvent.setup();
      const { container } = render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(container.querySelector('.cds--pagination')).toBeInTheDocument();
      });
    });

    it('should not render pagination when no results', async () => {
      (API.SearchEndpointService.searchComments as any).mockResolvedValue(mockEmptyResponse);

      const user = userEvent.setup();
      const { container } = render(<CommentSearchSection />, renderWithProviders());

      const input = screen.getByTestId('search-term-input');
      await user.type(input, 'test');

      const searchButton = screen.getByTestId('search-button');
      await user.click(searchButton);

      await waitFor(() => {
        expect(container.querySelector('.cds--pagination')).not.toBeInTheDocument();
      });
    });
  });
});
