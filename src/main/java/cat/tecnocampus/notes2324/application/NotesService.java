package cat.tecnocampus.notes2324.application;

import cat.tecnocampus.notes2324.application.dtos.NoteCreate;
import cat.tecnocampus.notes2324.application.dtos.NoteDTO;
import cat.tecnocampus.notes2324.application.dtos.NoteUpdate;
import cat.tecnocampus.notes2324.application.dtos.UserDTO;
import cat.tecnocampus.notes2324.application.exceptions.NoteNotFoundException;
import cat.tecnocampus.notes2324.application.exceptions.UserNotFoundException;
import cat.tecnocampus.notes2324.application.mapper.NoteMapper;
import cat.tecnocampus.notes2324.application.mapper.UserMapper;
import cat.tecnocampus.notes2324.domain.Note;
import cat.tecnocampus.notes2324.domain.Tag;
import cat.tecnocampus.notes2324.domain.User;
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
    private final PermissionService permissionService;
    private final TagRepository tagRepository;

    public NotesService(NoteRepository noteRepository, UserRepository userRepository, PermissionService permissionService,
                        TagRepository tagRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.permissionService = permissionService;
        this.tagRepository = tagRepository;
    }

    public UserDTO getUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return UserMapper.userToUserDTO(user, noteRepository.findAllByOwner(user));
    }

    public List<NoteDTO> getUserNotes(long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return noteRepository.findAllByOwner(owner).stream().map(NoteMapper::noteToNoteDTO).toList();
    }

    public void createUserNote(long ownerId, NoteCreate noteCreate) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(ownerId));
        Note note = new Note();
        note.setOwner(owner);
        note.setTitle(noteCreate.title());
        note.setContent(noteCreate.content());
        noteCreate.tags().stream().forEach(t -> note.addTag(new Tag(t)));

        noteRepository.save(note);
    }

    @Transactional
    public void updateUserNote(long ownerId, NoteUpdate noteUpdate) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(ownerId));
        Note note = noteRepository.findById(noteUpdate.noteId()).orElseThrow(() -> new NoteNotFoundException(noteUpdate.noteId()));

        if (note.isOwner(user.getId()) || permissionService.userCanEditNote(user, note)) {
            System.out.println("going to update note: " + note.getId());
            if (noteUpdate.title() != null) note.setTitle(noteUpdate.title());
            if (noteUpdate.content() != null) note.setContent(noteUpdate.content());
            if (noteUpdate.tags() != null) noteUpdate.tags().stream().forEach(t -> note.addTag(new Tag(t)));
        }
    }
}
