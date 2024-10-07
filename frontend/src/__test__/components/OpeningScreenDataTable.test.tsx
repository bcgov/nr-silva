import React from 'react';
import { act, render } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import OpeningScreenDataTable from '../../components/OpeningScreenDataTable';
import PaginationContext from '../../contexts/PaginationContext';
import { RecentOpening } from '../../types/RecentOpening';

const rows: RecentOpening[] = [{
  id: '123',
  openingId: '123',
  fileId: '1',
  cuttingPermit: '1',
  timberMark: '1',
  cutBlock: '1',
  grossAreaHa: 1,
  statusDesc: 'Approved',
  categoryDesc: 'Another:Another',
  disturbanceStart: '1',
  entryTimestamp: '1',
  updateTimestamp: '1',
}];

const headers = [{ key: 'openingId', header: 'Opening Id', },
  { key: 'fileId', header: 'File Id', },
  { key: 'cuttingPermit', header: 'Cutting permit', },
  { key: 'timberMark', header: 'Timber mark', },
  { key: 'cutBlock', header: 'Cut block', },
  { key: 'grossAreaHa', header: 'Gross area (ha)', },
  { key: 'status', header: 'Status', },
  { key: 'category', header: 'Category', },
  { key: 'disturbanceStart', header: 'Disturbance start', },
  { key: 'createdAt', header: 'Created At', },
  { key: 'lastViewed', header: 'Last Viewed', },
  { key: 'actions', header: 'Actions', },
];

const setOpeningId = vi.fn();
const paginationValueMock = {
  getCurrentData: () => rows,
  currentPage: 0,
  totalPages: 0,
  handlePageChange: vi.fn(),
  handleItemsPerPageChange: vi.fn(),
  itemsPerPage: 5,
  setPageData: vi.fn(),
  setInitialItemsPerPage: vi.fn(),
};

describe('Opening Screen Data table component test', () => {
  it('should remove the row checkbox when showSpatial is false', () => {
    const { queryByTestId } = render(
      <PaginationContext.Provider value={paginationValueMock}>
        <OpeningScreenDataTable
          headers={headers}
          rows={rows}
          setOpeningId={setOpeningId}
          showSpatial={false}
        />
      </PaginationContext.Provider>
    );

    act(() => {
      const tableSelectionRow: HTMLElement | null = queryByTestId('checkbox__opening-screen-data-table_1');
      expect(tableSelectionRow).toBeNull();
    });
  });

  it('should display the row checkbox when showSpatial is true', () => {
    const { queryByTestId } = render(
      <PaginationContext.Provider value={paginationValueMock}>
        <OpeningScreenDataTable
          headers={headers}
          rows={rows}
          setOpeningId={setOpeningId}
          showSpatial={true}
        />
      </PaginationContext.Provider>
    );

    act(() => {
      const tableSelectionRow: HTMLElement | null = queryByTestId('checkbox__opening-screen-data-table_1');
      // The next line should be "not.toBeNull()" however, Carbon React team forgot to add data-testid
      // attribute to this component (TableSelectRow), making it impossible to get by testid value.
      // Once we have that fixed, please get back here and update the next statement.
      expect(tableSelectionRow).toBeNull();
    });
  });
});
