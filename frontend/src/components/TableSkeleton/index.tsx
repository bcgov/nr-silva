import React, { useState } from 'react';
import {
  DataTableSkeleton
} from '@carbon/react';
import './styles.scss'
import { TableHeader } from '../../types/TableHeader';

interface Props {
  headers: TableHeader[];
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
