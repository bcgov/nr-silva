import { TYPES } from '@carbon/react/lib/components/Tag/Tag';
import { StockingStandardsCommentSearchResponseDto } from '@/services/OpenApi';

type StockingCommentLocationType = StockingStandardsCommentSearchResponseDto.commentLocation | 'unknown';

export const locationDisplayMap: Record<StockingCommentLocationType, string> = {
  STANDARDS_NAME: 'Standards name',
  ADDITIONAL_STANDARDS: 'Additional standards',
  STANDARDS_OBJECTIVE: 'Standards objective',
  unknown: 'Unknown',
};

export const locationColorMap: Record<StockingCommentLocationType, keyof typeof TYPES> = {
  STANDARDS_NAME: 'blue',
  ADDITIONAL_STANDARDS: 'purple',
  STANDARDS_OBJECTIVE: 'red',
  unknown: 'gray',
};
