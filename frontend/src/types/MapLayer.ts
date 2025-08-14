import { CodeDescriptionDto } from "@/services/OpenApi";
import { Feature, FeatureCollection, GeoJsonProperties, Geometry } from 'geojson';
import { PathOptions } from 'leaflet';
import { extractYearFromDateString } from "@/utils/DateUtils";

export type MapLayer = {
  position: number;
  name: string;
  format: string;
  layers: string;
  transparent: boolean;
  styles: { name: string, title: string }[];
  catalogueUrl: string;
  getCapabilitiesUrl: string;
  filterable: boolean;
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

export type LayerConfiguration = CodeDescriptionDto & {
  style: Record<string, any>;
  popup: (properties: GeoJsonProperties) => Record<string, any>
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
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW',
        'region': `${properties?.REGION_NAME} (${properties?.REGION_CODE})`,
        'district': `${properties?.DISTRICT_NAME} (${properties?.DISTRICT_CODE})`,
        'yearCreated': `${extractYearFromDateString(properties?.OPENING_WHEN_CREATED)}`,
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
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW',
        'Polygon type': 'Tenure / Cut Block',
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
      color: 'purple',
      fillColor: '#9013FE',
    },
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW',
        'polygonType': 'Standard Unit',
        'ssid': properties?.STOCKING_STANDARD_UNIT_ID,
        'standardUnitId': properties?.STANDARDS_UNIT_ID,
        'netArea': properties?.NET_AREA,
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
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW',
        'Polygon type': 'Activity',
        'Activity Id': properties?.ACTIVITY_TREATMENT_UNIT_ID,
        'Silviculture base code': properties?.SILV_BASE_CODE,
        'Map Label': properties?.MAP_LABEL
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
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW',
        'Polygon type': 'Forest Cover Inventory',
        'Forest Cover Id': properties?.FOREST_COVER_ID,
        'Polygon': properties?.SILV_POLYGON_NUMBER,
        'Polygon Area (ha)': properties?.SILV_POLYGON_AREA,
        'Net Area (ha)': properties?.SILV_POLYGON_NET_AREA,
        'Reference Year': properties?.REFERENCE_YEAR
      }
    }
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW',
    description: 'Forest Cover Reserve',
    style: {
      ...defaultStyle,
      color: 'yellow',
      fillColor: '#F8E71C', // A vibrant yellow
    },
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW',
        'Polygon type': 'Forest Cover Reserve',
        'Polygon': properties?.SILV_POLYGON_NO,
        'Polygon Area (ha)': properties?.SILV_POLYGON_AREA
      }
    }
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW',
    description: 'Forest Cover Silviculture',
    style: {
      ...defaultStyle,
      color: 'pink',
      fillColor: '#D0021B', // A vibrant pink
    },
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW',
        'Polygon type': 'Forest Cover Silviculture',
        'Forest Cover Id': properties?.FOREST_COVER_ID,
        'Polygon': properties?.SILV_POLYGON_NUMBER,
        'Polygon Area (ha)': properties?.SILV_POLYGON_AREA,
        'Net Area (ha)': properties?.SILV_POLYGON_NET_AREA,
        'Non Mapped Area (ha)': properties?.SILV_NON_MAPPED_AREA,
        'Reference Year': properties?.REFERENCE_YEAR
      }
    }
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_PLANTING_SVW',
    description: 'Planting',
    style: {
      ...defaultStyle,
      color: 'cyan',
      fillColor: '#50E3C2', // A vibrant cyan
    },
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_VEGETATION.RSLT_PLANTING_SVW',
        'Polygon type': 'Activity: Planting',
        'Activity Treatment Unit Id': properties?.ACTIVITY_TREATMENT_UNIT_ID,
        'Map Label': properties?.MAP_LABEL,
        'Silviculture Base Code': properties?.SILV_BASE_CODE,
        'Planting Results Seq Number': properties?.PLANTING_RESULTS_SEQ_NUMBER,
        'Silviculture Tree Species Code': properties?.SILV_TREE_SPECIES_CODE
      }
    }
  },
] as const;

