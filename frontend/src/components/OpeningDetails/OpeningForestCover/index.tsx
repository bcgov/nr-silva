import React, { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import {
  Table, TableBody, TableCell, TableContainer, TableHead, TableHeader, TableRow,
  TableExpandHeader, TableExpandRow, TableExpandedRow, TableToolbar, TableToolbarSearch,
  Button, Column, Grid,
  DefinitionTooltip,
  Checkbox,
  Tooltip
} from "@carbon/react";
import { CropGrowth, DiamondOutline, Layers, Search } from "@carbon/icons-react";
import { NOT_APPLICABLE, PLACE_HOLDER } from "@/constants";
import TableSkeleton from "@/components/TableSkeleton";
import EmptySection from "@/components/EmptySection";
import { StockingStatusTag } from "@/components/Tags";
import { MAX_SEARCH_LENGTH } from "@/constants/tableConstants";
import API from "@/services/API";
import { OpeningForestCoverDto } from "@/services/OpenApi";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";

import { EXPAND_PROMPT, ForestCoverTableHeaders } from "./constants";
import { formatForestCoverSpeciesArray } from "./utils";
import ForestCoverExpandedRow from "./ForestCoverExpandedRow";

import "./styles.scss";

type OpeningForestCoverProps = {
  openingId: number;
  availableForestCoverIds: string[];
  setAvailableForestCoverIds: React.Dispatch<React.SetStateAction<string[]>>;
  selectedForestCoverIds: string[];
  setSelectedForestCoverIds: React.Dispatch<React.SetStateAction<string[]>>;
};

const OpeningForestCover = ({
  openingId,
  availableForestCoverIds,
  setAvailableForestCoverIds,
  selectedForestCoverIds,
  setSelectedForestCoverIds }: OpeningForestCoverProps) => {
  const [expandedRows, setExpandedRows] = useState<number[]>([]);
  const [searchInput, setSearchInput] = useState<string>("");
  const [searchTerm, setSearchTerm] = useState<string | undefined>();

  const forestCoverDefaultQuery = useQuery({
    queryKey: ["opening", openingId, "cover"],
    queryFn: () => API.OpeningEndpointService.getCover(openingId)
  });

  const forestCoverSearchQuery = useQuery({
    queryKey: ["opening", openingId, "cover", { mainSearchTerm: searchTerm }],
    queryFn: () => API.OpeningEndpointService.getCover(openingId, searchTerm),
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
    setSearchTerm(searchInput);
  };

  const handleSearchKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      applySearchFilter();
    }
  };

  const handleSearchClear = () => {
    setSearchInput('');
    setSearchTerm(undefined);
  };

  const handleRowExpand = (forestCoverId: number) => {
    setExpandedRows((prev) =>
      prev.includes(forestCoverId)
        ? prev.filter((id) => id !== forestCoverId)
        : [...prev, forestCoverId]
    );
  };

  const allAvailableIds = forestCoverSearchQuery.data
    ?.map(row => `${row.coverId}-${row.polygonId}`)
    .filter(id => availableForestCoverIds.includes(id)) ?? [];
  const allSelected = allAvailableIds.length > 0 && allAvailableIds.every(id => selectedForestCoverIds.includes(id));
  const someSelected = allAvailableIds.some(id => selectedForestCoverIds.includes(id));

  // Render cell content similar to ActivityAccordion
  const renderCellContent = (
    row: OpeningForestCoverDto,
    headerKey: keyof OpeningForestCoverDto
  ) => {
    switch (headerKey) {
      case "coverId":
        return (
          <div className="opening-forest-cover-cell-multiple-lines">
            <span>Polygon ID: {row.polygonId}</span>
            <span>
              Standard unit: {row.standardUnitId ?? NOT_APPLICABLE}
            </span>
            <span>
              Unmapped area: {row.unmappedArea.code ? codeDescriptionToDisplayText(row.unmappedArea) : NOT_APPLICABLE}
            </span>
          </div>
        );
      case "isSingleLayer":
        return (
          <div className="opening-forest-cover-cell-multiple-lines">
            {
              <span className="icon-text-line">
                <span className="icon-in-line">
                  {
                    row.isSingleLayer ? <DiamondOutline /> : <Layers />
                  }
                </span>
                <span className="text-in-line">
                  {
                    row.isSingleLayer ? "Single layer" : "Multi layer"
                  }
                </span>
              </span>
            }
            {
              row.hasReserve
                ? (
                  <span className="icon-text-line">
                    <span className="icon-in-line"><CropGrowth /></span>
                    <span className="text-in-line">Reserve</span>
                  </span>
                )
                : null
            }
          </div>
        );
      case "grossArea":
        return (
          <div className="opening-forest-cover-cell-multiple-lines">
            <span>Gross: {row.grossArea} ha</span>
            <span>Net: {row.netArea} ha</span>
          </div>
        );
      case "status":
        return (
          <StockingStatusTag status={row.status} />
        );
      case "coverType":
        return codeDescriptionToDisplayText(row.coverType);
      case "inventoryLayer": {
        if (!row.isSingleLayer) {
          return EXPAND_PROMPT;
        }
        const { tooltipDefinition, displayText } = formatForestCoverSpeciesArray(row.inventoryLayer.species)
        return (
          <div className="opening-forest-cover-cell-multiple-lines">
            {
              row.inventoryLayer.species.length
                ? (
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
                )
                : `Species: ${NOT_APPLICABLE}`
            }
            <span>Total: {row.inventoryLayer.total !== null ? `${row.inventoryLayer.total} (st/ha)` : NOT_APPLICABLE}</span>
            <span>Total well spaced: {row.inventoryLayer.totalWellSpaced !== null ? `${row.inventoryLayer.totalWellSpaced} (st/ha)` : NOT_APPLICABLE}</span>
            <span>Well spaced: {row.inventoryLayer.wellSpaced !== null ? `${row.inventoryLayer.wellSpaced} (st/ha)` : NOT_APPLICABLE}</span>
            <span>Free growing: {row.inventoryLayer.freeGrowing !== null ? `${row.inventoryLayer.freeGrowing} (st/ha)` : NOT_APPLICABLE}</span>
          </div>
        );
      }
      case "silvicultureLayer": {
        if (!row.isSingleLayer) {
          return EXPAND_PROMPT;
        }
        const { tooltipDefinition, displayText } = formatForestCoverSpeciesArray(row.silvicultureLayer.species)

        return (
          <div className="opening-forest-cover-cell-multiple-lines">
            {
              row.silvicultureLayer.species.length
                ? (
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
                )
                : `Species: ${NOT_APPLICABLE}`
            }
            <span>Total well spaced: {row.silvicultureLayer.totalWellSpaced !== null ? `${row.silvicultureLayer.totalWellSpaced} (st/ha)` : NOT_APPLICABLE}</span>
            <span>Well spaced: {row.silvicultureLayer.wellSpaced !== null ? `${row.silvicultureLayer.wellSpaced} (st/ha)` : NOT_APPLICABLE}</span>
            <span>Free growing: {row.silvicultureLayer.freeGrowing !== null ? `${row.silvicultureLayer.freeGrowing} (st/ha)` : NOT_APPLICABLE}</span>
          </div>
        );
      }
      case "referenceYear":
        return row.referenceYear;
      default:
        return PLACE_HOLDER;
    }
  };

  if (forestCoverDefaultQuery.data?.length === 0) {
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
          {forestCoverDefaultQuery.isLoading ? '...' : `${forestCoverDefaultQuery.data?.length ?? 0}`} forest cover polygons in this opening
        </h3>
      </Column>
      <Column sm={4} md={8} lg={16}>
        <TableContainer className="default-table-container opening-forest-cover-table-container">
          <TableToolbar>
            <TableToolbarSearch
              className="default-tab-search-bar"
              persistent
              placeholder="Search by numeric value"
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
          {
            (forestCoverDefaultQuery.isLoading || forestCoverSearchQuery.isLoading)
              ? (
                <TableSkeleton
                  headers={ForestCoverTableHeaders}
                  showToolbar={false}
                  showHeader={false}
                  rowCount={10}
                />
              ) : (
                <Table className="default-zebra-table forest-cover-table" aria-label="Forest cover table" >
                  <TableHead>
                    <TableRow >
                      <TableExpandHeader />
                      <TableHeader key="map-header">
                        <Tooltip
                          label={allSelected ? "Unselect all" : "Select all"}
                          align="right"
                          className="forest-cover-map-tooltip"
                        >
                          <span>
                            <Checkbox
                              id="forest-cover-select-all"
                              checked={allSelected}
                              indeterminate={!allSelected && someSelected}
                              disabled={allAvailableIds.length === 0}
                              labelText=""
                              onChange={(_, { checked }) => {
                                setSelectedForestCoverIds(checked ? allAvailableIds : []);
                              }}
                            />
                          </span>
                        </Tooltip>
                      </TableHeader>
                      {ForestCoverTableHeaders.map((header) => (
                        <TableHeader key={String(header.key)}>{header.header}</TableHeader>
                      ))}
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {forestCoverSearchQuery.data?.map((row, idx) => {
                      const isExpanded = expandedRows.includes(row.coverId);
                      return (
                        <React.Fragment key={row.coverId + idx}>
                          <TableExpandRow
                            className="opening-forest-cover-table-row"
                            aria-label={`Expand row for Polygon ID ${row.coverId}`}
                            isExpanded={isExpanded}
                            onExpand={() => handleRowExpand(row.coverId)}
                          >
                            <TableCell key={"map-select" + row.coverId + row.polygonId}>
                              <Tooltip
                                label={
                                  !availableForestCoverIds.includes(`${row.coverId}-${row.polygonId}`)
                                    ? "Polygon is not available"
                                    : "Show on map"
                                }
                                align={
                                  !availableForestCoverIds.includes(`${row.coverId}-${row.polygonId}`)
                                    ? "right"
                                    : "top"
                                }
                              >
                                <span>
                                  <Checkbox
                                    id={`forest-cover-map-checkbox-${row.coverId}-${idx}`}
                                    key={`forest-cover-map-checkbox-${row.coverId}-${idx}`}
                                    checked={selectedForestCoverIds.includes(`${row.coverId}-${row.polygonId}`)}
                                    disabled={!availableForestCoverIds.includes(`${row.coverId}-${row.polygonId}`)}
                                    labelText=""
                                    onChange={(_, { checked }) => {
                                      setSelectedForestCoverIds(ids =>
                                        checked
                                          ? [...ids, `${row.coverId}-${row.polygonId}`]
                                          : ids.filter(id => id !== `${row.coverId}-${row.polygonId}`)
                                      );
                                    }}
                                  />
                                </span>
                              </Tooltip>
                            </TableCell>
                            {ForestCoverTableHeaders.map((header) => (
                              <TableCell key={String(header.key)} className="default-table-cell">
                                {renderCellContent(row, header.key)}
                              </TableCell>
                            ))}
                          </TableExpandRow>
                          <TableExpandedRow className="forest-cover-expanded-row" colSpan={ForestCoverTableHeaders.length + 1}>
                            {isExpanded ? <ForestCoverExpandedRow forestCoverId={row.coverId} openingId={openingId} /> : null}
                          </TableExpandedRow>
                        </React.Fragment>
                      );
                    })}
                  </TableBody>
                </Table>
              )
          }
          {
            forestCoverSearchQuery.data?.length === 0
              ? (
                <EmptySection
                  pictogram="UserSearch"
                  title="No results found"
                  description="No results found with the current filters. Try adjusting them to refine your search."
                />
              )
              : null
          }
        </TableContainer>
      </Column>
    </Grid>
  );
};

export default OpeningForestCover;
