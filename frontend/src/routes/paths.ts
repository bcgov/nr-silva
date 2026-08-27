/**
 * Route path constants
 *
 * These are extracted to a separate file to break circular dependencies
 * between routes/config.tsx → SideLayout → BCHeader → BCHeader/constants → routes/config
 */

export const DASHBOARD_PATH = '/dashboard';
export const OPENINGS_PATH = '/openings';
export const OPENINGS_SEARCH_PATH = '/openings-search';
export const OPENING_CREATE_SUCCESS_PATH = '/openings/create/success';
export const ACTIVITY_SEARCH_PATH = '/activity-search';
export const FOREST_COVER_SEARCH_PATH = '/forest-cover-search';
export const STANDARDS_UNIT_SEARCH_PATH = '/standards-unit-search';
export const COMMENT_SEARCH_PATH = '/comment-search';
export const STOCKING_STANDARDS_SEARCH_PATH = '/stocking-standards-search';
export const STOCKING_STANDARDS_COMMENT_SEARCH_PATH = '/stocking-standards-search/comments';
export const CREATE_OPENING_PATH = '/openings/create';
export const OPENING_DETAILS_PATH = '/openings/:openingId';
export const EDIT_TENURE_PATH = '/openings/:openingId/edit-tenure';
export const ACTIVITY_SEARCH_ACTIVITIES_PATH = 'activities';
export const ACTIVITY_SEARCH_DISTURBANCES_PATH = 'disturbances';
export const ACTIVITY_SEARCH_ACTIVITIES_FULL_PATH =
  `${ACTIVITY_SEARCH_PATH}/${ACTIVITY_SEARCH_ACTIVITIES_PATH}`;
export const WILDCARD_PATH = '*';
