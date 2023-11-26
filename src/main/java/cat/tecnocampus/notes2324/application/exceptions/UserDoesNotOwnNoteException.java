package cat.tecnocampus.notes2324.application.exceptions;

public class UserDoesNotOwnNoteException extends DomainException {
    public UserDoesNotOwnNoteException(long userid, long noteid) {
        super("User with id: " + userid + " does not own note with id: " + noteid);
    }
}
