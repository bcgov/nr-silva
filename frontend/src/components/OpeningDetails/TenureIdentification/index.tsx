import React from "react";
import {
  Accordion, AccordionItem, Button, Column,
  DefinitionTooltip,
  Grid, Table, TableBody, TableCell,
  TableContainer,
  TableHead, TableHeader, TableRow,
  TableToolbar,
  TableToolbarSearch,
  Tag,
  TextAreaSkeleton
} from "@carbon/react";
import { Search } from "@carbon/icons-react";

import './styles.scss';
import { useQuery } from "@tanstack/react-query";
import { fetchOpeningTenure } from "@/services/OpeningDetailsService";
import { TenureTableHeaders } from "./constants";
import { formatPrimaryTenureLabel } from "./utils";
import OpeningTenureTooltip from "./PrimaryTenureDefinition";
import { OpeningTenureDto } from "../../../types/OpeningTypes";
import CutBlockStatusTag from "../../CutBlockStatusTag";
import { PLACE_HOLDER } from "../../../constants";

type OpeningTenureProps = {
  openingId: number;
}

const TenureIdentification = ({ openingId }: OpeningTenureProps) => {

  const tenureQuery = useQuery({
    queryKey: ['opening', openingId, 'tenure'],
    queryFn: () => fetchOpeningTenure(openingId)
  });

  if (tenureQuery.isLoading) {
    return (
      <TextAreaSkeleton />
    )
  }

  const renderCellContent = (headerKey: keyof OpeningTenureDto, row: OpeningTenureDto) => {
    const content = tenureQuery.data?.content
    if (!content) {
      return null;
    }

    switch (headerKey) {
      case 'fileId':
        if (row.primaryTenure) {
          return (
            <span>
              <span>{row.fileId}</span>
              <Tag size="md" title="Primary tenure" type="purple">
                Primary tenure
              </Tag>
            </span>
          );
        }
        return row.fileId;

      case 'status':
        if (row.status) {
          return <CutBlockStatusTag status={row.status} />
        }
        return PLACE_HOLDER;

      default:
        return String(row[headerKey])
    }

  }

  return (
    <Grid className="opening-tenure-id-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <div className="tab-title-container">
          <h3 className="default-tab-content-title">
            {tenureQuery.data?.page.totalElements} tenures in this opening
          </h3>
          {
            tenureQuery.data?.primary
              ? (
                <DefinitionTooltip
                  openOnHover
                  className="primary-tenure-definition-tooltip"
                  definition={<OpeningTenureTooltip primary={tenureQuery.data.primary} />}
                >
                  {formatPrimaryTenureLabel(tenureQuery.data.primary)}
                </DefinitionTooltip>
              )
              : null
          }
        </div>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <TableContainer>
          <TableToolbar>
            <TableToolbarSearch
              className="default-tab-search-bar"
              persistent
              placeholder="Filter by keyword"
            />
            <Button kind="primary" renderIcon={Search}>
              Filter
            </Button>
          </TableToolbar>
          <Table
            className="default-zebra-table-with-border"
            aria-label="Tenure identification table"
            useZebraStyles
          >
            <TableHead>
              <TableRow>
                {
                  TenureTableHeaders
                    .map((header) => (
                      <TableHeader key={header.key}>{header.header}</TableHeader>
                    ))
                }
              </TableRow>
            </TableHead>
            <TableBody>
              {
                tenureQuery.data?.content.map((row) => (
                  <TableRow className="opening-table-row">
                    {
                      TenureTableHeaders
                        .map((header) => (
                          <TableCell key={header.key}>
                            {renderCellContent(header.key, row)}
                          </TableCell>
                        ))
                    }
                  </TableRow>
                ))
              }
            </TableBody>
          </Table>

        </TableContainer>
      </Column>
    </Grid >
  );
};


export default TenureIdentification;
