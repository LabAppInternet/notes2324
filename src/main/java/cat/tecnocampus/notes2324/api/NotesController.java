package cat.tecnocampus.notes2324.api;

import cat.tecnocampus.notes2324.application.NotesService;
import cat.tecnocampus.notes2324.application.PermissionService;
import cat.tecnocampus.notes2324.application.dtos.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotesController {

    private final NotesService notesService;
    private final PermissionService permissionService;


    public NotesController(NotesService notesService, PermissionService permissionService) {
        this.notesService = notesService;
        this.permissionService = permissionService;
    }

    @GetMapping("/users/{id}")
    public UserWithOwnedNotesDTO getUser(@PathVariable long id) {
        return notesService.getUserById(id);
    }

    @GetMapping("/users/ratedbynotes")
    public List<UserDTO> getUsersRatedByNotes() {
        return notesService.getUsersRatedByNotes();
    }

    @GetMapping("/users/{userId}/notes/canView")
    public List<NoteDTO> getNotesUserCanView(@PathVariable long userId) {
        return permissionService.getNotesUserCanView(userId);
    }

    @GetMapping("/users/{id}/permissions")
    public List<PermissionDTO> getUserPermissions(@PathVariable long id) {
        return permissionService.getUserPermissions(id);
    }

    @PostMapping("/users/{ownerId}/permissions")
    public void newPermission(@PathVariable long ownerId, @RequestBody PermissionCreation permissionCreation) {
        permissionService.createNotePermissions(ownerId, permissionCreation);
    }

    @GetMapping("/users/{userId}/notes")
    public List<NoteDTO> getUserNotes(@PathVariable long userId) {
        return notesService.getUserNotes(userId);
    }

    @GetMapping("/notes/{noteId}/users/canView")
    public List<UserDTO> getUsersCanViewNote(@PathVariable long noteId) {
        return permissionService.getUsersCanViewNote(noteId);
    }

    @GetMapping("/notes/{noteId}/users/canEdit")
    public List<UserDTO> getUsersWithPermissionCanEdit(@PathVariable long noteId) {
        return permissionService.getUsersWithPermissionCanEdit(noteId);
    }

    @PostMapping("/users/{ownerId}/notes")
    @ResponseStatus(HttpStatus.CREATED)
    public void newUserNote(@PathVariable long ownerId, @RequestBody @Valid NoteCreate noteCreate) {
        notesService.createUserNote(ownerId, noteCreate);
    }

    @PutMapping("/users/{ownerId}/notes")
    public void updateUserNote(@PathVariable long ownerId, @RequestBody @Valid NoteUpdate noteUpdate) {
        notesService.updateUserNote(ownerId, noteUpdate);
    }

    @DeleteMapping("/users/{ownerId}/permissions")
    public void revokePermission(@PathVariable long ownerId, @RequestBody PermissionCreation permissionCreation) {
        permissionService.revokePermission(ownerId, permissionCreation);
    }


}
