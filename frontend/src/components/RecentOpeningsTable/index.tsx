import React from 'react';
import { hashObject } from 'react-hash-string';
import { Table, TableHead, TableHeader, TableRow, TableBody, TableCell, Button, TableToolbar, TableToolbarSearch, TableToolbarContent, TableToolbarMenu, TableToolbarAction } from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import StatusTag from '../StatusTag';
import EmptySection from '../EmptySection';
import './styles.scss';
import { TableContainer } from '@carbon/react';

interface TableProps {
  elements: any[];
  headers: string[];
  clickFn: Function;
}

const RecentOpeningsTable = ({ elements, headers, clickFn }: TableProps) => {
  const iconSize = '18';

  const createTableCell = (obj: any, key: string, index: number) => {
    const mapKey = `${key}-${index}`;
    let cellContent;
  
    switch (key) {
      case 'status':
        cellContent = <StatusTag type={obj[key]} />;
        break;
      // Add more cases for other keys if needed
      default:
        cellContent = obj[key];
        break;
    }
  
    return (
      <TableCell key={mapKey} className="activities-table-cell">
        {cellContent}
      </TableCell>
    );
  };

  return (
    <>
      <TableContainer>
      <TableToolbar aria-label="data table toolbar">
          <TableToolbarContent>
            <TableToolbarSearch onChange={()=>{console.log("search term changed")}} placeholder={"Filter by opening ID, File ID, timber mark, cut block, status..."} persistent />
            <Button
              hasIconOnly
              iconDescription="Download"
              tooltipPosition="bottom"
              kind="ghost"
              onClick={() => console.log("Download Click")}
              renderIcon={Icons.Download}
              size="md"
            />
            <Button
              hasIconOnly
              iconDescription="Print"
              tooltipPosition="bottom"
              kind="ghost"
              onClick={() => console.log("Print Click")}
              renderIcon={Icons.Printer}
              size="md"
            />
          </TableToolbarContent>
        </TableToolbar>
        <Table size="lg" className="activity-table">
          <TableHead>
            <TableRow>
              {headers.map((header) => (
                <TableHeader key={header} className="activities-table-header">
                  {header}
                </TableHeader>
              ))}
            </TableRow>
          </TableHead>
          {/* Check if the table has some elements */}
          {elements.length>0?(
            <TableBody aria-live="off">
            {elements.map((item, idx) => (
              <TableRow key={hashObject(item)} id={`row${idx}`}>
                {Object.keys(item).map((key) => createTableCell(item, key, idx))}
                <TableCell className="activities-table-action" tabIndex={0} aria-label="View more">
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
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
          ):null}
        </Table>
      </TableContainer>
      
      {/* Check if therer are no elements in the table, if not then print the Empty */}
      {elements.length<=0?(
        <EmptySection
        pictogram="Magnify"
        title={'There are no openings to show yet'}
        description={'Your recent openings will appear here once you generate one'}
        fill = {'#0073E6'}
      />
      ):null}
    </>
  );
};

export default RecentOpeningsTable;
