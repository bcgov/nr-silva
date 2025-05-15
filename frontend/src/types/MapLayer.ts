import CodeDescriptionDto from '@/types/CodeDescriptionType';
import { Feature, FeatureCollection, GeoJsonProperties, Geometry } from 'geojson';
import { PathOptions } from 'leaflet';

export type MapLayer = {
  position: number;
  name: string;
  format: string;
  layers: string;
  transparent: boolean;
  styles: {name: string, title: string}[];
  catalogueUrl: string;
  getCapabilitiesUrl: string;
};

export type BaseMapLayer = {
  name: string;
  url: string;
  attribution: string;
  default: boolean;
};

export type MapKindType = 'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW'
  | 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW'
  | 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW'
  | 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW'
  | 'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW'
  | 'WHSE_FOREST_VEGETATION.RSLT_PLANTING_SVW'
  | 'WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW'
  | 'WHSE_FOREST_VEGETATION.VEG_COMP_LYR_R1_POLY'
  | 'WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW';

export type LayerConfiguration = CodeDescriptionDto<MapKindType> & {
  style: Record<string, any>;
  popup: (properties: GeoJsonProperties) => Record<string,any> 
}

export const defaultStyle = {
  fillColor: "#4A90E2", // A vibrant but professional blue
  color: "#2C3E50", // Darker stroke for contrast
  weight: 2, // Thin stroke for clarity
  opacity: 1, // Full opacity for strong visibility
  fillOpacity: 0.6, // Slight transparency to blend with the map
};

export const mapKinds: LayerConfiguration[] = [
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW',
    description: 'Openings',
    style: {
      ...defaultStyle,
      color: 'blue',
      fillColor: '#4A90E2', // A vibrant but professional blue
    },
    popup: (properties: GeoJsonProperties): Record<string,any> => {
      return {
      'Region': `${properties?.REGION_NAME} (${properties?.REGION_CODE})`,
      'District': `${properties?.DISTRICT_NAME} (${properties?.DISTRICT_CODE})`
      }
    }
  },
  {
    code: 'WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW',
    description: 'Cut Blocks',
    style: {
      ...defaultStyle,
      color: 'red',
      fillColor: '#E94E77', // A vibrant red
    },
    popup: (properties: GeoJsonProperties): Record<string,any> => {
      return{
      'Forest File': properties?.CUT_BLOCK_FOREST_FILE_ID,
      'Cut Block': properties?.CUT_BLOCK_ID,
      'Client': `${properties?.CLIENT_NAME} (${properties?.CLIENT_NUMBER})`,
      'Location': properties?.CLIENT_LOCATION_CODE,
      }
    }
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW',
    description: 'Standards Units',
    style: {
      ...defaultStyle,
      color: 'green',
      fillColor: '#50E3C2', // A vibrant green
    },
    popup: (properties: GeoJsonProperties): Record<string,any> => {
      return {
      'Stocking Standards Id': properties?.STOCKING_STANDARD_UNIT_ID,
      'Standard Units Id': properties?.STANDARDS_UNIT_ID,
      'Net Area (ha)': properties?.NET_AREA,
      }
    }
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW',
    description: 'Activity Treatment Units',
    style: {
      ...defaultStyle,
      color: 'purple',
      fillColor: '#9013FE', // A vibrant purple
    },
    popup: (properties: GeoJsonProperties): Record<string,any> => {
      return{
      'Activity Id': properties?.ACTIVITY_TREATMENT_UNIT_ID,
      'Silviculture base code': properties?.SILV_BASE_CODE
      }
    }
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW',
    description: 'Forest Cover Inventory',
    style: {
      ...defaultStyle,
      color: 'orange',
      fillColor: '#F5A623', // A vibrant orange
    },
    popup: (properties: GeoJsonProperties): Record<string,any> => { return {}}
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW',
    description: 'Forest Cover Reserve',
    style: {
      ...defaultStyle,
      color: 'yellow',
      fillColor: '#F8E71C', // A vibrant yellow
    },
    popup: (properties: GeoJsonProperties): Record<string,any> => { return {}}
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW',
    description: 'Forest Cover Silviculture',
    style: {
      ...defaultStyle,
      color: 'pink',
      fillColor: '#D0021B', // A vibrant pink
    },
    popup: (properties: GeoJsonProperties): Record<string,any> => { return {}}
  },  
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_PLANTING_SVW',
    description: 'Planting',
    style: {
      ...defaultStyle,
      color: 'cyan',
      fillColor: '#50E3C2', // A vibrant cyan
    },
    popup: (properties: GeoJsonProperties): Record<string,any> => { return {}}
  },
] as const;

