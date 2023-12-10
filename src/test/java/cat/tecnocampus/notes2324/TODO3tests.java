package cat.tecnocampus.notes2324;

import cat.tecnocampus.notes2324.application.NotesService;
import cat.tecnocampus.notes2324.application.dtos.CommentDTO;
import cat.tecnocampus.notes2324.domain.Comment;
import cat.tecnocampus.notes2324.domain.Note;
import cat.tecnocampus.notes2324.persistence.CommentRepository;
import cat.tecnocampus.notes2324.persistence.NoteRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// TODO 3.0: See them fail and than make them pass completing the exercise
@SpringBootTest
@AutoConfigureMockMvc
public class TODO3tests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    private NotesService notesService;


    @Test
    void getNoteCommentsHappyPathUserOwner() throws Exception {
        Result result = makeReturnTwoComments();
        saveComments(result);

        mockMvc.perform(get("/users/1/notes/2/comments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].body", containsInAnyOrder("contents", "second contents")));

        deleteComments(result);
    }

    @Test
    void getNoteCommentsHappyPathUserCanView() throws Exception {
        Result result = makeReturnTwoComments();
        saveComments(result);

        mockMvc.perform(get("/users/4/notes/2/comments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].body", containsInAnyOrder("contents", "second contents")));

        deleteComments(result);
    }

    @Test
    void getNoteCommentsUserNotAllowed() throws Exception {
        Result result = makeReturnTwoComments();
        saveComments(result);

        mockMvc.perform(get("/users/2/notes/2/comments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));

        deleteComments(result);
    }

    @Test
    void getNoteCommentsUserDoesNotExist() throws Exception {

        mockMvc.perform(get("/users/100/notes/2/comments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User with id: 100 not found"));

    }

    @Test
    void getNoteCommentsNoteDoesNotExist() throws Exception {
        mockMvc.perform(get("/users/2/notes/200/comments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Note with id: 200 doesn't exist"));

    }

    @Test
    @Transactional
    void newUserNoteCommentHappyPath() throws Exception {
        String comment = """
                {
                    "title": "New comment",
                    "body": "New comment body"
                }""";
        mockMvc.perform(post("/users/1/notes/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(comment))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());

        List<CommentDTO> comments = notesService.getNoteComments(1L, 1L);
        assertEquals(1, comments.size());
        assertThat(comments).filteredOn(c -> c.title().equals("New comment")).isNotEmpty();
    }

    @Test
    @Transactional
    void newUserNoteCommentUserNotOwner() throws Exception {
        String comment = """
                {
                    "title": "New comment",
                    "body": "New comment body"
                }""";
        mockMvc.perform(post("/users/2/notes/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(comment))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User with id: 2 does not own note with id: 1"));
    }

    @Test
    @Transactional
    void newUserNoteCommentNoteDoesNotExist() throws Exception {
        String comment = """
                {
                    "title": "New comment",
                    "body": "New comment body"
                }""";
        mockMvc.perform(post("/users/2/notes/1000/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(comment))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Note with id: 1000 doesn't exist"));
    }

    private void saveComments(Result result) {
        commentRepository.save(result.comment());
        commentRepository.save(result.comment2());
    }

    private void deleteComments(Result result) {
        commentRepository.delete(result.comment());
        commentRepository.delete(result.comment2());
    }

    private Result makeReturnTwoComments() {
        Note note = noteRepository.findById(2L).get();
        Comment comment = new Comment("This is a comment", "contents");
        comment.setNote(note);
        commentRepository.save(comment);
        Comment comment2 = new Comment("This is a second comment", "second contents");
        comment2.setNote(note);
        commentRepository.save(comment2);
        Result result = new Result(comment, comment2);
        return result;
    }

    private record Result(Comment comment, Comment comment2) {
    }

}
