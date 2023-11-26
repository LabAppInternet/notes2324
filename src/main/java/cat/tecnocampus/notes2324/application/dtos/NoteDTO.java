package cat.tecnocampus.notes2324.application.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record NoteDTO(long id,
                      String title,
                      String content, LocalDateTime creationDate, List<TagDTO> tags) {
}
