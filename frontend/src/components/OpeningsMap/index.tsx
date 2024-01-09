import React, { useRef } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";


interface MapProps {
  selectedBasemap: any;
}

const OpeningsMap: React.FC<MapProps> = ({ selectedBasemap }) => {
  const position: [number, number] = [49.11941257871176, -122.83461402566606];
  const lastClickedLayerRef = useRef<any>(null); // Replace 'any' with the specific type if known

  return (
    <MapContainer
      center={position} 
      zoom={13}
      style={{ height: "400px", width: "100%" }}
    >
      <TileLayer
        url={selectedBasemap.url}
      />
      <Marker position={position}>
        <Popup>
          A pretty CSS3 popup. <br /> Easily customizable. This is a fixed Marker on Map load
        </Popup>
      </Marker>
    </MapContainer>
  );
};

export default OpeningsMap;
