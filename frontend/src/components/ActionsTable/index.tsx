import React from 'react';
import { Table, TableHead, TableRow, TableHeader, TableBody, TableCell } from "@carbon/react";
import StatusTag from "../StatusTag";
import ActivityTag from "../ActivityTag";
import { RecentAction } from '../../types/RecentAction';
import { ActivityTagFileFormatEnum, ActivityTagTypeEnum } from '../../types/ActivityTagType';
import { ITableHeader } from '../../types/TableHeader';

interface IActionsTable {
  rows: RecentAction[];
  headers: ITableHeader[];
}

const ActionsTable: React.FC<IActionsTable> = ({ rows, headers }) => {
  const getTypeEnum = (typeStr: string): ActivityTagTypeEnum => {
    switch (typeStr) {
      case ActivityTagTypeEnum.OPENING_DETAILS:
        return ActivityTagTypeEnum.OPENING_DETAILS;
      case ActivityTagTypeEnum.OPENING_REPORT:
        return ActivityTagTypeEnum.OPENING_REPORT;
      case ActivityTagTypeEnum.OPENING_SPATIAL:
        return ActivityTagTypeEnum.OPENING_SPATIAL;
      case ActivityTagTypeEnum.UPDATE:
        return ActivityTagTypeEnum.UPDATE;
      default:
        return ActivityTagTypeEnum.UNKNOWN;
    }
  };

  const getFileFormatEnum = (formatStr: string): ActivityTagFileFormatEnum => {
    switch (formatStr) {
      case ActivityTagFileFormatEnum.PDF_FILE:
        return ActivityTagFileFormatEnum.PDF_FILE;
      case ActivityTagFileFormatEnum.CSV_FILE:
        return ActivityTagFileFormatEnum.CSV_FILE;
      case ActivityTagFileFormatEnum.EXCEL_FILE:
        return ActivityTagFileFormatEnum.EXCEL_FILE;
      default:
        return ActivityTagFileFormatEnum.UNKNOWN;
    }
  };

  const headerKeys = headers.map(header => header.key);

  return (
    <Table size="lg" useZebraStyles={false} aria-label="actions table">
      <TableHead>
        <TableRow>
          {headers.map(header => (
            <TableHeader id={header.key} key={header.key}>
              {header.header}
            </TableHeader>
          ))}
        </TableRow>
      </TableHead>
      <TableBody>
        {rows.map((row: RecentAction, idx: number) => (
          <TableRow key={idx}>
            {headerKeys.map((key: string) => (
              <TableCell key={key}>
                {key === "statusCode" ? (
                  <StatusTag code={row[key]} />
                ) : key === "activityType" && !row["fileFormat"] ? (
                  <ActivityTag type={getTypeEnum(row[key])} />
                ) : key === "activityType" && row["fileFormat"] ? (
                  <ActivityTag
                    type={getTypeEnum(row[key])}
                    fileFormat={getFileFormatEnum(row["fileFormat"])}
                  />
                ) : (
                  row[key]
                )}
              </TableCell>
            ))}
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
};

export default ActionsTable;
