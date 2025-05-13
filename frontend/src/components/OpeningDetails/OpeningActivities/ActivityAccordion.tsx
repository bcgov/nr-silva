import React, { useMemo, useState } from "react";
import { Accordion, AccordionItem, Button, DefinitionTooltip, Search, Table, TableBody, TableCell, TableExpandedRow, TableExpandHeader, TableExpandRow, TableHead, TableHeader, TableRow, Tag } from "@carbon/react";
import { Activity, Search as SearchIcon } from "@carbon/icons-react";
import { ActivityTableHeaders } from "./constants";
import { MockedActivityType } from "./definitions";
import { formatLocalDate } from "@/utils/DateUtils";
import { PLACE_HOLDER, UNIQUE_CHARACTERS_UNICODE } from "@/constants";
import CodeDescriptionDto from "@/types/CodeDescriptionType";
import ActivityDetail from "./ActivityDetail";

import "./styles.scss";
import { OpeningDetailsActivitiesActivitiesDto } from "@/types/OpeningTypes";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";

type ActivityAccordionProps = {
  data: OpeningDetailsActivitiesActivitiesDto[];
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
  const [searchTerm, setSearchTerm] = useState<string>("");

  const filteredData = useMemo(() => {
    const lower = searchTerm.toLowerCase();

    // TODO: Fix the filter to work with backend filtering
    return data.filter((row) => row
      // ActivityTableHeaders.some(({ key }) => {
      //   const value = key === "objective" ? row["objective"] : row[key];
      //   return value && String(value).toLowerCase().includes(lower);
      // })
    );
  }, [data, searchTerm]);

  const handleRowExpand = (activityId: number) => {
    setExpandedRows((prev) =>
      prev.includes(activityId)
        ? prev.filter((id) => id !== activityId)
        : [...prev, activityId]
    );
  };

  const isCodeDescription = (value: string): boolean => {
    const codeDescriptionColumns = ["status","base", "tech", "method", "objective", "funding"];
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
      } else if (columnKey === "objective") {
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
          <div className="activity-search-container">
            <Search
              id="activity-filter"
              className="default-tab-search-bar"
              size="lg"
              labelText="Search activities"
              placeholder="Search by keyword"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <Button className="filter-search-button">
              <span>Search</span>
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
                            header.key === "objective" ? row : row[header.key as keyof OpeningDetailsActivitiesActivitiesDto],
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
