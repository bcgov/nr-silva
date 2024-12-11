// OpeningsSearchProvider.test.tsx
import React from 'react';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { OpeningsSearchProvider, useOpeningsSearch } from '../../contexts/search/OpeningsSearch';

const TestComponent: React.FC = () => {
  const { filters, setFilters, searchTerm, setSearchTerm, clearFilters, clearIndividualField } = useOpeningsSearch();
  
  return (
    <div>
      <p data-testid="searchTerm">{searchTerm}</p>
      <p data-testid="startDate">{String(filters.startDate)}</p>
      <button onClick={() => setSearchTerm('test search')} data-testid="setSearchTerm">Set Search Term</button>
      <button onClick={() => setFilters({ ...filters, startDate: new Date() })} data-testid="setFilters">Set Start Date</button>
      <button onClick={() => clearFilters()} data-testid="clearFilters">Clear Filters</button>
      <button onClick={() => clearIndividualField('startDate')} data-testid="clearStartDate">Clear Start Date</button>
    </div>
  );
};

describe('OpeningsSearchProvider', () => {
  beforeEach(() => {
    render(
      <OpeningsSearchProvider>
        <TestComponent />
      </OpeningsSearchProvider>
    );
  });

  it('should initialize with default values', () => {
    expect(screen.getByTestId('searchTerm').textContent).toBe('');
    expect(screen.getByTestId('startDate').textContent).toBe('undefined');
  });

  it('should update searchTerm', () => {
    fireEvent.click(screen.getByTestId('setSearchTerm'));
    expect(screen.getByTestId('searchTerm').textContent).toBe('test search');
  });

  it('should set and then clear filters', () => {
    fireEvent.click(screen.getByTestId('setFilters'));
    expect(screen.getByTestId('startDate').textContent).not.toBe('null');

    fireEvent.click(screen.getByTestId('clearFilters'));
    expect(screen.getByTestId('startDate').textContent).toBe('undefined');
  });

  it('should clear individual field', () => {
    fireEvent.click(screen.getByTestId('setFilters'));
    expect(screen.getByTestId('startDate').textContent).not.toBe('null');

    fireEvent.click(screen.getByTestId('clearStartDate'));
    expect(screen.getByTestId('startDate').textContent).toBe('undefined');
  });
});
