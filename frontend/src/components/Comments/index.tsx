import { CommentDto } from "@/services/OpenApi";
import { PLACE_HOLDER } from "@/constants";

interface CommentProps {
  comments: CommentDto[];
}

const Comments = ({ comments }: CommentProps) => {
  return (
    comments?.length ? (
      <ul className="comment-list">
        {
          comments.map((comment, index) =>
            comment.commentText ? (
              <li key={`${comment.commentType.code?.toLocaleLowerCase()}-${index}`}>
                {`${comment.commentType.description}: ${comment.commentText}`}
              </li>
            ) : null
          )
        }
      </ul>
    ) : PLACE_HOLDER
  );
}

export default Comments;

