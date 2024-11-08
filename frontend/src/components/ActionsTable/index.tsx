import {
  Table,
  TableHead,
  TableRow,
  TableHeader,
  TableBody,
  TableCell
} from '@carbon/react';
import StatusTag from '../StatusTag';
import ActivityTag from '../ActivityTag';
import { RecentAction } from '../../types/RecentAction';
import { ActivityTagFileFormatEnum, ActivityTagTypeEnum } from '../../types/ActivityTagType';
import { ITableHeader } from '../../types/TableHeader';

interface IActionsTable {
  readonly rows: RecentAction[];
  readonly headers: ITableHeader[];
}

/**
 * Renders an Actions Table component.
 *
 * @component
 * @param {IActionsTable} props - The component props.
 * @returns {JSX.Element} The rendered ActionsTable component.
 */
function ActionsTable(props: IActionsTable): JSX.Element {
  const getTypeEnum = (value: string): ActivityTagTypeEnum => {
    // Find the enum entry by value, or fall back to UNKNOWN if not found
    const match = (Object.values(ActivityTagTypeEnum) as string[]).find(enumValue => enumValue === value);
    return (match as ActivityTagTypeEnum) || ActivityTagTypeEnum.UNKNOWN;
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

  const headerKeys = props.headers.map(header => header.key);

  return (
    <Table size="lg" useZebraStyles={false} aria-label="actions table">
      <TableHead>
        <TableRow>
          {props.headers.map(header => (
            <TableHeader id={header.key} key={header.key}>
              {header.header}
            </TableHeader>
          ))}
        </TableRow>
      </TableHead>
      <TableBody>
        {props.rows.map((row: RecentAction) => (
          <TableRow key={row.openingId}>
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
