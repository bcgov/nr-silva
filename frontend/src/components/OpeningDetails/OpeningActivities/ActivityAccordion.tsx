import React, { useMemo, useState } from "react";
import { Accordion, AccordionItem, Button, DefinitionTooltip, InlineLoading, Pagination, Table, TableBody, TableCell, TableContainer, TableExpandedRow, TableExpandHeader, TableExpandRow, TableHead, TableHeader, TableRow, TableToolbar, TableToolbarSearch, Tag } from "@carbon/react";
import { Activity, Search } from "@carbon/icons-react";
import { ActivityTableHeaders, DefaultFilter } from "./constants";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER, UNIQUE_CHARACTERS_UNICODE } from "@/constants";
import CodeDescriptionDto from "@/types/CodeDescriptionType";
import ActivityDetail from "./ActivityDetail";

import "./styles.scss";
import { OpeningDetailsActivitiesActivitiesDto } from "@/types/OpeningTypes";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { DEFAULT_PAGE_NUM, MAX_SEARCH_LENGTH, OddPageSizesConfig } from "../../../constants/tableConstants";
import { ActivityFilterType } from "./definitions";
import { PaginatedResponseType, SortDirectionType } from "../../../types/PaginationTypes";
import { PaginationOnChangeType } from "../../../types/GeneralTypes";
import { useQuery, UseQueryResult } from "@tanstack/react-query";
import EmptySection from "../../EmptySection";
import { fetchOpeningActivities } from "../../../services/OpeningDetailsService";

type ActivityAccordionProps = {
  openingId: number;
  totalUnfiltered: number;
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

const ActivityAccordion = ({ openingId, totalUnfiltered }: ActivityAccordionProps) => {
  const [expandedRows, setExpandedRows] = useState<number[]>([]);
  const [searchInput, setSearchInput] = useState<string>("");
  const [currPageNumber, setCurrPageNumber] = useState<number>(DEFAULT_PAGE_NUM);
  const [currPageSize, setCurrPageSize] = useState<number>(OddPageSizesConfig[0]);
  const [activityFilter, setActivityFilter] = useState<ActivityFilterType>(DefaultFilter);

  const activityQuery = useQuery({
    queryKey: ['opening', openingId, 'activities', { filter: activityFilter }],
    queryFn: () => fetchOpeningActivities(openingId, activityFilter),
  });

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
        return String(`${codeDescription.code} - ${codeDescription.description}`);
      } else if (columnKey === "objective1") {
        // TODO put these formatting into a util
        const objective1 = (data as OpeningDetailsActivitiesActivitiesDto)?.objective1 as CodeDescriptionDto;
        const objective2 = (data as OpeningDetailsActivitiesActivitiesDto)?.objective2 as CodeDescriptionDto;
        const objective3 = (data as OpeningDetailsActivitiesActivitiesDto)?.objective3 as CodeDescriptionDto;

        const tooltipDefinition = [
          (objective1?.code && objective1?.description) ? `${objective1.code} - ${objective1.description}` : false,
          (objective2?.code && objective2?.description) ? `${objective2.code} - ${objective2.description}` : false,
          (objective3?.code && objective3?.description) ? `${objective3.code} - ${objective3.description}` : false
        ].filter(Boolean).join(`${UNIQUE_CHARACTERS_UNICODE.NEW_LINE}`);

        const displayText = [
          objective1?.code ?? false,
          objective2?.code ?? false,
          objective3?.code ?? false
        ].filter(Boolean).join(` ${UNIQUE_CHARACTERS_UNICODE.BULLET} `);

        if (displayText.length === 0) return PLACE_HOLDER;

        return (
          <DefinitionTooltip
            definition={
              <div className="activity-objective-tooltip-definition">
                {tooltipDefinition}
              </div>
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
  // TODO add loading table skeleton
  return (
    <Accordion className="default-tab-accordion activity-accordion" align="end">
      <AccordionItem
        className="default-tab-accordion-item"
        title={<AccordionTitle total={totalUnfiltered} />}
      >
        <div>
          <TableContainer className="default-table-container">
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
            <Table
              className="default-zebra-table activity-table"
              aria-label="Activity table"
            >
              <TableHead>
                <TableRow>
                  <>
                    <TableExpandHeader />
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
                {activityQuery.data?.content.map((row, index) => {
                  const isExpanded = expandedRows.includes(row.atuId);
                  return (
                    <React.Fragment key={row.atuId}>
                      <TableExpandRow
                        aria-label={`Expand row for Activity ID ${row.atuId}`}
                        isExpanded={isExpanded}
                        onExpand={() => handleRowExpand(row.atuId)}
                      >
                        {ActivityTableHeaders.map((header) => (
                          <TableCell key={header.key}>
                            {renderCellContent(
                              header.key === "objective1" ? row : row[header.key as keyof OpeningDetailsActivitiesActivitiesDto],
                              header.key,
                              index === activityQuery.data?.content.length - 1
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
            {
              activityQuery.data?.page.totalElements === 0
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
                    totalItems={activityQuery.data?.page.totalElements}
                    onChange={handlePagination}
                  />
                )
            }
          </TableContainer>

        </div>
      </AccordionItem>
    </Accordion>
  );
};

export default ActivityAccordion;
