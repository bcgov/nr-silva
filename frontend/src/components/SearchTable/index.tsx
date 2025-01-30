import React from "react";
import { 
  Table,
  TableHead,
  TableRow,
  TableHeader,
  TableBody,
  TableCell,
  Pagination
} from "@carbon/react";

// Custom components
import EmptySection from "@/components/EmptySection";
import TableSkeleton from "@/components/TableSkeleton";

// Types
import { ITableHeader } from '@/types/TableHeader';

interface SearchTableProps {
  headers: ITableHeader[];
  rows: Record<string,any>[];
  loading: boolean;
  fetched: boolean;
  total: number;
  entriesPerPage: number;
  currentPage: number;
  onPageChange: (page: number) => void;
  onSizeChange: (page: number, size: number) => void;
}

const SearchTable: React.FC<SearchTableProps> = ({ 
  headers, 
  rows, 
  loading, 
  fetched,
  total,
  entriesPerPage,
  currentPage,
  onPageChange,
  onSizeChange
}) => {

  // If is loading, show skeleton
  if(loading) {
    return (<TableSkeleton headers={headers} />)
  }

  // Not loading, not fetched
  if (!fetched) {
    return (
      <EmptySection
        pictogram="Summit"
        title={"Nothing to show yet"}
        description={
          "At least one criteria must be entered to start the search: opening ID, opening number, timber mark, file ID or apply advanced search criteria. The matching results will be shown here."
        }
        fill="#0073E6"
      />
    );
  }

  // After fetch, but no data
  if(!rows.length) {
    return (
      <EmptySection
          pictogram="UserSearch"
          title={"Results not found"}
          description={"Check spelling or try different parameters"}
          fill="#0073E6"
        />
    );
  }

  // We have data, is fetched and is not loading
  return (
    <>
      <Table aria-label="sample table">
        <TableHead>
          <TableRow>
            {headers.filter(header => header.selected).map(header => <TableHeader key={header.key}>{header.header}</TableHeader>)}
          </TableRow>
        </TableHead>
        <TableBody>
          {
            rows
            .map(row => 
              <TableRow key={`${row['openingId']}-row`}>
              {
                headers
                .filter(header => header.selected)
                .map(header =>
                  row[header.key] 
                  ? <TableCell key={`${row['openingId']}-${header.key}`}>{row[header.key]}</TableCell> 
                  : <TableCell key={`${row['openingId']}-${header.key}`}></TableCell>            
                )
              }
              </TableRow>
            )
          }
        </TableBody>
      </Table>
      <Pagination
        totalItems={total}
        backwardText="Previous page"
        forwardText="Next page"
        pageSize={entriesPerPage}
        pageSizes={[20, 40, 60, 80, 100]}
        itemsPerPageText="Items per page"
        page={currentPage}
        onChange={({ page, pageSize }: {
          page: number;
          pageSize: number;
        }) => {
          onPageChange(page);
          onSizeChange(page, pageSize);
        }}
      />
    </>
  );
};

export default SearchTable;
