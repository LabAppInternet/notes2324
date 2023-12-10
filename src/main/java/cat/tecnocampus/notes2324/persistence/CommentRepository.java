package cat.tecnocampus.notes2324.persistence;

import cat.tecnocampus.notes2324.domain.Comment;
import cat.tecnocampus.notes2324.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByNote(Note note);
}
