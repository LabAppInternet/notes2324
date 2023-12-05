package cat.tecnocampus.notes2324;

import cat.tecnocampus.notes2324.application.NotesService;
import cat.tecnocampus.notes2324.application.dtos.NoteDTO;
import cat.tecnocampus.notes2324.domain.Note;
import cat.tecnocampus.notes2324.domain.Tag;
import cat.tecnocampus.notes2324.persistence.NoteRepository;
import cat.tecnocampus.notes2324.persistence.TagRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO 0: Before beginning your exam, run the tests and see that they pass

@SpringBootTest
@AutoConfigureMockMvc
class Notes2324ApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotesService notesService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private NoteRepository noteRepository;

    @Test
    void getNotesUserCanView() throws Exception {
        mockMvc.perform(get("/users/4/notes/canView")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2, 3, 4, 5)));
    }

    @Test
    void getNotesNonExistingUserCanView() throws Exception {
        mockMvc.perform(get("/users/100/notes/canView")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("User with id: 100 not found"));
    }

    @Test
    void getUserPermissions() throws Exception {
        mockMvc.perform(get("/users/4/permissions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[*].noteId", containsInAnyOrder(1, 2, 3, 4, 5)));
    }

    @Test
    void getNonExistingUserPermissions() throws Exception {
        mockMvc.perform(get("/users/100/permissions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getUsersCanViewNote() throws Exception {
        mockMvc.perform(get("/notes/2/users/canView")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(4))
                .andExpect(jsonPath("$[1].id").value(5));
    }

    @Test
    void getUsersCanEditNote() throws Exception {
        mockMvc.perform(get("/notes/2/users/canEdit")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getUsersCanEditNonExistingNote() throws Exception {
        mockMvc.perform(get("/notes/200/users/canEdit")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Transactional
    void newUserNote() throws Exception {
        String note = """
                {
                    "title": "New note",
                    "content": "New note content",
                    "tags": ["new tag", "Spring REST"]
                }""";
        mockMvc.perform(post("/users/2/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(note))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());

        List<NoteDTO> notes = notesService.getUserNotes(2L);
        assertEquals(3, notes.size());
        assertThat(notes).filteredOn(noteDTO -> noteDTO.title().equals("New note")).isNotEmpty();
        assertThat(notes).extracting("title").contains("New note"); //same as previous line
        assertTrue(tagRepository.existsById("new tag"));
    }

    @Test
    void newNonExistingUserNote() throws Exception {
        String note = """
                {
                    "title": "New note",
                    "content": "New note content",
                    "tags": ["new tag", "Spring REST"]
                }""";
        mockMvc.perform(post("/users/100/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(note))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User with id: 100 not found"));
    }

    @Test
    @Transactional
    void editOwnerUserNote() throws Exception {
        String note = """
                {
                   "noteId": 2,
                   "title": "New title to update",
                   "content": "This is a edited note for a user created with post 2",
                   "tags": ["Spring MVC", "Spring Boot", "informa"]
                 }""";
        mockMvc.perform(put("/users/1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(note))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        Note note1 = noteRepository.findById(2L).orElseThrow();
        assertEquals("New title to update", note1.getTitle());
        assertEquals("This is a edited note for a user created with post 2", note1.getContent());
        Set<Tag> expectedTags = Set.of(new Tag("Spring MVC"), new Tag("Spring Boot"), new Tag("informa"));
        assertTrue(note1.getTags().equals(expectedTags));
    }

    @Test
    @Transactional
    void editAllowedUserNote() throws Exception {
        String note = """
                {
                   "noteId": 1,
                   "title": "New title to update",
                   "content": "This is a edited note for a user created with post 2",
                   "tags": ["Spring MVC", "Spring Boot", "informa"]
                 }""";
        mockMvc.perform(put("/users/4/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(note))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        Note note1 = noteRepository.findById(1L).orElseThrow();
        assertEquals("New title to update", note1.getTitle());
        assertEquals("This is a edited note for a user created with post 2", note1.getContent());
        Set<Tag> expectedTags = Set.of(new Tag("Spring MVC"), new Tag("Spring Boot"), new Tag("informa"));
        assertTrue(note1.getTags().equals(expectedTags));
    }

    @Test
    @Transactional
    void editNotAllowedUserNote() throws Exception {
        String note = """
                {
                   "noteId": 3,
                   "title": "New title to update",
                   "content": "This is a edited note for a user created with post 2",
                   "tags": ["Spring MVC", "Spring Boot", "informa"]
                 }""";
        mockMvc.perform(put("/users/4/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(note))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        Note note1 = noteRepository.findById(3L).orElseThrow();
        assertEquals("Spring Security", note1.getTitle());
        assertTrue(note1.getTags().isEmpty());
    }
}



