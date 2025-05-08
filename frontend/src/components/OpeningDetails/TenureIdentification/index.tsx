import React from "react";
import {
  Accordion, AccordionItem, Column,
  Grid, Table, TableBody, TableCell,
  TableContainer,
  TableHead, TableHeader, TableRow,
  TableToolbarSearch
} from "@carbon/react";

import './styles.scss';

const TenureIdentification = () => {
  return (
    <Grid className="opening-standard-units-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <h3 className="default-tab-content-title">
          30 tenures in this opening
        </h3>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <TableContainer>
          <TableToolbarSearch
            className="default-tab-search-bar"
            persistent
            placeholder="Filter by keyword"
          />

        </TableContainer>
      </Column>
    </Grid >
  );
};


export default TenureIdentification;
