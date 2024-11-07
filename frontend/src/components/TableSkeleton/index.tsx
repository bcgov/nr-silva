import React from 'react';
import {
  DataTableSkeleton
} from '@carbon/react';
import './styles.scss'
import { ITableHeader } from '../../types/TableHeader';

interface Props {
  headers: ITableHeader[];
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
