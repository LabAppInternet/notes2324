package cat.tecnocampus.notes2324.application.mapper;

import cat.tecnocampus.notes2324.application.dtos.CommentDTO;
import cat.tecnocampus.notes2324.domain.Comment;

public class CommentMapper {
    public static CommentDTO commentToCommentDTO(Comment comment) {
        return new CommentDTO(comment.getId(), comment.getTitle(), comment.getBody());
    }
}
