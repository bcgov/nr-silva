import { BaseMapLayer, MapLayer } from "../../types/MapLayer";

// Default layers
export const allLayers: MapLayer[] = [
  {
    position: 1,
    name: 'RESULTS - Openings svw',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW',
    transparent: true,
    styles: [{name: '2941_2942', title: 'Openings_ALL_RSLT'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/results-openings-svw',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 2,
    name: 'RESULTS - Standards Units',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW',
    transparent: true,
    styles: [{name: '2945_2946', title: 'Standards_Unit_Poly_ALL_RSLT'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/results-standards-units',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 3,
    name: 'RESULTS - Activity Treatment Units',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW',
    transparent: true,
    styles: [{name: '2937_2938', title: 'Activity_Treatment_Poly_ALL_RSLT'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/results-activity-treatment-units',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW/ows?service=WMS&request=GetCapabilities'
  },

  // # FOREST_VEGETATION.RSLT_SILV_INVESTMENT_SP - No get capabilities !?
  // # WHSE_FOREST_TENURES.FOM_ROAD_SECTION.SP - No get capabilities!
  // # WHSE_FOREST_TENURE.FOM_WLDLFE_TREE_RETNTN_AREA_SP - No get capabilities!
  // # WHSE_FOREST_TENURES.FOM_CUTLOCKS.SP - not found!
  // # WHSE_CADASTRE.PMBC_PARCEL_FABRIC_POLY_FA_SVW - not found!
  // # WHSE_TANTALIS.TA_PROTECTED_LANDS_SVW - No exact match, which one to use?
  
  {
    position: 4,
    name: 'RESULTS - Forest Cover Inventory',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW',
    transparent: true,
    styles: [{name: '4284', title: 'Forest_Cover_Inventory_ALL_RSLT_Colour_Fill'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/results-forest-cover-inventory',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 5,
    name: 'RESULTS - Forest Cover Silviculture',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW',
    transparent: true,
    styles: [{name: '4403', title: 'Forest_Cover_Silviculture_ALL_RSLT_Colour_Fill'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/results-forest-cover-silviculture',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 6,
    name: 'VRI - 2023 - Forest Vegetation Composite Rank 1 Layer (R1)',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.VEG_COMP_LYR_R1_POLY',
    transparent: true,
    styles: [
      {name: '5324', title: 'Vegetated_Land_Cover_Colour_Themed'},
      {name: '1543', title: 'Non_Vegetated_Land_Cover_Colour_Themed'}
    ],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/vri-2023-forest-vegetation-composite-rank-1-layer-r1-',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.VEG_COMP_LYR_R1_POLY/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 7,
    name: 'Fire Burn Severity - Same Year',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.VEG_BURN_SEVERITY_SAME_YR_SP',
    transparent: true,
    styles: [{name: '9792', title: 'Burn_Severity_Rating_2023_Same_Year_Colour_Themed'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/fire-burn-severity-same-year',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.VEG_BURN_SEVERITY_SAME_YR_SP/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 8,
    name: 'BC Wildfire Fire Perimeters - Historical',
    format: 'image/png',
    layers: 'WHSE_LAND_AND_NATURAL_RESOURCE.PROT_HISTORICAL_FIRE_POLYS_SP',
    transparent: true,
    styles: [
      {name: '1756', title: 'BC_Wildfire_Fire_Perimeters_Historical_Labels'},
      {name: '1758', title: 'BC_Wildfire_Fire_Perimeters_Historical_Colour_Hatched'}
    ],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/bc-wildfire-fire-perimeters-historical',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_LAND_AND_NATURAL_RESOURCE.PROT_HISTORICAL_FIRE_POLYS_SP/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 9,
    name: 'BEC Map',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.BEC_BIOGEOCLIMATIC_POLY',
    transparent: true,
    styles: [{name: '1410', title: 'BEC_Analysis_Zones_Subzones_Variants_Colour_Themed'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/bec-map',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.BEC_BIOGEOCLIMATIC_POLY/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 10,
    name: 'Natural Resource (NR) Districts',
    format: 'image/png',
    layers: 'WHSE_ADMIN_BOUNDARIES.ADM_NR_DISTRICTS_SPG',
    transparent: true,
    styles: [{name: '365', title: 'Natural_Resource_Districts_Colour_Themed'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/natural-resource-nr-district',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_ADMIN_BOUNDARIES.ADM_NR_DISTRICTS_SPG/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 11,
    name: 'FADM - BC Timber Sales Area',
    format: 'image/png',
    layers: 'WHSE_ADMIN_BOUNDARIES.FADM_BCTS_AREA_SP',
    transparent: true,
    styles: [{name: '6', title: 'BC_Timber_Sale_Areas_Colour_Filled'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/fadm-bc-timber-sales-area',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_ADMIN_BOUNDARIES.FADM_BCTS_AREA_SP/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 12,
    name: 'FADM - Tree Farm License Current View (TFL)',
    format: 'image/png',
    layers: 'WHSE_ADMIN_BOUNDARIES.FADM_TFL_ALL_SP',
    transparent: true,
    styles: [{name: '6980', title: 'Tree_Farm_Licence_Current_Boundary_Colour_Filled'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/fadm-tree-farm-license-current-view-tfl-',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_ADMIN_BOUNDARIES.FADM_TFL_ALL_SP/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 13,
    name: 'Forest Tenure Managed Licence',
    format: 'image/png',
    layers: 'WHSE_FOREST_TENURE.FTEN_MANAGED_LICENCE_POLY_SVW',
    transparent: true,
    styles: [
      {name: '2891', title: 'Managed_Licence_Poly_Active_FTEN_Colour_Themed'},
      {name: '2893', title: 'Managed_Licence_Poly_Pending_FTEN_Colour_Themed'},
      {name: '2895', title: 'Managed_Licence_Poly_Retired_FTEN_Colour_Themed'}
    ],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/forest-tenure-managed-licence',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_TENURE.FTEN_MANAGED_LICENCE_POLY_SVW/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 14,
    name: 'ParcelMap BC Parcel Fabric',
    format: 'image/png',
    layers: 'WHSE_CADASTRE.PMBC_PARCEL_FABRIC_POLY_SVW',
    transparent: true,
    styles: [{name: '5162', title: 'Dominion_Coal_Blocks_Colour_Filled'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/parcelmap-bc-parcel-fabric',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_CADASTRE.PMBC_PARCEL_FABRIC_POLY_SVW/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 15,
    name: 'Forest Tenure Road Section Lines',
    format: 'image/png',
    layers: 'WHSE_FOREST_TENURE.FTEN_ROAD_SECTION_LINES_SVW',
    transparent: true,
    styles: [{name: '2864', title: 'All_Forest_Road_Sections_FTEN_Colour_Themed'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/forest-tenure-road-section-lines',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_TENURE.FTEN_ROAD_SECTION_LINES_SVW/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 16,
    name: 'Digital Road Atlas (DRA) - Master Partially-Attributed Roads',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.DRA_DGTL_ROAD_ATLAS_MPAR_SP',
    transparent: true,
    styles: [{name: '3241_4489', title: 'Digital_Road_Atlas'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/digital-road-atlas-dra-master-partially-attributed-roads',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_BASEMAPPING.DRA_DGTL_ROAD_ATLAS_MPAR_SP/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 17,
    name: 'Waterbodies - TRIM Enhanced Base Map (EBM)',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.TRIM_EBM_WATERBODIES',
    transparent: true,
    styles: [{name: '3370', title: 'Water_Lake_Reservoir_etc_Outlined_20K'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/waterbodies-trim-enhanced-base-map-ebm',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_BASEMAPPING.TRIM_EBM_WATERBODIES/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 18,
    name: 'Watercourses - TRIM Enhanced Base Map (EBM)',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.TRIM_EBM_WATERCOURSES',
    transparent: true,
    styles: [{name: '3372', title: 'Water_River_Canal_etc_Outlined_20K'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/watercourses-trim-enhanced-base-map-ebm',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_BASEMAPPING.TRIM_EBM_WATERCOURSES/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 19,
    name: 'Freshwater Atlas Stream Network',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.FWA_STREAM_NETWORKS_SP',
    transparent: true,
    styles: [
      {name: '698', title: 'FWA_Stream_Network_Labels'},
      {name: '699', title: 'FWA_Stream_Network_Lines'}
    ],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/freshwater-atlas-stream-network',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_BASEMAPPING.FWA_STREAM_NETWORKS_SP/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 20,
    name: 'Freshwater Atlas Rivers',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.FWA_RIVERS_POLY',
    transparent: true,
    styles: [{name: '704', title: 'FWA_Rivers_Colour_Filled'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/freshwater-atlas-rivers',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_BASEMAPPING.FWA_RIVERS_POLY/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 21,
    name: 'Protected Lands Access Restrictions - Points',
    format: 'image/png',
    layers: 'WHSE_PARKS.PA_PRTCTD_LND_FACILITIES_SP',
    transparent: true,
    styles: [{name: '7624', title: 'Protected_Lands_Access_Restrictions_Centre_Points_Provincial_Parks_Eco_Reserves_etc'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/protected-lands-access-restrictions-points',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_PARKS.PA_PRTCTD_LND_FACILITIES_SP/ows?service=WMS&request=GetCapabilities'
  },
  {
    position: 22,
    name: 'Forest Tenure Cutblock Polygons (FTA 4.0)',
    format: 'image/png',
    layers: 'WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW',
    transparent: true,
    styles: [
      {name: '2840_2841', title: 'All_Forest_Cut_Blocks_FTEN'},
      {name: '2845', title: 'Pending_Forest_Cut_Blocks_FTEN_Colour_Themed'},
      {name: '2842_2843', title: 'Active_Forest_Cut_Blocks_FTEN'},
      {name: '2847', title: 'Retired_Forest_Cut_Blocks_FTEN_Colour_Themed'},
      {name: '2841', title: 'All_Forest_Cut_Blocks_FTEN_Colour_Themed'},
      {name: '2843', title: 'Active_Forest_Cut_Blocks_FTEN_Colour_Themed'},
      {name: '5217', title: 'Cut_Blocks_SP_Exempt_FTEN'},
      {name: '2844_2845', title: 'Pending_Forest_Cut_Blocks_FTEN'},
      {name: '2844', title: 'Pending_Forest_Cut_Blocks_FTEN_Outlined'},
      {name: '2846_2847', title: 'Retired_Forest_Cut_Blocks_FTEN'},
      {name: '2840', title: 'All_Forest_Cut_Blocks_FTEN_Outlined'},
      {name: '2842', title: 'Active_Forest_Cut_Blocks_FTEN_Outlined'},
      {name: '2846', title: 'Retired_Forest_Cut_Blocks_FTEN_Outlined'}
    ],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/protected-lands-access-restrictions-points',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW/ows?service=WMS&request=GetCapabilities'
  }
];

// Default base maps
export const allBaseMaps: BaseMapLayer[] = [
  {
    name: 'Satellite',
    url: 'https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
    attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community',
    default: false
  },
  {
    name: 'Topographic',
    url: 'https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}',
    attribution: 'Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ, TomTom, Intermap, iPC, USGS, FAO, NPS, NRCAN, GeoBase, Kadaster NL, Ordnance Survey, Esri Japan, METI, Esri China (Hong Kong), and the GIS User Community',
    default: true
  },
  {
    name: 'National Geographic',
    url: 'https://server.arcgisonline.com/ArcGIS/rest/services/NatGeo_World_Map/MapServer/tile/{z}/{y}/{x}',
    attribution: 'Tiles &copy; Esri &mdash; National Geographic, Esri, DeLorme, NAVTEQ, UNEP-WCMC, USGS, NASA, ESA, METI, NRCAN, GEBCO, NOAA, iPC',
    default: false
  },
  {
    name: 'BC Web Mercator',
    url: 'https://maps.gov.bc.ca/arcgis/rest/services/province/web_mercator_cache/MapServer/tile/{z}/{y}/{x}',
    attribution: 'GeoBC, DataBC, TomTom, &copy; OpenStreetMap contributors',
    default: false
  },
  {
    name: 'Open Street Maps',
    url: 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    default: false
  }
];
