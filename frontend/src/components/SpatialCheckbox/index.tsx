// SpatialCheckbox.tsx

import React from "react";
import { Checkbox, CheckboxGroup, Tooltip } from "@carbon/react";

interface SpatialCheckboxProps {
  rowId: string;
  selectedRows: string[];
  handleRowSelectionChanged: (rowId: string) => void;
}

const SpatialCheckbox: React.FC<SpatialCheckboxProps> = ({
  rowId,
  selectedRows,
  handleRowSelectionChanged,
}) => (
  <CheckboxGroup orientation="horizontal">
    <Tooltip label="Click to view this opening's map activity.">
      <Checkbox
        id={`checkbox-label-${rowId}`}
        checked={selectedRows.includes(rowId)}
        onChange={() => handleRowSelectionChanged(rowId)}
      />
    </Tooltip>
  </CheckboxGroup>
);

export default SpatialCheckbox;
