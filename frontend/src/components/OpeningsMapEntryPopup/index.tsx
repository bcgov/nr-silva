import React from "react";
import OpeningsMapDownloader from "@/components/OpeningsMapDownloader";
import "./styles.scss";

interface OpeningsMapEntryPopupProps {
  openingId: number;
  data?: Record<string, any>;
  feature?: GeoJSON.FeatureCollection;
  isSelected?: boolean;
}

const OpeningsMapEntryPopup: React.FC<OpeningsMapEntryPopupProps> = ({
  openingId,
  data,
  feature,
  isSelected = false,
}) => {
  return (
    <div className="map-popup-container">
      <div className="map-popup-header-container">
        <h4 className="map-popup-header">Opening ID: {openingId}</h4>
      </div>

      <div className="map-popup-details-container">
        {data &&
          Object.entries(data).map(([key, value]) => (
            <span key={key}>{`${key}: ${value}`}</span>
          ))}
      </div>
      <div className="map-popup-links-container">
        {feature && <OpeningsMapDownloader feature={feature} />}
      </div>

      {!isSelected ? (
        <div className="map-popup-pin-hint">
          Click polygon to pin
        </div>
      ) : null}

    </div>
  );
};

export default OpeningsMapEntryPopup;
