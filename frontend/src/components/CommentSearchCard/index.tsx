import { CommentSearchResponseDto } from '@/services/OpenApi';

type Props = {
  comment: CommentSearchResponseDto;
};

const CommentSearchCard = ({ comment }: Props) => {
  return (
    <div className="comment-search-card">
      <p>{comment.commentText}</p>
    </div>
  );
};

export default CommentSearchCard;
