import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import StockingStandardsCommentSearch from '../../../screens/StockingStandardsCommentSearch';
import { renderWithProviders } from '../../utils/testAuthProvider';

vi.mock('../../../components/StockingStandardsCommentSearchSection', () => ({
  default: () => <div data-testid="ss-comment-search-section" />,
}));

vi.mock('../../../components/PageTitle', () => ({
  default: ({ title }: { title: string }) => <h1>{title}</h1>,
}));

describe('StockingStandardsCommentSearch screen', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  afterEach(() => {
    document.title = 'Silva';
  });

  it('renders the page title', () => {
    render(<StockingStandardsCommentSearch />, renderWithProviders());
    expect(screen.getByRole('heading', { name: /stocking standards keywords search/i })).toBeInTheDocument();
  });

  it('renders the search section', () => {
    render(<StockingStandardsCommentSearch />, renderWithProviders());
    expect(screen.getByTestId('ss-comment-search-section')).toBeInTheDocument();
  });

  it('sets the document title on mount', () => {
    render(<StockingStandardsCommentSearch />, renderWithProviders());
    expect(document.title).toBe('Stocking Standards Keywords Search - Silva');
  });

  it('restores document title on unmount', () => {
    const { unmount } = render(<StockingStandardsCommentSearch />, renderWithProviders());
    unmount();
    expect(document.title).toBe('Silva');
  });

  it('renders inside the correct grid classes', () => {
    const { container } = render(<StockingStandardsCommentSearch />, renderWithProviders());
    const grid = container.firstElementChild;
    expect(grid).toHaveClass('default-grid');
    expect(grid).toHaveClass('default-search-grid');
  });
});
