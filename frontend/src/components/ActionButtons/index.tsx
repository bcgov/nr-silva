// ActionButtons.tsx

import React from "react";
import { Button } from "@carbon/react";
import * as Icons from "@carbon/icons-react";

interface ActionButtonsProps {
  rowId: string;
}

const ActionButtons: React.FC<ActionButtonsProps> = ({ rowId }) => (
  <>
    <Button
      hasIconOnly
      iconDescription="View"
      kind="ghost"
      renderIcon={Icons.View}
      onClick={() => console.log(rowId)}
      size="md"
    />
    <Button
      hasIconOnly
      iconDescription="Document Download"
      kind="ghost"
      renderIcon={Icons.DocumentDownload}
      onClick={() => null}
      size="md"
    />
  </>
);

export default ActionButtons;
