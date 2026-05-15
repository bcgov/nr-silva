import React from 'react';
import { Tag } from '@carbon/react';
import { SIZES, TYPES } from '@carbon/react/lib/components/Tag/Tag';
import { CommentSearchResponseDto } from '@/services/OpenApi';
import { locationColorMap, locationDisplayMap } from './constants';

type CommentLocationType = CommentSearchResponseDto.commentLocation | 'unknown';

type CommentLocationTagProps = {
  location?: CommentSearchResponseDto.commentLocation | null;
  size?: keyof typeof SIZES;
  suffixText?: string;
};

const isKnownLocation = (location: string): location is CommentLocationType =>
  location in locationDisplayMap;

const CommentLocationTag = ({ location, size, suffixText }: CommentLocationTagProps) => {
  const tagLocation: CommentLocationType = location && isKnownLocation(location) ? location : 'unknown';
  const displayText = locationDisplayMap[tagLocation];
  const tagType = locationColorMap[tagLocation];

  return (
    <Tag
      title={displayText}
      size={size ?? 'md'}
      type={tagType}
    >
      {`${displayText}${suffixText ? `${suffixText}` : ''}`}
    </Tag>
  );
};

export default CommentLocationTag;
