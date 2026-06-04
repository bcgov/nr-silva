import { Tag } from '@carbon/react';
import { SIZES } from '@carbon/react/lib/components/Tag/Tag';
import { StockingStandardsCommentSearchResponseDto } from '@/services/OpenApi';
import { locationColorMap, locationDisplayMap } from './constants';

type StockingCommentLocationType = StockingStandardsCommentSearchResponseDto.commentLocation | 'unknown';

type Props = {
  location?: StockingStandardsCommentSearchResponseDto.commentLocation | null;
  size?: keyof typeof SIZES;
};

const isKnownLocation = (location: string): location is StockingCommentLocationType =>
  location in locationDisplayMap;

const StockingStandardsCommentLocationTag = ({ location, size }: Props) => {
  const tagLocation: StockingCommentLocationType = location && isKnownLocation(location) ? location : 'unknown';
  const displayText = locationDisplayMap[tagLocation];
  const tagType = locationColorMap[tagLocation];

  return (
    <Tag title={displayText} size={size ?? 'md'} type={tagType}>
      {displayText}
    </Tag>
  );
};

export default StockingStandardsCommentLocationTag;
