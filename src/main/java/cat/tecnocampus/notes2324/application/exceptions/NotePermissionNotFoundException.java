package cat.tecnocampus.notes2324.application.exceptions;

import cat.tecnocampus.notes2324.domain.NotePermissionId;

public class NotePermissionNotFoundException extends DomainException {
    public NotePermissionNotFoundException(NotePermissionId id) {
        super("NotePermission with id, note_id: " + id.getNoteId() + " and user_id: " + id.getUserId()+ " doesn't exist");
    }
}
