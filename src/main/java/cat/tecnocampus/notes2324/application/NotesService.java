package cat.tecnocampus.notes2324.application;

import cat.tecnocampus.notes2324.application.dtos.*;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public UserWithOwnedNotesDTO getUserById(long userId) {
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
            if (noteUpdate.title() != null) note.setTitle(noteUpdate.title());
            if (noteUpdate.content() != null) note.setContent(noteUpdate.content());
            updateNoteTags(noteUpdate, note);
        }
    }

    private void updateNoteTags(NoteUpdate noteUpdate, Note note) {
        Set<Tag> newTags;
        if (noteUpdate.tags() != null)
            newTags = noteUpdate.tags().stream().map(t -> new Tag(t)).collect(Collectors.toSet());
        else newTags = new HashSet<>();

        // tags to delete = current - new
        Set<Tag> tagsToDelete = new HashSet<>(note.getTags());
        tagsToDelete.removeAll(newTags);

        tagsToDelete.stream().forEach(t -> note.removeTag(t));
        newTags.stream().forEach(t -> note.addTag(t));
    }

    public List<UserDTO> getUsersRatedByNotes() {
        return userRepository.findUsersRatedByNotes();
    }
}
