package cat.tecnocampus.notes2324.application.exceptions;

public class NoteNotFoundException extends DomainException {
    public NoteNotFoundException(long noteId) {
        super("Note with id: " + noteId + " doesn't exist");
    }
}
