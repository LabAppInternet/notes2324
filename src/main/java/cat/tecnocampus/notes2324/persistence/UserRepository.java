package cat.tecnocampus.notes2324.persistence;

import cat.tecnocampus.notes2324.application.dtos.UserDTO;
import cat.tecnocampus.notes2324.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
   @Query("""
           select new cat.tecnocampus.notes2324.application.dtos.UserDTO(u.id, u.name, u.email) 
           from Note n 
           join User u on n.owner = u 
           where n.creationDate >= CURRENT_DATE - 30
           group by n.owner order by count(n.owner) desc
           """)
    List<UserDTO> findUsersRatedByNotes();

}
