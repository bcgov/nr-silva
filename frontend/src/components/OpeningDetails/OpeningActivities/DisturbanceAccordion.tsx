import React, { useMemo, useState } from "react";
import usePolygonAvailability from "@/hooks/usePolygonAvailability";
import { MAP_KINDS } from "@/constants/mapKindConstants";

import {
  Accordion,
  AccordionItem,
  Checkbox,
  DefinitionTooltip,
  Search,
  Table,
  TableBody,
  TableCell,
  TableExpandedRow,
  TableExpandHeader,
  TableExpandRow,
  TableHead,
  TableHeader,
  TableRow,
  Tooltip,
} from "@carbon/react";
import { TreeFallRisk } from "@carbon/icons-react";

import { CodeDescriptionDto, OpeningDetailsActivitiesDisturbanceDto } from "@/services/OpenApi";

import DisturbanceDetail from "./DisturbanceDetail";
import EmptySection from "../../EmptySection";

import { DisturbanceTableHeaders } from "./constants";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER } from "@/constants";

import "./styles.scss";

type DisturbanceAccordionProps = {
  openingId: number;
  data: OpeningDetailsActivitiesDisturbanceDto[],
  selectedDisturbanceIds: string[];
  setSelectedDisturbanceIds: React.Dispatch<React.SetStateAction<string[]>>;
}

const AccordionTitle = ({ total }: { total: number }) => (
  <div className="default-accordion-title-container">
    <div className="accordion-title-top">
      <TreeFallRisk size={20} />
      <h4>
        Disturbance events
      </h4>
    </div>
    <div className="accordion-title-bottom">
      {`Total disturbance: ${total}`}
    </div>
  </div>
)

type DisturbanceRowProps = {
  openingId: number;
  row: OpeningDetailsActivitiesDisturbanceDto;
  index: number;
  totalRows: number;
  selectedDisturbanceIds: string[];
  setSelectedDisturbanceIds: React.Dispatch<React.SetStateAction<string[]>>;
  renderCellContent: (
    data: CodeDescriptionDto | string | number | null,
    columnKey: string,
    isLastElement?: boolean
  ) => React.ReactNode;
};

const DisturbanceRow = ({
  openingId,
  row,
  index,
  totalRows,
  selectedDisturbanceIds,
  setSelectedDisturbanceIds,
  renderCellContent,
}: DisturbanceRowProps) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const compoundId = `${row.atuId}-DN`;
  const { isAvailable, isLoading } = usePolygonAvailability(
    openingId,
    MAP_KINDS.activityTreatment,
    compoundId,
  );
  const isSelected = selectedDisturbanceIds.includes(compoundId);
  const isLastElement = index === totalRows - 1;

  return (
    <React.Fragment key={row.atuId}>
      <TableExpandRow
        aria-label={`Expand row for Activity ID ${row.atuId}`}
        isExpanded={isExpanded}
        onExpand={() => setIsExpanded(prev => !prev)}
      >
        <TableCell key={"map-select" + row.atuId}>
          <Tooltip
            label={
              isLoading
                ? "Checking availability..."
                : !isAvailable
                  ? "Polygon is not available"
                  : isSelected
                    ? "Hide from map"
                    : "Show on map"
            }
            align={!isAvailable ? "right" : "top"}
          >
            <span>
              <Checkbox
                id={`disturbance-map-checkbox-${row.atuId}-${index}`}
                data-testid={`disturbance-map-checkbox-${row.atuId}-${index}`}
                key={`disturbance-map-checkbox-${row.atuId}-${index}`}
                checked={isSelected}
                disabled={!isAvailable || isLoading}
                labelText="Select polygon on map"
                hideLabel
                onChange={(_, { checked }) => {
                  setSelectedDisturbanceIds(ids =>
                    checked
                      ? [...ids, compoundId]
                      : ids.filter(id => id !== compoundId)
                  );
                }}
              />
            </span>
          </Tooltip>
        </TableCell>
        {
          DisturbanceTableHeaders.map(header => (
            <TableCell key={header.key}>
              {
                renderCellContent(
                  row[header.key] as string | number | CodeDescriptionDto | null,
                  header.key,
                  isLastElement,
                )
              }
            </TableCell>
          ))
        }
      </TableExpandRow>
      <TableExpandedRow colSpan={DisturbanceTableHeaders.length + 2}>
        <DisturbanceDetail detail={row} />
      </TableExpandedRow>
    </React.Fragment>
  );
};

