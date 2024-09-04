import React from 'react';
import {
  DataTable,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableHeader,
  TableRow,
  TableToolbarMenu,
  TableToolbarAction,

} from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import StatusTag from '../StatusTag';

const SearchScreenDataTable = ({
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
}:
{
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
  return (
    <TableContainer
              className="search-data-table"
            >
      <Table>
        <TableHead>
          <TableRow>
            {headers.map((header) => (
              <TableHeader key={header.key}>{header.header}</TableHeader>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow key={row.openingId}>
              {headers.map((header) => (
                <TableCell key={header.key}>
                {header.key === "statusCode" ? (
                  <StatusTag code={row[header.key]} />
                ) : header.key === "actions" ? (
                  <>
                    <TableToolbarMenu
                      iconDescription="More"
                      autoAlign
                      renderIcon={Icons.OverflowMenuVertical}
                      
                      className="float-start"
                    >
                      <TableToolbarAction
                        onClick={() => console.log("Download Click")}
                      >
                        Print
                      </TableToolbarAction>
                      <TableToolbarAction
                        onClick={() => {
                          console.log("Clicked print");
                        }}
                      >
                        Download
                      </TableToolbarAction>
                    </TableToolbarMenu>
                  </>
                ) : (
                  row[header.key]
                )}
              </TableCell>
              ))}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default SearchScreenDataTable;
