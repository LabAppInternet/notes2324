package cat.tecnocampus.notes2324;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO 1.0: See them fail and than make them pass completing the exercise
@SpringBootTest
@AutoConfigureMockMvc
public class TODO1tests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void newUserNoteWithErrors() throws Exception {
        String note = """
                {
                 "title": "New9",
                 "content": "his is a new note for a user created with post",
                 "tags": ["new tag", "Spring REST"]
                 }""";

        mockMvc.perform(post("/users/2/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(note))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations", hasSize(3)))
                .andExpect(jsonPath("$.violations[*].message", containsInAnyOrder(
                        "Title must begin with a capital letter.",
                        "Title must begin with a capital letter. Also only letters are allowed",
                        "size must be between 5 and 100")));
    }

    @Test
    void updateUserNoteWithErrors() throws Exception {
        String note = """
                {
                 "title": "New9",
                 "content": "his is a new note for a user created with post",
                 "tags": ["new tag", "Spring REST"]
                 }""";

        mockMvc.perform(put("/users/2/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(note))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations", hasSize(3)))
                .andExpect(jsonPath("$.violations[*].message", containsInAnyOrder(
                        "Title must begin with a capital letter.",
                        "Title must begin with a capital letter. Also only letters are allowed",
                        "size must be between 5 and 100")));
    }

}
