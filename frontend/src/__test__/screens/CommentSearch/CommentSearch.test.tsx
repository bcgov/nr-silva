import React from 'react';
import { render, screen } from '@testing-library/react';
import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import CommentSearch from '../../../screens/CommentSearch';
import { renderWithProviders } from '../../utils/testAuthProvider';

// Mock CommentSearchSection
vi.mock('../../../components/CommentSearchSection', () => ({
  default: () => <div data-testid="comment-search-section">Comment Search Section</div>,
}));

// Mock PageTitle
vi.mock('../../../components/PageTitle', () => ({
  default: ({ title }: { title: string }) => <h1 data-testid="page-title">{title}</h1>,
}));

describe('CommentSearch Screen', () => {
  const originalTitle = document.title;

  beforeEach(() => {
    document.title = 'Silva';
  });

  afterEach(() => {
    document.title = originalTitle;
  });

  it('should render the page title with "Comments" text', () => {
    render(<CommentSearch />, renderWithProviders());
    const pageTitle = screen.getByTestId('page-title');
    expect(pageTitle).toBeInTheDocument();
    expect(pageTitle).toHaveTextContent('Comments');
  });

  it('should render CommentSearchSection component', () => {
    render(<CommentSearch />, renderWithProviders());
    const searchSection = screen.getByTestId('comment-search-section');
    expect(searchSection).toBeInTheDocument();
  });

  it('should set document title to "Comments Search - Silva" on mount', () => {
    render(<CommentSearch />, renderWithProviders());
    expect(document.title).toBe('Comments Search - Silva');
  });

  it('should reset document title to "Silva" on unmount', () => {
    const { unmount } = render(<CommentSearch />, renderWithProviders());
    expect(document.title).toBe('Comments Search - Silva');
    unmount();
    expect(document.title).toBe('Silva');
  });

  it('should render with proper grid layout structure', () => {
    const { container } = render(<CommentSearch />, renderWithProviders());
    const grid = container.querySelector('.default-grid');
    expect(grid).toBeInTheDocument();
    expect(grid).toHaveClass('default-search-grid');
  });

  it('should render PageTitle in first column', () => {
    const { container } = render(<CommentSearch />, renderWithProviders());
    const columns = container.querySelectorAll('.default-grid > div');
    expect(columns.length).toBeGreaterThan(0);
  });

  it('should render CommentSearchSection in full-width column', () => {
    const { container } = render(<CommentSearch />, renderWithProviders());
    const fullWidthCol = container.querySelector('.full-width-col');
    expect(fullWidthCol).toBeInTheDocument();
    expect(screen.getByTestId('comment-search-section')).toBeInTheDocument();
  });
});
