import React from 'react';

import './styles.scss';

interface OpeningsMapEntryPopupProps {
  openingId: number;
}

const OpeningsMapEntryPopup: React.FC<OpeningsMapEntryPopupProps> = ({ openingId }) => {

  return (
    <div className="map-popup-container">
      <p>{`Opening ID: ${openingId}`}</p>
    </div>
  );
};

export default OpeningsMapEntryPopup;
