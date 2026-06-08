import { CodeDescriptionDto, StockingStandardsCommentSearchResponseDto } from '@/services/OpenApi';

const { commentLocation } = StockingStandardsCommentSearchResponseDto;

export const STOCKING_COMMENT_LOCATION_LIST: CodeDescriptionDto[] = [
  { code: commentLocation.STANDARDS_NAME, description: 'Standards name' },
  { code: commentLocation.ADDITIONAL_STANDARDS, description: 'Additional standards' },
  { code: commentLocation.STANDARDS_OBJECTIVE, description: 'Standards objective' },
] as const;

export const STOCKING_COMMENT_KEYWORD_MIN_LENGTH = 3;
export const STOCKING_COMMENT_KEYWORD_MAX_LENGTH = 2000;
