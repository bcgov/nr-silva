import React, { useState } from "react";
import qs from 'qs';
import {
  Accordion,
  AccordionItem,
  Button,
  Checkbox,
  DefinitionTooltip,
  Pagination,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableExpandedRow,
  TableExpandHeader,
  TableExpandRow,
  TableHead,
  TableHeader,
  TableRow,
  TableToolbar,
  TableToolbarSearch,
  Tag,
  Tooltip,
} from "@carbon/react";
import { Activity, Search } from "@carbon/icons-react";
import { useQuery } from "@tanstack/react-query";

import { SortDirectionType } from "@/types/PaginationTypes";
import { PaginationOnChangeType } from "@/types/GeneralTypes";
import { PLACE_HOLDER } from "@/constants";
import API from "@/services/API";
import { CodeDescriptionDto, OpeningDetailsActivitiesActivitiesDto } from "@/services/OpenApi";

import ActivityDetail from "./ActivityDetail";
import EmptySection from "../../EmptySection";
import TableSkeleton from "../../TableSkeleton";

import { ActivityTableHeaders, DefaultFilter } from "./constants";
import { formatLocalDate } from "@/utils/DateUtils";
import { DEFAULT_PAGE_NUM, MAX_SEARCH_LENGTH, OddPageSizesConfig } from "@/constants/tableConstants";
import { ActivityFilterType } from "./definitions";
import { formatActivityObjective } from "./utils";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";

import "./styles.scss";

type ActivityAccordionProps = {
  openingId: number;
  totalUnfiltered: number;
  availableSilvicultureActivityIds: string[];
  selectedSilvicultureActivityIds: string[];
  setSelectedSilvicultureActivityIds: React.Dispatch<React.SetStateAction<string[]>>;
};

const AccordionTitle = ({ total }: { total: number }) => (
  <div className="default-accordion-title-container">
    <div className="accordion-title-top">
      <Activity size={20} />
      <h4>Silviculture activities</h4>
    </div>
    <div className="accordion-title-bottom">
      {`Total activities: ${total}`}
    </div>
  </div>
);

