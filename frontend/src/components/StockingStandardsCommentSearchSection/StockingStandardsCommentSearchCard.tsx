import { StockingStandardsCommentSearchResponseDto } from '@/services/OpenApi';

type Props = {
  commentDto: StockingStandardsCommentSearchResponseDto;
  index: number;
};

const StockingStandardsCommentSearchCard = ({ commentDto, index }: Props) => (
  <div className={`stocking-standards-comment-card${index % 2 !== 0 ? ' --shaded' : ''}`}>
    <p>{commentDto.commentText}</p>
  </div>
);

export default StockingStandardsCommentSearchCard;
