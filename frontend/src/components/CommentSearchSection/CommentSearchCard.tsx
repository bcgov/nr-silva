import React from 'react';
import { CommentSearchResponseDto } from '@/services/OpenApi';
import { Stack } from '@carbon/react';
import OpeningBookmarkBtn from '../OpeningBookmarkBtn';
import { CommentLocationTag } from '@/components/Tags';
import { formatLocalDate } from '@/utils/DateUtils';
import { highlightKeyword } from '@/utils/HighlightUtils';
import { DEEP_LINK_PARAMS, DEEP_LINK_SECTIONS } from '@/constants/deepLinkConstants';

import './styles.scss';

type Props = {
  keyword: string;
  commentDto: CommentSearchResponseDto;
  index: number;
};

const CommentSearchCard = ({ keyword, commentDto, index }: Props) => {
  const [isFocused, setIsFocused] = React.useState(false);

  const getSuffixText = (): string | undefined => {
    if (commentDto.commentLocation === CommentSearchResponseDto.commentLocation.STANDARDS_UNIT && commentDto.standardsUnitName) {
      return `: ${commentDto.standardsUnitName}`;
    }

    if (commentDto.commentLocation === CommentSearchResponseDto.commentLocation.MILESTONE && commentDto.standardsUnitName) {
      return ` - Standard unit: ${commentDto.standardsUnitName}`;
    }

    if (commentDto.commentLocation === CommentSearchResponseDto.commentLocation.ACTIVITIES && commentDto.activityTreatmentUnitId) {
      return `: ${commentDto.activityTreatmentUnitId}`;
    }

    return undefined;
  }

  const getLinkPath = (): string => {
    const base = `/openings/${commentDto.openingId}`;
    switch (commentDto.commentLocation) {
      case CommentSearchResponseDto.commentLocation.OPENING:
        return `${base}?tab=overview&${DEEP_LINK_PARAMS.section}=${DEEP_LINK_SECTIONS.openingComment}`;
      case CommentSearchResponseDto.commentLocation.MILESTONE:
        return `${base}?tab=standards-units${commentDto.standardsUnitId ? `&${DEEP_LINK_PARAMS.ssuId}=${commentDto.standardsUnitId}` : ''}&${DEEP_LINK_PARAMS.section}=${DEEP_LINK_SECTIONS.milestoneComment}`;
      case CommentSearchResponseDto.commentLocation.STANDARDS_UNIT:
        return `${base}?tab=standards-units${commentDto.standardsUnitId ? `&${DEEP_LINK_PARAMS.ssuId}=${commentDto.standardsUnitId}` : ''}&${DEEP_LINK_PARAMS.section}=${DEEP_LINK_SECTIONS.ssuComment}`;
      case CommentSearchResponseDto.commentLocation.ACTIVITIES:
        if (commentDto.activityKind === CommentSearchResponseDto.activityKind.DISTURBANCE) {
          return `${base}?tab=activities${commentDto.activityTreatmentUnitId ? `&${DEEP_LINK_PARAMS.disturbanceId}=${commentDto.activityTreatmentUnitId}&${DEEP_LINK_PARAMS.section}=${DEEP_LINK_SECTIONS.disturbanceComment}` : ''}`;
        }
        return `${base}?tab=activities${commentDto.activityTreatmentUnitId ? `&${DEEP_LINK_PARAMS.activityId}=${commentDto.activityTreatmentUnitId}&${DEEP_LINK_PARAMS.section}=${DEEP_LINK_SECTIONS.activityComment}` : ''}`;
      case CommentSearchResponseDto.commentLocation.FOREST_COVER:
        return `${base}?tab=forest-cover&${DEEP_LINK_PARAMS.section}=${DEEP_LINK_SECTIONS.fcComment}`;
      default:
        return base;
    }
  }

  const handleRowClick = () => {
    window.open(getLinkPath(), '_blank', 'noopener,noreferrer');
  };

  const handleBookmarkClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
  };


  return (
    <div className={`default-search-card-container ${index % 2 !== 0 ? 'default-search-card-container--shaded' : ''} ${isFocused ? 'comment-search-card-container--focused' : ''}`} id={`comment-search-card-${commentDto.openingId}-${commentDto.commentLocation.toLowerCase()}`}>
      <Stack gap={2}>
        <div
          className='comment-search-title-row'
          onClick={handleRowClick}
          onKeyDown={(e) => {
            if ((e.key === 'Enter' || e.key === ' ') && document.activeElement === e.currentTarget) {
              e.preventDefault();
              handleRowClick();
            }
          }}
          onFocus={() => setIsFocused(true)}
          onBlur={() => setIsFocused(false)}
          tabIndex={0}
          role="link"
          aria-label={`Open comment search result for Opening ID ${commentDto.openingId}, ${commentDto.commentLocation}`}
        >
          <div className='bookmark-title-tag'>
            <div className='bookmark-and-title'>
              <OpeningBookmarkBtn openingId={commentDto.openingId} tooltipPosition="right" btnSize="sm" className="comment-search-bookmark-btn" onClick={handleBookmarkClick} />
              <div className='opening-id-title'>
                {`Opening ID ${commentDto.openingId}`}
              </div>
            </div>
            <CommentLocationTag location={commentDto.commentLocation} activityKind={commentDto.activityKind} size="sm" suffixText={getSuffixText()} />
          </div>

          <div className='comment-date'>
            {`Date: ${formatLocalDate(commentDto.updateTimestamp)}`}
          </div>
        </div>

        <div className='comment-search-comment-row'>
          {highlightKeyword(commentDto.commentText, keyword)}
        </div>

      </Stack>

    </div>
  );
};

export default CommentSearchCard;
