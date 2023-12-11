package cat.tecnocampus.notes2324.application.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

//TODO 1.1: you may need to add some annotations here
public record NoteCreate(
        long noteId,
        @Size(min=5, max=100)
        @Pattern(regexp = "^[A-ZÀÁÈÉÍÒÓÚÜ][A-Za-zÀ-ÿ\s.,;:_'\\-]*", message = "Title must begin with a capital letter. Also only letters are allowed")
        String title,
        @Size(min=5, max=100)
        @Pattern(regexp = "^[A-ZÀÁÈÉÍÒÓÚÜ0-9].*", message = "Title must begin with a capital letter.")
        String content,
        List<String> tags) {
}
