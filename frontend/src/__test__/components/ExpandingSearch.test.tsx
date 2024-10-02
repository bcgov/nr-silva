import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import ExpandingSearch from '../../components/ExpandingSearch';

describe('ExpandingSearch Component', () => {
  it('should render the component', () => {
    render(<ExpandingSearch />);
    const searchInput = screen.getByRole('searchbox');
    expect(searchInput).toBeDefined();
  });

  it('should update the search input value', () => {
    render(<ExpandingSearch />);
    const searchInput: HTMLInputElement = screen.getByRole('searchbox');
    fireEvent.change(searchInput, { target: { value: 'test' } });
    expect(searchInput.value).toBe('test');
  });

  it('should clear the search input value when close button is clicked', () => {
    render(<ExpandingSearch />);
    const searchInput: HTMLInputElement = screen.getByRole('searchbox');
    fireEvent.change(searchInput, { target: { value: 'test' } });
    const closeButton = screen.getByLabelText('Clear search input');
    fireEvent.click(closeButton);
    expect(searchInput.value).toBe('');
  });
});
