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
} from '@carbon/react';
import { TrashCan, Save, Download, Add } from '@carbon/icons-react';
import * as Icons from '@carbon/icons-react'
import StatusTag from '../StatusTag'; // Import the StatusTag component

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

export default function OpeningScreenDataTable({ rows, headers }) {
  const [filteredRows, setFilteredRows] = useState(rows);

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
    <DataTable rows={filteredRows} headers={headers}>
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
            title="DataTable 1"
            description="This is just a sample test to show the usage of the Data Table with multiple row selection in the carbon design system using React."
            {...getTableContainerProps()}
          >
            <TableToolbar {...getToolbarProps()}>
              {/* ... (existing code) */}
              <TableToolbarContent
                aria-hidden={batchActionProps.shouldShowBatchActions}
              >
                <TableToolbarSearch
                  tabIndex={batchActionProps.shouldShowBatchActions ? -1 : 0}
                  onChange={(e) => handleSearchChange(e.target.value)}
                />
                {/* ... (existing code) */}
              </TableToolbarContent>
            </TableToolbar>
            <Table {...getTableProps()} aria-label="sample table">
              <TableHead>
                <TableRow>
                  <TableSelectAll {...getSelectionProps()} />
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
  );
}
