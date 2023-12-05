package cat.tecnocampus.notes2324;

import cat.tecnocampus.notes2324.domain.NotePermission;
import cat.tecnocampus.notes2324.domain.NotePermissionId;
import cat.tecnocampus.notes2324.persistence.NotePermissionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// TODO 4.0: See them fail and than make them pass completing the exercise
@SpringBootTest
@AutoConfigureMockMvc
public class TODO4tests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    NotePermissionRepository notePermissionRepository;

    @Test
    void revokePermissionDelete() throws Exception {
        String permission = """
                {
                  "allowedId": 4,
                  "noteId": 4,
                  "canEdit": false,
                  "canView": false
                }""";
        mockMvc.perform(delete("/users/2/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(permission))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        assertFalse(notePermissionRepository.existsById(new NotePermissionId(4L, 4L)));
    }
    @Test
    void revokePermissionUpdate() throws Exception {
        String permission = """
                {
                  "allowedId": 4,
                  "noteId": 5,
                  "canEdit": false,
                  "canView": true
                }""";
        mockMvc.perform(delete("/users/2/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(permission))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        NotePermission notePermission = notePermissionRepository.findById(new NotePermissionId(5L, 4L)).orElseThrow();
        assertTrue(notePermission.isCanView() && !notePermission.isCanEdit());
    }
}
