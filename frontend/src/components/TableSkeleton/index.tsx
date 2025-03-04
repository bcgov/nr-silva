import React from 'react';
import {
  DataTableSkeleton
} from '@carbon/react';

import './styles.scss';

const TableSkeleton = <T extends { header: string }>({
  headers,
  showToolbar,
  showHeader,
  rowCount
}: {
  headers: T[];
  showToolbar?: boolean;
  showHeader?: boolean;
  rowCount?: number;
}) => {
  return (
    <DataTableSkeleton
      className="default-table-skeleton"
      headers={headers}
      aria-label="loading table data"
      showToolbar={showToolbar}
      showHeader={showHeader}
      rowCount={rowCount ?? 5}
      zebra
    />)
};

export default TableSkeleton;
