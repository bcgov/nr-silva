import React, { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import {
  Table, TableBody, TableCell, TableContainer, TableHead, TableHeader, TableRow,
  TableExpandHeader, TableExpandRow, TableExpandedRow, TableToolbar, TableToolbarSearch,
  Button, Column, Grid,
  DefinitionTooltip
} from "@carbon/react";
import { Search } from "@carbon/icons-react";
import { NOT_APPLICABLE, PLACE_HOLDER } from "@/constants";
import TableSkeleton from "@/components/TableSkeleton";
import EmptySection from "@/components/EmptySection";
import { delayMock } from "@/utils/MockUtils";
import { PageSizesConfig, MAX_SEARCH_LENGTH } from "@/constants/tableConstants";

import { DefaultFilter, ForestCoverTableHeaders } from "./constants";
import {
  mockOpeningDetailsForestCover,
  ForestCoverFilterType,
  type OpeningForestCoverType,
} from "./definitions";
import { formatForestCoverSpeciesArray } from "./utils";
import ForestCoverExpandedRow from "./ForestCoverExpandedRow";

import "./styles.scss";
import StockingStatusTag from "../../StockingStatusTag";

const fetchForestCover = async (_openingId: number, filter: ForestCoverFilterType) => {
  let data = [...mockOpeningDetailsForestCover];
  const totalElements = data.length;
  const content = data;
  return delayMock({ content, page: { totalElements } }, 500);
};

type OpeningForestCoverProps = {
  openingId: number;
};

const OpeningForestCover = ({ openingId }: OpeningForestCoverProps) => {
  const [expandedRows, setExpandedRows] = useState<string[]>([]);
  const [searchInput, setSearchInput] = useState<string>("");
  const [filter, setFilter] = useState<ForestCoverFilterType>(DefaultFilter);

  const forestCoverQuery = useQuery({
    queryKey: ["opening", openingId, "forestcover", { filter }],
    queryFn: () => fetchForestCover(openingId, filter),
  });

  const handleSearchInputChange = (
    event: "" | React.ChangeEvent<HTMLInputElement>,
    _value?: string
  ) => {
    if (event === "") {
      setSearchInput("");
      return;
    }
    const inputValue = event.target.value;
    if (inputValue.length <= MAX_SEARCH_LENGTH) {
      setSearchInput(inputValue);
    }
  };

  const applySearchFilter = () => {
    const trimmed = searchInput.trim();
    setFilter((prev) => {
      const next = { ...prev, page: 0 };
      if (trimmed === '') {
        const { filter, ...rest } = next;
        return rest;
      }
      return { ...next, filter: trimmed };
    });
  };

  const handleSearchKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      applySearchFilter();
    }
  };

  const handleSearchClear = () => {
    setSearchInput('');
    setFilter((prev) => {
      const { filter, ...rest } = prev;
      return {
        ...rest,
        page: 0
      };
    });
  };

  const handleRowExpand = (forestCoverId: string) => {
    setExpandedRows((prev) =>
      prev.includes(forestCoverId)
        ? prev.filter((id) => id !== forestCoverId)
        : [...prev, forestCoverId]
    );
  };

  // Render cell content similar to ActivityAccordion
  const renderCellContent = (
    row: OpeningForestCoverType,
    headerKey: string
  ) => {
    switch (headerKey) {
      case "forestCover":
        return (
          <div className="opening-forest-cover-cell-multiple-lines">
            <span>Polygon ID: {row.forestCoverPolygonId}</span>
            <span>
              Standard unit: {row.forestCoverStandardUnit ?? NOT_APPLICABLE}
            </span>
            <span>
              Unmapped area: {row.forestCoverUnmappedArea ?? NOT_APPLICABLE}
            </span>
          </div>
        );
      case "polygonArea":
        return (
          <div className="opening-forest-cover-cell-multiple-lines">
            <span>Gross: {row.polygonAreaGross} ha</span>
            <span>Net: {row.polygonAreaNet} ha</span>
          </div>
        );
      case "stockingStatus":
        return (
          <StockingStatusTag status={row.stockingStatus} />
        );
      case "stockingType":
        return row.stockingType;
      case "inventoryLayer": {
        const { tooltipDefinition, displayText } = formatForestCoverSpeciesArray(row.inventoryLayerSpecies)
        return (
          <div className="opening-forest-cover-cell-multiple-lines">
            <DefinitionTooltip
              className="forest-cover-species-tooltip-definition"
              definition={
                tooltipDefinition.map((line, index) => (
                  <div key={index}>{line}</div>
                ))
              }
              openOnHover
            >
              <span>Species: {displayText}</span>
            </DefinitionTooltip>
            <span>Total: {row.inventoryLayerFreeGrowing !== null ? `${row.inventoryLayerFreeGrowing} (st/ha)` : NOT_APPLICABLE}</span>
            <span>Total well spaced: {row.inventoryLayerTotalWellSpaced !== null ? `${row.inventoryLayerTotalWellSpaced} (st/ha)` : NOT_APPLICABLE}</span>
            <span>Well spaced: {row.inventoryLayerWellSpaced !== null ? `${row.inventoryLayerWellSpaced} (st/ha)` : NOT_APPLICABLE}</span>
            <span>Free growing: {row.inventoryLayerFreeGrowing !== null ? `${row.inventoryLayerFreeGrowing} (st/ha)` : NOT_APPLICABLE}</span>
          </div>
        );
      }
      case "silvicultureLayer": {
        const { tooltipDefinition, displayText } = formatForestCoverSpeciesArray(row.silvicultureLayerSpecies)

        return (
          <div className="opening-forest-cover-cell-multiple-lines">
            <DefinitionTooltip
              className="forest-cover-species-tooltip-definition"
              definition={
                tooltipDefinition.map((line, index) => (
                  <div key={index}>{line}</div>
                ))
              }
              openOnHover
            >
              <span>Species: {displayText}</span>
            </DefinitionTooltip>
            <span>Total well spaced: {row.silvicultureLayerTotalWellSpaced !== null ? `${row.silvicultureLayerTotalWellSpaced} (st/ha)` : NOT_APPLICABLE}</span>
            <span>Well spaced: {row.silvicultureLayerWellSpaced !== null ? `${row.silvicultureLayerWellSpaced} (st/ha)` : NOT_APPLICABLE}</span>
            <span>Free growing: {row.silvicultureLayerFreeGrowing !== null ? `${row.silvicultureLayerFreeGrowing} (st/ha)` : NOT_APPLICABLE}</span>
          </div>
        );
      }
      case "referenceYear":
        return row.referenceYear;
      default:
        return PLACE_HOLDER;
    }
  };

  if (forestCoverQuery.data?.page.totalElements === 0) {
    return (
      <EmptySection
        pictogram="Summit"
        title="Nothing to show yet!"
        description="No forest cover have been added to this opening yet"
      />
    )
  }

  return (
    <Grid className="opening-forest-cover-grid default-grid">
      <Column sm={4} md={8} lg={16}>
        <h3 className="default-tab-content-title">
          {forestCoverQuery.data?.page.totalElements ?? 0} forest cover polygons in this opening
        </h3>
      </Column>
      <Column sm={4} md={8} lg={16}>
        <TableContainer className="default-table-container opening-forest-cover-table-container">
          <TableToolbar>
            <TableToolbarSearch
              className="default-tab-search-bar"
              persistent
              placeholder="Search by keyword"
              value={searchInput}
              onChange={handleSearchInputChange}
              onKeyDown={handleSearchKeyDown}
              onClear={handleSearchClear}
            />
            <Button
              kind="primary"
              className="default-button-with-loading"
              renderIcon={Search}
              onClick={applySearchFilter}
            >
              Search
            </Button>
          </TableToolbar>
          {forestCoverQuery.isLoading ? (
            <TableSkeleton
              headers={ForestCoverTableHeaders}
              showToolbar={false}
              showHeader={false}
              rowCount={10}
            />
          ) : (
            <Table className="default-zebra-table forest-cover-table" aria-label="Forest cover table">
              <TableHead>
                <TableRow>
                  <TableExpandHeader />
                  {ForestCoverTableHeaders.map((header) => (
                    <TableHeader key={String(header.key)}>{header.header}</TableHeader>
                  ))}
                </TableRow>
              </TableHead>
              <TableBody>
                {forestCoverQuery.data?.content.map((row, idx) => {
                  const isExpanded = expandedRows.includes(row.forestCoverId);
                  return (
                    <React.Fragment key={row.forestCoverId + idx}>
                      <TableExpandRow
                        className="opening-forest-cover-table-row"
                        aria-label={`Expand row for Polygon ID ${row.forestCoverId}`}
                        isExpanded={isExpanded}
                        onExpand={() => handleRowExpand(row.forestCoverId)}
                      >
                        {ForestCoverTableHeaders.map((header) => (
                          <TableCell key={String(header.key)} className="default-table-cell">
                            {renderCellContent(row, header.key)}
                          </TableCell>
                        ))}
                      </TableExpandRow>
                      <TableExpandedRow className="forest-cover-expanded-row" colSpan={ForestCoverTableHeaders.length + 1}>
                        {isExpanded ? (
                          <ForestCoverExpandedRow forestCoverId={row.forestCoverId} />
                        ) : null}
                      </TableExpandedRow>
                    </React.Fragment>
                  );
                })}
              </TableBody>
            </Table>
          )}
        </TableContainer>
      </Column>
    </Grid>
  );
};

export default OpeningForestCover;
