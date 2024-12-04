import React from "react";
import { Checkbox, Tooltip } from "@carbon/react";

interface SpatialCheckboxProps {
  rowId: string;
  selectedRows: string[];
  handleRowSelectionChanged: (rowId: string) => void;
}

const SpatialCheckbox: React.FC<SpatialCheckboxProps> = ({
  rowId,
  selectedRows,
  handleRowSelectionChanged
}) => (
  <Tooltip
    className="align-self-stretch"
    align="bottom-left"
    autoAlign
    label="Click to view this opening's map activity.">
    <Checkbox
      data-testid={`checkbox-${rowId}`}
      className="align-self-stretch"
      id={`checkbox-label-${rowId}`}
      checked={selectedRows.includes(rowId)}
      onChange={() => handleRowSelectionChanged(rowId)}
    />
  </Tooltip>
);

export default SpatialCheckbox;
