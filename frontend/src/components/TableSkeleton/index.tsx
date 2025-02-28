import React from 'react';
import {
  DataTableSkeleton
} from '@carbon/react';

import './styles.scss';

const TableSkeleton = <T extends { header: string }>({
  headers,
  showToolbar,
  showHeader
}: {
  headers: T[];
  showToolbar?: boolean;
  showHeader?: boolean;
}) => {
  return (
    <DataTableSkeleton
      className="default-table-skeleton"
      headers={headers}
      aria-label="loading table data"
      showToolbar={showToolbar}
      showHeader={showHeader}
      zebra
    />)
};

export default TableSkeleton;
