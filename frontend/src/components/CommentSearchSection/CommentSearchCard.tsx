import React from 'react';
import { CommentSearchResponseDto } from '@/services/OpenApi';
import { Stack } from '@carbon/react';
import OpeningBookmarkBtn from '../OpeningBookmarkBtn';
import { CommentLocationTag } from '@/components/Tags';
import { formatLocalDate } from '@/utils/DateUtils';

import './styles.scss';
import { Link } from 'react-router-dom';


type Props = {
  keyword: string;
  commentDto: CommentSearchResponseDto;
  index: number;
};

const CommentSearchCard = ({ keyword, commentDto, index }: Props) => {

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


  return (
    <div className={`comment-search-card-container ${index % 2 !== 0 ? 'comment-search-card-container--shaded' : ''}`} id={`comment-search-card-${commentDto.openingId}-${commentDto.commentLocation.toLowerCase()}`}>
      <Stack gap={2}>
        <div className='comment-search-title-row'>
          <div className='bookmark-title-tag'>
            <div className='bookmark-and-title'>
              <OpeningBookmarkBtn openingId={commentDto.openingId} tooltipPosition="right" btnSize="sm" className="comment-search-bookmark-btn" />
              <Link className='opening-id-title default-plain-link' to={getLinkPath()} target='_blank' rel="noopener noreferrer">
                {`Opening ID: ${commentDto.openingId}`}
              </Link>
            </div>
            <CommentLocationTag location={commentDto.commentLocation} size="sm" suffixText={getSuffixText()} />
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
