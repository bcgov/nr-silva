import React, { useMemo, useState } from "react";
import { MockedDisturbanceType } from "./definitions";
import { Accordion, AccordionItem, Search, Table, TableBody, TableCell, TableExpandedRow, TableExpandHeader, TableExpandRow, TableHead, TableHeader, TableRow, Tag, Tooltip } from "@carbon/react";
import { TreeFallRisk } from "@carbon/icons-react";
import { DisturbanceTableHeaders } from "./constants";
import DisturbanceDetail from "./DisturbanceDetail";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import CodeDescriptionDto from "@/types/CodeDescriptionType";
import { PLACE_HOLDER } from "@/constants";
import { formatLocalDate } from "@/utils/DateUtils";

type DisturbanceAccordionProps = {
  data: MockedDisturbanceType[]
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

const DisturbanceAccordion = ({ data }: DisturbanceAccordionProps) => {
  const [expandedRows, setExpandedRows] = useState<number[]>([]);
  const [searchTerm, setSearchTerm] = useState<string>('');

  const filteredData = useMemo(() => {
    const lower = searchTerm.toLowerCase();

    return data.filter((row) =>
      DisturbanceTableHeaders.some(({ key }) => {
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
  };

  const isCodeDescription = (value: string): boolean => {
    const codeDescriptionColumns = ["disturbance", "silvicultureSystem", "variant", "cutPhase"];
    return codeDescriptionColumns.includes(value);
  }

  const isDate = (value: string): boolean => {
    const dateColumns = ["startDate", "endDate", "updateTimestamp"];
    return dateColumns.includes(value);
  }

  const renderCellContent = (
    data: CodeDescriptionDto | string | number | null,
    columnKey: string,
    isLastElement: boolean
  ) => {
    if (isCodeDescription(columnKey)) {
      const codeDescription = data as CodeDescriptionDto;

      if (columnKey === "disturbance") {
        return codeDescriptionToDisplayText(codeDescription);
      }
  
      return codeDescription?.code ? (
        <Tooltip
          label={codeDescription.description}
          align={isLastElement ? "top" : "bottom"}
        >
          <span>{codeDescription.code}</span>
        </Tooltip>
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
      size="lg"
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
            useZebraStyles
          >
            <TableHead>
              <TableRow>
                {
                  <>
                    <TableExpandHeader />
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
                filteredData.map((row, index) => {
                  const isExpanded = expandedRows.includes(row.activityId);
                  return (
                    <React.Fragment key={row.activityId}>
                      <TableExpandRow
                        aria-label={`Expand row for Activity ID ${row.activityId}`}
                        isExpanded={isExpanded}
                        onExpand={() => handleRowExpand(row.activityId)}
                      >
                        {
                          DisturbanceTableHeaders.map(header => (
                            <TableCell key={header.key}>
                              {
                                renderCellContent(row[header.key], header.key, index === filteredData.length - 1)
                              }
                            </TableCell>
                          ))
                        }
                      </TableExpandRow>
                      <TableExpandedRow colSpan={DisturbanceTableHeaders.length + 1}>
                        <DisturbanceDetail detail={row} />
                      </TableExpandedRow>
                    </React.Fragment>
                  )
                })
              }
            </TableBody>
          </Table>
        </div>
      </AccordionItem>
    </Accordion>
  )
}

export default DisturbanceAccordion;
