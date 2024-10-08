import React, { useCallback, useContext, useEffect, useState } from 'react';
import {
  DataTable,
  TableToolbar,
  TableToolbarAction,
  TableToolbarContent,
  TableToolbarSearch,
  TableToolbarMenu,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableHeader,
  TableRow,
  TableSelectRow,
  Button,
  Pagination
} from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import StatusTag from '../StatusTag';
import './styles.scss';
import EmptySection from '../EmptySection';
import PaginationContext from '../../contexts/PaginationContext';
import { RecentOpening } from '../../types/RecentOpening';
import { ITableHeader } from '../../types/TableHeader';

interface IOpeningScreenDataTable {
  rows: RecentOpening[],
  headers: ITableHeader[],
  setOpeningId: Function,
  showSpatial: boolean
}

const OpeningScreenDataTable: React.FC<IOpeningScreenDataTable> = ({
  rows,
  headers,
  setOpeningId,
  showSpatial
}) => {
  const [filteredRows, setFilteredRows] = useState<RecentOpening[]>(rows);
  const {
    getCurrentData,
    currentPage,
    totalPages,
    handlePageChange,
    handleItemsPerPageChange,
    itemsPerPage, // Use itemsPerPage from the hook
    setPageData,
    setInitialItemsPerPage
  } = useContext(PaginationContext);

  const handleSearchChange = (event: any) => {
    const searchTerm: string = event.target.value;

    const filtered = rows.filter((item) =>
      Object.values(item)
        .join(' ')
        .toLowerCase()
        .includes(searchTerm.toLowerCase())
    );
    setFilteredRows(filtered);
    if (event.key === 'Enter' && searchTerm.length > 0) {
      setOpeningId(searchTerm);
    } if (event.key === 'Enter' && !searchTerm.length) {
      setOpeningId(0);
    }
  };

  const clickViewAction = useCallback((id: string) => {
    console.log(`Clicked view on id ${id}`);
  }, []);

  const selectRowEvent = useCallback((openingId: string, selected: boolean) => {
    if (!selected) {
      setOpeningId(openingId);
    }
  }, []);

  const batchActionClick = (selectedRows: any[]) => () => {
    console.log('Batch action clicked with selected rows:', selectedRows);
    // Add your logic to handle batch actions here
  };

  useEffect(() => {
    setPageData(filteredRows);
    setInitialItemsPerPage(5);
  }, [filteredRows]);

  return (
    <>
      <DataTable rows={getCurrentData()} headers={headers}>
      {({
        rows,
        headers,
        getHeaderProps,
        getRowProps,
        getSelectionProps,
        getToolbarProps,
        getBatchActionProps,
        onInputChange,
        selectedRows,
        getTableProps,
        getTableContainerProps,
        selectRow,
      }:{
        rows: any[],
        headers: any[],
        getHeaderProps: Function,
        getRowProps: Function,
        getSelectionProps: Function,
        getToolbarProps: Function,
        getBatchActionProps: Function,
        onInputChange: Function,
        selectedRows: any[],
        getTableProps: Function,
        getTableContainerProps: Function,
        selectRow: any
      }) => {
        const batchActionProps = getBatchActionProps();

        return (
          <TableContainer
            {...getTableContainerProps()}
          >
            <TableToolbar aria-label="data-table-toolbar">
              <TableToolbarContent className="table-toolbar">
                <TableToolbarSearch
                  tabIndex={batchActionProps.shouldShowBatchActions ? -1 : 0}
                  //onChange={(e: React.ChangeEvent<HTMLInputElement>) => handleSearchChange(e)}
                  onKeyDown={(e: any) => handleSearchChange(e)}
                  placeholder="Filter by opening ID, File ID, timber mark, cut block, status..."
                  persistent
                />
                <TableToolbarMenu iconDescription="More" tooltipposition="bottom" renderIcon={Icons.OverflowMenuVertical} tabIndex={batchActionProps.shouldShowBatchActions ? -1 : 0} className="d-block d-sm-none">
                  <TableToolbarAction onClick={() => console.log('Download Click')} disabled={selectedRows.length === 0}>
                    Print
                  </TableToolbarAction>
                  <TableToolbarAction
                    onClick={() => {
                      batchActionClick(selectedRows);
                      batchActionProps.onCancel();
                      console.log('Clicked print')
                    }}
                    disabled={selectedRows.length === 0}
                  >
                    Download
                  </TableToolbarAction>
                </TableToolbarMenu>
                <div className="d-none d-sm-flex my-auto">
                  <Button
                    iconDescription="Download"
                    tooltipposition="bottom"
                    kind="ghost"
                    onClick={() => console.log('Download Click')}
                    disabled={selectedRows.length === 0}
                    renderIcon={Icons.Download}
                    size="md"
                  >
                    Download table
                  </Button>
                  <Button
                    iconDescription="Print"
                    tooltipposition="bottom"
                    kind="ghost"
                    onClick={() => {
                      batchActionClick(selectedRows);
                      batchActionProps.onCancel();
                      console.log('Clicked print')
                    }}
                    disabled={selectedRows.length === 0}
                    renderIcon={Icons.Printer}
                    size="md"
                  >
                    Print table
                  </Button>
                </div>
              </TableToolbarContent>
            </TableToolbar>
            <Table {...getTableProps()} aria-label="sample-table">
              <TableHead>
                <TableRow>
                  {showSpatial && (
                    <th id='blank'></th>
                  )}
                  {headers.map((header, i) => (
                    <TableHeader key={header.key}>
                      { header.header }
                    </TableHeader>
                  ))}
                </TableRow>
              </TableHead>
              <TableBody>
                {rows.map((row, i) => (
                  <TableRow key={row.id}>
                    {showSpatial && (
                      <TableSelectRow data-testid={`checkbox__opening-screen-data-table_${row.id}`} {
                        ...getSelectionProps({
                          row,
                          onClick: (e: Event) => selectRowEvent(row.id, row.isSelected),
                        })
                      } />
                    )}
                    {row.cells.map((cell: any, j: number) => (
                      <TableCell key={j}>
                        {cell.info.header === "status" ? (
                          <StatusTag code={cell.value} />
                        ) : cell.info.header === "actions" ? (
                          <>
                            <Button
                              hasIconOnly
                              iconDescription="View"
                              tooltipPosition="bottom"
                              kind="ghost"
                              onClick={() => clickViewAction(row.id)}
                              renderIcon={Icons.View}
                              size="md"
                            />
                            <Button
                              hasIconOnly
                              iconDescription="Document Download"
                              tooltipPosition="bottom"
                              kind="ghost"
                              onClick={() => null}
                              renderIcon={Icons.DocumentDownload}
                              size="md"
                            />
                          </>
                        ) : (
                          cell.value
                        )}
                      </TableCell>
                    ))}
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        );
      }}
    </DataTable>

    {/* Check if there are no elements in the table, if not then print the Empty */}
    {filteredRows.length <= 0 ? (
      <EmptySection
        pictogram="Magnify"
        title={'There are no openings to show yet'}
        description={
          'Your recent openings will appear here once you generate one'
        }
        fill="#0073E6"
      />
    ) : null}

    {/* Check if there are no elements in the table, if not then print the Empty */}
    {filteredRows.length > 0 ? (
      <Pagination
        totalItems={filteredRows.length}
        backwardText="Previous page"
        forwardText="Next page"
        pageSize={itemsPerPage}
        pageSizes={[5, 20, 50]}
        itemsPerPageText="Items per page"
        onChange={({ page, pageSize }:{ page: number, pageSize: number}) => {
          handlePageChange( page );
          handleItemsPerPageChange(page, pageSize);
        }}
      />
      ) : null}
    </>
  );
}

export default OpeningScreenDataTable;
