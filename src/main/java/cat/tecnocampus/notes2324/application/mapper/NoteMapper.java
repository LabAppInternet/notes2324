package cat.tecnocampus.notes2324.application.mapper;

import cat.tecnocampus.notes2324.application.dtos.NoteDTO;
import cat.tecnocampus.notes2324.application.dtos.TagDTO;
import cat.tecnocampus.notes2324.domain.Note;

public class NoteMapper {
    public static NoteDTO noteToNoteDTO(Note note) {
        return new NoteDTO(note.getId(), note.getTitle(), note.getContent(), note.getCreationDate(),
                note.getTags().stream().map(t -> new TagDTO(t.getName())).toList());
    }
}
