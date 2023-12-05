package cat.tecnocampus.notes2324;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// TODO 2.0: See them fail and than make them pass completing the exercise
@SpringBootTest
@AutoConfigureMockMvc
public class TODO2test {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRatedByNotesUser() throws Exception {
        mockMvc.perform(get("/users/ratedbynotes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id").value(7))
                .andExpect(jsonPath("$[1].id").value(8))
                .andExpect(jsonPath("$[2].id").value(6))
                .andExpect(jsonPath("$[3].id").value(1))
                .andExpect(jsonPath("$[4].id").value(2));
    }
}
