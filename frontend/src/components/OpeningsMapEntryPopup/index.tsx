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

  const renderPopupHeader = (data?: Record<string, any>, openingId?: number) => {
    if (!data) return null;
    switch (data.mapKindType) {
      case "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW":
        return <h4 className="map-popup-header">Opening ID: {openingId}</h4>;
      case "WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW":
        return <h4 className="map-popup-header">Standard Unit: {data["standardUnitId"]}</h4>;
      default:
        return <h4 className="map-popup-header">Opening ID: {openingId}</h4>;
    }
  };

  const renderPopupContent = (data?: Record<string, any>, openingId?: number) => {
    if (!data) return null;
    switch (data.mapKindType) {
      case "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW":
        return (
          <>
            <span>Region: {data["region"]}</span>
            <span>District: {data["district"]}</span>
            <span>Year created: {data["yearCreated"]}</span>
          </>
        );
      case "WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW":
        return (
          <>
            <span>Opening ID: <span className="popup-value">{openingId}</span></span>
            <span>SSID: <span className="popup-value">{data["ssid"]}</span></span>
            <span>Net area (ha): <span className="popup-value">{data["netArea"]}</span></span>
          </>
        );
      default:
        return (
          <>
            {Object.entries(data)
              .filter(([key]) => key !== "mapKindType")
              .map(([key, value]) => (
                <span key={key}>{`${key}: ${value}`}</span>
              ))}
          </>
        );
    }
  };

  return (
    <div className="map-popup-container">
      <div className="map-popup-header-container">
        {renderPopupHeader(data, openingId)}
      </div>

      <div className="map-popup-details-container">
        {renderPopupContent(data, openingId)}
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
