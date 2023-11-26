package cat.tecnocampus.notes2324.persistence;

import cat.tecnocampus.notes2324.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, String> {
}