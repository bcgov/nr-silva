import React from 'react';
import { render } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import SearchScreenDataTable from '../../../../components/SilvicultureSearch/Openings/SearchScreenDataTable/index';
import { columns, rows } from '../../../../components/SilvicultureSearch/Openings/SearchScreenDataTable/testData';
import PaginationProvider from '../../../../contexts/PaginationProvider';
import { NotificationProvider } from '../../../../contexts/NotificationProvider';
import { BrowserRouter } from 'react-router-dom';
import { OpeningsSearchProvider } from '../../../../contexts/search/OpeningsSearch';

const handleCheckboxChange = vi.fn();
const toggleSpatial = vi.fn();

describe('Search Screen Data table test', () => {

  it('should render the Search Screen Data table', () => {
    const { getByText, container } =
    render(
      <BrowserRouter>
        <PaginationProvider>
          <OpeningsSearchProvider>
            <NotificationProvider>
              <SearchScreenDataTable
                rows={rows}
                headers={columns}
                defaultColumns={columns}
                showSpatial={false}
                handleCheckboxChange={handleCheckboxChange}
                toggleSpatial={toggleSpatial}
                totalItems={rows.length}
              />
            </NotificationProvider>
          </OpeningsSearchProvider>
        </PaginationProvider>
      </BrowserRouter>
    );

    expect(container).toBeInTheDocument();
    expect(container.querySelector('.total-search-results')).toBeInTheDocument();
    expect(container.querySelector('.total-search-results')).toContainHTML('Total Search Results');
    
  });

  it('should render the Search Screen Data table with no data', () => {
    const { getByText, container } =
    render(
      <BrowserRouter>
        <PaginationProvider>
          <OpeningsSearchProvider>
            <NotificationProvider>
              <SearchScreenDataTable
                rows={[]}
                headers={columns}
                defaultColumns={columns}
                showSpatial={false}
                handleCheckboxChange={handleCheckboxChange}
                toggleSpatial={toggleSpatial}
                totalItems={0}
              />
            </NotificationProvider>
          </OpeningsSearchProvider>
        </PaginationProvider>
      </BrowserRouter>
    );

    expect(container).toBeInTheDocument();
    expect(container.querySelector('.total-search-results')).toBeInTheDocument();
    expect(container.querySelector('.total-search-results')).toContainHTML('Total Search Results');
    expect(container.querySelector('.total-search-results')).toContainHTML('0');
  });

});