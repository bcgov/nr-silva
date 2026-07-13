/**
 * URL search parameter keys used for deep-linking into Opening Details sections.
 * These are appended to the opening URL by search result links and read by
 * detail page tab components to determine which item to expand and scroll to.
 */
export const DEEP_LINK_PARAMS = {
  /** Standards unit ID within the Standards Units tab */
  ssuId: "ssuId",
  /** Disturbance activity treatment unit ID within the Activities tab */
  disturbanceId: "disturbanceId",
  /** Silviculture activity treatment unit ID within the Activities tab */
  activityId: "activityId",
  /** Forest cover compound ID (coverId-polygonId) within the Forest Cover tab */
  forestCoverId: "forestCoverId",
  /** Section identifier for scrolling to a specific sub-section (e.g., comments) */
  section: "section",
} as const;

/**
 * Values for the `section` URL search parameter.
 * Each value identifies a specific sub-section within a tab to scroll to.
 */
export const DEEP_LINK_SECTIONS = {
  /** Opening-level comment in the Overview tab */
  openingComment: "opening-comment",
  /** Standards unit comment inside an SSU accordion */
  ssuComment: "ssu-comment",
  /** Milestone comment inside an SSU accordion */
  milestoneComment: "milestone-comment",
  /** Forest cover comment at the bottom of the Forest Cover tab */
  fcComment: "fc-comment",
  /** Comment inside an expanded silviculture activity row */
  activityComment: "activity-comment",
  /** Comment inside an expanded disturbance row */
  disturbanceComment: "disturbance-comment",
} as const;

/**
 * Builders for DOM element IDs used as scroll targets.
 * Both link producers (search result rows) and consumers (detail tab components)
 * must use these to ensure the generated ID matches the rendered element ID.
 */
export const DEEP_LINK_ELEMENT_ID = {
  /** Comment section in the Overview tab */
  openingComment: "opening-comment-section",
  /** Comment section at the bottom of the Forest Cover tab */
  fcComment: "fc-comment-section",
  /** SSU accordion wrapper by stocking standard unit identifier */
  ssuAccordion: (ssuId: string | number) => `ssu-accordion-${ssuId}`,
  /** SSU-level comment inside an SSU accordion */
  ssuComment: (ssuId: string | number) => `ssu-comment-${ssuId}`,
  /** Milestone comment inside an SSU accordion */
  milestoneComment: (ssuId: string | number) => `milestone-comment-${ssuId}`,
  /** Disturbance table row by activity treatment unit ID */
  disturbanceRow: (atuId: string | number) => `disturbance-row-${atuId}`,
  /** Silviculture activity table row by activity treatment unit ID */
  activityRow: (atuId: string | number) => `activity-row-${atuId}`,
  /** Forest cover table row by cover ID and polygon ID */
  fcRow: (coverId: string | number, polygonId: string | number) => `fc-row-${coverId}-${polygonId}`,
  /** Comment field inside an expanded silviculture activity row */
  activityComment: (atuId: string | number) => `activity-comment-${atuId}`,
  /** Comment field inside an expanded disturbance row */
  disturbanceComment: (atuId: string | number) => `disturbance-comment-${atuId}`,
} as const;
