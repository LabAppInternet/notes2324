package cat.tecnocampus.notes2324;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// TODO 5.0: in this exercise you need to implement two tests

@SpringBootTest
@AutoConfigureMockMvc
public class TODO5tests {
    @Autowired
    private MockMvc mockMvc;

    //TODO 5.1: implement the happy path test for getting the user with id 1
    @Test
    void getOneExistingUser() throws Exception {
        mockMvc.perform(get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pere"))
                .andExpect(jsonPath("$.email").value("pere@tecnocampus.cat"))
                .andExpect(jsonPath("$.notes").isArray())
                .andExpect(jsonPath("$.notes", hasSize(3)))
                .andExpect(jsonPath("$.notes[*].title", containsInAnyOrder("Spring Boot Introduction", "Spring Data JPA", "Spring Security")));
    }

    //TODO 5.2: implement the test for getting a non existing user. User with id 100
    @Test
    void getOneNonExistingUser() throws Exception {
        mockMvc.perform(get("/users/100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User with id: 100 not found"));
    }
}
