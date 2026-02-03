import React from "react";
import { Button } from "@carbon/react";
import { Location, LocationFilled } from "@carbon/icons-react";

import "./styles.scss";

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
  <Button
    className={selectedRows.includes(rowId) ? 'spatial-checkbox-checked' : 'spatial-checkbox'}
    hasIconOnly
    renderIcon={selectedRows.includes(rowId) ? LocationFilled : Location}
    onClick={() => handleRowSelection(rowId)}
    iconDescription="Click to view this opening on the map"
    tooltipPosition="right"
    kind="ghost"
    size="sm"
  />
);

export default SpatialCheckbox;
