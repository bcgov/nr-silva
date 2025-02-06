import React from "react";
import { Checkbox, Tooltip } from "@carbon/react";

interface SpatialCheckboxProps {
  rowId: number;
  selectedRows: number[];
  handleRowSelection: (rowId: number) => void;
}

const SpatialCheckbox: React.FC<SpatialCheckboxProps> = ({
  rowId,
  selectedRows,
  handleRowSelection
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
      onChange={() => handleRowSelection(rowId)}
      labelText=""
    />
  </Tooltip>
);

export default SpatialCheckbox;
