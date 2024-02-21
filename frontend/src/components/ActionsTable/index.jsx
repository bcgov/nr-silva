import { Table, TableHead, TableRow, TableHeader, TableBody, TableCell } from "@carbon/react";
import StatusTag from "../StatusTag";
import { rows, headers } from "./testData";
import ActivityTag from "../ActivityTag";
const ActionsTable = () => {
  return (
    <Table size="lg" useZebraStyles={false} aria-label="sample table">
      <TableHead>
        <TableRow>
          {headers.map(header => <TableHeader id={header.key} key={header.key}>
              {header.header}
            </TableHeader>)}
        </TableRow>
      </TableHead>
      <TableBody>
        {rows.map((row, idx) => <TableRow key={idx}>
        {Object.keys(row).filter(key => key !== 'id').map(key => {
          return (
          <TableCell key={key}>
            {key === "status" ? (
              <StatusTag type={row[key]} />
            ):
            key === "activityType" ? (
              <ActivityTag type={row[key]} />
            ):
            row[key]}
          </TableCell>
          );
        })}
          </TableRow>)}
      </TableBody>
    </Table>
  );
};

export default ActionsTable;
