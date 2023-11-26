package cat.tecnocampus.notes2324.application;

import cat.tecnocampus.notes2324.application.dtos.*;
import cat.tecnocampus.notes2324.application.exceptions.NoteNotFoundException;
import cat.tecnocampus.notes2324.application.exceptions.UserDoesNotOwnNoteException;
import cat.tecnocampus.notes2324.application.exceptions.UserNotFoundException;
import cat.tecnocampus.notes2324.application.mapper.NoteMapper;
import cat.tecnocampus.notes2324.application.mapper.UserMapper;
import cat.tecnocampus.notes2324.domain.*;
import cat.tecnocampus.notes2324.persistence.NotePermissionRepository;
import cat.tecnocampus.notes2324.persistence.NoteRepository;
import cat.tecnocampus.notes2324.persistence.TagRepository;
import cat.tecnocampus.notes2324.persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NotePermissionRepository notePermissionRepository;
    private final TagRepository tagRepository;

    public NotesService(NoteRepository noteRepository, UserRepository userRepository, NotePermissionRepository notePermissionRepository,
                        TagRepository tagRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.notePermissionRepository = notePermissionRepository;
        this.tagRepository = tagRepository;
    }

    public UserDTO getUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return UserMapper.userToUserDTO(user, noteRepository.findAllByOwner(user));
    }

    public List<PermissionDTO> getUserPermissions(long userId) {
        return notePermissionRepository.findUserPermissions(userId);
    }

    public void createNotePermissions(long ownerId, PermissionCreation permissionCreation) {
        Note note = noteRepository.findById(permissionCreation.noteId()).orElseThrow(() -> new NoteNotFoundException(permissionCreation.noteId()));
        if(!note.isOwner(ownerId)) throw new UserDoesNotOwnNoteException(ownerId, note.getId());

        if (this.permissionAlreadyExists(ownerId, permissionCreation))
            this.updatePermission(ownerId, permissionCreation);
        else actionCreateNotePermission(ownerId, permissionCreation, note);
    }

    private void actionCreateNotePermission(long ownerId, PermissionCreation permissionCreation, Note note) {
        User allowedUser = userRepository.findById(permissionCreation.allowedId()).orElseThrow(() -> new UserNotFoundException(permissionCreation.allowedId()));
        NotePermission permission = createPermission(permissionCreation, allowedUser, note);
        notePermissionRepository.save(permission);
    }

    private NotePermission createPermission(PermissionCreation permissionCreation, User allowedUser, Note note) {
        NotePermission permission = new NotePermission(allowedUser, note);
        permission.setCanView(permissionCreation.canView());
        permission.setCanEdit(permissionCreation.canEdit());
        return permission;
    }

    @Transactional
    public void updatePermission(long ownerId, PermissionCreation permissionCreation) {
        NotePermission permission =
                notePermissionRepository.findById(new NotePermissionId(permissionCreation.noteId(), ownerId))
                        .orElseThrow();
        permission.setCanEdit(permissionCreation.canEdit());
        permission.setCanView(permissionCreation.canView());
    }

    private boolean permissionAlreadyExists (long ownerId, PermissionCreation permissionCreation) {
        return notePermissionRepository.existsById(new NotePermissionId(permissionCreation.noteId(), ownerId));
    }

    public List<NoteDTO> getUserNotes(long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return noteRepository.findAllByOwner(owner).stream().map(NoteMapper::noteToNoteDTO).toList();
    }

    public void createUserNote(long ownerId, NoteCreation noteCreation) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(ownerId));
        Note note = new Note();
        note.setOwner(owner);
        note.setTitle(noteCreation.title());
        note.setContent(noteCreation.content());
        noteCreation.tags().stream().forEach(t -> note.addTag(new Tag(t)));

        noteRepository.save(note);
    }

    private void addTagToNote(String tagName, Note note) {
        //Tag tag = tagRepository.findById(tagName).orElseGet(() -> new Tag(tagName));
        //note.addTag(tag);
        note.addTag(new Tag(tagName));
    }

    public List<NoteDTO> getNotesUserCanView(long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return notePermissionRepository.findNotePermissionsById_UserIdAndCanViewIsTrue(userId)
                .stream().map(NotePermission::getNote).map(NoteMapper::noteToNoteDTO).toList();
    }

    @Transactional
    public void updateUserNote(long ownerId, NoteUpdate noteUpdate) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(ownerId));
        Note note = noteRepository.findById(noteUpdate.noteId()).orElseThrow(() -> new NoteNotFoundException(noteUpdate.noteId()));

        if (note.isOwner(user.getId()) || userCanEditNote(user, note)) {
            System.out.println("going to update note: " + note.getId());
            if (noteUpdate.title() != null) note.setTitle(noteUpdate.title());
            if (noteUpdate.content() != null) note.setContent(noteUpdate.content());
            if (noteUpdate.tags() != null) noteUpdate.tags().stream().forEach(t -> note.addTag(new Tag(t)));
        }
    }

    private boolean userCanEditNote(User user, Note note) {
        return notePermissionRepository.canEdit(user.getId(), note.getId());
    }
}
