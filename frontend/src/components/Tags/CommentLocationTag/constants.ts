import { TYPES } from '@carbon/react/lib/components/Tag/Tag';
import { CommentSearchResponseDto } from '@/services/OpenApi';
import { COMMENT_LOCATION_LIST } from '@/components/CommentSearchSection/constants';

type CommentLocationType = CommentSearchResponseDto.commentLocation | 'unknown';

// Build display map from COMMENT_LOCATION_LIST
export const locationDisplayMap: Record<CommentLocationType, string> = {
  ...Object.fromEntries(COMMENT_LOCATION_LIST.map(item => [item.code, item.description])),
  unknown: 'Unknown',
} as Record<CommentLocationType, string>;

export const locationColorMap: Record<CommentLocationType, keyof typeof TYPES> = {
  STANDARDS_UNIT: 'teal',
  OPENING: 'blue',
  MILESTONE: 'magenta',
  ACTIVITIES: 'green',
  FOREST_COVER: 'cyan',
  unknown: 'gray',
};

export const activityKindColorMap: Record<CommentSearchResponseDto.activityKind, keyof typeof TYPES> = {
  [CommentSearchResponseDto.activityKind.ACTIVITY]: 'green',
  [CommentSearchResponseDto.activityKind.DISTURBANCE]: 'purple',
};
