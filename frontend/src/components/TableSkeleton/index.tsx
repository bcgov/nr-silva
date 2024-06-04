import React, { useState } from 'react';
import {
  DataTableSkeleton
} from '@carbon/react';
import './styles.scss'

interface Props {
  headers: {
    key: string;
    header: string;
  }[];
}

const TableSkeleton: React.FC<Props> = ({headers}) => (
  <div>
    <DataTableSkeleton 
      headers={headers}
      aria-label="loading table"
    />
  </div>
);

export default TableSkeleton;