const ActivityAccordion = ({
  openingId,
  totalUnfiltered,
  availableSilvicultureActivityIds,
  selectedSilvicultureActivityIds,
  setSelectedSilvicultureActivityIds
}: ActivityAccordionProps) => {
  const [expandedRows, setExpandedRows] = useState<number[]>([]);
  const [searchInput, setSearchInput] = useState<string>("");
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(OddPageSizesConfig[0]!);
  const [activityFilter, setActivityFilter] = useState<ActivityFilterType>(DefaultFilter);

  const activityQuery = useQuery({
    queryKey: ['opening', openingId, 'activities', activityFilter],
    queryFn: () => {
      const { page, size, sortField, sortDirection, filter } = activityFilter;

      const sort =
        sortField && sortDirection !== 'NONE'
          ? [`${sortField},${sortDirection}`]
          : undefined;

      return API.OpeningEndpointService.getOpeningActivities(
        openingId,
        filter,
        page,
        size,
        sort
      );
    },
  });

  const allAvailableIds = activityQuery.data?.content ?
    activityQuery.data?.content
      .map(row => `${row.atuId}-${row.base.code}`)
      .filter(id => availableSilvicultureActivityIds.includes(id)) ?? []
    : [];
  const allSelected = allAvailableIds.length > 0 && allAvailableIds.every(id => selectedSilvicultureActivityIds.includes(id));
  const someSelected = allAvailableIds.some(id => selectedSilvicultureActivityIds.includes(id));

  const handleSort = (field: keyof OpeningDetailsActivitiesActivitiesDto) => {
    let newDirection: SortDirectionType = 'NONE';

    if (activityFilter.sortField !== field || activityFilter.sortDirection === 'NONE') {
      newDirection = 'ASC';
    } else if (activityFilter.sortDirection === 'ASC') {
      newDirection = 'DESC';
    }

    setActivityFilter((prev) => {
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
    setActivityFilter((prev) => ({
      ...prev,
      page: nextPageNum,
      size: nextPageSize
    }))
  };

  const handleSearchInputChange = (
    event: "" | React.ChangeEvent<HTMLInputElement>,
    _value?: string
  ) => {
    // Handle string clearing
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

    setActivityFilter((prev) => {
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
    setActivityFilter((prev) => {
      const { filter, ...rest } = prev;
      return {
        ...rest,
        page: 0
      };
    });
    setCurrPageNumber(0);
  };

  const handleRowExpand = (activityId: number) => {
    setExpandedRows((prev) =>
      prev.includes(activityId)
        ? prev.filter((id) => id !== activityId)
        : [...prev, activityId]
    );
  };

  const isCodeDescription = (value: string): boolean => {
    const codeDescriptionColumns = ["status", "base", "tech", "method", "objective1", "funding"];
    return codeDescriptionColumns.includes(value);
  };

  const isDate = (value: string): boolean => {
    const dateColumns = ["plannedDate", "endDate", "lastUpdate"];
    return dateColumns.includes(value);
  };

  const renderCellContent = (
    data: OpeningDetailsActivitiesActivitiesDto | CodeDescriptionDto | string | number | null,
    columnKey: string,
    isLastElement: boolean
  ) => {
    if (isCodeDescription(columnKey)) {
      const codeDescription = data as CodeDescriptionDto;

      if (columnKey === "status") {
        return (
          <Tag
            className="activity-status-tag"
            type={codeDescription.code === "C" ? "green" : "purple"}
            size="md"
          >
            {codeDescription.description}
          </Tag>
        );
      } else if (columnKey === "base") {
        return codeDescriptionToDisplayText(codeDescription);
      } else if (columnKey === "objective1") {
        const { tooltipDefinition, displayText } = formatActivityObjective(data as OpeningDetailsActivitiesActivitiesDto);

        if (displayText === PLACE_HOLDER) return PLACE_HOLDER;

        return (
          <DefinitionTooltip
            className="activity-objective-tooltip-definition"
            definition={
              tooltipDefinition!.map((line, index) => (
                <div key={index}>{line}</div>
              ))
            }
            align={isLastElement ? "top" : "bottom"}
            openOnHover={true}
          >
            <span>{displayText}</span>
          </DefinitionTooltip>
        );
      }

      return codeDescription?.code ? (
        <DefinitionTooltip
          definition={codeDescription.description}
          align={isLastElement ? "top" : "bottom"}
          openOnHover
        >
          <span>{codeDescription.code}</span>
        </DefinitionTooltip>
      ) : (
        PLACE_HOLDER
      );
    } else if (isDate(columnKey)) {
      return data ? formatLocalDate(String(data)) : PLACE_HOLDER;
    } else {
      return data ? String(data) : PLACE_HOLDER;
    }
  };

  return (
    <Accordion
      className="default-tab-accordion activity-accordion"
      align="end"
      data-testid="activity-accordion-container">
      <AccordionItem
        className="default-tab-accordion-item"
        title={<AccordionTitle total={totalUnfiltered} />}
      >

        <TableContainer className="default-table-container activity-table-container">
          <TableToolbar className="activity-search-toolbar-container">
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

          {/* Table skeleton */}
          {
            activityQuery.isLoading
              ? <TableSkeleton
                headers={ActivityTableHeaders}
                showToolbar={false}
                showHeader={false}
                rowCount={10}
              />
              : (
                <Table
                  className="default-zebra-table activity-table"
                  aria-label="Activity table"
                >
                  <TableHead>
                    <TableRow>
                      <>
                        <TableExpandHeader />
                        <TableHeader key="map-header">
                          <div className="map-header-checkbox-container">
                            <Tooltip
                              label={allSelected ? "Unselect all" : "Select all"}
                              align="right"
                            >
                              <span>
                                <Checkbox
                                  id="activities-map-select-all"
                                  data-testid="activities-map-select-all"
                                  checked={allSelected}
                                  indeterminate={!allSelected && someSelected}
                                  disabled={allAvailableIds.length === 0}
                                  labelText=""
                                  onChange={(_, { checked }) => {
                                    setSelectedSilvicultureActivityIds(checked ? allAvailableIds : []);
                                  }}
                                />
                              </span>
                            </Tooltip>
                            <span>Map</span>
                          </div>

                        </TableHeader>
                        {ActivityTableHeaders.map((header) => (
                          <TableHeader
                            key={header.key}
                            isSortable={header.sortable}
                            isSortHeader={activityFilter.sortField === header.key}
                            sortDirection={activityFilter.sortDirection}
                            onClick={() => handleSort(header.key as keyof OpeningDetailsActivitiesActivitiesDto)}
                          >
                            {header.header}
                          </TableHeader>
                        ))}
                      </>
                    </TableRow>
                  </TableHead>

                  <TableBody>
                    {activityQuery.data?.content?.map((row, index) => {
                      const isExpanded = expandedRows.includes(row.atuId);
                      return (
                        <React.Fragment key={row.atuId}>
                          <TableExpandRow
                            aria-label={`Expand row for Activity ID ${row.atuId}`}
                            isExpanded={isExpanded}
                            onExpand={() => handleRowExpand(row.atuId)}
                          >
                            <TableCell key={"map-select" + row.atuId}>
                              <Tooltip
                                label={
                                  !availableSilvicultureActivityIds.includes(`${row.atuId}-DN`)
                                    ? "Polygon is not available"
                                    : selectedSilvicultureActivityIds.includes(`${row.atuId}-${row.base.code}`)
                                      ? "Hide from map"
                                      : "Show on map"
                                }
                                align={
                                  !availableSilvicultureActivityIds.includes(`${row.atuId}-DN`)
                                    ? "right"
                                    : "top"
                                }
                              >
                                <span>
                                  <Checkbox
                                    id={`activities-map-checkbox-${row.atuId}-${index}`}
                                    data-testid={`activities-map-checkbox-${row.atuId}-${index}`}
                                    key={`activities-map-checkbox-${row.atuId}-${index}`}
                                    checked={selectedSilvicultureActivityIds.includes(`${row.atuId}-${row.base.code}`)}
                                    disabled={!availableSilvicultureActivityIds.includes(`${row.atuId}-${row.base.code}`)}
                                    labelText=""
                                    onChange={(_, { checked }) => {
                                      setSelectedSilvicultureActivityIds(ids =>
                                        checked
                                          ? [...ids, `${row.atuId}-${row.base.code}`]
                                          : ids.filter(id => id !== `${row.atuId}-${row.base.code}`)
                                      );
                                    }}
                                  />
                                </span>

                              </Tooltip>
                            </TableCell>
                            {ActivityTableHeaders.map((header) => (
                              <TableCell key={header.key}>
                                {renderCellContent(
                                  header.key === "objective1" ? row : row[header.key],
                                  header.key,
                                  index === (activityQuery.data?.content?.length ?? 0) - 1
                                )}
                              </TableCell>
                            ))}
                          </TableExpandRow>
                          <TableExpandedRow colSpan={ActivityTableHeaders.length + 1}>
                            {isExpanded ? (
                              <ActivityDetail
                                activity={row}
                                openingId={openingId}
                              />
                            ) : null}
                          </TableExpandedRow>
                        </React.Fragment>
                      );
                    })}
                  </TableBody>
                </Table>
              )
          }

          {
            activityQuery.data?.page?.totalElements === 0
              ? (
                <EmptySection
                  pictogram="UserSearch"
                  title={`No results for "${activityFilter.filter}"`}
                  description="Consider adjusting your search term and try again."
                  whiteLayer
                />
              )
              : (
                <Pagination
                  className="default-pagination-white"
                  page={currPageNumber + 1}
                  pageSize={currPageSize}
                  pageSizes={OddPageSizesConfig}
                  totalItems={activityQuery.data?.page?.totalElements}
                  onChange={handlePagination}
                />
              )
          }
        </TableContainer>
      </AccordionItem>
    </Accordion>
  );
};

export default ActivityAccordion;
