import React from 'react';
import { render, screen, fireEvent, act, waitFor } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import SearchScreenDataTable from '../../../../components/SilvicultureSearch/Openings/SearchScreenDataTable';
import { searchScreenColumns as columns } from '../../../../constants/tableConstants';
import PaginationProvider from '../../../../contexts/PaginationProvider';
import { NotificationProvider } from '../../../../contexts/NotificationProvider';
import { BrowserRouter } from 'react-router-dom';
import { OpeningsSearchProvider } from '../../../../contexts/search/OpeningsSearch';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { setOpeningFavorite, deleteOpeningFavorite } from '../../../../services/OpeningFavouriteService';

vi.mock('../../../../services/OpeningFavouriteService', async () => {
  const actual = await vi.importActual<typeof import('../../../../services/OpeningFavouriteService')>('../../../../services/OpeningFavouriteService');
  return {
    ...actual,
    setOpeningFavorite: vi.fn((openingIds: number[]) => {}),
    deleteOpeningFavorite: vi.fn((openingIds: number[]) => {})
  };
});

const handleCheckboxChange = vi.fn();
const toggleSpatial = vi.fn();
const queryClient = new QueryClient();
const setOpeningIds = vi.fn((openingIds: number[]) => {});

const rows:any = [
  {
    id: '114207',
    openingId: '114207',
    fileId: 'TFL99',
    cuttingPermit: '12S',
    timberMark: '47/12S',
    cutBlock: '12-69',
    grossAreaHa: '12.9',
    status: 'Free growing',
    category: 'FTML',
    disturbanceStart: '-',
    createdAt: '2022-10-27',
    orgUnit: 'DCC - Cariboo chilcotin natural resources',
    lastViewed: '2022-10-27',
    favourite: false
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
    lastViewed: '2022-10-27',
    favourite: true
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
    lastViewed: '2022-10-27',
    favourite: false
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
    lastViewed: '2022-10-26',
    favourite: false
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
    lastViewed: '2022-10-26',
    favourite: false
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
    lastViewed: '2022-10-25',
    favourite: false
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
    lastViewed: '2022-10-25',
    favourite: false
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
    lastViewed: '2022-10-24',
    favourite: false
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
    lastViewed: '2022-10-24',
    favourite: false
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
    lastViewed: '2022-10-23',
    favourite: false
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
    lastViewed: '2022-10-23',
    favourite: false
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
    lastViewed: '2022-10-22',
    favourite: false
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
    lastViewed: '2022-10-22',
    favourite: false
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
    lastViewed: '2022-10-21',
    favourite: false
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
    lastViewed: '2022-10-21',
    favourite: false
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
                  setOpeningIds={setOpeningIds}
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
                  setOpeningIds={setOpeningIds}
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

  it('should render the checkbox for showSpatial being true', async () => {    
    await act(async () => render(
      <BrowserRouter>
      <QueryClientProvider client={queryClient}>
        <PaginationProvider>
          <OpeningsSearchProvider>
            <NotificationProvider>
              <SearchScreenDataTable
                rows={rows}
                headers={columns}
                defaultColumns={columns}
                showSpatial={true}
                handleCheckboxChange={handleCheckboxChange}
                toggleSpatial={toggleSpatial}
                totalItems={0}
                setOpeningIds={setOpeningIds}
              />
            </NotificationProvider>
          </OpeningsSearchProvider>
        </PaginationProvider>
        </QueryClientProvider>
      </BrowserRouter>
    ));
    
    expect(screen.getByTestId('toggle-spatial')).toContainHTML('Hide map');

    const checkbox = await screen.findByTestId('checkbox-114207');
    expect(checkbox).toBeInTheDocument();

  });

  it('should check the checkbox for showSpatial', () => {
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
                showSpatial={true}
                handleCheckboxChange={handleCheckboxChange}
                toggleSpatial={toggleSpatial}
                totalItems={rows.length}
                setOpeningIds={setOpeningIds}
              />
            </NotificationProvider>
          </OpeningsSearchProvider>
        </PaginationProvider>
        </QueryClientProvider>
      </BrowserRouter>
    );
    const checkbox = screen.getByTestId(`checkbox-${rows[0].openingId}`);
    expect(checkbox).toBeInTheDocument();

    expect(screen.getByTestId(`checkbox-${rows[0].openingId}`)).toBeInTheDocument();
    const checkbox2 = screen.getByTestId(`checkbox-${rows[0].openingId}`);
    fireEvent.click(checkbox2);
    expect(checkbox2).toBeChecked();
    expect(setOpeningIds).toHaveBeenCalledWith([parseFloat(rows[0].openingId)]);

  });

  it('should favorite and unfavorite an opening', async () => {

    (setOpeningFavorite as vi.Mock).mockResolvedValue({});
    (deleteOpeningFavorite as vi.Mock).mockResolvedValue({});

    let container;

    await act(async () => 
    ({ container } =
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
                  setOpeningIds={setOpeningIds}
                />
              </NotificationProvider>
            </OpeningsSearchProvider>
          </PaginationProvider>
        </QueryClientProvider>
      </BrowserRouter>
    )));

    expect(container).toBeInTheDocument();
    expect(container.querySelector('.total-search-results')).toBeInTheDocument();
    expect(container.querySelector('.total-search-results')).toContainHTML('Total Search Results');

    await act(async () => expect(screen.getByTestId('row-114207')).toBeInTheDocument() );
    await act(async () => expect(screen.getByTestId('cell-actions-114206')).toBeInTheDocument() );
    
    const actionOverflow = screen.getByTestId(`action-fav-114207`);
    await act(async () => expect(actionOverflow).toBeInTheDocument() );
    expect(actionOverflow).toHaveAttribute('aria-pressed', 'false');
    await act(async () => fireEvent.click(actionOverflow));

    expect(screen.getByTestId(`action-fav-114207`)).toHaveAttribute('aria-pressed', 'true');
    
  });

  it('should call handleCheckboxChange appropriately when the select-all/default-column div is clicked', async () => {
    const handleCheckboxChange = vi.fn();
    let container;

    await act(async () => 
    ({ container } =
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
                  setOpeningIds={setOpeningIds}
                />
              </NotificationProvider>
            </OpeningsSearchProvider>
          </PaginationProvider>
        </QueryClientProvider>
      </BrowserRouter>
    )));
    expect(container).toBeInTheDocument();
    const selectAllColumn = screen.getByTestId('select-all-column');
    fireEvent.click(selectAllColumn);
    expect(handleCheckboxChange).toHaveBeenCalledWith("select-all");
    const selectDefaultColumn = screen.getByTestId('select-default-column');
    fireEvent.click(selectDefaultColumn);
    expect(handleCheckboxChange).toHaveBeenCalledWith("select-default");
  });

});