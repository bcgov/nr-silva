import React from "react";
import tokml from "tokml";

interface OpeningsMapDownloaderProps {
  feature: GeoJSON.FeatureCollection;
}

const OpeningsMapDownloader: React.FC<OpeningsMapDownloaderProps> = ({
  feature,
}) => {
  const generateKmlFile = () => {
    const blob = new Blob([tokml(feature)], {
      type: "application/vnd.google-earth.kml+xml",
    });

    return URL.createObjectURL(blob);
  };

  const generateGeoJsonFile = () => {
    const blob = new Blob([JSON.stringify(feature)], {
      type: "application/json",
    });
    return URL.createObjectURL(blob);
  };

  return (
    <>
      <a href={generateKmlFile()} download="data.kml">
        Download as a KML file
      </a>
      <a href={generateGeoJsonFile()} download="data.geojson">
        Download as a GeoJSON file
      </a>
    </>
  );
};

export default OpeningsMapDownloader;
