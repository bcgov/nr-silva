import { TableRow, TableCell, Stack, Tooltip } from "@carbon/react";
import { Warning } from "@carbon/icons-react";
import { PLACE_HOLDER } from "@/constants";
import { StockingStandardsHeaderKeyType, StockingStandardsHeaderType } from "@/types/TableHeader";
import { StockingStandardsSearchResponseDto } from "@/services/OpenApi";

import "./styles.scss";

type props = {
  headers: StockingStandardsHeaderType[];
  rowData: StockingStandardsSearchResponseDto;
}

const StockingStandardsSearchTableRow = ({ headers, rowData }: props) => {

  const renderCellContent = (header: StockingStandardsHeaderKeyType) => {
    switch (header) {
      case "standardsRegimeId":
        return (
          <Stack gap={1} orientation="horizontal">
            <span>{rowData.standardsRegimeId ?? PLACE_HOLDER}</span>
            {
              rowData.isExpired
                ? (
                  <Tooltip definition="Expired stocking standard">
                    <Warning size={16} className="expired-standard-warning-icon" />
                  </Tooltip>
                )
                : null
            }
          </Stack>
        );

      case "bgc":
        return [
          rowData.bgcZone,
          rowData.bgcSubZone,
          rowData.bgcVariant,
          rowData.bgcPhase,
          rowData.becSiteSeries,
          rowData.becSiteType,
          rowData.becSeral,
        ]
          .map(val => val ?? '-')
          .join('.');

      default:
        if (rowData[header as keyof StockingStandardsSearchResponseDto]) {
          return String(rowData[header as keyof StockingStandardsSearchResponseDto]);
        }
        return PLACE_HOLDER;
    }
  };

  return (
    <TableRow data-testid={`stocking-standards-search-table-row-${rowData.standardsRegimeId}`}>
      {headers
        .filter((header) => header.selected)
        .map((header) => (
          <TableCell
            key={header.key}
            data-testid={`stocking-standards-search-table-cell-${header.key}-${rowData.standardsRegimeId}`}
          >
            {renderCellContent(header.key) ?? PLACE_HOLDER}
          </TableCell>
        ))}
    </TableRow>
  );
};

export default StockingStandardsSearchTableRow;
