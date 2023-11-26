package cat.tecnocampus.notes2324.application.mapper;

import cat.tecnocampus.notes2324.application.dtos.UserDTO;
import cat.tecnocampus.notes2324.domain.Note;
import cat.tecnocampus.notes2324.domain.User;

import java.util.List;

public class UserMapper {

    //static method that creates a userDTO from data received from a User. The method is named UserToUserDTO
    public static UserDTO userToUserDTO(User user, List<Note> notes) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), notes.stream().map(NoteMapper::noteToNoteDTO).toList());
    }
}
