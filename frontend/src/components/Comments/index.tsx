import { CommentDto } from "@/types/CommentTypes";

interface CommentProps {
    comments: CommentDto[];
}

const Comments = ({comments}: CommentProps) => {
    return (
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
    );
}

export default Comments;
