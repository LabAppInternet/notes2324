package cat.tecnocampus.notes2324.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserWithOwnedNotesDTO(long id,
                                    @Size(min=5, max=10)
                      @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Nickname must begin with a capital letter. Also only letters are allowed")
                      String name,
                                    @Email
                      String email,
                                    List<NoteDTO> notes) {
}
