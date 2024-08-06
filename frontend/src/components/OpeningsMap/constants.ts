import { MapLayer } from "../../types/MapLayer";

// Default layers
export const allLayers: MapLayer[] = [
  // #1 
  {
    name: 'RESULTS - Openings svw',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW',
    transparent: true,
    styles: [{name: '2942', title: 'Openings_ALL_RSLT_Colour_Filled'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/results-openings-svw',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW/ows?service=WMS&request=GetCapabilities'
  },
  
  // #2 
  {
    name: 'RESULTS - Standards Units',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW',
    transparent: true,
    styles: [{name: '2946', title: 'Standards_Unit_Poly_ALL_RSLT_Colour_Filled'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/results-standards-units',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW/ows?service=WMS&request=GetCapabilities'
  },
  
  // #3 
  {
    name: 'RESULTS - Activity Treatment Units',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW',
    transparent: true,
    styles: [{name: '2938', title: 'Activity_Treatment_Poly_ALL_RSLT_Colour_Filled'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/results-activity-treatment-units',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW/ows?service=WMS&request=GetCapabilities'
  },

  // #4 https://catalogue.data.gov.bc.ca/dataset/silviculture-investments
  // #4 FOREST_VEGETATION.RSLT_SILV_INVESTMENT_SP - No get capabilities !?
  
  // #5 
  {
    name: 'RESULTS - Forest Cover Inventory',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW',
    transparent: true,
    styles: [{name: '4284', title: 'Forest_Cover_Inventory_ALL_RSLT_Colour_Fill'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/results-forest-cover-inventory',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW/ows?service=WMS&request=GetCapabilities'
  },

  // #6 
  {
    name: 'RESULTS - Forest Cover Silviculture',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW',
    transparent: true,
    styles: [{name: '4403', title: 'Forest_Cover_Silviculture_ALL_RSLT_Colour_Fill'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/results-forest-cover-silviculture',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW/ows?service=WMS&request=GetCapabilities'
  },

  // #7 FOREST_VEGETATION.VEG_COMP_LYR_R1_POLY
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },

  // #8 FOREST_VEGETATION.VEG_BURN_SEVERITY_SP
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },

  // #9 FOREST_VEGETATION.VEG_BURN_SEVERITY_SAME_YR_SP 
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },
  
  // #10 LAND_AND_NATURAL_RESOURCE.PROT_HISTORICAL_FIRE_POLYS_SP
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },
  
  // #11 FOREST_VEGETATION.BEC_BIOGEOCLIMATIC_POLY
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },
  
  // #12 WHSE_ADMIN_BOUNDARIES.ADM_NR_DISTRICTS_SPG
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },
  
  // #13 ADMIN_BOUNDARIES.FADM_BCTS_AREA_SP 
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },
  
  // #14 ADMIN_BOUNDARIES.FADM_TFL_ALL_SP
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },
  
  // #15 WHSE_FOREST_TENURE.FTEN_MANAGED_LICENCE_POLY_SVW
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },
  
  // #16 WHSE_CADASTRE.PMBC_PARCEL_FABRIC_POLY_SVW
  {
    name: 'ParcelMap BC Parcel Fabric',
    format: 'image/png',
    layers: 'WHSE_CADASTRE.PMBC_PARCEL_FABRIC_POLY_SVW',
    transparent: true,
    styles: [{name: '5162', title: 'Dominion_Coal_Blocks_Colour_Filled'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/parcelmap-bc-parcel-fabric',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_CADASTRE.PMBC_PARCEL_FABRIC_POLY_SVW/ows?service=WMS&request=GetCapabilities'
  },
  
  // #17 WHSE_CADASTRE.PMBC_PARCEL_FABRIC_POLY_FA_SVW - not found
  // {
  //   name: '',
  //   format: 'image/png',
  //   layers: '',
  //   transparent: true,
  //   styles: [{name: '', title: ''}],
  //   catalogueUrl: '',
  //   getCapabilitiesUrl: ''
  // },

  // #18
  {
    name: 'Forest Tenure Road Section Lines',
    format: 'image/png',
    layers: 'WHSE_FOREST_TENURE.FTEN_ROAD_SECTION_LINES_SVW',
    transparent: true,
    styles: [{name: '2864', title: 'All_Forest_Road_Sections_FTEN_Colour_Themed'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/forest-tenure-road-section-lines',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_FOREST_TENURE.FTEN_ROAD_SECTION_LINES_SVW/ows?service=WMS&request=GetCapabilities'
  },
 
  // # 19 WHSE_FOREST_TENURES.FOM_CUTLOCKS.SP 
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },
  
  // # 20 WHSE_FOREST_TENURES.FOM_ROAD_SECTION.SP 
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },
  
  // # 21 WHSE_FOREST_TENURE.FOM_WLDLFE_TREE_RETNTN_AREA_SP
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },
  
  // # 22 WHSE_BASEMAPPING.DRA_DGTL_ROAD_ATLAS_MPAR_SP
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  },
  
  // # 23
  {
    name: 'Waterbodies - TRIM Enhanced Base Map (EBM)',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.TRIM_EBM_WATERBODIES',
    transparent: true,
    styles: [{name: '3370', title: 'Water_Lake_Reservoir_etc_Outlined_20K'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/waterbodies-trim-enhanced-base-map-ebm',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_BASEMAPPING.TRIM_EBM_WATERBODIES/ows?service=WMS&request=GetCapabilities'
  },
  
  // # 24
  {
    name: 'Watercourses - TRIM Enhanced Base Map (EBM)',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.TRIM_EBM_WATERCOURSES',
    transparent: true,
    styles: [{name: '3372', title: 'Water_River_Canal_etc_Outlined_20K'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/watercourses-trim-enhanced-base-map-ebm',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_BASEMAPPING.TRIM_EBM_WATERCOURSES/ows?service=WMS&request=GetCapabilities'
  },
  
  // # 25
  {
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
  
  // # 26
  {
    name: 'Freshwater Atlas Rivers',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.FWA_RIVERS_POLY',
    transparent: true,
    styles: [{name: '704', title: 'FWA_Rivers_Colour_Filled'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/freshwater-atlas-rivers',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_BASEMAPPING.FWA_RIVERS_POLY/ows?service=WMS&request=GetCapabilities'
  },
  
  // # 27
  {
    name: 'Protected Lands Access Restrictions - Points',
    format: 'image/png',
    layers: 'WHSE_PARKS.PA_PRTCTD_LND_FACILITIES_SP',
    transparent: true,
    styles: [{name: '7624', title: 'Protected_Lands_Access_Restrictions_Centre_Points_Provincial_Parks_Eco_Reserves_etc'}],
    catalogueUrl: 'https://catalogue.data.gov.bc.ca/dataset/protected-lands-access-restrictions-points',
    getCapabilitiesUrl: 'https://openmaps.gov.bc.ca/geo/pub/WHSE_PARKS.PA_PRTCTD_LND_FACILITIES_SP/ows?service=WMS&request=GetCapabilities'
  },
  
  // # 28 WHSE_TANTALIS.TA_PROTECTED_LANDS_SVW
  {
    name: '',
    format: 'image/png',
    layers: '',
    transparent: true,
    styles: [{name: '', title: ''}],
    catalogueUrl: '',
    getCapabilitiesUrl: ''
  }
];
