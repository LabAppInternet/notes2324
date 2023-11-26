package cat.tecnocampus.notes2324.persistence;

import cat.tecnocampus.notes2324.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
