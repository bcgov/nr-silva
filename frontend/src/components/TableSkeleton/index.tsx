import React from 'react';
import {
  DataTableSkeleton
} from '@carbon/react';

import './styles.scss';
interface Props {
  headers: {
    header: string; // Accept any array as long as it contains a header field
    [key: string]: unknown;
  }[];
  showToolbar?: boolean;
  showHeader?: boolean;
}

const TableSkeleton: React.FC<Props> = ({ headers, showToolbar, showHeader }) => (
  <DataTableSkeleton
    className="default-table-skeleton"
    headers={headers}
    aria-label="loading table data"
    showToolbar={showToolbar}
    showHeader={showHeader}
  />
);

export default TableSkeleton;
