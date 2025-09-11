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
      fillColor: '#E94E77',
    },
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW',
        'polygonType': 'Tenure / Cut Block',
        'forestFileId': properties?.CUT_BLOCK_FOREST_FILE_ID,
        'cutBlockId': properties?.CUT_BLOCK_ID,
        'cuttingPermitId': properties?.HARVEST_AUTH_CUTTING_PERMIT_ID,
        'client': `${properties?.CLIENT_NAME} (${properties?.CLIENT_NUMBER})`,
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
      color: 'orange',
      fillColor: '#FFBC99', // A vibrant orange
    },
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW',
        'polygonType': 'Activity and Disturbance',
        'activityId': properties?.ACTIVITY_TREATMENT_UNIT_ID,
        'silvBaseCode': properties?.SILV_BASE_CODE,
        'disturbanceCode': properties?.DISTURBANCE_CODE,
        'area': properties?.ACTUAL_TREATMENT_AREA,
        'endDate': properties?.ATU_COMPLETION_DATE
      }
    }
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW',
    description: 'Forest Cover Inventory',
    style: {
      ...defaultStyle,
      color: 'pink',
    },
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW',
        'polygonType': 'Forest Cover Inventory',
        'forestCoverId': properties?.FOREST_COVER_ID,
        'polygon': properties?.SILV_POLYGON_NUMBER,
        'polygonArea': properties?.SILV_POLYGON_AREA,
        'netArea': properties?.SILV_POLYGON_NET_AREA,
        'referenceYear': properties?.REFERENCE_YEAR
      }
    }
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW',
    description: 'Forest Cover Reserve',
    style: {
      ...defaultStyle,
      color: 'pink',
    },
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW',
        'polygonType': 'Forest Cover Reserve',
        'polygon': properties?.SILV_POLYGON_NO,
        'polygonArea': properties?.SILV_POLYGON_AREA
      }
    }
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW',
    description: 'Forest Cover Silviculture',
    style: {
      ...defaultStyle,
      color: 'pink',
    },
    popup: (properties: GeoJsonProperties): Record<string, any> => {
      return {
        'mapKindType': 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW',
        'polygonType': 'Forest Cover Silviculture',
        'forestCoverId': properties?.FOREST_COVER_ID,
        'polygon': properties?.SILV_POLYGON_NUMBER,
        'polygonArea': properties?.SILV_POLYGON_AREA,
        'netArea': properties?.SILV_POLYGON_NET_AREA,
        'referenceYear': properties?.REFERENCE_YEAR
      }
    }
  },
  {
    code: 'WHSE_FOREST_VEGETATION.RSLT_PLANTING_SVW',
    description: 'Planting',
    style: {
      ...defaultStyle,
      color: 'orange',
      fillColor: '#FFBC99', // A vibrant orange
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

  const isSelected = selectedFeature && feature.id === selectedFeature.id;
  const isHovered = hoveredFeature && feature.id === hoveredFeature.id;
  const showOutline = (isSelected && !hoveredFeature) || isHovered;
  const hoveredOrSelectedColor = "#000000";
  const defaultWeight = 1;

  // Activity Treatment: DN (Disturbance)
  if (
    feature.properties?.SILV_BASE_CODE &&
    feature.properties?.ACTIVITY_TREATMENT_UNIT_ID &&
    feature.properties?.SILV_BASE_CODE === "DN"
  ) {
    return {
      ...defaultStyle,
      color: showOutline ? hoveredOrSelectedColor : outlineColorMap.grey,
      fillColor: colorMap.grey![0],
      weight: showOutline ? kindWeightMap['WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW'] : defaultWeight,
    };
  }

  // Activity Treatment: Non-DN (Activity)
  if (
    feature.properties?.SILV_BASE_CODE &&
    feature.properties?.ACTIVITY_TREATMENT_UNIT_ID
  ) {
    return {
      ...defaultStyle,
      color: showOutline ? hoveredOrSelectedColor : outlineColorMap.orange,
      fillColor: colorMap.orange![0],
      weight: showOutline ? kindWeightMap['WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW'] : defaultWeight,
    };
  }

  const kindEntry = mapKinds.find(
    (kind) => kind.code !== null && String(feature.id).startsWith(kind.code)
  );

  if (!kindEntry) return defaultStyle;
  const colors = colorMap[kindEntry.style.color] || colorMap.default;
  const fillColor = getSpacedColor(colors!, featureIndex ?? 0, totalFeatures ?? colors!.length);
  const outlineColor = outlineColorMap[kindEntry.style.color] || kindEntry.style.color;

  // Highlight if selected or hovered
  if ((isSelected || isHovered)) {
    return {
      ...defaultStyle,
      ...kindEntry.style,
      fillColor,
      color: showOutline ? hoveredOrSelectedColor : outlineColor,
      weight: showOutline ? kindWeightMap[kindEntry.code as MapKindType] || 2 : defaultWeight,
    };
  }

  return {
    ...defaultStyle,
    ...kindEntry.style,
    fillColor,
    color: outlineColor,
    weight: defaultWeight,
  };
}

const colorMap: Record<string, string[]> = {
  blue: ['#9FB3DF', '#9EC6F3', '#BDDDE4', '#FFF1D5'],
  red: [
    '#FFF6F4',
    '#FFE9E4',
    '#FFDCD5',
    '#FFD0C6',
    '#FFC0B3',
    '#FF846B',
    '#FF4822',
    '#D92500',
    '#911900',
    '#480C00',
  ],
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
  orange: ['#FFBC99'],
  yellow: ['#FFF085', '#FCB454', '#FF9B17', '#F16767'],
  pink: [
    '#FFFBFD',
    '#FFF6FA',
    '#FFF1F8',
    '#FFEBF5',
    '#FFE5F2',
    '#FF94CA',
    '#FF44A1',
    '#F20079',
    '#A10051',
    '#510028'
  ],
  cyan: ['#F5F0BB', '#DBDFAA', '#B3C890', '#73A9AD'],
  grey: ['#D2D2D4'],
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
  'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW': 3,
  'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW': 3,
  'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW': 3,
  'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW': 3,
  'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW': 3,
  'WHSE_FOREST_VEGETATION.RSLT_PLANTING_SVW': 3,
  'WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW': 3,
  'WHSE_FOREST_VEGETATION.VEG_COMP_LYR_R1_POLY': 3,
  'WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW': 3,
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
