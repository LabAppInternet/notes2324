package cat.tecnocampus.notes2324.application.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NoteCreation(
        @Size(min=5, max=100)
        @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Title must begin with a capital letter. Also only letters are allowed")
        String title,
        @Size(min=5, max=100)
        @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Content must begin with a capital letter. Also only letters are allowed")
        String content,
        List<String> tags) {
}
