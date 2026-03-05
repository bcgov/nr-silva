/**
 * Map Kind Constants - Enum-like object for accessing GeoServer map layer kinds
 * These constants represent the various spatial data layers available in the application
 */

export const MAP_KINDS = {
  // Activity Treatment/Disturbance
  activityTreatment: 'WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW',

  // Forest Cover variants
  forestCoverInventory: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_INV_SVW',
  forestCoverReserve: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_RESERVE_SVW',
  forestCoverSilviculture: 'WHSE_FOREST_VEGETATION.RSLT_FOREST_COVER_SILV_SVW',

  // Opening
  opening: 'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW',

  // Planting
  planting: 'WHSE_FOREST_VEGETATION.RSLT_PLANTING_SVW',

  // Standards Unit
  standardsUnit: 'WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW',

  // Vegetation Composition
  vegCompositionLayer: 'WHSE_FOREST_VEGETATION.VEG_COMP_LYR_R1_POLY',

  // Cut Block
  cutBlock: 'WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW',
} as const;

/**
 * Type for map kind key values
 */
export type MapKindKey = typeof MAP_KINDS[keyof typeof MAP_KINDS];