export type MapPositionType = {
  lat: number;
  lng: number;
  zoom: number;
};

export const getStyleForFeature = (
    feature: Feature<Geometry, any> | undefined
  ): PathOptions =>{
    if (!feature?.id) return defaultStyle;

  const kindEntry = mapKinds.find((kind) =>
    String(feature.id).startsWith(kind.code)
  );

  if (!kindEntry) return defaultStyle;
  const colors = colorMap[kindEntry.style.color] || colorMap.default;
  const fillColor = colors[Math.floor(Math.random() * colors.length)];
  return {
    ...defaultStyle,
    ...kindEntry.style,
    fillColor,
  };
}

const colorMap: Record<string, string[]> = {
  blue: ['#9FB3DF', '#9EC6F3', '#BDDDE4', '#FFF1D5'],
  red: ['#DE5B7B','#ECCFD1','#F0E3C4','#98DED3'],
  green: ['#ACE1AF','#B0EBB4','#BFF6C3','#E0FBE2'],
  purple: ['#BEADFA','#D0BFFF','#DFCCFB','#FFF8C9'],
  orange: ['#FFB38E','#FFCF9D','#FFB26F','#DE8F5F'],
  yellow: ['#FFF085','#FCB454','#FF9B17','#F16767'],
  pink: ['#8F87F1', '#C68EFD', '#E9A5F1', '#FED2E2'],
  cyan: ['#F5F0BB','#DBDFAA','#B3C890','#73A9AD'],
  default: [defaultStyle.fillColor],
}

export const getPropertyForFeature = (feature: Feature<Geometry,GeoJsonProperties>): Record<string,any> => {
  const kindEntry = mapKinds.find((kind) =>
    String(feature.id).startsWith(kind.code)
  );

  if (!kindEntry) return {};

  const properties = feature.properties || {};
  return kindEntry.popup(properties);
}

export const getPopupCenter = (geometry: Geometry): [number, number] => {
  switch (geometry.type) {
    case "Point":
      return geometry.coordinates as [number, number];

    case "LineString":
    case "MultiPoint":
      return averageCoords(geometry.coordinates as [number, number][]);

    case "Polygon":
      // Use the outer ring
      return averageCoords(geometry.coordinates[0] as [number, number][]);

    case "MultiPolygon": {
      // Average of all outer rings
      const allCoords = geometry.coordinates.flatMap(
        (polygon) => polygon[0] // outer ring
      ) as [number, number][];
      return averageCoords(allCoords);
    }

    default:
      console.warn("Unsupported geometry for popup center:", geometry);
      return [0, 0];
  }
}

export const getCenterOfFeatureCollection = (
  collection: FeatureCollection<Geometry>
): [number, number] => {
  const allCoords: [number, number][] = [];

  for (const feature of collection.features) {
    const geom = feature.geometry;

    if (!geom) continue;

    switch (geom.type) {
      case "Point":
        allCoords.push(geom.coordinates as [number, number]);
        break;

      case "MultiPoint":
      case "LineString":
        allCoords.push(...(geom.coordinates as [number, number][]));
        break;

      case "Polygon":
        allCoords.push(...(geom.coordinates[0] as [number, number][])); // outer ring only
        break;

      case "MultiPolygon":
        geom.coordinates.forEach(polygon =>
          allCoords.push(...(polygon[0] as [number, number][])) // outer ring
        );
        break;
    }
  }
  return averageCoords(allCoords);
}

const averageCoords = (coords: [number, number][]): [number, number] => {
  const total = coords.reduce(
    (acc, [lng, lat]) => {
      acc[0] += lat;
      acc[1] += lng;
      return acc;
    },
    [0, 0]
  );

  const count = coords.length || 1;
  return [total[0] / count, total[1] / count];
}
