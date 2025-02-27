import React from 'react';

import { IconButton } from '@carbon/react';
import { Close } from '@carbon/icons-react';

import './RightPanelTitle.scss';

interface RightPanelTitleProps {
  title: string;
  closeFn: () => void;
}

const RightPanelTitle = ({ title, closeFn }: RightPanelTitleProps) => (
  <div className="right-title-section">
    <h4>
      {title}
    </h4>
    <div className="right-title-buttons">
      <IconButton kind="ghost" label="Close" onClick={closeFn} align="bottom">
        <Close />
      </IconButton>
    </div>
  </div>
);

export default RightPanelTitle;
