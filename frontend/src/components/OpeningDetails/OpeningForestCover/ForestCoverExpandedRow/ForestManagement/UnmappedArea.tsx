import React from "react";
import { CardContainer } from "@/components/Card";
import { Column, Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@carbon/react";
import { Road } from "@carbon/icons-react";
import { StockingStatusTag } from "@/components/Tags";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import { UnmappedAreaDto } from "../../definitions";
import { UnmappedAreaHeaders } from "./constants";

type UnmappedAreaProps = {
  data: UnmappedAreaDto[];
}

const UnmappedArea = ({ data }: UnmappedAreaProps) => {
  const renderCellContent = (row: UnmappedAreaDto, key: keyof UnmappedAreaDto) => {
    switch (key) {
      case 'stockingStatus':
        return <StockingStatusTag status={row.stockingStatus} />;
      case 'stockingType':
        return codeDescriptionToDisplayText(row.stockingType);
      case 'area':
        return `${row.area} ha`
      default:
        return String(row[key]);
    }
  }

  return (
    <CardContainer>
      <Column sm={4} md={8} lg={16}>
        <div className="card-title-container">
          <div className="icon-and-title">
            <Road size={20} />
            <h3>
              Unmapped area
            </h3>
          </div>
          <p className="card-subtitle">Areas that are not surveyed, digitized, or included in spatial datasets</p>
        </div>
      </Column>

      <Column sm={4} md={8} lg={16}>
        <div className="child-table-container">
          <Table className="default-zebra-table-with-border child-table" aria-label="Unmapped area table">
            <TableHead>
              <TableRow>
                {
                  UnmappedAreaHeaders.map((header) => (
                    <TableHeader key={header.key}>{header.header}</TableHeader>
                  ))
                }
              </TableRow>
            </TableHead>

            <TableBody>
              {
                data.map((row) => {
                  return (
                    <TableRow key={row.unmappedAreaId}>
                      {
                        UnmappedAreaHeaders.map((header) => {
                          return (
                            <TableCell key={header.key}>
                              {renderCellContent(row, header.key)}
                            </TableCell>
                          )
                        })
                      }
                    </TableRow>
                  )
                })
              }
            </TableBody>
          </Table>
        </div>
      </Column>

    </CardContainer>
  )
}

export default UnmappedArea;
