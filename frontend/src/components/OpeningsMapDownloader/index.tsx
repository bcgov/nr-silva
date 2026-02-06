import { Download } from "@carbon/icons-react";
import React, { useMemo, useEffect } from "react";
import GeoJSON from "ol/format/GeoJSON";
import KML from "ol/format/KML";

import './styles.scss';

interface OpeningsMapDownloaderProps {
  feature: GeoJSON.FeatureCollection;
}

const getProjection = (feature: GeoJSON.FeatureCollection): string => {
  const crs = (feature as any).crs?.properties?.name;
  if (crs) {
    const crsUpper = crs.toUpperCase();
    const epsgIndex = crsUpper.indexOf("EPSG:");
    if (epsgIndex !== -1) {
      const epsgPart = crs.substring(epsgIndex);
      let code = "";
      for (let i = 5; i < epsgPart.length; i++) {
        const char = epsgPart[i];
        if (char >= "0" && char <= "9") {
          code += char;
        } else {
          break;
        }
      }
      if (code) {
        return `EPSG:${code}`;
      }
    }
  }
  return "EPSG:4326";
};

const OpeningsMapDownloader: React.FC<OpeningsMapDownloaderProps> = ({
  feature,
}) => {
  const kmlFileUrl = useMemo(() => {
    const projection = getProjection(feature);
    const geojsonFmt = new GeoJSON();
    const kmlFmt = new KML();
    const features = geojsonFmt.readFeatures(feature, {
      dataProjection: projection,
      featureProjection: projection,
    });
    const kmlText = kmlFmt.writeFeatures(features, {
      dataProjection: projection,
      featureProjection: projection,
    });
    const blob = new Blob([kmlText], {
      type: "application/vnd.google-earth.kml+xml",
    });
    return URL.createObjectURL(blob);
  }, [feature]);

  const geoJsonFileUrl = useMemo(() => {
    const blob = new Blob([JSON.stringify(feature)], {
      type: "application/json",
    });
    return URL.createObjectURL(blob);
  }, [feature]);

  useEffect(() => {
    return () => {
      URL.revokeObjectURL(kmlFileUrl);
      URL.revokeObjectURL(geoJsonFileUrl);
    };
  }, [kmlFileUrl, geoJsonFileUrl]);

  return (
    <>
      <a href={kmlFileUrl} download="data.kml">
        <div className="link-content">
          Download as a KML file
          <Download />
        </div>

      </a>

      <a href={geoJsonFileUrl} download="data.geojson">
        <div className="link-content">
          Download as a GeoJSON file
          <Download />
        </div>
      </a>
    </>
  );
};

export default OpeningsMapDownloader;
