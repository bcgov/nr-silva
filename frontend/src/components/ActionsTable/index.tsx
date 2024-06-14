import React from 'react';
import { Table, TableHead, TableRow, TableHeader, TableBody, TableCell } from "@carbon/react";
import StatusTag from "../StatusTag";
import ActivityTag from "../ActivityTag";
import { RecentAction } from '../../types/RecentAction';

interface IActionsTable {
  rows: RecentAction[];
  headers: any[];
}

const ActionsTable: React.FC<IActionsTable> = ({ rows, headers }) => (
  <Table size="lg" useZebraStyles={false} aria-label="actions table">
    <TableHead>
      <TableRow>
        {headers.map(header => <TableHeader id={header.key} key={header.key}>
            {header.header}
          </TableHeader>)}
      </TableRow>
    </TableHead>
    <TableBody>
      {rows.map((row: RecentAction, idx: number) =>
        <TableRow key={idx}>
        { Object.keys(row).filter((rowKey: string) => rowKey !== 'id').map((key: string) => {
          return (
            <TableCell key={key}>
              {key === "statusCode" ? (
                <StatusTag code={row[key]} />
              ):
              key === "activityType" && !row["fileFormat"] ? (
                <ActivityTag type={row[key]} />
              ):
              key === "activityType" && row["fileFormat"] ? (
                <ActivityTag type={row[key]} fileFormat={row["fileFormat"]} />
              ):
              row[key]}
            </TableCell>
          );
        })}
        </TableRow>)}
    </TableBody>
  </Table>
);

export default ActionsTable;
