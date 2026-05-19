import { CodeDescriptionDto, CommentSearchResponseDto } from '@/services/OpenApi';

const { commentLocation } = CommentSearchResponseDto;

export const COMMENT_LOCATION_LIST: CodeDescriptionDto[] = [
  { code: commentLocation.ACTIVITIES, description: 'Activities' },
  { code: commentLocation.FOREST_COVER, description: 'Forest cover' },
  { code: commentLocation.MILESTONE, description: 'Milestone' },
  { code: commentLocation.OPENING, description: 'Opening' },
  { code: commentLocation.STANDARDS_UNIT, description: 'Standards unit' },
] as const;

export const COMMENT_KEYWORD_MIN_LENGTH = 3;
export const COMMENT_KEYWORD_MAX_LENGTH = 2000;
