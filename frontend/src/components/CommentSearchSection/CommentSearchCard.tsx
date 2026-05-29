import React from 'react';
import { CommentSearchResponseDto } from '@/services/OpenApi';
import { Stack } from '@carbon/react';
import OpeningBookmarkBtn from '../OpeningBookmarkBtn';
import { CommentLocationTag } from '@/components/Tags';
import { formatLocalDate } from '@/utils/DateUtils';

import './styles.scss';

type Props = {
  keyword: string;
  commentDto: CommentSearchResponseDto;
  index: number;
};

const CommentSearchCard = ({ keyword, commentDto, index }: Props) => {
  const [isFocused, setIsFocused] = React.useState(false);

  const highlightKeyword = (text: string | null, searchTerm: string): (string | React.ReactNode)[] => {
    if (!text || !searchTerm.trim()) return [text ?? ''];

    const result: (string | React.ReactNode)[] = [];
    const lowerText = text.toLowerCase();
    const lowerKeyword = searchTerm.toLowerCase();
    let lastIndex = 0;
    let currentIndex = 0;

    while ((currentIndex = lowerText.indexOf(lowerKeyword, lastIndex)) !== -1) {
      // Add text before match
      if (currentIndex > lastIndex) {
        result.push(text.substring(lastIndex, currentIndex));
      }
      // Add bolded match
      result.push(
        <strong key={`${currentIndex}-${searchTerm}`}>
          {text.substring(currentIndex, currentIndex + searchTerm.length)}
        </strong>
      );
      lastIndex = currentIndex + searchTerm.length;
    }

    // Add remaining text
    if (lastIndex < text.length) {
      result.push(text.substring(lastIndex));
    }

    return result.length > 0 ? result : [text];
  };

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
        return base;
      case CommentSearchResponseDto.commentLocation.MILESTONE:
        return `${base}?tab=standards-units`;
      case CommentSearchResponseDto.commentLocation.STANDARDS_UNIT:
        return `${base}?tab=standards-units`;
      case CommentSearchResponseDto.commentLocation.ACTIVITIES:
        return `${base}?tab=activities`;
      case CommentSearchResponseDto.commentLocation.FOREST_COVER:
        return `${base}?tab=forest-cover`;
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
    <div className={`comment-search-card-container ${index % 2 !== 0 ? 'comment-search-card-container--shaded' : ''} ${isFocused ? 'comment-search-card-container--focused' : ''}`} id={`comment-search-card-${commentDto.openingId}-${commentDto.commentLocation.toLowerCase()}`}>
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
