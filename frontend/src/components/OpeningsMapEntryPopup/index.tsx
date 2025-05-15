import React from "react";

import "./styles.scss";

interface OpeningsMapEntryPopupProps {
  openingId: number;
  data?: Record<string, any>;
}

const OpeningsMapEntryPopup: React.FC<OpeningsMapEntryPopupProps> = ({
  openingId,
  data,
}) => {
  return (
    <div className="map-popup-container">
      <p>{`Opening ID: ${openingId}`}</p>
      {data &&
        Object.entries(data).map(([key, value]) => <p>{`${key}: ${value}`}</p>)}
    </div>
  );
};

export default OpeningsMapEntryPopup;
