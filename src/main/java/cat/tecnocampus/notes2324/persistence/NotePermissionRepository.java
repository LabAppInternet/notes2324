package cat.tecnocampus.notes2324.persistence;

import cat.tecnocampus.notes2324.application.dtos.NoteDTO;
import cat.tecnocampus.notes2324.application.dtos.PermissionDTO;
import cat.tecnocampus.notes2324.domain.Note;
import cat.tecnocampus.notes2324.domain.NotePermission;
import cat.tecnocampus.notes2324.domain.NotePermissionId;
import cat.tecnocampus.notes2324.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotePermissionRepository extends JpaRepository<NotePermission, NotePermissionId> {


    List<NotePermission> findNotePermissionsById_UserId(long userId);

    @Query("""
        select new cat.tecnocampus.notes2324.application.dtos.PermissionDTO(n.owner.id, n.owner.name, n.id, n.title, np.canView, np.canEdit) 
        from NotePermission np
        join np.note n
        where np.id.userId = :userId
        """)
    List<PermissionDTO> findUserPermissions(long userId);

    List<NotePermission> findNotePermissionsById_UserIdAndCanViewIsTrue(long userId);

    @Query("""
        select count(*) >= 1 from NotePermission np
        join np.note 
        where np.note.id = :noteId and np.allowed.id = :userId and np.canEdit
        """)
    boolean canEdit(long userId, long noteId);
}