const DisturbanceAccordion = ({
  openingId,
  data,
  selectedDisturbanceIds,
  setSelectedDisturbanceIds
}: DisturbanceAccordionProps) => {
  const [searchTerm, setSearchTerm] = useState<string>('');

  const isCodeDescription = (value: string): boolean => {
    const codeDescriptionColumns = ["disturbance", "system", "variant", "cutPhase"];
    return codeDescriptionColumns.includes(value);
  }

  const isDate = (value: string): boolean => {
    const dateColumns = ["startDate", "endDate", "lastUpdatedOn"];
    return dateColumns.includes(value);
  }

  const filteredData = useMemo(() => {
    const lower = searchTerm.toLowerCase();

    return data.filter((row) =>
      DisturbanceTableHeaders.some(({ key }) => {
        const value = row[key];

        if (isCodeDescription(key)) {
          return value && codeDescriptionToDisplayText(value as CodeDescriptionDto).toLowerCase().includes(lower);
        } else if (isDate(key)) {
          return value && formatLocalDate(String(value)).toLowerCase().includes(lower);
        }
        return value && String(value).toLowerCase().includes(lower);
      })
    );
  }, [data, searchTerm]);

  // Select-all: based on all rows on the current filtered page (availability checked per-row)
  const allOnPage = filteredData.map(row => `${row.atuId}-DN`);
  const allSelected = allOnPage.length > 0 && allOnPage.every(id => selectedDisturbanceIds.includes(id));
  const someSelected = allOnPage.some(id => selectedDisturbanceIds.includes(id));

  const renderCellContent = (
    data: CodeDescriptionDto | string | number | null,
    columnKey: string,
    isLastElement: boolean = false
  ) => {
    if (isCodeDescription(columnKey)) {
      const codeDescription = data as CodeDescriptionDto;

      if (columnKey === "disturbance") {
        return codeDescriptionToDisplayText(codeDescription);
      }

      return codeDescription?.code ? (
        <DefinitionTooltip
          definition={codeDescription.description}
          align={isLastElement ? "top" : "bottom"}
          openOnHover={true}
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
      className="default-tab-accordion"
      align="end"
      data-testid="disturbance-accordion-container"
    >
      <AccordionItem
        className="default-tab-accordion-item"
        title={<AccordionTitle total={data.length} />}
      >
        <div>
          <Search
            id="disturbance-filter"
            className="default-tab-search-bar"
            size="lg"
            labelText="Filter disturbances"
            placeholder="Filter disturbance by keyword"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <Table
            className="default-zebra-table-with-border"
            aria-label="Disturbance table"
          >
            <TableHead>
              <TableRow>
                {
                  <>
                    <TableExpandHeader />
                    <TableHeader key="map-header">
                      <div className="default-map-header-checkbox-container">
                        <Tooltip
                          label={
                            allSelected ? "Unselect all" : "Select all"
                          }
                          align="right"
                        >
                          <span>
                            <Checkbox
                              id="disturbance-map-select-all"
                              data-testid="disturbance-map-select-all"
                              checked={allSelected}
                              indeterminate={!allSelected && someSelected}
                              disabled={allOnPage.length === 0}
                              labelText="Select all polygons on map"
                              hideLabel
                              onChange={(_, { checked }) => {
                                setSelectedDisturbanceIds(ids =>
                                  checked
                                    ? [...new Set([...ids, ...allOnPage])]
                                    : ids.filter(id => !allOnPage.includes(id))
                                );
                              }}
                            />
                          </span>
                        </Tooltip>
                        <span>Map</span>
                      </div>

                    </TableHeader>
                    {
                      DisturbanceTableHeaders.map((header) => (

                        <TableHeader key={header.key}>{header.header}</TableHeader>
                      ))
                    }
                  </>
                }
              </TableRow>
            </TableHead>
            <TableBody>
              {
                filteredData.length ? (
                  filteredData.map((row, index) => (
                    <DisturbanceRow
                      key={row.atuId}
                      openingId={openingId}
                      row={row}
                      index={index}
                      totalRows={filteredData.length}
                      selectedDisturbanceIds={selectedDisturbanceIds}
                      setSelectedDisturbanceIds={setSelectedDisturbanceIds}
                      renderCellContent={renderCellContent}
                    />
                  ))
                ) : (
                  <TableRow key="empty-row">
                    <TableCell colSpan={DisturbanceTableHeaders.length + 2}>
                      <EmptySection
                        pictogram="UserSearch"
                        title={`No results for "${searchTerm}"`}
                        description="Consider adjusting your search term(s) and try again."
                      />
                    </TableCell>
                  </TableRow>
                )

              }
            </TableBody>
          </Table>
        </div>
      </AccordionItem>
    </Accordion>
  )
}

export default DisturbanceAccordion;
