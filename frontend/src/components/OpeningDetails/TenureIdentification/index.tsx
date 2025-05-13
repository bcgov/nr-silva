import React, { useState } from "react";
import {
  Button, Column,
  DefinitionTooltip,
  Grid, Pagination, Table, TableBody, TableCell,
  TableContainer,
  TableHead, TableHeader, TableRow,
  TableToolbar,
  TableToolbarSearch,
  Tag,
} from "@carbon/react";
import { Search } from "@carbon/icons-react";
import { useQuery } from "@tanstack/react-query";
import { fetchOpeningTenure } from "@/services/OpeningDetailsService";
import { pluralize } from "@/utils/StringUtils";
import { SortDirectionType } from "@/types/PaginationTypes";
import { PLACE_HOLDER } from "@/constants";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import { DEFAULT_PAGE_NUM, OddPageSizesConfig } from "@/constants/tableConstants";
import { OpeningTenureDto } from "@/types/OpeningTypes";

import OpeningTenureTooltip from "./PrimaryTenureDefinition";
import CutBlockStatusTag from "../../CutBlockStatusTag";
import EmptySection from "../../EmptySection";
import TableSkeleton from "../../TableSkeleton";

import { DefaultFilter, TenureTableHeaders } from "./constants";
import { formatPrimaryTenureLabel } from "./utils";
import { TenureFilterType } from "./definitions";
import './styles.scss';

type OpeningTenureProps = {
  openingId: number;
}

const TenureIdentification = ({ openingId }: OpeningTenureProps) => {
  const [searchInput, setSearchInput] = useState('');
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(() => OddPageSizesConfig[0]);
  const [tenureFilter, setTenureFilter] = useState<TenureFilterType>(() => DefaultFilter);

  const tenureQuery = useQuery({
    queryKey: ['opening', openingId, 'tenure', { tenureFilter }],
    queryFn: () => fetchOpeningTenure(openingId, tenureFilter),
  });

  const renderCellContent = (headerKey: keyof OpeningTenureDto, row: OpeningTenureDto) => {
    const content = tenureQuery.data?.content
    if (!content) {
      return null;
    }

    switch (headerKey) {
      case 'fileId':
        if (row.primaryTenure) {
          return (
            <>
              <span>{row.fileId}</span>
              <Tag size="md" title="Primary tenure" type="purple">
                Primary tenure
              </Tag>
            </>
          );
        }
        return row.fileId;

      case 'status':
        if (row.status) {
          return (
            <CutBlockStatusTag status={row.status} />
          );
        }
        return PLACE_HOLDER;

      default:
        return String(row[headerKey]);
    }
  }

  if (tenureQuery.data?.totalUnfiltered === 0) {
    return (
      <Grid className="opening-tenure-id-grid default-grid">
        <Column sm={4} md={8} lg={16}>
          <EmptySection
            pictogram="UserSearch"
            title="Nothing to show yet!"
            description="No tenures have been added to this opening yet."
          />
        </Column>
      </Grid>
    )
  }

  const handleSort = (field: keyof OpeningTenureDto) => {
    let newDirection: SortDirectionType = 'NONE';

    if (tenureFilter.sortField !== field || tenureFilter.sortDirection === 'NONE') {
      newDirection = 'ASC';
    } else if (tenureFilter.sortDirection === 'ASC') {
      newDirection = 'DESC';
    }

    setTenureFilter((prev) => {
      if (newDirection === 'NONE') {
        const { sortField, sortDirection, ...rest } = prev;
        return { ...rest };
      }

      return {
        ...prev,
        sortField: field,
        sortDirection: newDirection
      };
    });
  };

  const handlePagination = (paginationObj: PaginationOnChangeType) => {
    // Convert to 0 based index
    const nextPageNum = paginationObj.page - 1;
    const nextPageSize = paginationObj.pageSize;

    setCurrPageNumber(nextPageNum);
    setCurrPageSize(nextPageSize);
    setTenureFilter((prev) => ({
      ...prev,
      page: nextPageNum,
      size: nextPageSize
    }))
  };

  const handleSearchInputChange = (
    event: "" | React.ChangeEvent<HTMLInputElement>,
    value?: string
  ) => {
    // Handle string clearing
    if (event === "") {
      setSearchInput("");
      return;
    }

    setSearchInput(event.target.value);
  };

  const applySearchFilter = () => {
    const trimmed = searchInput.trim();

    setTenureFilter((prev) => {
      const next = { ...prev, page: 0 };

      if (trimmed === '') {
        const { filter, ...rest } = next;
        return rest;
      }

      return { ...next, filter: trimmed };
    });

    setCurrPageNumber(0);
  };

  const handleSearchKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      applySearchFilter();
    }
  };

  const handleSearchClear = () => {
    setSearchInput('');
    setTenureFilter((prev) => {
      const { filter, ...rest } = prev;
      return {
        ...rest,
        page: 0
      };
    });
    setCurrPageNumber(0);
  };

  return (
    <Grid className="opening-tenure-id-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <div className="tab-title-container">
          <h3 className="default-tab-content-title">
            {tenureQuery.data?.totalUnfiltered}
            {' '}
            {
              pluralize('tenure', tenureQuery.data?.totalUnfiltered)
            }
            {' '}
            in this opening
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
        <TableContainer className="default-table-container">
          <TableToolbar>
            <TableToolbarSearch
              className="default-tab-search-bar"
              persistent
              placeholder="Filter by keyword"
              value={searchInput}
              onChange={handleSearchInputChange}
              onKeyDown={handleSearchKeyDown}
              onClear={handleSearchClear}
            />
            <Button
              kind="primary"
              renderIcon={Search}
              onClick={applySearchFilter}
            >
              Filter
            </Button>
          </TableToolbar>

          {/* Table skeleton */}
          {
            tenureQuery.isLoading
              ? <TableSkeleton
                headers={TenureTableHeaders}
                showToolbar={false}
                showHeader={false}
                rowCount={10}
              />
              : null
          }

          {/* Loaded Table section */}
          <Table
            className="default-zebra-table"
            aria-label="Tenure identification table"
            useZebraStyles
          >
            <TableHead>
              <TableRow>
                {
                  TenureTableHeaders
                    .map((header) => (
                      <TableHeader
                        key={header.key}
                        isSortable={header.sortable}
                        isSortHeader={tenureFilter.sortField === header.key}
                        sortDirection={tenureFilter.sortDirection}
                        onClick={() => handleSort(header.key)}
                      >
                        {header.header}
                      </TableHeader>
                    ))
                }
              </TableRow>
            </TableHead>
            <TableBody>
              {
                tenureQuery.data?.content.map((row) => (
                  <TableRow key={row.id}>
                    {
                      TenureTableHeaders
                        .map((header) => (
                          <TableCell key={header.key} className="tenure-table-cell">
                            <span className="cell-content">
                              {renderCellContent(header.key, row)}
                            </span>
                          </TableCell>
                        ))
                    }
                  </TableRow>
                ))
              }
            </TableBody>
          </Table>
          <Pagination
            className="default-pagination-white"
            page={currPageNumber + 1}
            pageSize={currPageSize}
            pageSizes={OddPageSizesConfig}
            totalItems={tenureQuery.data?.page.totalElements}
            onChange={handlePagination}
          />
        </TableContainer>
      </Column>
    </Grid >
  );
};


export default TenureIdentification;