export type MapPositionType = {
  lat: number;
  lng: number;
  zoom: number;
};

const getDeterministicColor = (colors: string[], featureId: string | number): string => {
  const str = String(featureId);
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash += str.charCodeAt(i);
  }
  return colors[hash % colors.length]!;
}

const getSpacedColor = (colors: string[], index: number, total: number): string => {
  if (total <= 1) return colors[0]!;
  const colorIdx = Math.round((index / (total - 1)) * (colors.length - 1));
  return colors[colorIdx]!;
};

export const getStyleForFeature = (
  feature: Feature<Geometry, any> | undefined,
  selectedFeature?: Feature<Geometry, any> | null,
  hoveredFeature?: Feature<Geometry, any> | null,
  featureIndex?: number,
  totalFeatures?: number,
): PathOptions => {
  if (!feature?.id) return defaultStyle;

  const kindEntry = mapKinds.find(
    (kind) => kind.code !== null && String(feature.id).startsWith(kind.code)
  );

  if (!kindEntry) return defaultStyle;
  const colors = colorMap[kindEntry.style.color] || colorMap.default;
  const fillColor = getSpacedColor(colors!, featureIndex ?? 0, totalFeatures ?? colors!.length);
  const outlineColor = outlineColorMap[kindEntry.style.color] || kindEntry.style.color;

  // Highlight if selected or hovered
  if ((selectedFeature && feature.id === selectedFeature.id) || (hoveredFeature && feature.id === hoveredFeature.id)) {
    return {
      ...defaultStyle,
      ...kindEntry.style,
      fillColor,
      color: "#000000",
      weight: kindWeightMap[kindEntry.code as MapKindType] || 2,
    };
  }

  return {
    ...defaultStyle,
    ...kindEntry.style,
    fillColor,
    color: outlineColor
  };
}

const colorMap: Record<string, string[]> = {
  blue: ['#9FB3DF', '#9EC6F3', '#BDDDE4', '#FFF1D5'],
  red: ['#DE5B7B', '#ECCFD1', '#F0E3C4', '#98DED3'],
  green: ['#ACE1AF', '#B0EBB4', '#BFF6C3', '#E0FBE2'],
  purple: [
    '#fcfbfd',
    '#efedf5',
    '#dadaeb',
    '#bcbddc',
    '#9e9ac8',
    '#807dba',
    '#6a51a3',
    '#54278f',
    '#3f007d'
  ],
  orange: ['#FFB38E', '#FFCF9D', '#FFB26F', '#DE8F5F'],
  yellow: ['#FFF085', '#FCB454', '#FF9B17', '#F16767'],
  pink: ['#8F87F1', '#C68EFD', '#E9A5F1', '#FED2E2'],
  cyan: ['#F5F0BB', '#DBDFAA', '#B3C890', '#73A9AD'],
  default: [defaultStyle.fillColor],
}

const outlineColorMap: Record<string, string> = {
  blue: '#005CB8',
  red: '#801701',
  purple: '#6202C5',
  orange: '#E64F02',
  pink: '#B3035C',
  grey: '#939395',
};

export const kindWeightMap: Record<MapKindType, number> = {
  'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW': 2,
  'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW': 2,
  'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW': 2,
  'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW': 2,
  'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW': 3,
  'WHSE_FOREST_VEGETATION.RSLT_PLANTING_SVW': 2,
  'WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW': 2,
  'WHSE_FOREST_VEGETATION.VEG_COMP_LYR_R1_POLY': 2,
  'WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW': 2,
};

export const getPropertyForFeature = (feature: Feature<Geometry, GeoJsonProperties>): Record<string, any> => {
  const kindEntry = mapKinds.find(
    (kind) => !!kind.code && String(feature.id).startsWith(kind.code)
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
