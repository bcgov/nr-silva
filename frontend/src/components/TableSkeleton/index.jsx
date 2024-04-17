import React, { useState } from 'react';
import {
  DataTableSkeleton
} from '@carbon/react';
import './styles.scss'



export default function TableSkeleton({ headers }) {
  return (
    <div>
    <DataTableSkeleton  headers={headers} aria-label="sample table" />
    </div>
  );
}
