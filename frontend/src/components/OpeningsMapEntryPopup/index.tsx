import React from "react";
import OpeningsMapDownloader from "@/components/OpeningsMapDownloader";
import { silvicultureStatusCodes } from "@/constants/statucCodes";
import { formatDate, formatLocalDate } from "@/utils/DateUtils";

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
      case "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW":
      case "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW":
      case "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW":
        return <h4 className="map-popup-header">Polygon ID: {data["polygon"]}</h4>;
      case "WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW":
        return <h4 className="map-popup-header">Cut Block: {data["cutBlockId"]}</h4>;
      case "WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW":
        return <h4 className="map-popup-header">{`${silvicultureStatusCodes[data["silvBaseCode"]]} (${data["silvBaseCode"]})`}</h4>;
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
            <span><span className="popup-value">Region:&nbsp;</span>{data["region"]}</span>
            <span><span className="popup-value">District:&nbsp;</span>{data["district"]}</span>
            <span><span className="popup-value">Year created:&nbsp;</span>{data["yearCreated"]}</span>
          </>
        );
      case "WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW":
        return (
          <>
            <span>Opening ID:&nbsp;<span className="popup-value">{openingId}</span></span>
            <span>SSID:&nbsp;<span className="popup-value">{data["ssid"]}</span></span>
            <span>Net area (ha):&nbsp;<span className="popup-value">{data["netArea"]}</span></span>
          </>
        );

      case "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW":
        return (
          <>
            <span>Opening ID:&nbsp;<span className="popup-value">{openingId}</span></span>
            <span>Forest Cover ID:&nbsp;<span className="popup-value">{data["forestCoverId"]}</span></span>
            <span>Reference Year:&nbsp;<span className="popup-value">{data["referenceYear"]}</span></span>
            <span>Net Area (ha):&nbsp;<span className="popup-value">{data["netArea"]}</span></span>
            <span>Polygon Area (ha):&nbsp;<span className="popup-value">{data["polygonArea"]}</span></span>
          </>
        );
      case "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW":
        return (
          <>
            <span>Opening ID:&nbsp;<span className="popup-value">{openingId}</span></span>
            <span>Polygon Area (ha):&nbsp;<span className="popup-value">{data["polygonArea"]}</span></span>
          </>
        );
      case "WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW":
        return (
          <>
            <span>Opening ID:&nbsp;<span className="popup-value">{openingId}</span></span>
            <span>Forest Cover ID:&nbsp;<span className="popup-value">{data["forestCoverId"]}</span></span>
            <span>Reference Year:&nbsp;<span className="popup-value">{data["referenceYear"]}</span></span>
            <span>Net Area (ha):&nbsp;<span className="popup-value">{data["netArea"]}</span></span>
            <span>Polygon Area (ha):&nbsp;<span className="popup-value">{data["polygonArea"]}</span></span>
          </>
        );
      case "WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW":
        return (
          <>
            <span>Opening ID:&nbsp;<span className="popup-value">{openingId}</span></span>
            <span>Forest File:&nbsp;<span className="popup-value">{data["forestFileId"]}</span></span>
            <span>Cutting Permit:&nbsp;<span className="popup-value">{data["cuttingPermitId"]}</span></span>
            <span>Client:&nbsp;<span className="popup-value">{data["client"]}</span></span>
          </>
        );
      case "WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW":
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
