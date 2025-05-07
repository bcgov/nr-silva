import React, { useMemo, useState } from "react";
import { Accordion, AccordionItem, Button, DefinitionTooltip, Search, Table, TableBody, TableCell, TableExpandedRow, TableExpandHeader, TableExpandRow, TableHead, TableHeader, TableRow, Tag } from "@carbon/react";
import { Activity, Search as SearchIcon } from "@carbon/icons-react";
import { ActivityTableHeaders } from "./constants";
import { MockedActivityType } from "./definitions";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER } from "@/constants";
import CodeDescriptionDto from "@/types/CodeDescriptionType";
import ActivityDetail from "./ActivityDetail";

import "./styles.scss";

type ActivityAccordionProps = {
  data: MockedActivityType[];
  openingId: number;
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

const ActivityAccordion = ({ data, openingId }: ActivityAccordionProps) => {
  const [expandedRows, setExpandedRows] = useState<number[]>([]);
  const [clickedRow, setClickedRow] = useState<number | null>(null);
  const [searchTerm, setSearchTerm] = useState<string>("");

  const filteredData = useMemo(() => {
    const lower = searchTerm.toLowerCase();

    return data.filter((row) =>
      ActivityTableHeaders.some(({ key }) => {
        const value = row[key];
        return value && String(value).toLowerCase().includes(lower);
      })
    );
  }, [data, searchTerm]);

  const handleRowExpand = (activityId: number) => {
    setExpandedRows((prev) =>
      prev.includes(activityId)
        ? prev.filter((id) => id !== activityId)
        : [...prev, activityId]
    );
    setClickedRow(activityId);
  };

  const handleRowClick = (activityId: number) => {
    setClickedRow(activityId);
  };

  const isCodeDescription = (value: string): boolean => {
    const codeDescriptionColumns = ["status", "base", "tech", "method", "objective", "fundingSource"];
    return codeDescriptionColumns.includes(value);
  };

  const isDate = (value: string): boolean => {
    const dateColumns = ["plannedDate", "endDate", "updateTimestamp"];
    return dateColumns.includes(value);
  };

  const renderCellContent = (
    data: CodeDescriptionDto | string | number | null,
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
    <Accordion className="default-tab-accordion activity-accordion" align="end">
      <AccordionItem
        className="default-tab-accordion-item"
        title={<AccordionTitle total={data.length} />}
      >
        <div>
          <div className="activity-filter-container">
            <Search
              id="activity-filter"
              className="default-tab-search-bar"
              size="lg"
              labelText="Filter activities"
              placeholder="Filter activities by keyword"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <Button className="filter-search-button">
              <span>Filter</span>
              <SearchIcon/>
            </Button>
          </div>

          <Table
            className="default-zebra-table-with-border activity-table"
            aria-label="Activity table"
          >
            <TableHead>
              <TableRow>
                <>
                  <TableExpandHeader />
                  {ActivityTableHeaders.map((header) => (
                    <TableHeader key={header.key}>{header.header}</TableHeader>
                  ))}
                </>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredData.map((row, index) => {
                const isExpanded = expandedRows.includes(row.activityId);
                return (
                  <React.Fragment key={row.activityId}>
                    <TableExpandRow
                      aria-label={`Expand row for Activity ID ${row.activityId}`}
                      isExpanded={isExpanded}
                      onExpand={() => handleRowExpand(row.activityId)}
                      onClick={() => handleRowClick(row.activityId)}
                    >
                      {ActivityTableHeaders.map((header) => (
                        <TableCell key={header.key}>
                          {renderCellContent(
                            row[header.key],
                            header.key,
                            index === filteredData.length - 1
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
                      ): null}
                    </TableExpandedRow>
                  </React.Fragment>
                );
              })}
            </TableBody>
          </Table>
        </div>
      </AccordionItem>
    </Accordion>
  );
};

export default ActivityAccordion;
