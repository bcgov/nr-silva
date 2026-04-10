import { Button, Tooltip } from "@carbon/react";
import { Location, LocationFilled } from "@carbon/icons-react";

import "./styles.scss";

interface SpatialCheckboxProps {
  spatialType: 'opening' | 'activity' | 'disturbance' | 'forest cover' | 'standards unit';
  rowId: number;
  selectedRows: number[];
  handleRowSelection: (rowId: number) => void;
  isAvailable?: boolean;
  isLoading?: boolean;
}

const SpatialCheckbox = ({
  spatialType,
  rowId,
  selectedRows,
  handleRowSelection,
  isAvailable = true,
  isLoading = false,
}: SpatialCheckboxProps) => {
  const isDisabled = !isAvailable || isLoading;

  const tooltipLabel = isLoading
    ? 'Checking spatial availability...'
    : isAvailable
      ? `Click to view this ${spatialType} on the map`
      : `Spatial data is not available for this ${spatialType}`;

  const button = (
    <Button
      className={selectedRows.includes(rowId) ? 'spatial-checkbox-checked' : 'spatial-checkbox'}
      hasIconOnly
      renderIcon={selectedRows.includes(rowId) ? LocationFilled : Location}
      onClick={() => handleRowSelection(rowId)}
      iconDescription={tooltipLabel}
      tooltipPosition="right"
      kind="ghost"
      size="sm"
      disabled={isDisabled}
    />
  );

  // When disabled, the button won't receive mouse events so its built-in
  // iconDescription tooltip never fires. Wrap in a Tooltip + span so the
  // span intercepts hover and the user still sees the reason why it's disabled.
  if (isDisabled) {
    return (
      <Tooltip label={tooltipLabel} align="right">
        <span>
          {button}
        </span>
      </Tooltip >
    );
  }

  return button;
};

export default SpatialCheckbox;
