/**
 * A mapping of silviculture status codes to their corresponding descriptions.
 *
 * @remarks
 * Each key is a short code representing a specific silviculture activity,
 * and the value is a human-readable description of that activity.
 *
 * @example
 * silvicultureStatusCodes["PL"] // returns "Planting"
 */
export const silvicultureStatusCodes: Record<string, string> = {
  AU: "Audit",
  BR: "Brushing",
  CC: "Cone Collection",
  CM: "Crown Modification",
  DN: "Disturbance",
  DS: "Direct Seeding",
  EP: "Experimental Activities",
  FE: "Fertilization",
  HC: "Habitat Connectivity",
  HI: "Habitat Installation",
  JS: "Juvenile Spacing",
  LB: "Land Based Planning",
  PC: "Pest Control",
  PL: "Planting",
  PR: "Pruning",
  RC: "Recreation",
  RD: "Roads",
  SP: "Site Preparation",
  ST: "Species Translocation",
  SU: "Surveys",
  SX: "Silviculture Trials",
};

/**
 * A mapping of disturbance status codes to their corresponding descriptions.
 *
 * @remarks
 * Each key is a short code representing a specific disturbance type,
 * and the value is a human-readable description of that disturbance.
 *
 * @example
 * disturbanceStatusCodes["B"] // returns "Burnt Wildfire"
 */
export const disturbanceStatusCodes: Record<string, string> = {
  B: "Burnt Wildfire",
  C: "Drought",
  D: "Disease",
  E: "Stand Enhancement",
  F: "Flood",
  I: "Slide",
  L: "Logged",
  P: "Pest (beetle)",
  R: "Rehabilitation",
  S: "Salvage",
  W: "Windblow",
};

