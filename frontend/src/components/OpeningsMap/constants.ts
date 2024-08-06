import { MapLayer } from "../../types/MapLayer";

// Default layers
export const allLayers: MapLayer[] = [
  // https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW/ows?service=WMS&request=GetCapabilities
  {
    name: 'RESULTS - Openings svw',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW',
    transparent: true,
    styles: '2942'
  },
  // https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_TENURE.FTEN_ROAD_SECTION_LINES_SVW/ows?service=WMS&request=GetCapabilities
  {
    name: 'Forest Tenure Road Section Lines',
    format: 'image/png',
    layers: 'WHSE_FOREST_TENURE.FTEN_ROAD_SECTION_LINES_SVW',
    transparent: true,
    styles: '2864'
  }
];
