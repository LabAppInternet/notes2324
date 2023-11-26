package cat.tecnocampus.notes2324.application.exceptions;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(long id) {
        super("User with id: " + id + " not found");
    }
}
