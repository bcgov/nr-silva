import { Download } from "@carbon/icons-react";
import React, { useMemo, useEffect } from "react";
import tokml from "tokml";

import './styles.scss';

interface OpeningsMapDownloaderProps {
  feature: GeoJSON.FeatureCollection;
}

const OpeningsMapDownloader: React.FC<OpeningsMapDownloaderProps> = ({
  feature,
}) => {
  const kmlFileUrl = useMemo(() => {
    const blob = new Blob([tokml(feature)], {
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
