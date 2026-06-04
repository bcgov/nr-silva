import React from 'react';
import { Stack } from '@carbon/react';
import { StockingStandardsCommentSearchResponseDto } from '@/services/OpenApi';
import { StockingStandardsCommentLocationTag } from '@/components/Tags';
import { formatLocalDate } from '@/utils/DateUtils';
import { highlightKeyword } from '@/utils/HighlightUtils';

import './styles.scss';

type Props = {
  commentDto: StockingStandardsCommentSearchResponseDto;
  index: number;
  keyword: string;
};

const StockingStandardsCommentSearchCard = ({ commentDto, index, keyword }: Props) => {
  return (
    <div
      className={`default-search-card-container${index % 2 !== 0 ? ' default-search-card-container--shaded' : ''}`}
      id={`stocking-comment-card-${commentDto.standardsRegimeId}-${index}`}
    >
      <Stack gap={2}>
        <div className="stocking-comment-title-row">
          <div className="stocking-comment-title-and-tag">
            <span className="stocking-comment-title">{`SSID ${commentDto.standardsRegimeId}`}</span>
            <StockingStandardsCommentLocationTag location={commentDto.commentLocation} size="sm" />
          </div>
          <div className="stocking-comment-date">
            {`Date: ${formatLocalDate(commentDto.updateTimestamp)}`}
          </div>
        </div>
        <div className="stocking-comment-text">
          {highlightKeyword(commentDto.commentText, keyword)}
        </div>
      </Stack>
    </div>
  );
};

export default StockingStandardsCommentSearchCard;
