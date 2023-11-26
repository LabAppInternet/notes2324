package cat.tecnocampus.notes2324.api;

import cat.tecnocampus.notes2324.application.NotesService;
import cat.tecnocampus.notes2324.application.dtos.*;
import cat.tecnocampus.notes2324.domain.NotePermission;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotesController {

    public final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable long id) {
        return notesService.getUserById(id);
    }

    @GetMapping("/users/{userId}/notes/canView")
    public List<NoteDTO> getNotesUserCanView(@PathVariable long userId) {
        return notesService.getNotesUserCanView(userId);
    }

    @GetMapping("/users/{id}/permissions")
    public List<PermissionDTO> getUserPermissions(@PathVariable long id) {
        return notesService.getUserPermissions(id);
    }

    @PostMapping("/users/{ownerId}/permissions")
    public void newPermission(@PathVariable long ownerId, @RequestBody PermissionCreation permissionCreation) {
        notesService.createNotePermissions(ownerId, permissionCreation);
    }

    @GetMapping("/users/{userId}/notes")
    public List<NoteDTO> getUserNotes(@PathVariable long userId) {
        return notesService.getUserNotes(userId);
    }

    @PostMapping("/users/{ownerId}/notes")
    public void newUserNote(@PathVariable long ownerId, @RequestBody @Valid NoteCreation noteCreation) {
        notesService.createUserNote(ownerId, noteCreation);
    }

    @PutMapping("users/{ownerId}/notes")
    public void updateUserNote(@PathVariable long ownerId, @RequestBody @Valid NoteUpdate noteUpdate) {
        notesService.updateUserNote(ownerId, noteUpdate);
    }
}
