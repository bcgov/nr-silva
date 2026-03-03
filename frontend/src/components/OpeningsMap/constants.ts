import { MapLayer, MapPositionType } from "@/types/MapLayer";

// Default layers
export const allLayers: MapLayer[] = [
  {
    position: 1,
    name: 'RESULTS - Openings svw',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW',
    transparent: true,
    filterable: true,
    styles: [{ name: '2941_2942', title: 'Openings_ALL_RSLT' }],
  },
  {
    position: 2,
    name: 'RESULTS - Standards Units',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW',
    transparent: true,
    filterable: true,
    styles: [{ name: '2945_2946', title: 'Standards_Unit_Poly_ALL_RSLT' }],
  },
  {
    position: 3,
    name: 'RESULTS - Activity Treatment Units',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW',
    transparent: true,
    filterable: true,
    styles: [{ name: '2937_2938', title: 'Activity_Treatment_Poly_ALL_RSLT' }],
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
    filterable: true,
    styles: [{ name: '4284', title: 'Forest_Cover_Inventory_ALL_RSLT_Colour_Fill' }],
  },
  {
    position: 5,
    name: 'RESULTS - Forest Cover Silviculture',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW',
    transparent: true,
    filterable: true,
    styles: [{ name: '4403', title: 'Forest_Cover_Silviculture_ALL_RSLT_Colour_Fill' }],
  },
  {
    position: 6,
    name: 'VRI - 2023 - Forest Vegetation Composite Rank 1 Layer (R1)',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.VEG_COMP_LYR_R1_POLY',
    transparent: true,
    filterable: false,
    styles: [
      { name: '5324', title: 'Vegetated_Land_Cover_Colour_Themed' },
      { name: '1543', title: 'Non_Vegetated_Land_Cover_Colour_Themed' }
    ],
  },
  {
    position: 7,
    name: 'Fire Burn Severity - Same Year',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.VEG_BURN_SEVERITY_SAME_YR_SP',
    transparent: true,
    filterable: false,
    styles: [{ name: '9792', title: 'Burn_Severity_Rating_2023_Same_Year_Colour_Themed' }],
  },
  {
    position: 8,
    name: 'BC Wildfire Fire Perimeters - Historical',
    format: 'image/png',
    layers: 'WHSE_LAND_AND_NATURAL_RESOURCE.PROT_HISTORICAL_FIRE_POLYS_SP',
    transparent: true,
    filterable: false,
    styles: [
      { name: '1756', title: 'BC_Wildfire_Fire_Perimeters_Historical_Labels' },
      { name: '1758', title: 'BC_Wildfire_Fire_Perimeters_Historical_Colour_Hatched' }
    ],
  },
  {
    position: 9,
    name: 'BEC Map',
    format: 'image/png',
    layers: 'WHSE_FOREST_VEGETATION.BEC_BIOGEOCLIMATIC_POLY',
    transparent: true,
    filterable: false,
    styles: [{ name: '1410', title: 'BEC_Analysis_Zones_Subzones_Variants_Colour_Themed' }],
  },
  {
    position: 10,
    name: 'Natural Resource (NR) Districts',
    format: 'image/png',
    layers: 'WHSE_ADMIN_BOUNDARIES.ADM_NR_DISTRICTS_SPG',
    transparent: true,
    filterable: false,
    styles: [{ name: '365', title: 'Natural_Resource_Districts_Colour_Themed' }],
  },
  {
    position: 11,
    name: 'FADM - BC Timber Sales Area',
    format: 'image/png',
    layers: 'WHSE_ADMIN_BOUNDARIES.FADM_BCTS_AREA_SP',
    transparent: true,
    filterable: false,
    styles: [{ name: '6', title: 'BC_Timber_Sale_Areas_Colour_Filled' }],
  },
  {
    position: 12,
    name: 'FADM - Tree Farm License Current View (TFL)',
    format: 'image/png',
    layers: 'WHSE_ADMIN_BOUNDARIES.FADM_TFL_ALL_SP',
    transparent: true,
    filterable: false,
    styles: [{ name: '6980', title: 'Tree_Farm_Licence_Current_Boundary_Colour_Filled' }],
  },
  {
    position: 13,
    name: 'Forest Tenure Managed Licence',
    format: 'image/png',
    layers: 'WHSE_FOREST_TENURE.FTEN_MANAGED_LICENCE_POLY_SVW',
    transparent: true,
    filterable: false,
    styles: [
      { name: '2891', title: 'Managed_Licence_Poly_Active_FTEN_Colour_Themed' },
      { name: '2893', title: 'Managed_Licence_Poly_Pending_FTEN_Colour_Themed' },
      { name: '2895', title: 'Managed_Licence_Poly_Retired_FTEN_Colour_Themed' }
    ],
  },
  {
    position: 14,
    name: 'ParcelMap BC Parcel Fabric',
    format: 'image/png',
    layers: 'WHSE_CADASTRE.PMBC_PARCEL_FABRIC_POLY_SVW',
    transparent: true,
    filterable: false,
    styles: [{ name: '5162', title: 'Dominion_Coal_Blocks_Colour_Filled' }],
  },
  {
    position: 15,
    name: 'Forest Tenure Road Section Lines',
    format: 'image/png',
    layers: 'WHSE_FOREST_TENURE.FTEN_ROAD_SECTION_LINES_SVW',
    transparent: true,
    filterable: false,
    styles: [{ name: '2864', title: 'All_Forest_Road_Sections_FTEN_Colour_Themed' }],
  },
  {
    position: 16,
    name: 'Digital Road Atlas (DRA) - Master Partially-Attributed Roads',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.DRA_DGTL_ROAD_ATLAS_MPAR_SP',
    transparent: true,
    filterable: false,
    styles: [{ name: '3241_4489', title: 'Digital_Road_Atlas' }],
  },
  {
    position: 17,
    name: 'Waterbodies - TRIM Enhanced Base Map (EBM)',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.TRIM_EBM_WATERBODIES',
    transparent: true,
    filterable: false,
    styles: [{ name: '3370', title: 'Water_Lake_Reservoir_etc_Outlined_20K' }],
  },
  {
    position: 18,
    name: 'Watercourses - TRIM Enhanced Base Map (EBM)',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.TRIM_EBM_WATERCOURSES',
    transparent: true,
    filterable: false,
    styles: [{ name: '3372', title: 'Water_River_Canal_etc_Outlined_20K' }],
  },
  {
    position: 19,
    name: 'Freshwater Atlas Stream Network',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.FWA_STREAM_NETWORKS_SP',
    transparent: true,
    filterable: false,
    styles: [
      { name: '698', title: 'FWA_Stream_Network_Labels' },
      { name: '699', title: 'FWA_Stream_Network_Lines' }
    ],
  },
  {
    position: 20,
    name: 'Freshwater Atlas Rivers',
    format: 'image/png',
    layers: 'WHSE_BASEMAPPING.FWA_RIVERS_POLY',
    transparent: true,
    filterable: false,
    styles: [{ name: '704', title: 'FWA_Rivers_Colour_Filled' }],
  },
  {
    position: 21,
    name: 'Protected Lands Access Restrictions - Points',
    format: 'image/png',
    layers: 'WHSE_PARKS.PA_PRTCTD_LND_FACILITIES_SP',
    transparent: true,
    filterable: false,
    styles: [{ name: '7624', title: 'Protected_Lands_Access_Restrictions_Centre_Points_Provincial_Parks_Eco_Reserves_etc' }],
  },
  {
    position: 22,
    name: 'Forest Tenure Cutblock Polygons (FTA 4.0)',
    format: 'image/png',
    layers: 'WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW',
    transparent: true,
    filterable: false,
    styles: [
      { name: '2840_2841', title: 'All_Forest_Cut_Blocks_FTEN' },
      { name: '2845', title: 'Pending_Forest_Cut_Blocks_FTEN_Colour_Themed' },
      { name: '2842_2843', title: 'Active_Forest_Cut_Blocks_FTEN' },
      { name: '2847', title: 'Retired_Forest_Cut_Blocks_FTEN_Colour_Themed' },
      { name: '2841', title: 'All_Forest_Cut_Blocks_FTEN_Colour_Themed' },
      { name: '2843', title: 'Active_Forest_Cut_Blocks_FTEN_Colour_Themed' },
      { name: '5217', title: 'Cut_Blocks_SP_Exempt_FTEN' },
      { name: '2844_2845', title: 'Pending_Forest_Cut_Blocks_FTEN' },
      { name: '2844', title: 'Pending_Forest_Cut_Blocks_FTEN_Outlined' },
      { name: '2846_2847', title: 'Retired_Forest_Cut_Blocks_FTEN' },
      { name: '2840', title: 'All_Forest_Cut_Blocks_FTEN_Outlined' },
      { name: '2842', title: 'Active_Forest_Cut_Blocks_FTEN_Outlined' },
      { name: '2846', title: 'Retired_Forest_Cut_Blocks_FTEN_Outlined' }
    ],
  }
];

export const defaultLocation: MapPositionType = {
  lat: 51.339506220208065,
  lng: -121.40991210937501,
  zoom: 6,
};
