import { useState } from "react";
import { Button } from "@carbon/react";
import { Location, LocationFilled } from "@carbon/icons-react";

import "./styles.scss";

interface SpatialCheckboxProps {
  spatialType: 'opening' | 'activity';
  rowId: number;
  selectedRows: number[];
  handleRowSelection: (rowId: number) => void;
}

const SpatialCheckbox = ({
  spatialType,
  rowId,
  selectedRows,
  handleRowSelection
}: SpatialCheckboxProps) => {
  const [isSpatialAvailable, setIsSpatialAvailable] = useState(true);


  return (
    <Button
      className={selectedRows.includes(rowId) ? 'spatial-checkbox-checked' : 'spatial-checkbox'}
      hasIconOnly
      renderIcon={selectedRows.includes(rowId) ? LocationFilled : Location}
      onClick={() => handleRowSelection(rowId)}
      iconDescription={
        isSpatialAvailable
          ? `Click to view this ${spatialType} on the map`
          : `Spatial data is not available for this ${spatialType}`
      }
      tooltipPosition="right"
      kind="ghost"
      size="sm"
      disabled={!isSpatialAvailable}
    />
  );
};

export default SpatialCheckbox;
