import React from 'react';
import { render } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import SearchScreenDataTable from '../../../../components/SilvicultureSearch/Openings/SearchScreenDataTable';
import { columns } from '../../../../components/SilvicultureSearch/Openings/SearchScreenDataTable/headerData';
import PaginationProvider from '../../../../contexts/PaginationProvider';
import { NotificationProvider } from '../../../../contexts/NotificationProvider';
import { BrowserRouter } from 'react-router-dom';
import { OpeningsSearchProvider } from '../../../../contexts/search/OpeningsSearch';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const handleCheckboxChange = vi.fn();
const toggleSpatial = vi.fn();
const queryClient = new QueryClient();

const rows:any = [
  {
    id: '114207',
    openingId: '114207',
    fileId: 'TFL47',
    cuttingPermit: '12S',
    timberMark: '47/12S',
    cutBlock: '12-69',
    grossAreaHa: '12.9',
    status: 'Free growing',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2022-10-27',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-27'
  },
  {
    id: '114206',
    openingId: '114206',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-69',
    grossAreaHa: '12.9',
    status: 'Free growing',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2022-09-04',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-27'
  },
  {
    id: '114205',
    openingId: '114205',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Free growing',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2022-09-04',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-27'
  },
  {
    id: '114204',
    openingId: '114204',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Active',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2022-01-16',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-26'
  },
  {
    id: '114203',
    openingId: '114203',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Active',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2021-12-08',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-26'
  },
  {
    id: '114202',
    openingId: '114202',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Free growing',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2021-11-15',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-25'
  },
  {
    id: '114201',
    openingId: '114201',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Free growing',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2021-11-15',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-25'
  },
  {
    id: '114200',
    openingId: '114200',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Active',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2021-10-20',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-24'
  },
  {
    id: '114199',
    openingId: '114199',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Active',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2021-10-20',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-24'
  },
  {
    id: '114198',
    openingId: '114198',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Free growing',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2021-09-12',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-23'
  },
  {
    id: '114197',
    openingId: '114197',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Free growing',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2021-09-12',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-23'
  },
  {
    id: '114196',
    openingId: '114196',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Free growing',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2021-08-05',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-22'
  },
  {
    id: '114195',
    openingId: '114195',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Free growing',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2021-08-05',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-22'
  },
  {
    id: '114194',
    openingId: '114194',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Active',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2021-07-10',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-21'
  },
  {
    id: '114193',
    openingId: '114193',
    fileId: 'TFL47',
    cuttingPermit: '12T',
    timberMark: '47/12S',
    cutBlock: '12-44A',
    grossAreaHa: '12.9',
    status: 'Active',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2021-07-10',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-21'
  }
];

describe('Search Screen Data table test', () => {

  it('should render the Search Screen Data table', () => {
    const { getByText, container } =
    render(
      <BrowserRouter>
        <QueryClientProvider client={queryClient}>
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
        </QueryClientProvider>
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
        <QueryClientProvider client={queryClient}>
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
        </QueryClientProvider>
      </BrowserRouter>
    );

    expect(container).toBeInTheDocument();
    expect(container.querySelector('.total-search-results')).toBeInTheDocument();
    expect(container.querySelector('.total-search-results')).toContainHTML('Total Search Results');
    expect(container.querySelector('.total-search-results')).toContainHTML('0');
  });


  it('should render the checkbox for showSPatial being true', () => {
    render(
      <BrowserRouter>
      <QueryClientProvider client={queryClient}>
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
                totalItems={0}
              />
            </NotificationProvider>
          </OpeningsSearchProvider>
        </PaginationProvider>
        </QueryClientProvider>
      </BrowserRouter>
    );
    const checkbox = document.querySelector('.cds--checkbox-group');
    expect(checkbox).toBeInTheDocument();

  });

});