import React from "react";
import OpeningsMapDownloader from "@/components/OpeningsMapDownloader";
import "./styles.scss";

interface OpeningsMapEntryPopupProps {
  openingId: number;
  data?: Record<string, any>;
  feature?: GeoJSON.FeatureCollection;
}

const OpeningsMapEntryPopup: React.FC<OpeningsMapEntryPopupProps> = ({
  openingId,
  data,
  feature,
}) => {
  return (
    <div className="map-popup-container">
      <p>{`Opening ID: ${openingId}`}</p>
      {data &&
        Object.entries(data).map(([key, value]) => (
          <p key={key}>{`${key}: ${value}`}</p>
        ))}
      {feature && <OpeningsMapDownloader feature={feature} />}
    </div>
  );
};

export default OpeningsMapEntryPopup;
