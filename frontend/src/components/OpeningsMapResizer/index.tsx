import React, { useEffect } from "react";
import { useMap } from "react-leaflet";

const OpeningsMapResizer: React.FC<{ height: number }> = ({ height }) => {
  const map = useMap();

  useEffect(() => {
    // Invalidate the map size on height change
    setTimeout(() => {
      map.invalidateSize();
    }, 100);
  }, [height, map]);

  return null;
};

export default OpeningsMapResizer;
