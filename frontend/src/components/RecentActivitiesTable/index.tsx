import React from 'react';
import { hashObject } from 'react-hash-string';
import { Table, TableHead, TableHeader, TableRow, TableBody, TableCell, Button } from '@carbon/react';
import * as Icons from '@carbon/icons-react';
import StatusTag from '../StatusTag';
import './styles.scss';

interface TableProps {
  elements: any[];
  headers: string[];
  clickFn: Function;
}

const RecentActivitiesTable = ({ elements, headers, clickFn }: TableProps) => {
  const iconSize = '18';

  const createTableCell = (obj: any, key: string, index: number) => {
    const mapKey = `${key}-${index}`;
    return (
      <TableCell key={mapKey} className="activities-table-cell">
        {obj[key]}
      </TableCell>
    );
  };

  return (
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
    </Table>
  );
};

export default RecentActivitiesTable;
