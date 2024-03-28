import React, { useState } from 'react';
import {
  DataTable,
  TableBatchAction,
  TableBatchActions,
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
  TableSelectAll,
  TableSelectRow,
  Button,
  Pagination
} from '@carbon/react';
import { TrashCan, Save, Download, Add } from '@carbon/icons-react';
import * as Icons from '@carbon/icons-react'
import StatusTag from '../StatusTag'; // Import the StatusTag component
import './styles.scss'
import EmptySection from '../EmptySection';

export const batchActionClick = (selectedRows) => () => {
  console.log('Batch action clicked with selected rows:', selectedRows);
  // Add your logic to handle batch actions here
};

export const buttonsCol = (
  <>
  <Button
    hasIconOnly
    iconDescription="View"
    tooltipPosition="bottom"
    kind="ghost"
    onClick={() => clickFn(item.id)}
    renderIcon={Icons.DataViewAlt}
    size="md"
  />
  <Button
    hasIconOnly
    iconDescription="Download"
    tooltipPosition="bottom"
    kind="ghost"
    onClick={() => null}
    renderIcon={Icons.Download}
    size="md"
  />
  </>
)

// A custom hook to handle pagination logic
const usePagination = (data, initialItemsPerPage) => {
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(initialItemsPerPage);

  // Update the total number of pages when itemsPerPage changes
  const totalPages = Math.ceil(data.length / itemsPerPage);

  // Get the current page data
  const currentData = () => {
    const start = (currentPage - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    return data.slice(start, end);
  };

  // Update the current page when the user changes the page
  const handlePageChange = ({ page }) => {
    setCurrentPage(page);
  };
  

  // Update the items per page when the user changes the value
  const handleItemsPerPageChange = (event) => {
    setCurrentPage(event.page);
    setItemsPerPage(event.pageSize);
  };

  

  return {
    currentData,
    currentPage,
    totalPages,
    handlePageChange,
    handleItemsPerPageChange,
    itemsPerPage, // Expose the current itemsPerPage value
  };
};



export default function OpeningScreenDataTable({ rows, headers }) {
  const [filteredRows, setFilteredRows] = useState(rows);
  const {
    currentData,
    currentPage,
    totalPages,
    handlePageChange,
    handleItemsPerPageChange,
    itemsPerPage, // Use itemsPerPage from the hook
  } = usePagination(filteredRows, 5);

  

  const handleSearchChange = (searchTerm) => {
    const filtered = rows.filter((item) =>
      Object.values(item)
        .join(' ')
        .toLowerCase()
        .includes(searchTerm.toLowerCase())
    );
    setFilteredRows(filtered);
  };

  return (
    <div>
      <DataTable rows={currentData()} headers={headers}>
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
      }) => {
        const batchActionProps = getBatchActionProps();

        return (
          <TableContainer
            {...getTableContainerProps()}
          >
            <TableToolbar aria-label="data table toolbar">
              <TableToolbarContent className="table-toolbar">
                <TableToolbarSearch
                  tabIndex={batchActionProps.shouldShowBatchActions ? -1 : 0}
                  onChange={(e) => handleSearchChange(e.target.value)}
                  placeholder="Filter by opening ID, File ID, timber mark, cut block, status..."
                  persistent
                />
                <TableToolbarMenu iconDescription="More" tooltipPosition="bottom" renderIcon={Icons.OverflowMenuVertical} tabIndex={batchActionProps.shouldShowBatchActions ? -1 : 0} className="d-block d-sm-none">
                  <TableToolbarAction onClick={() => console.log('Download Click')} disabled={selectedRows.length === 0}>
                    Print
                  </TableToolbarAction>
                  <TableToolbarAction onClick={() => {
                    batchActionClick(selectedRows);
                    batchActionProps.onCancel();
                    console.log('Clicked print')
                  }}
                  disabled={selectedRows.length === 0}
                  >
                    Download
                  </TableToolbarAction>
                </TableToolbarMenu>
                <div className="d-none d-sm-flex">
                  <Button
                    hasIconOnly
                    iconDescription="Download"
                    tooltipPosition="bottom"
                    kind="ghost"
                    onClick={() => console.log('Download Click')}
                    disabled={selectedRows.length === 0}
                    renderIcon={Icons.Download}
                    size="md"
                  />
                  <Button
                    hasIconOnly
                    iconDescription="Print"
                    tooltipPosition="bottom"
                    kind="ghost"
                    onClick={() => {
                      batchActionClick(selectedRows);
                      batchActionProps.onCancel();
                      console.log('Clicked print')
                    }}
                    disabled={selectedRows.length === 0}
                    renderIcon={Icons.Printer}
                    size="md"
                  />
                </div>
              </TableToolbarContent>
            </TableToolbar>
            <Table {...getTableProps()} aria-label="sample table">
              <TableHead>
                <TableRow>
                  <th id='blank'></th>
                  {headers.map((header, i) => (
                    <TableHeader key={i} {...getHeaderProps({ header })}>
                      {header.header}
                    </TableHeader>
                  ))}
                </TableRow>
              </TableHead>
              <TableBody>
                {rows.map((row, i) => (
                  <TableRow key={i} {...getRowProps({ row })}>
                    <TableSelectRow {...getSelectionProps({ row })} />
                    {row.cells.map((cell, j) => (
                      <TableCell key={j}>
                        {cell.info.header === "status" ? (
                          <StatusTag type={cell.value} />
                        ) : cell.info.header === "actions" ? (
                          buttonsCol
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
          onChange={({ page, pageSize }) => {
            handlePageChange({ page, pageSize });
            handleItemsPerPageChange({ page,pageSize });
          }}
        />
        ) : null}


    

    </div>
  );
}
