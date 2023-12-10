package cat.tecnocampus.notes2324.application;

import cat.tecnocampus.notes2324.application.dtos.*;
import cat.tecnocampus.notes2324.application.exceptions.NoteNotFoundException;
import cat.tecnocampus.notes2324.application.exceptions.NotePermissionNotFoundException;
import cat.tecnocampus.notes2324.application.exceptions.UserDoesNotOwnNoteException;
import cat.tecnocampus.notes2324.application.exceptions.UserNotFoundException;
import cat.tecnocampus.notes2324.application.mapper.NoteMapper;
import cat.tecnocampus.notes2324.domain.Note;
import cat.tecnocampus.notes2324.domain.NotePermission;
import cat.tecnocampus.notes2324.domain.NotePermissionId;
import cat.tecnocampus.notes2324.domain.User;
import cat.tecnocampus.notes2324.persistence.NotePermissionRepository;
import cat.tecnocampus.notes2324.persistence.NoteRepository;
import cat.tecnocampus.notes2324.persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {


    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NotePermissionRepository notePermissionRepository;

    public PermissionService(NoteRepository noteRepository, UserRepository userRepository, NotePermissionRepository notePermissionRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.notePermissionRepository = notePermissionRepository;
    }

    public List<PermissionDTO> getUserPermissions(long userId) {
        return notePermissionRepository.findUserPermissions(userId);
    }

    //should we return an error if user does not exist? or would it be better to return an empty list not to give
    // sensible information to possible attackers?
    public List<UserDTO> getUsersCanViewNote(long noteId) {
        return notePermissionRepository.findUsersWithPermissionCanView(noteId);
    }
    public List<UserDTO> getUsersWithPermissionCanEdit(long noteId) {
        return notePermissionRepository.findUsersWithPermissionCanEdit(noteId);
    }

    public List<NoteDTO> getNotesUserCanView(long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return notePermissionRepository.findNotePermissionsById_UserIdAndCanViewIsTrue(userId)
                .stream().map(NotePermission::getNote).map(NoteMapper::noteToNoteDTO).toList();
    }

    public boolean userCanEditNote(User user, Note note) {
        return notePermissionRepository.canEdit(user.getId(), note.getId());
    }

public boolean userCanViewNote (User user, Note note) {
        return notePermissionRepository.canView(user.getId(), note.getId());
    }

    private void actionCreateNotePermission(long ownerId, PermissionCreation permissionCreation, Note note) {
        User allowedUser = userRepository.findById(permissionCreation.allowedId())
                .orElseThrow(() -> new UserNotFoundException(permissionCreation.allowedId()));
        NotePermission permission = new NotePermission(allowedUser, note);
        permission.setCanView(permissionCreation.canView());
        permission.setCanEdit(permissionCreation.canEdit());
        notePermissionRepository.save(permission);
    }

    private boolean permissionAlreadyExists (long ownerId, PermissionCreation permissionCreation) {
        return notePermissionRepository.existsById(new NotePermissionId(permissionCreation.noteId(), ownerId));
    }

    @Transactional
    public void createNotePermissions(long ownerId, PermissionCreation permissionCreation) {
        Note note = noteRepository.findById(permissionCreation.noteId())
                .orElseThrow(() -> new NoteNotFoundException(permissionCreation.noteId()));
        if(!note.isOwner(ownerId)) throw new UserDoesNotOwnNoteException(ownerId, note.getId());

        //if user is not allowed to edit nor to view, permission is not created
        if (!permissionCreation.canEdit() && !permissionCreation.canView())
            return;

        if (this.permissionAlreadyExists(ownerId, permissionCreation))
            this.updatePermission(ownerId, permissionCreation);
        else actionCreateNotePermission(ownerId, permissionCreation, note);
    }

    @Transactional
    public void revokePermission(long ownerId, PermissionCreation permissionCreation) {
        // TODO 4.2 get the note from the repository. If it does not exist, throw a NoteNotFoundException
        Note note = noteRepository.findById(permissionCreation.noteId())
                .orElseThrow(() -> new NoteNotFoundException(permissionCreation.noteId()));
        // TODO 4.3 check if the user is the owner of the note. If not, throw a UserDoesNotOwnNoteException
        if (!note.isOwner(ownerId))
            throw new UserDoesNotOwnNoteException(ownerId, note.getId());

        if (!permissionCreation.canEdit() && !permissionCreation.canView()) {
            //TODO 4.4 delete the permission from the repository
            NotePermissionId notePermissionId = new NotePermissionId(permissionCreation.noteId(), permissionCreation.allowedId());
            notePermissionRepository.deleteById(notePermissionId);
        }
        else { // you do not need to do anything here. The code already updates the permission
            updatePermission(ownerId, permissionCreation);
        }
    }

    private void updatePermission(long ownerId, PermissionCreation permissionCreation) {
        NotePermissionId notePermissionId = new NotePermissionId(permissionCreation.noteId(), permissionCreation.allowedId());
        NotePermission permission =
                notePermissionRepository.findById(new NotePermissionId(permissionCreation.noteId(), permissionCreation.allowedId()))
                        .orElseThrow(() -> new NotePermissionNotFoundException(notePermissionId));
        permission.setCanEdit(permissionCreation.canEdit());
        permission.setCanView(permissionCreation.canView());
    }
}
