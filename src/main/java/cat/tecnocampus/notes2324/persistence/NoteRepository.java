package cat.tecnocampus.notes2324.persistence;

import cat.tecnocampus.notes2324.domain.Note;
import cat.tecnocampus.notes2324.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByOwner(User owner);
}