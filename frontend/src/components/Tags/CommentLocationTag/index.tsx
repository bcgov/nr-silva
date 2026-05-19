import React from 'react';
import { Tag } from '@carbon/react';
import { SIZES, TYPES } from '@carbon/react/lib/components/Tag/Tag';
import { CommentSearchResponseDto } from '@/services/OpenApi';
import { locationColorMap, locationDisplayMap, activityKindColorMap } from './constants';

type CommentLocationType = CommentSearchResponseDto.commentLocation | 'unknown';

type CommentLocationTagProps = {
  location?: CommentSearchResponseDto.commentLocation | null;
  activityKind?: CommentSearchResponseDto.activityKind | null;
  size?: keyof typeof SIZES;
  suffixText?: string;
};

const isKnownLocation = (location: string): location is CommentLocationType =>
  location in locationDisplayMap;

const CommentLocationTag = ({ location, activityKind, size, suffixText }: CommentLocationTagProps) => {
  const tagLocation: CommentLocationType = location && isKnownLocation(location) ? location : 'unknown';

  const getDisplayText = (): string => {
    if (location === CommentSearchResponseDto.commentLocation.ACTIVITIES && activityKind) {
      return activityKind === CommentSearchResponseDto.activityKind.ACTIVITY ? 'Activity' : 'Disturbance';
    }
    return locationDisplayMap[tagLocation];
  };

  const getTagType = (): keyof typeof TYPES => {
    if (location === CommentSearchResponseDto.commentLocation.ACTIVITIES && activityKind) {
      return activityKindColorMap[activityKind];
    }
    return locationColorMap[tagLocation];
  };

  const displayText = getDisplayText();
  const tagType = getTagType();

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
