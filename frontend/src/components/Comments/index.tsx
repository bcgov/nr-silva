import { CommentDto } from "@/types/CommentTypes";
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
              <li key={index}>
                {comment.commentText}
              </li>
            ) : null
          )
        }
      </ul>
    ) : PLACE_HOLDER
  );
}

export default Comments;

