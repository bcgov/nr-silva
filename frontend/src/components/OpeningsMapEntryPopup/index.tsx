import React from "react";
import OpeningsMapDownloader from "@/components/OpeningsMapDownloader";
import { MAP_KINDS } from "@/constants/mapKindConstants";
import { silvicultureStatusCodes } from "@/constants/statusCodes";
import { formatLocalDate } from "@/utils/DateUtils";

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
      case MAP_KINDS.opening:
        return <h4 className="map-popup-header">Opening ID: {openingId}</h4>;
      case MAP_KINDS.standardsUnit:
        return <h4 className="map-popup-header">Standards Unit: {data["standardUnitId"]}</h4>;
      case MAP_KINDS.forestCoverInventory:
      case MAP_KINDS.forestCoverReserve:
      case MAP_KINDS.forestCoverSilviculture:
        return <h4 className="map-popup-header">Polygon ID: {data["polygon"]}</h4>;
      case MAP_KINDS.cutBlock:
        return <h4 className="map-popup-header">Cut Block: {data["cutBlockId"]}</h4>;
      case MAP_KINDS.activityTreatment:
        return <h4 className="map-popup-header">{`${silvicultureStatusCodes[data["silvBaseCode"]]} (${data["silvBaseCode"]})`}</h4>;
      default:
        return <h4 className="map-popup-header">Opening ID: {openingId}</h4>;
    }
  };

  const renderPopupContent = (data?: Record<string, any>, openingId?: number) => {
    if (!data) return null;
    switch (data.mapKindType) {
      case MAP_KINDS.opening:
        return (
          <>
            <span><span className="popup-value">Region:&nbsp;</span>{data["region"]}</span>
            <span><span className="popup-value">District:&nbsp;</span>{data["district"]}</span>
            <span><span className="popup-value">Year created:&nbsp;</span>{data["yearCreated"]}</span>
          </>
        );
      case MAP_KINDS.standardsUnit:
        return (
          <>
            <span>Opening ID:&nbsp;<span className="popup-value">{openingId}</span></span>
            <span>SSID:&nbsp;<span className="popup-value">{data["ssid"]}</span></span>
            <span>Net area (ha):&nbsp;<span className="popup-value">{data["netArea"]}</span></span>
          </>
        );

      case MAP_KINDS.forestCoverInventory:
        return (
          <>
            <span>Opening ID:&nbsp;<span className="popup-value">{openingId}</span></span>
            <span>Forest Cover ID:&nbsp;<span className="popup-value">{data["forestCoverId"]}</span></span>
            <span>Reference Year:&nbsp;<span className="popup-value">{data["referenceYear"]}</span></span>
            <span>Net Area (ha):&nbsp;<span className="popup-value">{data["netArea"]}</span></span>
            <span>Polygon Area (ha):&nbsp;<span className="popup-value">{data["polygonArea"]}</span></span>
          </>
        );
      case MAP_KINDS.forestCoverReserve:
        return (
          <>
            <span>Opening ID:&nbsp;<span className="popup-value">{openingId}</span></span>
            <span>Polygon Area (ha):&nbsp;<span className="popup-value">{data["polygonArea"]}</span></span>
          </>
        );
      case MAP_KINDS.forestCoverSilviculture:
        return (
          <>
            <span>Opening ID:&nbsp;<span className="popup-value">{openingId}</span></span>
            <span>Forest Cover ID:&nbsp;<span className="popup-value">{data["forestCoverId"]}</span></span>
            <span>Reference Year:&nbsp;<span className="popup-value">{data["referenceYear"]}</span></span>
            <span>Net Area (ha):&nbsp;<span className="popup-value">{data["netArea"]}</span></span>
            <span>Polygon Area (ha):&nbsp;<span className="popup-value">{data["polygonArea"]}</span></span>
          </>
        );
      case MAP_KINDS.cutBlock:
        return (
          <>
            <span>Opening ID:&nbsp;<span className="popup-value">{openingId}</span></span>
            <span>Forest File:&nbsp;<span className="popup-value">{data["forestFileId"]}</span></span>
            <span>Cutting Permit:&nbsp;<span className="popup-value">{data["cuttingPermitId"]}</span></span>
            <span>Client:&nbsp;<span className="popup-value">{data["client"]}</span></span>
          </>
        );
      case MAP_KINDS.activityTreatment:
        return (
          <>
            <span>Opening ID:&nbsp;<span className="popup-value">{openingId}</span></span>
            <span>Activity ID:&nbsp;<span className="popup-value">{data["activityId"]}</span></span>
            <span>{data["silvBaseCode"] === "DN" ? "Disturbance area (ha)" : "Treatment area (ha)"}:&nbsp;
              <span className="popup-value">{data["area"]}</span>
            </span>
            <span>End Date:&nbsp;<span className="popup-value">{formatLocalDate(data["endDate"])}</span></span>
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
